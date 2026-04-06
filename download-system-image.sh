#!/bin/bash
# Download Android Emulator System Image

set -e

echo "📦 Downloading Android Emulator System Image"
echo "============================================="

AEB_DIR="../Muse-AEB"
cd "$AEB_DIR"

# Set Android SDK path if not already set
if [ -z "$ANDROID_HOME" ]; then
    if [ -d "$HOME/android-sdk" ]; then
        export ANDROID_HOME="$HOME/android-sdk"
        export PATH="$PATH:$ANDROID_HOME/cmdline-tools/latest/bin"
        echo "✓ Found Android SDK at $ANDROID_HOME"
    fi
fi

# Check if sdkmanager is available
if command -v sdkmanager &> /dev/null; then
    echo "✓ Using sdkmanager to download system image"
    
    # Download Android 14 (API 34) x86_64 system image
    sdkmanager "system-images;android-34;google_apis;x86_64"
    
    # Copy to our directory
    ANDROID_SDK="$ANDROID_HOME"
    SYS_IMG="$ANDROID_SDK/system-images/android-34/google_apis/x86_64"
    
    if [ -d "$SYS_IMG" ]; then
        echo "✓ Copying system image files..."
        cp -r "$SYS_IMG"/* ./base-system/
        echo "✓ System image ready!"
    else
        echo "✗ System image not found after download"
        exit 1
    fi
else
    echo "⚠ sdkmanager not found"
    echo ""
    echo "Please install Android SDK:"
    echo "1. Download Android Studio: https://developer.android.com/studio"
    echo "2. Or install command-line tools: https://developer.android.com/studio#command-tools"
    echo ""
    echo "Alternative: Manual download"
    echo "Visit: https://developer.android.com/studio/emulator_archive"
    echo "Download system image and extract to: $AEB_DIR/base-system/"
fi
