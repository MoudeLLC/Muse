#!/bin/bash
# Create Muse-AEB Android Virtual Device

set -e

echo "📱 Creating Muse-AEB Virtual Device"
echo "===================================="

# Set Android SDK path
export ANDROID_HOME=~/android-sdk
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$ANDROID_HOME/emulator

AEB_DIR="../Muse-AEB"

echo "✓ Android SDK: $ANDROID_HOME"

# Create AVD with the system image we downloaded
echo "Creating AVD 'Muse_AEB'..."

avdmanager create avd \
    -n Muse_AEB \
    -k "system-images;android-34;google_apis;x86_64" \
    -d "pixel_5" \
    -f

echo "✓ AVD created successfully!"

# Update AVD config
AVD_PATH="$HOME/.android/avd/Muse_AEB.avd"

if [ -d "$AVD_PATH" ]; then
    echo "Configuring AVD..."
    
    # Update config.ini with better settings
    cat >> "$AVD_PATH/config.ini" << EOF

# Muse-AEB Custom Settings
hw.ramSize=4096
hw.lcd.density=420
hw.lcd.height=2400
hw.lcd.width=1080
hw.gpu.enabled=yes
hw.gpu.mode=host
disk.dataPartition.size=2048M
EOF
    
    echo "✓ AVD configured!"
fi

echo ""
echo "===================================="
echo "✓ Muse-AEB AVD Ready!"
echo "===================================="
echo ""
echo "To launch:"
echo "  ./launch-muse-aeb.sh"
echo ""
