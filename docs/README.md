# Balasore 360 - Official Developer Website & AdMob Verification

This directory contains the ready-to-deploy static website for **Balasore 360** (`com.niharsales.balasore360`).

## Files
- `index.html`: Official landing page, app features, download hub, privacy policy, and AdMob verification center.
- `app-ads.txt`: Official IAB Tech Lab app-ads.txt record:
  ```text
  google.com, pub-4880243637225183, DIRECT, f08c47fec0942fa0
  ```
- `privacy.html`: Dedicated Google Play Store compliant Privacy Policy.

## Free 1-Click Hosting Options

### 1. GitHub Pages (100% Free Forever)
1. Push this repository to GitHub.
   - *Best Practice:* Name your repo `<username>.github.io` (e.g., `niharbhuyan.github.io`).
2. Go to **Settings** > **Pages**.
3. Under **Build and deployment**, set Source to **Deploy from a branch**.
4. Select branch `main` and folder `/docs`.
5. Click **Save**.
6. Your website is live at `https://<username>.github.io/` and your AdMob verification file is live at:
   `https://<username>.github.io/app-ads.txt`

### 2. Netlify / Vercel / Cloudflare Pages (Free Tier)
1. Upload or connect this folder.
2. Publish with 1 click.
3. Access `https://your-app.netlify.app/app-ads.txt`.

### 3. Google Play Store Configuration
In Google Play Console:
- Go to **Store presence** > **Store settings** > **Developer contact details**.
- Enter your website URL in the **Website** field.
- Save changes.

### 4. AdMob Verification
In Google AdMob Console:
- Go to **Apps** > **Balasore 360** > **app-ads.txt**.
- Click **Check for updates**.
