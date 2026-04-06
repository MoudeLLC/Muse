#!/bin/bash
# Build MuseOS with single thread (minimum RAM usage)

set -e

echo "🏗️  Building MuseOS ROM (Single Thread Mode)"
echo "============================================="
echo ""
echo "⚠️  This will be VERY slow (4-6 hours) but uses minimal RAM"
echo ""

cd /home/aimoude149/aosp-full

echo "⚙️  Setting up build environment..."
source build/envsetup.sh

echo "🎯 Selecting MuseOS target..."
lunch museos-eng

echo ""
echo "🔨 Building MuseOS with single thread..."
echo "   (Only 1 job at a time - slowest but safest)"
echo "   Started at: $(date)"
echo ""

# Build with only 1 job (single threaded)
make -j1 2>&1 | tee /home/aimoude149/museos-build-j1.log

echo ""
echo "======================================"
echo "✅ MuseOS ROM Build Complete!"
echo "======================================"
echo "   Finished at: $(date)"
echo ""
