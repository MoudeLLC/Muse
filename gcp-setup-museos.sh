#!/bin/bash
# MuseOS Google Cloud Setup Script
# Run this on your GCP VM instance

set -e

echo "🚀 MuseOS Google Cloud Build Setup"
echo "===================================="
echo ""

# Update system
echo "📦 Updating system packages..."
sudo apt-get update

# Install AOSP build dependencies
echo "🔧 Installing AOSP build dependencies..."
sudo apt-get install -y \
    git-core gnupg flex bison build-essential zip curl \
    zlib1g-dev gcc-multilib g++-multilib libc6-dev-i386 \
    libncurses5 lib32ncurses5-dev x11proto-core-dev \
    libx11-dev lib32z1-dev libgl1-mesa-dev libxml2-utils \
    xsltproc unzip fontconfig python3 python3-pip \
    openjdk-11-jdk

echo "✅ Dependencies installed"

# Install repo tool
echo "🔧 Installing repo tool..."
mkdir -p ~/bin
curl https://storage.googleapis.com/git-repo-downloads/repo > ~/bin/repo
chmod a+x ~/bin/repo
export PATH=~/bin:$PATH

echo "✅ Repo tool installed"

# Create swap (GCP allows it!)
echo "💾 Creating 16GB swap space..."
sudo fallocate -l 16G /swapfile
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile
echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab

echo "✅ Swap enabled"
free -h

# Download AOSP
echo ""
echo "📥 Downloading AOSP (this will take 1-2 hours)..."
mkdir -p ~/aosp-full
cd ~/aosp-full

~/bin/repo init -u https://android.googlesource.com/platform/manifest -b android-14.0.0_r1 --depth=1
~/bin/repo sync -c -j8 --no-tags --no-clone-bundle

echo ""
echo "======================================"
echo "✅ GCP Setup Complete!"
echo "======================================"
echo ""
echo "AOSP downloaded to: ~/aosp-full"
echo "Next steps:"
echo "  1. Upload your MuseOS modifications"
echo "  2. Run the build script"
echo ""
