#!/bin/bash
# Download AOSP source (minimal for MuseOS)

set -e

echo "📥 Downloading AOSP Source for MuseOS"
echo "======================================"
echo ""
echo "This will download ~50GB of Android source code"
echo "Estimated time: 1-3 hours depending on connection"
echo ""

read -p "Continue? (y/n): " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    exit 1
fi

AOSP_DIR=~/museos-aosp
mkdir -p $AOSP_DIR
cd $AOSP_DIR

echo "🔧 Initializing repo for Android 14..."
~/bin/repo init -u https://android.googlesource.com/platform/manifest -b android-14.0.0_r1 --depth=1

echo "📦 Syncing essential AOSP components..."
echo "   This downloads only what we need for MuseOS"

~/bin/repo sync -c -j8 --no-tags --no-clone-bundle \
    platform/frameworks/base \
    platform/frameworks/native \
    platform/packages/apps/Launcher3 \
    platform/packages/apps/Settings \
    platform/build \
    platform/system/core \
    platform/prebuilts/sdk

echo ""
echo "✅ AOSP source downloaded!"
echo "   Location: $AOSP_DIR"
echo ""
echo "Next: Run build-museos.sh to build MuseOS"
