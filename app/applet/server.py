import os
import sys
from http.server import SimpleHTTPRequestHandler, ThreadingHTTPServer

class ResilientHandler(SimpleHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, directory="/app/applet/download_dist", **kwargs)

    def log_message(self, format, *args):
        sys.stderr.write("%s - - [%s] %s\n" % (self.address_string(), self.log_date_time_string(), format % args))
        sys.stderr.flush()

    def handle(self):
        try:
            super().handle()
        except (BrokenPipeError, ConnectionResetError):
            pass

def run():
    server_address = ('0.0.0.0', 3000)
    httpd = ThreadingHTTPServer(server_address, ResilientHandler)
    sys.stderr.write("Download server running on port 3000...\n")
    sys.stderr.flush()
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        pass

if __name__ == '__main__':
    run()
