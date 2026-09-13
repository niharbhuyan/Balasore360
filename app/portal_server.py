import http.server
import socketserver
import os
import urllib.parse

PORT = 3000
DIRECTORY = "/app/applet"

HTML_CONTENT = """<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Download Balasore 360 Play Store AAB</title>
    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
        }
        body {
            background-color: #0f172a;
            color: #f8fafc;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            padding: 20px;
        }
        .card {
            background-color: #1e293b;
            border: 1px solid #334155;
            border-radius: 24px;
            padding: 32px 24px;
            max-width: 500px;
            width: 100%;
            box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.5), 0 8px 10px -6px rgba(0, 0, 0, 0.5);
            text-align: center;
        }
        .icon-badge {
            width: 72px;
            height: 72px;
            background: linear-gradient(135deg, #0284c7, #2563eb);
            border-radius: 20px;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            font-size: 36px;
            margin-bottom: 20px;
            box-shadow: 0 10px 15px -3px rgba(37, 99, 235, 0.4);
        }
        h1 {
            font-size: 24px;
            font-weight: 700;
            margin-bottom: 8px;
            color: #ffffff;
        }
        p.subtitle {
            font-size: 14px;
            color: #94a3b8;
            margin-bottom: 28px;
            line-height: 1.5;
        }
        .btn-primary {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 12px;
            width: 100%;
            background: linear-gradient(135deg, #2563eb, #1d4ed8);
            color: #ffffff;
            font-size: 16px;
            font-weight: 600;
            padding: 18px 24px;
            border-radius: 16px;
            text-decoration: none;
            box-shadow: 0 10px 15px -3px rgba(37, 99, 235, 0.4);
            transition: transform 0.15s ease, background 0.15s ease;
            margin-bottom: 14px;
        }
        .btn-primary:active {
            transform: scale(0.98);
        }
        .btn-secondary {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 10px;
            width: 100%;
            background-color: #334155;
            color: #e2e8f0;
            font-size: 15px;
            font-weight: 500;
            padding: 14px 20px;
            border-radius: 14px;
            text-decoration: none;
            margin-bottom: 12px;
            transition: background 0.15s ease;
        }
        .btn-secondary:active {
            background-color: #475569;
        }
        .file-meta {
            font-size: 12px;
            color: #64748b;
            margin-top: 16px;
            background: #0f172a;
            padding: 14px;
            border-radius: 12px;
            text-align: left;
            line-height: 1.6;
        }
        .file-meta strong {
            color: #cbd5e1;
        }
        .steps-box {
            margin-top: 24px;
            background: rgba(30, 41, 59, 0.6);
            border: 1px dashed #475569;
            border-radius: 16px;
            padding: 16px;
            text-align: left;
        }
        .steps-title {
            font-size: 13px;
            font-weight: 600;
            color: #38bdf8;
            margin-bottom: 8px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        ol {
            padding-left: 20px;
            font-size: 13px;
            color: #94a3b8;
            line-height: 1.6;
        }
        ol li {
            margin-bottom: 4px;
        }
    </style>
</head>
<body>
    <div class="card">
        <div class="icon-badge">📦</div>
        <h1>Balasore 360</h1>
        <p class="subtitle">Official Google Play Store Signed Release Bundle (.aab)</p>

        <a href="/download/aab" class="btn-primary" download="Balasore360-release.aab">
            <span>📥</span>
            <span>Download Signed .AAB (23.8 MB)</span>
        </a>

        <a href="/download/apk" class="btn-secondary" download="Balasore360-debug.apk">
            <span>📱</span>
            <span>Download Test APK (Direct Phone Install)</span>
        </a>

        <a href="/download/assets" class="btn-secondary" download="Balasore360_PlayStore_Assets.zip">
            <span>🎨</span>
            <span>Download Play Store Graphic Assets (ZIP)</span>
        </a>

        <div class="file-meta">
            <div><strong>File:</strong> Balasore360-release.aab</div>
            <div><strong>Package:</strong> com.niharsales.balasore360</div>
            <div><strong>Version:</strong> 1.0.0 (Version Code: 1)</div>
            <div><strong>Target SDK:</strong> Android 15+ (API 36)</div>
            <div><strong>Signing:</strong> Release Signed (my-upload-key.jks)</div>
        </div>

        <div class="steps-box">
            <div class="steps-title">What to do next:</div>
            <ol>
                <li>Tap the blue <strong>Download Signed .AAB</strong> button above.</li>
                <li>The file saves to your phone's <strong>Downloads</strong> folder.</li>
                <li>Open <strong>play.google.com/console</strong>.</li>
                <li>Go to <strong>Production</strong> &rarr; <strong>Create release</strong> &rarr; <strong>Upload</strong>.</li>
            </ol>
        </div>
    </div>
</body>
</html>
"""

class DownloadHandler(http.server.SimpleHTTPRequestHandler):
    def do_GET(self):
        url_path = urllib.parse.urlparse(self.path).path
        if url_path == "/" or url_path == "/index.html":
            self.send_response(200)
            self.send_header("Content-Type", "text/html; charset=utf-8")
            content = HTML_CONTENT.encode("utf-8")
            self.send_header("Content-Length", str(len(content)))
            self.end_headers()
            self.wfile.write(content)
            return

        if url_path == "/download/aab" or url_path == "/Balasore360-release.aab":
            filepath = os.path.join(DIRECTORY, "Balasore360-release.aab")
            self.serve_file_attachment(filepath, "Balasore360-release.aab")
            return

        if url_path == "/download/apk" or url_path == "/Balasore360-debug.apk":
            filepath = os.path.join(DIRECTORY, "Balasore360-debug.apk")
            if not os.path.exists(filepath):
                filepath = "/app/applet/app/build/outputs/apk/debug/app-debug.apk"
            self.serve_file_attachment(filepath, "Balasore360-debug.apk")
            return

        if url_path == "/download/assets" or url_path == "/Balasore360_PlayStore_Assets.zip":
            filepath = os.path.join(DIRECTORY, "Balasore360_PlayStore_Assets.zip")
            self.serve_file_attachment(filepath, "Balasore360_PlayStore_Assets.zip")
            return

        # Default fallback: serve from directory
        super().do_GET()

    def serve_file_attachment(self, filepath, filename):
        if not os.path.exists(filepath):
            self.send_error(404, f"File not found: {filename}")
            return
        
        file_size = os.path.getsize(filepath)
        self.send_response(200)
        self.send_header("Content-Type", "application/octet-stream")
        self.send_header("Content-Disposition", f'attachment; filename="{filename}"')
        self.send_header("Content-Length", str(file_size))
        self.end_headers()

        with open(filepath, "rb") as f:
            while chunk := f.read(65536):
                self.wfile.write(chunk)

class ReuseTCPServer(socketserver.TCPServer):
    allow_reuse_address = True

if __name__ == "__main__":
    os.chdir(DIRECTORY)
    with ReuseTCPServer(("", PORT), DownloadHandler) as httpd:
        print(f"Serving download portal on port {PORT}...")
        httpd.serve_forever()
