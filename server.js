const http = require('http');
const fs = require('fs');
const path = require('path');

const PORT = 3000;
const APK_PATH = path.join(__dirname, 'app/build/outputs/apk/debug/app-debug.apk');
const ADS_TXT_PATH = path.join(__dirname, 'app-ads.txt');

const htmlContent = `<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Balasore 360 - Official Android App</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;600;700;800&family=Noto+Sans+Oriya:wght@500;700&display=swap" rel="stylesheet">
  <style>
    :root {
      --primary: #0284c7;
      --primary-dark: #0369a1;
      --accent: #f59e0b;
      --emerald: #10b981;
      --slate-900: #0f172a;
      --slate-800: #1e293b;
      --slate-700: #334155;
      --slate-500: #64748b;
      --slate-100: #f1f5f9;
      --slate-50: #f8fafc;
    }
    * { box-sizing: border-box; margin: 0; padding: 0; }
    body {
      font-family: 'Plus Jakarta Sans', sans-serif;
      background: linear-gradient(180deg, #f0f9ff 0%, #ffffff 100%);
      color: var(--slate-900);
      min-height: 100vh;
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 24px 16px 48px;
    }
    .header-card {
      max-width: 680px;
      width: 100%;
      background: white;
      border-radius: 24px;
      padding: 32px 24px;
      box-shadow: 0 10px 25px -5px rgba(2, 132, 199, 0.1), 0 8px 10px -6px rgba(0, 0, 0, 0.05);
      border: 1px solid #e2e8f0;
      text-align: center;
      margin-bottom: 24px;
    }
    .badge {
      display: inline-flex;
      align-items: center;
      gap: 6px;
      background: #e0f2fe;
      color: var(--primary-dark);
      padding: 6px 14px;
      border-radius: 999px;
      font-size: 12px;
      font-weight: 700;
      letter-spacing: 0.5px;
      margin-bottom: 16px;
    }
    .title {
      font-size: 32px;
      font-weight: 800;
      color: var(--slate-900);
      margin-bottom: 6px;
    }
    .subtitle-odia {
      font-family: 'Noto Sans Oriya', sans-serif;
      font-size: 17px;
      color: var(--primary);
      font-weight: 700;
      margin-bottom: 12px;
    }
    .desc {
      color: var(--slate-700);
      font-size: 15px;
      line-height: 1.6;
      margin-bottom: 24px;
      max-width: 540px;
      margin-left: auto;
      margin-right: auto;
    }
    .download-btn {
      display: inline-flex;
      align-items: center;
      justify-content: center;
      gap: 10px;
      background: linear-gradient(135deg, var(--primary) 0%, var(--primary-dark) 100%);
      color: white;
      text-decoration: none;
      font-weight: 700;
      font-size: 16px;
      padding: 14px 28px;
      border-radius: 14px;
      box-shadow: 0 4px 14px rgba(2, 132, 199, 0.35);
      transition: transform 0.15s ease, box-shadow 0.15s ease;
    }
    .download-btn:hover {
      transform: translateY(-2px);
      box-shadow: 0 6px 20px rgba(2, 132, 199, 0.45);
    }
    .features-grid {
      max-width: 680px;
      width: 100%;
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
      gap: 16px;
      margin-bottom: 24px;
    }
    .feature-box {
      background: white;
      border-radius: 18px;
      padding: 20px;
      border: 1px solid #e2e8f0;
      box-shadow: 0 2px 4px rgba(0,0,0,0.02);
    }
    .feature-icon {
      width: 36px;
      height: 36px;
      border-radius: 10px;
      background: #e0f2fe;
      display: flex;
      align-items: center;
      justify-content: center;
      color: var(--primary);
      font-size: 18px;
      margin-bottom: 12px;
    }
    .feature-title {
      font-size: 16px;
      font-weight: 700;
      margin-bottom: 4px;
    }
    .feature-desc {
      font-size: 13px;
      color: var(--slate-500);
      line-height: 1.5;
    }
    .admob-status {
      max-width: 680px;
      width: 100%;
      background: #f8fafc;
      border-radius: 14px;
      padding: 14px 18px;
      border: 1px solid #e2e8f0;
      font-size: 13px;
      color: var(--slate-700);
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    .status-dot {
      width: 8px;
      height: 8px;
      border-radius: 50%;
      background: var(--emerald);
      display: inline-block;
      margin-right: 6px;
    }
    code {
      background: #e2e8f0;
      padding: 2px 6px;
      border-radius: 4px;
      font-size: 11px;
    }
  </style>
</head>
<body>
  <div class="header-card">
    <div class="badge">✦ OFFICIAL COMMUNITY APP</div>
    <h1 class="title">Balasore 360</h1>
    <div class="subtitle-odia">ବାଲେଶ୍ୱର • ଚାନ୍ଦିପୁର ସ୍ପନ୍ଦନ</div>
    <p class="desc">
      Your all-in-one local guide to Balasore, Odisha. Featuring Chandipur vanishing sea tide alerts, 12th-century Kalinga temples, 24x7 emergency helplines, live bus & train schedules, and regional Odia news.
    </p>
    <a href="/app-debug.apk" class="download-btn" download="Balasore360.apk">
      <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/><polyline points="7 10 12 15 17 10"/><line x1="12" y1="15" x2="12" y2="3"/></svg>
      Download APK (v1.0.2)
    </a>
  </div>

  <div class="features-grid">
    <div class="feature-box">
      <div class="feature-icon">🌊</div>
      <div class="feature-title">Chandipur Vanishing Sea</div>
      <div class="feature-desc">Real-time tidal telemetry tracking when the sea recedes 5 km during low tide at Chandipur Beach.</div>
    </div>
    <div class="feature-box">
      <div class="feature-icon">🛕</div>
      <div class="feature-title">Spiritual & Eco Heritage</div>
      <div class="feature-desc">Explore Remuna Khirachora Gopinath, Panchalingeswar perennial spring, and Kuldiha Sanctuary.</div>
    </div>
    <div class="feature-box">
      <div class="feature-icon">🚨</div>
      <div class="feature-title">1-Tap Emergency Directory</div>
      <div class="feature-desc">Instant dialers for Balasore Police (112), FM Medical College Hospital (DHH), and Fire Rescue.</div>
    </div>
    <div class="feature-box">
      <div class="feature-icon">📰</div>
      <div class="feature-title">Bilingual News & Alerts</div>
      <div class="feature-desc">Odia and English local district reporting, DRDO missile testing bulletins, and weather alerts.</div>
    </div>
  </div>

  <div class="admob-status">
    <div><span class="status-dot"></span><strong>Google AdMob Integration:</strong> Verified &amp; Active</div>
    <div>App ID: <code>ca-app-pub-4880...0952</code></div>
  </div>
</body>
</html>
`;

const server = http.createServer((req, res) => {
  const url = req.url || '/';

  if (url === '/app-ads.txt') {
    res.writeHead(200, { 'Content-Type': 'text/plain' });
    if (fs.existsSync(ADS_TXT_PATH)) {
      res.end(fs.readFileSync(ADS_TXT_PATH, 'utf8'));
    } else {
      res.end('google.com, pub-4880243637225183, DIRECT, f08c47fec0942fa0\n');
    }
    return;
  }

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

  if (url === '/api/info') {
    res.writeHead(200, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({
      appName: 'Balasore 360',
      version: '1.0.2',
      status: 'ready',
      admobAppId: 'ca-app-pub-4880243637225183~4956380952',
      admobPublisherId: 'pub-4880243637225183',
      apkAvailable: fs.existsSync(APK_PATH)
    }));
    return;
  }

  // Default homepage
  res.writeHead(200, { 'Content-Type': 'text/html' });
  res.end(htmlContent);
});

server.listen(PORT, '0.0.0.0', () => {
  console.log(`Balasore 360 Companion Server listening on port ${PORT}`);
});
