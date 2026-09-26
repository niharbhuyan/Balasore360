const http = require('http');
const fs = require('fs');
const path = require('path');

const PORT = 3000;
const ROOT_DIR = __dirname;
const AAB_FILE_NAME = 'Balasore360-v1.0.8-release.aab';
const AAB_PATH = path.join(ROOT_DIR, AAB_FILE_NAME);
const FALLBACK_AAB_PATH = path.join(ROOT_DIR, 'Balasore360-release.aab');
const APK_PATH = path.join(ROOT_DIR, 'Balasore360-debug.apk');
const FALLBACK_APK_PATH = path.join(ROOT_DIR, 'app-debug.apk');
const ASSETS_ZIP_PATH = path.join(ROOT_DIR, 'Balasore360_PlayStore_Assets.zip');
const ADS_TXT_PATH = path.join(ROOT_DIR, 'app-ads.txt');
const INDEX_HTML_PATH = path.join(ROOT_DIR, 'index.html');
const PRIVACY_HTML_PATH = path.join(ROOT_DIR, 'privacy.html');

function serveFileWithRanges(req, res, filePath, contentType, downloadFilename) {
  if (!fs.existsSync(filePath)) {
    res.writeHead(404, { 'Content-Type': 'text/plain; charset=utf-8' });
    res.end(`File not found: ${downloadFilename}`);
    return;
  }

  const stat = fs.statSync(filePath);
  const totalSize = stat.size;
  const range = req.headers.range;

  if (range) {
    const parts = range.replace(/bytes=/, '').split('-');
    const start = parseInt(parts[0], 10);
    const end = parts[1] ? parseInt(parts[1], 10) : totalSize - 1;

    if (start >= totalSize || end >= totalSize) {
      res.writeHead(416, {
        'Content-Range': `bytes */${totalSize}`,
        'Content-Type': 'text/plain'
      });
      res.end('Requested range not satisfiable');
      return;
    }

    const chunksize = (end - start) + 1;
    const fileStream = fs.createReadStream(filePath, { start, end });
    res.writeHead(206, {
      'Content-Range': `bytes ${start}-${end}/${totalSize}`,
      'Accept-Ranges': 'bytes',
      'Content-Length': chunksize,
      'Content-Type': contentType,
      'Content-Disposition': `attachment; filename="${downloadFilename}"`,
      'Cache-Control': 'no-cache'
    });
    fileStream.pipe(res);
    fileStream.on('error', (err) => {
      console.error('Stream error:', err.message);
      if (!res.headersSent) {
        res.writeHead(500);
        res.end();
      }
    });
  } else {
    res.writeHead(200, {
      'Content-Length': totalSize,
      'Content-Type': contentType,
      'Accept-Ranges': 'bytes',
      'Content-Disposition': `attachment; filename="${downloadFilename}"`,
      'Cache-Control': 'no-cache'
    });
    const fileStream = fs.createReadStream(filePath);
    fileStream.pipe(res);
    fileStream.on('error', (err) => {
      console.error('Stream error:', err.message);
      if (!res.headersSent) {
        res.writeHead(500);
        res.end();
      }
    });
  }
}

