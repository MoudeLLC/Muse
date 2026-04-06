# MuseOS GitHub Codespaces Build Guide

## Complete Step-by-Step Instructions

### Step 1: Create GitHub Account
1. Go to https://github.com
2. Click "Sign up"
3. Follow the registration process
4. Verify your email

### Step 2: Create New Repository
1. Click the "+" icon in top-right corner
2. Select "New repository"
3. Repository name: `MuseOS`
4. Choose "Public" or "Private"
5. **DO NOT** initialize with README
6. Click "Create repository"

### Step 3: Upload Your Code to GitHub

Open your terminal and run these commands:

```bash
cd ~/Muse

# Initialize git repository
git init

# Add all files
git add .

# Commit files
git commit -m "MuseOS initial commit"

# Add your GitHub repository as remote
# Replace YOUR_USERNAME with your actual GitHub username
git remote add origin https://github.com/YOUR_USERNAME/MuseOS.git

# Rename branch to main
git branch -M main

# Push to GitHub
git push -u origin main
```

**Note:** You'll be asked for your GitHub username and password. For password, use a Personal Access Token:
- Go to GitHub Settings → Developer settings → Personal access tokens → Tokens (classic)
- Generate new token with "repo" permissions
- Use this token as your password

### Step 4: Create Codespace

1. Go to your GitHub repository: `https://github.com/YOUR_USERNAME/MuseOS`
2. Click the green "Code" button
3. Click the "Codespaces" tab
4. Click "Create codespace on main"
5. **IMPORTANT:** When prompted, select machine type:
   - Choose: **8-core, 32 GB RAM, 128 GB storage**
   - This is CRITICAL for the build to succeed!

### Step 5: Build MuseOS in Codespaces

Once your Codespace opens (it looks like VS Code in your browser):

1. Open the terminal (it should open automatically, or press Ctrl+`)

2. Run the setup script:
```bash
cd /workspaces/MuseOS
chmod +x codespaces-setup.sh
./codespaces-setup.sh
```

3. Wait for the build to complete:
   - AOSP download: 1-2 hours
   - Build: 2-3 hours
   - **Total: 3-5 hours**

4. The script will automatically:
   - Install all dependencies
   - Download AOSP source code
   - Configure MuseOS device
   - Build the ROM

### Step 6: Download Your ROM

After the build completes:

1. In VS Code file explorer, navigate to:
   ```
   /home/codespaces/aosp-full/out/target/product/museos/
   ```

2. Download these files:
   - `system.img` - Main system image
   - `vendor.img` - Vendor partition
   - `boot.img` - Boot image
   - `ramdisk.img` - RAM disk

3. Right-click each file → "Download"

### Step 7: Test on Your Local Machine

Once downloaded, test the ROM:

```bash
# On your local machine
cd ~/android-sdk/emulator

# Create AVD if you haven't
./emulator -avd MuseOS_Test \
  -system /path/to/downloaded/system.img \
  -vendor /path/to/downloaded/vendor.img
```

## Troubleshooting

### "Out of disk space"
- Codespaces has 128GB, which should be enough
- If you run out, delete the `~/aosp-full/.repo` folder after sync completes

### "Build failed - out of memory"
- Make sure you selected **8-core, 32GB RAM** machine type
- You can check with: `free -h`
- If you have less than 32GB, delete the codespace and create a new one with correct specs

### "Permission denied" when pushing to GitHub
- Use a Personal Access Token instead of password
- GitHub Settings → Developer settings → Personal access tokens
- Generate token with "repo" scope

### Build is taking too long
- AOSP download: 1-2 hours is normal
- Build: 2-3 hours is normal
- Don't close the browser tab - codespace will pause

## Cost

- **FREE** for 120 hours/month
- Your build will take ~4 hours
- You can use Codespaces for ~30 builds per month for free!

## Tips

- Keep the browser tab open during build
- Codespace auto-saves your work
- You can pause/resume anytime
- Download ROM files before deleting the codespace

## What You'll Get

After completion, you'll have:
- ✅ Full MuseOS ROM built from AOSP
- ✅ Bootable system.img
- ✅ Custom MuseOS branding
- ✅ Ready to flash to emulator or device

## Next Steps After Build

1. Download ROM files
2. Test in Android Emulator
3. Customize MuseOS further
4. Add your custom apps (muse-launcher, muse-systemui, etc.)
5. Rebuild with customizations

---

**Ready to start? Follow Step 1 above!**
