#!/bin/bash
# Build MuseOS with lower memory usage

set -e

echo "🏗️  Building MuseOS ROM (Low Memory Mode)"
echo "=========================================="
echo ""

cd /home/aimoude149/aosp-full

echo "⚙️  Setting up build environment..."
source build/envsetup.sh

echo "🎯 Selecting MuseOS target..."
lunch museos-eng

echo ""
echo "🔨 Building MuseOS with reduced parallelism..."
echo "   (Using fewer parallel jobs to save RAM)"
echo "   Started at: $(date)"
echo ""

# Build with only 4 parallel jobs instead of all cores
make -j4 2>&1 | tee /home/aimoude149/museos-build.log

echo ""
echo "======================================"
echo "✅ MuseOS ROM Build Complete!"
echo "======================================"
echo "   Finished at: $(date)"
echo ""
