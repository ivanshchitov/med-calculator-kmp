const CACHE_PREFIX = "medcalc-";
const MANIFEST_URL = "./precache-manifest.json";
const INDEX_URL = "./index.html";

let activeCacheName = null;

self.addEventListener("install", (event) => {
    event.waitUntil((async () => {
        const response = await fetch(MANIFEST_URL, { cache: "no-store" });
        if (!response.ok) {
            throw new Error("Cannot load precache manifest");
        }

        const manifest = await response.json();
        activeCacheName = CACHE_PREFIX + manifest.version;
        const cache = await caches.open(activeCacheName);
        await cache.addAll(manifest.files);
    })());
});

self.addEventListener("activate", (event) => {
    event.waitUntil((async () => {
        if (!activeCacheName) {
            const response = await fetch(MANIFEST_URL, { cache: "no-store" });
            if (response.ok) {
                const manifest = await response.json();
                activeCacheName = CACHE_PREFIX + manifest.version;
            }
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
        url.pathname.endsWith("/precache-manifest.json") ||
        url.pathname.endsWith("/_headers")) {
        return;
    }

    event.respondWith((async () => {
        const cached = await caches.match(request);
        if (cached) {
            return cached;
        }

        const response = await fetch(request);
        if (response.ok) {
            const cache = await caches.open(activeCacheName || CACHE_PREFIX + "runtime");
            await cache.put(request, response.clone());
        }
        return response;
    })());
});
