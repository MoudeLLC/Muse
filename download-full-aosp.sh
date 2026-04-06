#!/bin/bash
# Download FULL Android Open Source Project (AOSP)
# This is the complete Android source code (~150GB)

set -e

echo "🚀 Full AOSP Download for MuseOS"
echo "================================="
echo ""
echo "This will download the COMPLETE Android source code"
echo ""
echo "📊 Requirements:"
echo "   - Disk space: ~150GB for source + 50GB for build = 200GB total"
echo "   - Time: 2-6 hours (depends on internet speed)"
echo "   - RAM: 16GB minimum for building"
echo ""

# Check available space
AVAILABLE_SPACE=$(df -BG . | tail -1 | awk '{print $4}' | sed 's/G//')
echo "💾 Available disk space: ${AVAILABLE_SPACE}GB"

if [ "$AVAILABLE_SPACE" -lt 200 ]; then
    echo "⚠️  Warning: Recommended 200GB, you have ${AVAILABLE_SPACE}GB"
    echo "   Build may fail if space runs out"
    echo ""
fi

read -p "Download FULL AOSP? This will take several hours (y/n): " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "Cancelled."
    exit 1
fi

# Setup directories
AOSP_DIR=~/aosp-full
mkdir -p $AOSP_DIR
cd $AOSP_DIR

echo ""
echo "🔧 Initializing repo for Android 14 (full source)..."
echo "   This gets the complete Android operating system"
echo ""

# Initialize repo for full AOSP
~/bin/repo init -u https://android.googlesource.com/platform/manifest -b android-14.0.0_r1

echo ""
echo "📥 Starting full AOSP sync..."
echo "   Downloading ~150GB of source code"
echo "   This will take 2-6 hours depending on your connection"
echo ""
echo "   You can monitor progress below:"
echo "   - Each line shows a component being downloaded"
echo "   - Progress percentage will update"
echo ""

# Sync ALL of AOSP (full download)
~/bin/repo sync -c -j8 --no-tags

echo ""
echo "======================================"
echo "✅ Full AOSP Download Complete!"
echo "======================================"
echo ""
echo "📁 Location: $AOSP_DIR"
echo "📊 Size: $(du -sh $AOSP_DIR | cut -f1)"
echo ""
echo "What you now have:"
echo "  ✅ Complete Android 14 source code"
echo "  ✅ All frameworks, services, apps"
echo "  ✅ Native code (C/C++)"
echo "  ✅ Build system"
echo "  ✅ Kernel sources"
echo "  ✅ Everything needed to build a complete OS"
echo ""
echo "Next: Modify the source code for MuseOS"
echo ""
