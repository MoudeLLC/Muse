#!/bin/bash
# MuseOS GitHub Codespaces Setup Script
# Run this INSIDE GitHub Codespaces after creating your codespace

set -e

echo "🚀 MuseOS GitHub Codespaces Build"
echo "===================================="
echo ""

# Install dependencies
echo "📦 Installing AOSP build dependencies..."
sudo apt-get update
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
echo 'export PATH=~/bin:$PATH' >> ~/.bashrc

echo "✅ Repo tool installed"

# Download AOSP
echo ""
echo "📥 Downloading AOSP source code..."
echo "   This will take 1-2 hours (108GB download)"
echo ""

mkdir -p ~/aosp-full
cd ~/aosp-full

echo "Initializing AOSP repository..."
~/bin/repo init -u https://android.googlesource.com/platform/manifest -b android-14.0.0_r1 --depth=1

echo "Syncing AOSP source (this is the long part)..."
~/bin/repo sync -c -j8 --no-tags --no-clone-bundle

echo ""
echo "✅ AOSP downloaded successfully!"
echo ""

# Copy MuseOS device configuration
echo "📝 Setting up MuseOS device configuration..."

# Check if device config exists in workspace
if [ -d "/workspaces/MuseOS/device/muse" ]; then
    echo "   Copying device config from workspace..."
    mkdir -p ~/aosp-full/device/muse
    cp -r /workspaces/MuseOS/device/muse/* ~/aosp-full/device/muse/
elif [ -d "$HOME/aosp-full/device/muse/museos" ]; then
    echo "   Device config already exists"
else
    echo "   Creating MuseOS device configuration..."
    mkdir -p ~/aosp-full/device/muse/museos
    
    # We'll create the config files here
    cat > ~/aosp-full/device/muse/museos/AndroidProducts.mk << 'EOF'
PRODUCT_MAKEFILES := \
    $(LOCAL_DIR)/museos.mk

COMMON_LUNCH_CHOICES := \
    museos-eng \
    museos-userdebug \
    museos-user
EOF

    cat > ~/aosp-full/device/muse/museos/museos.mk << 'EOF'
$(call inherit-product, $(SRC_TARGET_DIR)/product/aosp_x86_64.mk)

PRODUCT_NAME := museos
PRODUCT_DEVICE := museos
PRODUCT_BRAND := Muse
PRODUCT_MODEL := MuseOS
PRODUCT_MANUFACTURER := Muse

PRODUCT_PROPERTY_OVERRIDES += \
    ro.build.display.id=MuseOS \
    ro.product.brand=Muse \
    ro.product.name=MuseOS
EOF

    cat > ~/aosp-full/device/muse/museos/BoardConfig.mk << 'EOF'
include build/make/target/board/generic_x86_64/BoardConfig.mk

TARGET_BOARD_PLATFORM := museos
BOARD_USES_GENERIC_AUDIO := true
USE_CAMERA_STUB := true

# Vendor
TARGET_COPY_OUT_VENDOR := vendor
BOARD_USES_VENDORIMAGE := true
EOF

    cat > ~/aosp-full/device/muse/museos/device.mk << 'EOF'
$(call inherit-product, $(SRC_TARGET_DIR)/product/aosp_x86_64.mk)

PRODUCT_PACKAGES += \
    Launcher3 \
    Settings
EOF
fi

echo "✅ MuseOS device configuration ready"

# Build MuseOS
echo ""
echo "🔨 Building MuseOS ROM..."
echo "   This will take 2-3 hours"
echo ""

cd ~/aosp-full
source build/envsetup.sh
lunch museos-eng

echo "Starting build with 8 parallel jobs..."
make -j8

echo ""
echo "======================================"
echo "✅ MuseOS Build Complete!"
echo "======================================"
echo ""
echo "ROM files location:"
echo "  ~/aosp-full/out/target/product/museos/"
echo ""
echo "Key files:"
echo "  - system.img"
echo "  - vendor.img"
echo "  - boot.img"
echo "  - ramdisk.img"
echo ""
echo "To download these files:"
echo "  1. Right-click on files in VS Code file explorer"
echo "  2. Select 'Download'"
echo ""
