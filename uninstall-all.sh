#!/bin/bash

# MuseOS Uninstallation Script
# Removes all MuseOS components and restores defaults

set -e

echo "========================================="
echo "  MuseOS Uninstallation"
echo "========================================="
echo ""
echo "⚠️  WARNING: This will remove all MuseOS apps"
echo ""
echo "Press Enter to continue or Ctrl+C to cancel..."
read

# Check if device is connected
if ! adb devices | grep -q "device$"; then
    echo "❌ Error: No Android device connected via ADB"
    exit 1
fi

echo "✓ Device connected"
echo ""

# Stop SystemUI service
echo "🛑 Stopping SystemUI service..."
adb shell am force-stop com.muse.systemui || true

# Uninstall apps
echo "🗑️  Uninstalling Muse Launcher..."
adb uninstall com.muse.launcher || echo "  (not installed)"

echo "🗑️  Uninstalling Muse SystemUI..."
adb uninstall com.muse.systemui || echo "  (not installed)"

echo "🗑️  Uninstalling Muse Settings..."
adb uninstall com.muse.settings || echo "  (not installed)"

echo ""
echo "🏠 Restoring default launcher..."
echo "Please select your default launcher on the device"
adb shell am start -a android.intent.action.MAIN -c android.intent.category.HOME

echo ""
echo "========================================="
echo "  ✓ Uninstallation Complete!"
echo "========================================="
echo ""
echo "All MuseOS components have been removed."
echo "Your device is back to its default state."
echo ""
