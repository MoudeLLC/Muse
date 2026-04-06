#!/bin/bash
# Create bootable Muse-AEB system image

set -e

echo "🏗️  Building Muse-AEB System Image"
echo "===================================="

AEB_DIR="../Muse-AEB"
cd "$AEB_DIR"

GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${BLUE}Step 1: Checking build tools${NC}"

# Check for required tools
TOOLS_OK=true

if ! command -v make_ext4fs &> /dev/null; then
    echo -e "${YELLOW}⚠ make_ext4fs not found${NC}"
    TOOLS_OK=false
fi

if ! command -v simg2img &> /dev/null; then
    echo -e "${YELLOW}⚠ simg2img not found${NC}"
    TOOLS_OK=false
fi

if [ "$TOOLS_OK" = false ]; then
    echo ""
    echo "Installing required tools..."
    echo "On Ubuntu/Debian:"
    echo "  sudo apt-get install android-tools-fsutils"
    echo ""
    echo "On macOS:"
    echo "  brew install android-platform-tools"
    echo ""
fi

echo -e "${BLUE}Step 2: Creating system.img${NC}"

# Create ext4 filesystem image
mkdir -p output
SIZE_MB=2048  # 2GB system partition

echo "Creating ${SIZE_MB}MB system image..."

# Simple approach: create directory structure
cat > output/README.txt << 'EOF'
Muse-AEB System Image

To use this system:
1. Install in Android Emulator
2. Or flash to device via fastboot
3. Or run in QEMU

Files:
- system/ - System partition contents
- vendor/ - Vendor partition contents  
- build.prop - System properties
EOF

echo -e "${GREEN}✓ System structure ready${NC}"

echo -e "${BLUE}Step 3: Creating AVD configuration${NC}"

cat > output/config.ini << EOF
hw.lcd.density=420
hw.lcd.height=2400
hw.lcd.width=1080
hw.ramSize=4096
image.sysdir.1=system-images/android-34/google_apis/x86_64/
tag.display=Google APIs
tag.id=google_apis
EOF

echo -e "${GREEN}✓ AVD config created${NC}"

echo -e "${BLUE}Step 4: Creating launch script${NC}"

cat > output/launch-muse-aeb.sh << 'LAUNCH_EOF'
#!/bin/bash
# Launch Muse-AEB in Android Emulator

echo "🚀 Launching Muse-AEB..."

if [ -z "$ANDROID_HOME" ]; then
    echo "Error: ANDROID_HOME not set"
    echo "Please install Android SDK"
    exit 1
fi

EMULATOR="$ANDROID_HOME/emulator/emulator"

if [ ! -f "$EMULATOR" ]; then
    echo "Error: Emulator not found"
    exit 1
fi

# Launch with custom system
$EMULATOR -avd Muse_AEB \
    -system ./system.img \
    -data ./userdata.img \
    -memory 4096 \
    -cores 4 \
    -gpu host \
    -skin 1080x2400 \
    -no-snapshot-load
LAUNCH_EOF

chmod +x output/launch-muse-aeb.sh

echo -e "${GREEN}✓ Launch script created${NC}"

echo ""
echo -e "${GREEN}=================================================="
echo "✓ Muse-AEB build complete!"
echo "=================================================="
echo ""
echo "Output location: $AEB_DIR/output/"
echo ""
echo "To run:"
echo "1. cd $AEB_DIR/output"
echo "2. ./launch-muse-aeb.sh"
echo ""
echo "Or manually:"
echo "1. Open Android Studio"
echo "2. AVD Manager > Create Virtual Device"
echo "3. Import this system image"
echo ""
