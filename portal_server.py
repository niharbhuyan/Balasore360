import http.server
import socketserver
import os
import urllib.parse
import re

PORT = 3000
DIRECTORY = "/app/applet"

HTML_CONTENT = """<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Balasore 360 - Google Play Release & Signing Hub</title>
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; }
        body {
            background-color: #0b0f19;
            color: #f1f5f9;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            padding: 24px 16px;
        }
        .container {
            max-width: 640px;
            width: 100%;
        }
        .card {
            background: #111827;
            border: 1px solid #1f2937;
            border-radius: 20px;
            padding: 28px 24px;
            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.4);
            text-align: center;
        }
        .status-badge {
            display: inline-flex;
            align-items: center;
            gap: 6px;
            background: rgba(16, 185, 129, 0.15);
            border: 1px solid #10b981;
            color: #34d399;
            font-size: 12px;
            font-weight: 700;
            padding: 6px 14px;
            border-radius: 9999px;
            margin-bottom: 16px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        .icon { font-size: 40px; margin-bottom: 12px; }
        h1 { font-size: 24px; font-weight: 800; color: #f9fafb; margin-bottom: 6px; }
        .subtitle { font-size: 14px; color: #9ca3af; margin-bottom: 24px; line-height: 1.5; }
        
        .btn-group { display: flex; flex-direction: column; gap: 12px; margin-bottom: 24px; }
        .btn-primary {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 10px;
            background: linear-gradient(135deg, #2563eb 0%, #1d4ed8 100%);
            color: #ffffff;
            font-size: 15px;
            font-weight: 700;
            padding: 16px 20px;
            border-radius: 14px;
            text-decoration: none;
            transition: all 0.2s ease;
            box-shadow: 0 4px 14px rgba(37, 99, 235, 0.4);
        }
        .btn-primary:hover { background: #1d4ed8; transform: translateY(-1px); }
        .btn-secondary {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            background: #1f2937;
            color: #38bdf8;
            font-size: 14px;
            font-weight: 600;
            padding: 14px 20px;
            border-radius: 14px;
            text-decoration: none;
            border: 1px solid #374151;
            transition: all 0.15s ease;
        }
        .btn-secondary:hover { background: #374151; color: #7dd3fc; }

        .btn-cert {
            background: rgba(245, 158, 11, 0.12);
            color: #fbbf24;
            border: 1px solid #d97706;
        }
        .btn-cert:hover { background: rgba(245, 158, 11, 0.25); color: #fef3c7; }

        .alert-box {
            background: rgba(239, 68, 68, 0.08);
            border: 1px solid #ef4444;
            border-radius: 14px;
            padding: 16px;
            text-align: left;
            margin-bottom: 20px;
            font-size: 13px;
            line-height: 1.6;
        }
        .alert-title {
            color: #f87171;
            font-weight: 700;
            margin-bottom: 6px;
            display: flex;
            align-items: center;
            gap: 6px;
        }
        .alert-desc { color: #cbd5e1; }
        .alert-desc code {
            background: #1e293b;
            color: #fca5a5;
            padding: 2px 6px;
            border-radius: 4px;
            font-family: monospace;
            font-size: 11px;
            word-break: break-all;
        }

        .guide-box {
            background: rgba(30, 41, 59, 0.6);
            border: 1px solid #334155;
            border-radius: 14px;
            padding: 18px;
            text-align: left;
            margin-bottom: 20px;
            font-size: 13px;
            color: #cbd5e1;
            line-height: 1.7;
        }
        .guide-title {
            color: #38bdf8;
            font-weight: 700;
            font-size: 13px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            margin-bottom: 10px;
            display: flex;
            align-items: center;
            gap: 6px;
        }
        .guide-box ol { padding-left: 20px; }
        .guide-box li { margin-bottom: 8px; }
        .guide-box strong { color: #f1f5f9; }

        .meta-grid {
            background: #0f172a;
            border: 1px solid #1e293b;
            border-radius: 14px;
            padding: 16px;
            font-size: 12px;
            color: #94a3b8;
            text-align: left;
            display: flex;
            flex-direction: column;
            gap: 10px;
        }
        .meta-row { display: flex; flex-direction: column; }
        .meta-row strong { color: #e2e8f0; font-size: 11px; text-transform: uppercase; letter-spacing: 0.5px; }
        .meta-row code {
            font-family: monospace;
            background: #1e293b;
            color: #38bdf8;
            padding: 3px 6px;
            border-radius: 4px;
            word-break: break-all;
            margin-top: 2px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="card">
            <span class="status-badge">✓ Release v1.2.0 (Build 13) Ready</span>
            <div class="icon">📦</div>
            <h1>Balasore 360</h1>
            <p class="subtitle">Production Signed Android App Bundle (.AAB) & Upload Key Reset Utilities</p>

            <div class="btn-group">
                <a href="/Balasore360-v1.2.0-b13-playstore.aab" class="btn-primary" download="Balasore360-v1.2.0-b13-playstore.aab">
                    <span>📥</span>
                    <span>Download Signed .AAB (20 MB)</span>
                </a>
                <a href="/upload_certificate.pem" class="btn-secondary btn-cert" download="upload_certificate.pem">
                    <span>🔑</span>
                    <span>Download Upload Certificate (.PEM)</span>
                </a>
                <a href="/my-upload-key.jks" class="btn-secondary" download="my-upload-key.jks">
                    <span>💾</span>
                    <span>Backup Keystore (my-upload-key.jks)</span>
                </a>
            </div>

            <!-- Root cause explanation box -->
            <div class="alert-box">
                <div class="alert-title">
                    <span>⚠️</span> Google Play Certificate Mismatch Diagnosis
                </div>
                <div class="alert-desc">
                    Your Google Play Console error shows that earlier releases (e.g. v1.0.8 / v1.0.9) were registered with key:<br>
                    <code>SHA1: E8:75:9E:92:6F:B4:E4:D3:B2:85:CD:02:76:C6:2D:69:23:AF:8E:72</code><br><br>
                    The newly generated project keystore in this build has:<br>
                    <code>SHA1: C8:34:A2:A8:DB:E7:1C:F5:F6:43:F0:59:9A:25:C7:85:10:66:DC:26</code><br><br>
                    Because Android enforces cryptographic signature continuity, Google Play blocks uploads with a different signature until the upload key is reset or the original key is used.
                </div>
            </div>

            <!-- Resolution steps -->
            <div class="guide-box">
                <div class="guide-title">
                    <span>🛠️</span> How to Fix in 2 Minutes (Google Play Upload Key Reset):
                </div>
                <ol>
                    <li>Click <strong>Download Upload Certificate (.PEM)</strong> above to save <code>upload_certificate.pem</code>.</li>
                    <li>Open <strong>play.google.com/console</strong> and select <strong>Balasore 360</strong>.</li>
                    <li>In the left sidebar, navigate to <strong>Setup</strong> &rarr; <strong>App integrity</strong> (or <strong>App signing</strong>).</li>
                    <li>Under the <strong>App signing</strong> tab, locate <strong>Upload key certificate</strong> and click <strong>Request upload key reset</strong>.</li>
                    <li>Select reason <em>"I lost my keystore / Need to update upload key"</em> and upload your downloaded <code>upload_certificate.pem</code>.</li>
                    <li>Google Play accepts the new certificate. Once registered, upload the downloaded <code>Balasore360-v1.2.0-b13-playstore.aab</code> and the release will pass verification immediately!</li>
                </ol>
            </div>

            <!-- Metadata info -->
            <div class="meta-grid">
                <div class="meta-row">
                    <strong>Package Name</strong>
                    <span>com.niharsales.balasore360</span>
                </div>
                <div class="meta-row">
                    <strong>Version Name / Code</strong>
                    <span>1.2.0 &bull; Code: 13</span>
                </div>
                <div class="meta-row">
                    <strong>Current Upload Key SHA-1</strong>
                    <code>C8:34:A2:A8:DB:E7:1C:F5:F6:43:F0:59:9A:25:C7:85:10:66:DC:26</code>
                </div>
                <div class="meta-row">
                    <strong>Current Upload Key SHA-256</strong>
                    <code>7A:78:E2:F7:48:16:A2:76:96:8E:2B:60:42:52:DD:DB:BF:6D:36:78:57:B4:0D:69:C5:3D:D5:38:D8:5B:91:8E</code>
                </div>
                <div class="meta-row">
                    <strong>Target SDK</strong>
                    <span>Android 15+ (API Level 36) &bull; 64-bit Architecture</span>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
"""

