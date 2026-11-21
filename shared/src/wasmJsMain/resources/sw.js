const CACHE_PREFIX = "medcalc-";
const MANIFEST_URL = "./precache-manifest.json";
const INDEX_URL = "./index.html";
const CRITICAL_ASSET = /(^index\.html$|^shared\.js$|\.wasm$)/;

let activeCacheName = null;

async function readManifest() {
    const response = await fetch(MANIFEST_URL, { cache: "no-store" });
    if (!response.ok) {
        throw new Error("Cannot load precache manifest");
    }
    return response.json();
}

async function getActiveCacheName() {
    if (activeCacheName) {
        return activeCacheName;
    }

    const keys = await caches.keys();
    activeCacheName = keys.find((key) => key.startsWith(CACHE_PREFIX)) || CACHE_PREFIX + "runtime";
    return activeCacheName;
}

async function precache(cache, files) {
    const results = await Promise.allSettled(files.map(async (url) => {
        const response = await fetch(url, { cache: "no-store" });
        if (!response.ok) {
            throw new Error(`${url}: HTTP ${response.status}`);
        }
        await cache.put(url, response);
    }));

    const failed = results
        .map((result, index) => result.status === "rejected" ? files[index] : null)
        .filter(Boolean);

    if (failed.some((url) => CRITICAL_ASSET.test(url))) {
        throw new Error(`Critical precache files failed: ${failed.join(", ")}`);
    }

    if (failed.length > 0) {
        console.warn("Optional precache files failed", failed);
    }
}

self.addEventListener("install", (event) => {
    event.waitUntil((async () => {
        const manifest = await readManifest();
        activeCacheName = CACHE_PREFIX + manifest.version;
        const cache = await caches.open(activeCacheName);
        await precache(cache, manifest.files);
        await self.skipWaiting();
    })());
});

self.addEventListener("activate", (event) => {
    event.waitUntil((async () => {
        if (!activeCacheName) {
            try {
                const manifest = await readManifest();
                activeCacheName = CACHE_PREFIX + manifest.version;
            } catch (error) {
                console.warn("Cannot refresh precache manifest during activation", error);
            }
        }

        if (!activeCacheName) {
            return;
        }

        const keys = await caches.keys();
        await Promise.all(
            keys
                .filter((key) => key.startsWith(CACHE_PREFIX) && key !== activeCacheName)
                .map((key) => caches.delete(key))
        );
        await self.clients.claim();
    })());
});

self.addEventListener("fetch", (event) => {
    const request = event.request;
    if (request.method !== "GET") {
        return;
    }

    const url = new URL(request.url);
    if (url.origin !== location.origin) {
        return;
    }

    if (request.mode === "navigate") {
        event.respondWith((async () => {
            try {
                return await fetch(request);
            } catch (error) {
                return (await caches.match(INDEX_URL)) || Response.error();
            }
        })());
        return;
    }

    if (url.pathname.endsWith("/sw.js") ||
        url.pathname.endsWith("/precache-manifest.json")) {
        return;
    }

    event.respondWith((async () => {
        const cached = await caches.match(request);
        if (cached) {
            return cached;
        }

        const response = await fetch(request);
        if (response.ok) {
            const cacheName = await getActiveCacheName();
            const cache = await caches.open(cacheName);
            await cache.put(request, response.clone());
        }
        return response;
    })());
});
