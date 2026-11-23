#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
ENV_FILE="$ROOT_DIR/.env"
DIST_DIR="$ROOT_DIR/shared/build/dist/wasmJs/productionExecutable"

IMMUTABLE_CACHE="public, max-age=31536000, immutable"
NO_CACHE="no-cache"

log() {
    printf '==> %s\n' "$*"
}

fail() {
    printf 'ERROR: %s\n' "$*" >&2
    exit 1
}

usage() {
    cat <<'EOF'
Usage: scripts/deploy_web.sh [options]

Builds the wasm PWA and uploads it to Yandex Cloud Object Storage.

Options:
  --skip-build   Upload the existing build without running Gradle.
  -h, --help     Show this help.

Configuration is read from .env in the repository root (see .env.template).
Stale objects that are no longer part of the build are deleted from the bucket.
EOF
}

load_env() {
    [ -f "$ENV_FILE" ] || fail ".env not found. Create it from the template: cp .env.template .env"

    while IFS= read -r line || [ -n "$line" ]; do
        line="${line%$'\r'}"
        line="${line#"${line%%[![:space:]]*}"}"
        case "$line" in
            ''|'#'*) continue ;;
        esac

        line="${line#export }"
        case "$line" in
            *=*) ;;
            *) continue ;;
        esac

        key="${line%%=*}"
        value="${line#*=}"

        # Trim whitespace around the key and the value.
        key="${key%"${key##*[![:space:]]}"}"
        key="${key#"${key%%[![:space:]]*}"}"
        value="${value%"${value##*[![:space:]]}"}"
        value="${value#"${value%%[![:space:]]*}"}"

        # Drop matching quotes.
        case "$value" in
            \"*\")
                value="${value#\"}"
                value="${value%\"}"
                ;;
            \'*\')
                value="${value#\'}"
                value="${value%\'}"
                ;;
        esac

        export "$key=$value"
    done < "$ENV_FILE"
}

require_var() {
    local name="$1"
    if [ -z "${!name:-}" ]; then
        fail "$name is not set. Add it to $ENV_FILE (see .env.template)."
    fi
}

aws_s3() {
    aws --endpoint-url "$YC_S3_ENDPOINT" s3 "$@"
}

content_type_for() {
    case "$1" in
        *.html) echo "text/html; charset=utf-8" ;;
        *.js) echo "text/javascript; charset=utf-8" ;;
        *.json) echo "application/json; charset=utf-8" ;;
        *) return 1 ;;
    esac
}

SKIP_BUILD=0
while [ $# -gt 0 ]; do
    case "$1" in
        --skip-build) SKIP_BUILD=1 ;;
        -h|--help)
            usage
            exit 0
            ;;
        *)
            usage >&2
            fail "Unknown option: $1"
            ;;
    esac
    shift
done

load_env
YC_S3_ENDPOINT="${YC_S3_ENDPOINT:-https://storage.yandexcloud.net}"
AWS_DEFAULT_REGION="${AWS_DEFAULT_REGION:-ru-central1}"
export YC_S3_ENDPOINT AWS_DEFAULT_REGION AWS_EC2_METADATA_DISABLED=true

require_var AWS_ACCESS_KEY_ID
require_var AWS_SECRET_ACCESS_KEY
require_var YC_S3_BUCKET

if ! command -v aws >/dev/null 2>&1; then
    fail "AWS CLI not found. Install it with: brew install awscli"
fi

if [ "$SKIP_BUILD" -eq 0 ]; then
    log "Building PWA: ./gradlew :shared:wasmJsBrowserDistribution"
    (cd "$ROOT_DIR" && ./gradlew :shared:wasmJsBrowserDistribution --console=plain)
else
    log "Skipping build (--skip-build)"
fi

[ -d "$DIST_DIR" ] || fail "Build output not found: $DIST_DIR"
[ -f "$DIST_DIR/index.html" ] || fail "index.html not found in $DIST_DIR"

FILE_COUNT=$(find "$DIST_DIR" -type f | wc -l | tr -d ' ')
log "Uploading $FILE_COUNT files to s3://$YC_S3_BUCKET"

# Hashed assets change name on every build, so they can be cached forever.
# The entry points keep no-cache and are uploaded separately below.
aws_s3 sync "$DIST_DIR/" "s3://$YC_S3_BUCKET/" \
    --delete \
    --cache-control "$IMMUTABLE_CACHE" \
    --exclude "index.html" \
    --exclude "sw.js" \
    --exclude "precache-manifest.json" \
    --exclude "manifest.json"

for file in index.html sw.js precache-manifest.json manifest.json; do
    [ -f "$DIST_DIR/$file" ] || continue

    aws_s3 cp "$DIST_DIR/$file" "s3://$YC_S3_BUCKET/$file" \
        --cache-control "$NO_CACHE" \
        --content-type "$(content_type_for "$file")"
done

# AWS CLI guesses application/wasm correctly on modern Python, but make it
# explicit: the browser needs this type for streaming compilation.
find "$DIST_DIR" -type f -name '*.wasm' -print0 | while IFS= read -r -d '' wasm_file; do
    key="${wasm_file#"$DIST_DIR"/}"

    aws_s3 cp "s3://$YC_S3_BUCKET/$key" "s3://$YC_S3_BUCKET/$key" \
        --metadata-directive REPLACE \
        --cache-control "$IMMUTABLE_CACHE" \
        --content-type "application/wasm"
done

PUBLIC_URL="${PWA_PUBLIC_URL:-https://$YC_S3_BUCKET}"
log "Done. Site: $PUBLIC_URL"
