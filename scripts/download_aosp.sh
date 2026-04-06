#!/bin/bash
# AOSP Download Script for MuseOS

set -e

echo "========================================="
echo "  MuseOS AOSP Download Script"
echo "========================================="

# Check prerequisites
command -v repo >/dev/null 2>&1 || {
    echo "Installing repo tool..."
    mkdir -p ~/bin
    curl https://storage.googleapis.com/git-repo-downloads/repo > ~/bin/repo
    chmod a+x ~/bin/repo
    export PATH=~/bin:$PATH
}

# Create directory
AOSP_DIR=~/museos-aosp
mkdir -p $AOSP_DIR
cd $AOSP_DIR

echo "Initializing AOSP repository..."
repo init -u https://android.googlesource.com/platform/manifest -b android-14.0.0_r1

echo "Syncing AOSP source (this will take several hours)..."
echo "Estimated download size: 200GB+"
echo "Estimated time: 4-8 hours"

repo sync -c -j$(nproc) --force-sync --no-clone-bundle --no-tags

echo "========================================="
echo "  AOSP Download Complete!"
echo "========================================="
echo "Next steps:"
echo "1. cd ~/museos-aosp"
echo "2. Run: ../Muse/scripts/integrate_museos.sh"
