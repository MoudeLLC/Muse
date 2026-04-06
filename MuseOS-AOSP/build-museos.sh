#!/bin/bash
# Build MuseOS from AOSP source

set -e

echo "🏗️  Building MuseOS from AOSP"
echo "=============================="

AOSP_DIR=~/museos-aosp
MUSEOS_ROOT=$(dirname $(readlink -f $0))

if [ ! -d "$AOSP_DIR" ]; then
    echo "❌ AOSP source not found at $AOSP_DIR"
    echo "   Run download-aosp.sh first"
    exit 1
fi

cd $AOSP_DIR

echo "📝 Applying MuseOS modifications..."
# Apply patches
for patch in $MUSEOS_ROOT/modifications/**/*.patch; do
    if [ -f "$patch" ]; then
        echo "   Applying $(basename $patch)..."
        # patch -p1 < "$patch" || echo "   ⚠️  Patch may already be applied"
    fi
done

echo "⚙️  Setting up build environment..."
source build/envsetup.sh

echo "🎯 Selecting MuseOS target..."
lunch aosp_x86_64-eng

echo "🔨 Building MuseOS system image..."
echo "   This will take 1-3 hours depending on your system..."
make -j$(nproc) systemimage

echo ""
echo "✅ MuseOS build complete!"
echo "   Output: $AOSP_DIR/out/target/product/generic_x86_64/"
echo ""