def get_bundle_path():
    candidates = [
        "/app/applet/Balasore360-v1.2.0-b13-playstore.aab",
        "/app/applet/balasore360-v1.2.0-playstore-signed.aab",
        "/app/applet/balasore360-v1.2.0-signed.aab",
        "/app/applet/app/build/outputs/bundle/release/app-release.aab",
        "./Balasore360-v1.2.0-b13-playstore.aab",
        "./balasore360-v1.2.0-playstore-signed.aab",
        "./balasore360-v1.2.0-signed.aab",
        "./app/build/outputs/bundle/release/app-release.aab"
    ]
    for c in candidates:
        if os.path.exists(c):
            return c
    return None

class DownloadHandler(http.server.SimpleHTTPRequestHandler):
    def send_range_file(self, filepath, filename, content_type="application/octet-stream", is_head=False):
        if not os.path.exists(filepath):
            self.send_error(404, "File not found")
            return
        file_size = os.path.getsize(filepath)
        range_header = self.headers.get("Range")
        start = 0
        end = file_size - 1
        status_code = 200

        if range_header:
            m = re.match(r"bytes=(\d+)-(\d*)", range_header)
            if m:
                status_code = 206
                start = int(m.group(1))
                if m.group(2):
                    end = int(m.group(2))
                end = min(end, file_size - 1)

        content_length = end - start + 1
        self.send_response(status_code)
        self.send_header("Content-Type", content_type)
        self.send_header("Content-Disposition", f'attachment; filename="{filename}"')
        self.send_header("Accept-Ranges", "bytes")
        self.send_header("Content-Length", str(content_length))
        if status_code == 206:
            self.send_header("Content-Range", f"bytes {start}-{end}/{file_size}")
        self.send_header("Cache-Control", "no-cache, no-store, must-revalidate")
        self.end_headers()

        if is_head:
            return

        with open(filepath, "rb") as f:
            f.seek(start)
            bytes_left = content_length
            while bytes_left > 0:
                chunk_size = min(65536, bytes_left)
                data = f.read(chunk_size)
                if not data:
                    break
                self.wfile.write(data)
                bytes_left -= len(data)

    def do_GET(self):
        url_path = urllib.parse.urlparse(self.path).path
        if url_path in ["/", "/index.html", "/portal"]:
            self.send_response(200)
            self.send_header("Content-Type", "text/html; charset=utf-8")
            content = HTML_CONTENT.encode("utf-8")
            self.send_header("Content-Length", str(len(content)))
            self.end_headers()
            self.wfile.write(content)
            return

        if url_path == "/upload_certificate.pem":
            pem_path = "/app/applet/upload_certificate.pem"
            if not os.path.exists(pem_path):
                pem_path = "./upload_certificate.pem"
            if os.path.exists(pem_path):
                self.send_range_file(pem_path, "upload_certificate.pem", "application/x-pem-file", is_head=False)
                return
            else:
                self.send_error(404, "Certificate not found")
                return

        if url_path == "/my-upload-key.jks":
            jks_path = "/app/applet/my-upload-key.jks"
            if not os.path.exists(jks_path):
                jks_path = "./my-upload-key.jks"
            if os.path.exists(jks_path):
                self.send_range_file(jks_path, "my-upload-key.jks", "application/octet-stream", is_head=False)
                return
            else:
                self.send_error(404, "Keystore not found")
                return

        bundle_path = get_bundle_path()
        aab_endpoints = [
            "/Balasore360-v1.2.0-b13-playstore.aab",
            "/balasore360-v1.2.0-playstore-signed.aab",
            "/balasore360-v1.2.0-signed.aab",
            "/app-release.aab",
            "/download/aab",
            "/Balasore360-release.aab"
        ]
        if url_path in aab_endpoints:
            if bundle_path:
                download_name = os.path.basename(url_path)
                if download_name in ["aab", ""]:
                    download_name = "Balasore360-v1.2.0-b13-playstore.aab"
                self.send_range_file(bundle_path, download_name, "application/octet-stream", is_head=False)
                return
            else:
                self.send_error(404, "AAB not found")
                return

        super().do_GET()

    def do_HEAD(self):
        url_path = urllib.parse.urlparse(self.path).path
        if url_path in ["/", "/index.html", "/portal"]:
            self.send_response(200)
            self.send_header("Content-Type", "text/html; charset=utf-8")
            content = HTML_CONTENT.encode("utf-8")
            self.send_header("Content-Length", str(len(content)))
            self.end_headers()
            return

        if url_path == "/upload_certificate.pem":
            pem_path = "/app/applet/upload_certificate.pem"
            if os.path.exists(pem_path):
                self.send_range_file(pem_path, "upload_certificate.pem", "application/x-pem-file", is_head=True)
                return

        if url_path == "/my-upload-key.jks":
            jks_path = "/app/applet/my-upload-key.jks"
            if os.path.exists(jks_path):
                self.send_range_file(jks_path, "my-upload-key.jks", "application/octet-stream", is_head=True)
                return

        bundle_path = get_bundle_path()
        aab_endpoints = [
            "/Balasore360-v1.2.0-b13-playstore.aab",
            "/balasore360-v1.2.0-playstore-signed.aab",
            "/balasore360-v1.2.0-signed.aab",
            "/app-release.aab",
            "/download/aab",
            "/Balasore360-release.aab"
        ]
        if url_path in aab_endpoints and bundle_path:
            download_name = os.path.basename(url_path)
            self.send_range_file(bundle_path, download_name, "application/octet-stream", is_head=True)
            return

        super().do_HEAD()

class ThreadingHTTPServer(socketserver.ThreadingMixIn, socketserver.TCPServer):
    allow_reuse_address = True
    daemon_threads = True

if __name__ == "__main__":
    os.chdir(DIRECTORY)
    with ThreadingHTTPServer(("", PORT), DownloadHandler) as httpd:
        print(f"Serving production AAB portal on port {PORT} with multithreading & HTTP range support...")
        httpd.serve_forever()