const server = http.createServer((req, res) => {
  req.on('error', (err) => {
    console.error('Request error:', err.message);
  });
  res.on('error', (err) => {
    console.error('Response error:', err.message);
  });

  const rawUrl = (req.url || '/').split('?')[0];
  const url = decodeURIComponent(rawUrl);

  // Health check
  if (url === '/health' || url === '/ping') {
    res.writeHead(200, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({ status: 'ok', time: new Date().toISOString() }));
    return;
  }

  // AdMob app-ads.txt
  if (url === '/app-ads.txt') {
    res.writeHead(200, { 'Content-Type': 'text/plain; charset=utf-8' });
    if (fs.existsSync(ADS_TXT_PATH)) {
      res.end(fs.readFileSync(ADS_TXT_PATH, 'utf8'));
    } else {
      res.end('google.com, pub-4880243637225183, DIRECT, f08c47fec0942fa0\n');
    }
    return;
  }

  // AAB Download routes
  if (
    url === '/download/aab' ||
    url === '/Balasore360-v1.0.5-release.aab' ||
    url === '/Balasore360-v1.0.4-release.aab' ||
    url === '/Balasore360-v1.0.3-release.aab' ||
    url === '/Balasore360-release.aab' ||
    url === '/app-release.aab' ||
    url.endsWith('.aab')
  ) {
    const targetFile = fs.existsSync(AAB_PATH) ? AAB_PATH : FALLBACK_AAB_PATH;
    serveFileWithRanges(req, res, targetFile, 'application/octet-stream', AAB_FILE_NAME);
    return;
  }

  // APK Download routes
  if (
    url === '/download/apk' ||
    url === '/app-debug.apk' ||
    url === '/Balasore360-debug.apk' ||
    url === '/download' ||
    url.endsWith('.apk')
  ) {
    const targetApk = fs.existsSync(APK_PATH) ? APK_PATH : (fs.existsSync(FALLBACK_APK_PATH) ? FALLBACK_APK_PATH : path.join(ROOT_DIR, 'Balasore360-release.apk'));
    serveFileWithRanges(req, res, targetApk, 'application/vnd.android.package-archive', 'Balasore360.apk');
    return;
  }

  // Graphic Assets Zip
  if (
    url === '/download/assets' ||
    url === '/Balasore360_PlayStore_Assets.zip' ||
    url.endsWith('.zip')
  ) {
    serveFileWithRanges(req, res, ASSETS_ZIP_PATH, 'application/zip', 'Balasore360_PlayStore_Assets.zip');
    return;
  }

  // Google Play Store Mandatory Privacy Policy
  if (url === '/privacy' || url === '/privacy.html' || url === '/privacy-policy') {
    res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
    if (fs.existsSync(PRIVACY_HTML_PATH)) {
      res.end(fs.readFileSync(PRIVACY_HTML_PATH, 'utf8'));
    } else if (fs.existsSync(INDEX_HTML_PATH)) {
      res.end(fs.readFileSync(INDEX_HTML_PATH, 'utf8'));
    } else {
      res.end('<h1>Privacy Policy</h1><p>Balasore 360 values your privacy. For inquiries contact: niharbhuyan@gmail.com</p>');
    }
    return;
  }

  // Diagnostic API
  if (url === '/api/info') {
    res.writeHead(200, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({
      appName: 'Balasore 360',
      version: '1.0.5',
      versionCode: 6,
      status: 'ready',
      aabAvailable: fs.existsSync(AAB_PATH),
      aabSize: fs.existsSync(AAB_PATH) ? fs.statSync(AAB_PATH).size : 0,
      apkAvailable: fs.existsSync(APK_PATH),
      privacyPolicyUrl: '/privacy'
    }));
    return;
  }

  // Serve static assets if requested (images, css, js)
  const safePath = path.normalize(path.join(ROOT_DIR, url));
  if (safePath.startsWith(ROOT_DIR) && fs.existsSync(safePath) && fs.statSync(safePath).isFile() && url !== '/') {
    const ext = path.extname(safePath).toLowerCase();
    const mimeTypes = {
      '.png': 'image/png',
      '.jpg': 'image/jpeg',
      '.jpeg': 'image/jpeg',
      '.svg': 'image/svg+xml',
      '.js': 'application/javascript',
      '.css': 'text/css',
      '.json': 'application/json',
      '.txt': 'text/plain'
    };
    const contentType = mimeTypes[ext] || 'application/octet-stream';
    res.writeHead(200, { 'Content-Type': contentType });
    fs.createReadStream(safePath).pipe(res);
    return;
  }

  // Default homepage (index.html)
  res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
  if (fs.existsSync(INDEX_HTML_PATH)) {
    res.end(fs.readFileSync(INDEX_HTML_PATH, 'utf8'));
  } else {
    res.end('<h1>Balasore 360</h1><p>Welcome to Balasore 360.</p>');
  }
});

server.listen(PORT, '0.0.0.0', () => {
  console.log(`Balasore 360 Production Server listening on port ${PORT}`);
});
