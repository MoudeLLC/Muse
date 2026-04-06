#!/bin/bash
# Build MuseOS ROM

set -e

AOSP_DIR=~/museos-aosp

echo "========================================="
echo "  Building MuseOS ROM"
echo "========================================="

cd $AOSP_DIR

# Set up environment
source build/envsetup.sh

# Select build target
lunch museos_generic-userdebug

# Build ROM
echo "Starting build (this will take 2-6 hours)..."
make -j$(nproc) bacon

echo "========================================="
echo "  Build Complete!"
echo "========================================="
echo "Output: out/target/product/generic/museos-*.zip"
echo ""
echo "To flash:"
echo "1. Boot device into TWRP recovery"
echo "2. Wipe System, Data, Cache"
echo "3. Flash the ZIP file"
echo "4. Reboot"
