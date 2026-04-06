#!/bin/bash

# MuseOS SystemUI Installation Script
# Builds and installs Muse SystemUI via ADB

set -e

echo "========================================="
echo "  MuseOS SystemUI Installation"
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

# Build the SystemUI APK
echo "📦 Building Muse SystemUI APK..."
./gradlew :muse-systemui:assembleDebug

if [ ! -f "muse-systemui/build/outputs/apk/debug/muse-systemui-debug.apk" ]; then
    echo "❌ Error: APK build failed"
    exit 1
fi

echo "✓ APK built successfully"
echo ""

# Install APK
echo "📲 Installing Muse SystemUI..."
adb install -r muse-systemui/build/outputs/apk/debug/muse-systemui-debug.apk

echo "✓ Muse SystemUI installed"
echo ""

# Grant necessary permissions
echo "🔐 Granting permissions..."
adb shell pm grant com.muse.systemui android.permission.SYSTEM_ALERT_WINDOW
adb shell pm grant com.muse.systemui android.permission.BIND_NOTIFICATION_LISTENER_SERVICE

echo "✓ Permissions granted"
echo ""

# Start SystemUI service
echo "🚀 Starting SystemUI service..."
adb shell am start-foreground-service com.muse.systemui/.SystemUIService

echo ""
echo "========================================="
echo "  ✓ Installation Complete!"
echo "========================================="
echo ""
echo "Muse SystemUI is now running."
echo "You should see the status bar overlay."
echo ""
echo "Note: You may need to grant notification access manually:"
echo "Settings → Apps → Muse SystemUI → Permissions"
echo ""
