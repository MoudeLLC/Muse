#!/bin/bash
# Integrate MuseOS into AOSP

set -e

AOSP_DIR=~/museos-aosp
MUSE_DIR=$(pwd)

echo "========================================="
echo "  Integrating MuseOS into AOSP"
echo "========================================="

cd $AOSP_DIR

# Copy device tree
echo "Copying device tree..."
mkdir -p device/muse
cp -r $MUSE_DIR/rom/device/muse/* device/muse/

# Copy vendor files
echo "Copying vendor files..."
mkdir -p vendor/muse
cp -r $MUSE_DIR/rom/vendor/muse/* vendor/muse/

# Copy InfiniteUI system apps
echo "Copying InfiniteUI..."
mkdir -p packages/apps/InfiniteUI
cp -r $MUSE_DIR/infiniteui/* packages/apps/InfiniteUI/

# Copy MuseOS framework
echo "Copying MuseOS framework..."
mkdir -p frameworks/base/museos
cp -r $MUSE_DIR/museos/* frameworks/base/museos/

# Copy GaxialAI
echo "Copying GaxialAI..."
mkdir -p packages/apps/GaxialAI
cp -r $MUSE_DIR/gaxialai/* packages/apps/GaxialAI/

echo "========================================="
echo "  Integration Complete!"
echo "========================================="
echo "Next steps:"
echo "1. source build/envsetup.sh"
echo "2. lunch museos_generic-userdebug"
echo "3. make -j\$(nproc) bacon"
