const cacheName = 'v1';
const cachesAssets = [
    '/','main.css','bundle.js'
];

self.addEventListener('install',(e)=>{
    console.log('Service worker: installed');
    e['waitUntill'](caches.open(cacheName).then((cache)=>{
        console.log('Service worker: caching files');
        cache.addAll(cachesAssets).then(()=>self['skipWaiting']());
    }));
});
self.addEventListener('activate',(e)=>{
    console.log('Service worker: activated');
});