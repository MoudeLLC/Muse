#!/bin/bash
# Download FULL Android Open Source Project (AOSP) - Automated
# This is the complete Android source code (~150GB)

set -e

echo "🚀 Full AOSP Download for MuseOS - STARTING"
echo "============================================"
echo ""
echo "Downloading COMPLETE Android 14 source code"
echo "Size: ~150GB | Time: 2-6 hours"
echo ""

# Check available space
AVAILABLE_SPACE=$(df -BG . | tail -1 | awk '{print $4}' | sed 's/G//')
echo "💾 Available: ${AVAILABLE_SPACE}GB"
echo ""

# Setup directories
AOSP_DIR=~/aosp-full
mkdir -p $AOSP_DIR
cd $AOSP_DIR

echo "🔧 Initializing repo for Android 14..."
~/bin/repo init -u https://android.googlesource.com/platform/manifest -b android-14.0.0_r1 --depth=1

echo ""
echo "📥 Syncing FULL AOSP (this will take hours)..."
echo "   Started at: $(date)"
echo ""

# Sync ALL of AOSP
~/bin/repo sync -c -j8 --no-tags --no-clone-bundle 2>&1 | tee $AOSP_DIR/sync.log

echo ""
echo "======================================"
echo "✅ Full AOSP Download Complete!"
echo "======================================"
echo "   Finished at: $(date)"
echo "   Location: $AOSP_DIR"
echo "   Size: $(du -sh $AOSP_DIR 2>/dev/null | cut -f1 || echo 'calculating...')"
echo ""
