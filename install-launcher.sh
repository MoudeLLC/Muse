#!/bin/bash

# MuseOS Launcher Installation Script
# Builds and installs Muse Launcher as system app via ADB

set -e

echo "========================================="
echo "  MuseOS Launcher Installation"
echo "========================================="
echo ""

# Check if device is connected
if ! adb devices | grep -q "device$"; then
    echo "❌ Error: No Android device connected via ADB"
    echo "Please connect your device and enable USB debugging"
    exit 1
fi

echo "✓ Device connected"
echo ""

# Build the launcher APK
echo "📦 Building Muse Launcher APK..."
./gradlew :muse-launcher:assembleDebug

if [ ! -f "muse-launcher/build/outputs/apk/debug/muse-launcher-debug.apk" ]; then
    echo "❌ Error: APK build failed"
    exit 1
fi

echo "✓ APK built successfully"
echo ""

# Push APK to device
echo "📲 Installing Muse Launcher..."
adb install -r muse-launcher/build/outputs/apk/debug/muse-launcher-debug.apk

echo "✓ Muse Launcher installed"
echo ""

# Set as default launcher
echo "🏠 Setting as default launcher..."
echo "Please select 'Muse Launcher' and tap 'Always' on your device"
adb shell am start -a android.intent.action.MAIN -c android.intent.category.HOME

echo ""
echo "========================================="
echo "  ✓ Installation Complete!"
echo "========================================="
echo ""
echo "Muse Launcher is now installed."
echo "Press the home button to use it."
echo ""
