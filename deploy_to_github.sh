#!/usr/bin/env bash
set -e

echo "=================================================="
echo " Balasore 360 - Automated GitHub Pages Deployment"
echo "=================================================="

# Ensure git is initialized
if [ ! -d ".git" ]; then
    echo "Initializing local Git repository..."
    git init
    git branch -M main
fi

# Configure git committer details
git config user.name "Nihar Bhuyan"
git config user.email "niharbhuyan@gmail.com"

# Ensure docs/.nojekyll exists
mkdir -p docs
touch docs/.nojekyll
cp -f index.html docs/index.html
cp -f privacy.html docs/privacy.html
cp -f app-ads.txt docs/app-ads.txt

# Stage all files
echo "Staging files..."
git add -A

# Commit changes
if git diff --cached --quiet; then
    echo "No changes to commit (already up to date)."
else
    echo "Committing website and app-ads.txt..."
    git commit -m "Deploy Balasore 360 AdMob website, privacy policy, and app-ads.txt for GitHub Pages"
fi

# If remote URL is supplied as argument, push
if [ -n "$1" ]; then
    echo "Configuring remote origin: $1"
    git remote remove origin 2>/dev/null || true
    git remote add origin "$1"
    echo "Pushing to GitHub (main branch)..."
    git push -u origin main --force
    echo "Successfully pushed to GitHub! GitHub Pages workflow will deploy in 1-2 minutes."
else
    echo ""
    echo "Repository is staged, committed, and ready to push!"
    echo "To push automatically to your GitHub repo, run:"
    echo "  ./deploy_to_github.sh https://<GITHUB_TOKEN>@github.com/<USERNAME>/<REPO_NAME>.git"
    echo "Or use AI Studio's top menu: Settings -> Push to GitHub"
fi

echo "=================================================="
