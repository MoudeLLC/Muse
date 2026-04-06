#!/bin/bash
# MuseOS ROM Build Environment Setup Script

set -e

echo "========================================="
echo "MuseOS ROM Build Environment Setup"
echo "========================================="
echo ""

# Check system requirements
echo "Checking system requirements..."
TOTAL_RAM=$(free -g | awk '/^Mem:/{print $2}')
if [ "$TOTAL_RAM" -lt 16 ]; then
    echo "⚠️  WARNING: Less than 16GB RAM detected. Build may fail."
    echo "   Recommended: 32GB+ RAM"
fi

FREE_SPACE=$(df -BG . | awk 'NR==2 {print $4}' | sed 's/G//')
if [ "$FREE_SPACE" -lt 250 ]; then
    echo "⚠️  WARNING: Less than 250GB free space. Build may fail."
    echo "   Recommended: 300GB+ free space"
fi

# Install dependencies
echo ""
echo "Installing build dependencies..."
sudo apt-get update
sudo apt-get install -y \
    git-core gnupg flex bison build-essential zip curl \
    zlib1g-dev gcc-multilib g++-multilib libc6-dev-i386 \
    libncurses5 lib32ncurses5-dev x11proto-core-dev \
    libx11-dev lib32z1-dev libgl1-mesa-dev libxml2-utils \
    xsltproc unzip fontconfig python3 python-is-python3 \
    bc cpio rsync schedtool lzop pngcrush imagemagick \
    openjdk-11-jdk

# Install repo tool
echo ""
echo "Installing repo tool..."
mkdir -p ~/bin
curl https://storage.googleapis.com/git-repo-downloads/repo > ~/bin/repo
chmod a+x ~/bin/repo
export PATH=~/bin:$PATH

# Create build directory
echo ""
echo "Creating MuseOS build directory..."
mkdir -p ~/museos-rom
cd ~/museos-rom

echo ""
echo "========================================="
echo "Setup Complete!"
echo "========================================="
echo ""
echo "Next steps:"
echo "1. Choose your base:"
echo "   - AOSP (pure Android): repo init -u https://android.googlesource.com/platform/manifest -b android-14.0.0_r1"
echo "   - LineageOS (easier): repo init -u https://github.com/LineageOS/android.git -b lineage-21.0"
echo ""
echo "2. Sync source code (takes hours):"
echo "   cd ~/museos-rom"
echo "   repo sync -c -j\$(nproc --all)"
echo ""
echo "3. Add MuseOS customizations"
echo ""
echo "4. Build ROM:"
echo "   source build/envsetup.sh"
echo "   lunch museos_<device>-userdebug"
echo "   make -j\$(nproc --all)"
echo ""
