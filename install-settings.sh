#!/bin/bash

# MuseOS Settings Installation Script
# Builds and installs Muse Settings via ADB

set -e

echo "========================================="
echo "  MuseOS Settings Installation"
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

# Build the Settings APK
echo "📦 Building Muse Settings APK..."
./gradlew :muse-settings:assembleDebug

if [ ! -f "muse-settings/build/outputs/apk/debug/muse-settings-debug.apk" ]; then
    echo "❌ Error: APK build failed"
    exit 1
fi

echo "✓ APK built successfully"
echo ""

# Install APK
echo "📲 Installing Muse Settings..."
adb install -r muse-settings/build/outputs/apk/debug/muse-settings-debug.apk

echo "✓ Muse Settings installed"
echo ""

# Grant necessary permissions
echo "🔐 Granting permissions..."
adb shell pm grant com.muse.settings android.permission.WRITE_SETTINGS
adb shell pm grant com.muse.settings android.permission.CHANGE_WIFI_STATE
adb shell pm grant com.muse.settings android.permission.BLUETOOTH_ADMIN

echo "✓ Permissions granted"
echo ""

# Launch settings
echo "🚀 Launching Muse Settings..."
adb shell am start -n com.muse.settings/.MuseSettingsActivity

echo ""
echo "========================================="
echo "  ✓ Installation Complete!"
echo "========================================="
echo ""
echo "Muse Settings is now installed and running."
echo "You can access it from your app drawer or by"
echo "opening system settings."
echo ""
