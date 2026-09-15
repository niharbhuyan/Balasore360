const http = require('http');
const fs = require('fs');
const path = require('path');

const PORT = 3000;
const APK_PATH = path.join(__dirname, 'app/build/outputs/apk/debug/app-debug.apk');
const ADS_TXT_PATH = path.join(__dirname, 'app-ads.txt');
const INDEX_HTML_PATH = path.join(__dirname, 'index.html');
const PRIVACY_HTML_PATH = path.join(__dirname, 'privacy.html');

const server = http.createServer((req, res) => {
  const url = (req.url || '/').split('?')[0];

  // AdMob ads verification file
  if (url === '/app-ads.txt') {
    res.writeHead(200, { 'Content-Type': 'text/plain; charset=utf-8' });
    if (fs.existsSync(ADS_TXT_PATH)) {
      res.end(fs.readFileSync(ADS_TXT_PATH, 'utf8'));
    } else {
      res.end('google.com, pub-4880243637225183, DIRECT, f08c47fec0942fa0\n');
    }
    return;
  }

  // APK download
  if (url === '/app-debug.apk' || url === '/download') {
    if (fs.existsSync(APK_PATH)) {
      const stat = fs.statSync(APK_PATH);
      res.writeHead(200, {
        'Content-Type': 'application/vnd.android.package-archive',
        'Content-Length': stat.size,
        'Content-Disposition': 'attachment; filename="Balasore360.apk"'
      });
      fs.createReadStream(APK_PATH).pipe(res);
    } else {
      res.writeHead(404, { 'Content-Type': 'text/plain' });
      res.end('APK not found. Please compile the applet first.');
    }
    return;
  }

  // Google Play Store Mandatory Privacy Policy permalink
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

  // Application diagnostic API
  if (url === '/api/info') {
    res.writeHead(200, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({
      appName: 'Balasore 360',
      version: '1.0.2',
      status: 'ready',
      privacyPolicyUrl: '/privacy',
      admobAppId: 'ca-app-pub-4880243637225183~4956380952',
      admobPublisherId: 'pub-4880243637225183',
      apkAvailable: fs.existsSync(APK_PATH)
    }));
    return;
  }

  // Default homepage (serves index.html with prominent Privacy Policy section)
  res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
  if (fs.existsSync(INDEX_HTML_PATH)) {
    res.end(fs.readFileSync(INDEX_HTML_PATH, 'utf8'));
  } else {
    res.end('<h1>Balasore 360</h1><p>Welcome to Balasore 360.</p>');
  }
});

server.listen(PORT, '0.0.0.0', () => {
  console.log(`Balasore 360 Companion Server listening on port ${PORT}`);
});
