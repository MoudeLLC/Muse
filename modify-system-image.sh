#!/bin/bash
# Modify Android system.img to inject MuseOS components

set -e

echo "🔧 MuseOS System Image Modifier"
echo "================================"

AEB_DIR="../Muse-AEB"
WORK_DIR="$AEB_DIR/work"
SYSTEM_IMG="$AEB_DIR/base-system/system.img"
MOUNT_POINT="$WORK_DIR/system_mount"

GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

# Check if running as root
if [ "$EUID" -ne 0 ]; then 
    echo -e "${YELLOW}⚠ This script needs root access to mount images${NC}"
    echo "Re-running with sudo..."
    exec sudo bash "$0" "$@"
fi

echo -e "${BLUE}Step 1: Preparing work directory${NC}"
mkdir -p "$WORK_DIR"
mkdir -p "$MOUNT_POINT"

echo -e "${BLUE}Step 2: Converting sparse image to raw${NC}"
if [ ! -f "$WORK_DIR/system.raw.img" ]; then
    # Check if simg2img is available
    if command -v simg2img &> /dev/null; then
        simg2img "$SYSTEM_IMG" "$WORK_DIR/system.raw.img"
        echo -e "${GREEN}✓ Converted to raw image${NC}"
    else
        echo -e "${YELLOW}⚠ simg2img not found, trying direct copy${NC}"
        cp "$SYSTEM_IMG" "$WORK_DIR/system.raw.img"
    fi
else
    echo -e "${GREEN}✓ Raw image already exists${NC}"
fi

echo -e "${BLUE}Step 3: Resizing image to add space for MuseOS${NC}"
# Add 500MB for our components
e2fsck -f -y "$WORK_DIR/system.raw.img" || true
resize2fs "$WORK_DIR/system.raw.img" 5G
echo -e "${GREEN}✓ Image resized${NC}"

echo -e "${BLUE}Step 4: Mounting system image${NC}"
mount -o loop "$WORK_DIR/system.raw.img" "$MOUNT_POINT"
echo -e "${GREEN}✓ System image mounted at $MOUNT_POINT${NC}"

echo -e "${BLUE}Step 5: Injecting MuseOS components${NC}"

# Create directories if they don't exist
mkdir -p "$MOUNT_POINT/system/priv-app/MuseOS"
mkdir -p "$MOUNT_POINT/system/framework"
mkdir -p "$MOUNT_POINT/vendor/overlay/infiniteui"

# Copy MuseOS APK
if [ -f "$AEB_DIR/system/priv-app/MuseOS.apk" ]; then
    cp "$AEB_DIR/system/priv-app/MuseOS.apk" "$MOUNT_POINT/system/priv-app/MuseOS/"
    chmod 644 "$MOUNT_POINT/system/priv-app/MuseOS/MuseOS.apk"
    echo -e "${GREEN}✓ MuseOS.apk injected${NC}"
fi

# Copy InfiniteUI SDK
if [ -f "$AEB_DIR/system/framework/infiniteui-sdk.aar" ]; then
    cp "$AEB_DIR/system/framework/infiniteui-sdk.aar" "$MOUNT_POINT/system/framework/"
    chmod 644 "$MOUNT_POINT/system/framework/infiniteui-sdk.aar"
    echo -e "${GREEN}✓ InfiniteUI SDK injected${NC}"
fi

# Copy theme overlays
if [ -d "$AEB_DIR/vendor/overlay/infiniteui" ]; then
    cp -r "$AEB_DIR/vendor/overlay/infiniteui"/* "$MOUNT_POINT/vendor/overlay/infiniteui/"
    echo -e "${GREEN}✓ Theme overlays injected${NC}"
fi

echo -e "${BLUE}Step 6: Modifying build.prop${NC}"
BUILD_PROP="$MOUNT_POINT/system/build.prop"

if [ -f "$BUILD_PROP" ]; then
    # Backup original
    cp "$BUILD_PROP" "$BUILD_PROP.original"
    
    # Add MuseOS properties
    cat >> "$BUILD_PROP" << 'EOF'

# MuseOS Customizations
ro.build.id=MuseOS
ro.build.display.id=Muse-AEB 1.0.0
ro.product.brand=Muse
ro.product.name=museos
ro.build.product=museos

# InfiniteUI
ro.infiniteui.version=1.0.0
ro.infiniteui.enabled=true

# GaxialAI
ro.gaxialai.version=1.0.0
ro.gaxialai.assistant=genna
EOF
    
    echo -e "${GREEN}✓ build.prop modified${NC}"
fi

echo -e "${BLUE}Step 7: Setting permissions${NC}"
chmod -R 755 "$MOUNT_POINT/system/priv-app"
chmod -R 644 "$MOUNT_POINT/system/priv-app/MuseOS"/*
chmod 755 "$MOUNT_POINT/system/priv-app/MuseOS"

echo -e "${BLUE}Step 8: Unmounting${NC}"
sync
umount "$MOUNT_POINT"
echo -e "${GREEN}✓ Unmounted${NC}"

echo -e "${BLUE}Step 9: Converting back to sparse image${NC}"
if command -v img2simg &> /dev/null; then
    img2simg "$WORK_DIR/system.raw.img" "$AEB_DIR/museos-system.img"
    echo -e "${GREEN}✓ Created sparse image${NC}"
else
    cp "$WORK_DIR/system.raw.img" "$AEB_DIR/museos-system.img"
    echo -e "${YELLOW}⚠ img2simg not found, using raw image${NC}"
fi

echo ""
echo -e "${GREEN}=================================================="
echo "✓ MuseOS System Image Created!"
echo "=================================================="
echo ""
echo "Modified system image: $AEB_DIR/museos-system.img"
echo ""
echo "To use:"
echo "1. Update AVD to use this system image"
echo "2. Or launch with: emulator -avd Muse_AEB -system $AEB_DIR/museos-system.img"
echo ""
