# AOSP Integration Guide for MuseOS

## Step 1: Download AOSP Source

### Prerequisites
```bash
# Install repo tool
mkdir -p ~/bin
curl https://storage.googleapis.com/git-repo-downloads/repo > ~/bin/repo
chmod a+x ~/bin/repo
export PATH=~/bin:$PATH

# Install dependencies
sudo apt-get install -y git-core gnupg flex bison build-essential \
    zip curl zlib1g-dev gcc-multilib g++-multilib libc6-dev-i386 \
    libncurses5 lib32ncurses5-dev x11proto-core-dev libx11-dev \
    lib32z1-dev libgl1-mesa-dev libxml2-utils xsltproc unzip fontconfig
```

### Download AOSP
```bash
# Create directory
mkdir -p ~/museos-aosp
cd ~/museos-aosp

# Initialize repo (Android 14)
repo init -u https://android.googlesource.com/platform/manifest -b android-14.0.0_r1

# Sync source (this takes 4-8 hours and 200GB+)
repo sync -c -j$(nproc) --force-sync --no-clone-bundle --no-tags
```

## Step 2: Add MuseOS Device Tree

```bash
# Clone MuseOS device tree
cd ~/museos-aosp
git clone <museos-repo> device/muse/generic

# Clone vendor files
git clone <museos-vendor> vendor/muse

# Add InfiniteUI to system apps
mkdir -p packages/apps/InfiniteUI
cp -r <infiniteui-source>/* packages/apps/InfiniteUI/

# Add MuseOS framework
mkdir -p frameworks/base/museos
cp -r <museos-framework>/* frameworks/base/museos/
```

## Step 3: Build Configuration

```bash
# Set up environment
source build/envsetup.sh

# List available lunch targets
lunch

# Select MuseOS target
lunch museos_generic-userdebug
```

## Step 4: Build ROM

### Full Build
```bash
# Clean build (first time)
make clobber

# Build ROM (takes 2-6 hours)
make -j$(nproc) bacon

# Output: out/target/product/generic/museos-*.zip
```

### Build Individual Images
```bash
# System image
make systemimage

# Boot image
make bootimage

# Vendor image
make vendorimage

# Recovery image
make recoveryimage
```

## Step 5: Create Flashable ZIP

The build system automatically creates a flashable ZIP at:
```
out/target/product/generic/museos-1.0.0-20260405-OFFICIAL-generic.zip
```

### Manual ZIP Creation
```bash
cd out/target/product/generic

# Create ZIP structure
mkdir -p flashable/META-INF/com/google/android

# Copy images
cp system.img flashable/
cp boot.img flashable/
cp vendor.img flashable/

# Create updater-script
cat > flashable/META-INF/com/google/android/updater-script << 'EOF'
ui_print("Installing MuseOS...");
package_extract_file("system.img", "/dev/block/bootdevice/by-name/system");
package_extract_file("boot.img", "/dev/block/bootdevice/by-name/boot");
package_extract_file("vendor.img", "/dev/block/bootdevice/by-name/vendor");
ui_print("MuseOS installed!");
EOF

# Create ZIP
cd flashable
zip -r ../museos-flashable.zip ./*
```

## Step 6: Test in Emulator

```bash
# Build for emulator
lunch museos_emulator-userdebug
make -j$(nproc)

# Run emulator
emulator
```

## Step 7: Flash to Device

### Via TWRP Recovery
1. Boot device into TWRP
2. Wipe: System, Data, Cache, Dalvik
3. Install ZIP: museos-*.zip
4. Reboot

### Via Fastboot
```bash
# Boot into fastboot
adb reboot bootloader

# Flash images
fastboot flash system system.img
fastboot flash boot boot.img
fastboot flash vendor vendor.img
fastboot flash recovery recovery.img

# Wipe data (optional)
fastboot -w

# Reboot
fastboot reboot
```

## Build Variants

### ARM64 (Phones/Tablets)
```bash
lunch museos_generic-userdebug
make -j$(nproc) bacon
```

### x86_64 (PCs)
```bash
lunch museos_x86_64-userdebug
make -j$(nproc) iso_img
```

### Emulator
```bash
lunch museos_emulator-userdebug
make -j$(nproc)
emulator
```

## Troubleshooting

### Out of Memory
```bash
# Reduce parallel jobs
make -j4 bacon
```

### Build Fails
```bash
# Clean and rebuild
make clean
make -j$(nproc) bacon
```

### Missing Dependencies
```bash
# Install missing packages
sudo apt-get install <package-name>
```

## Next Steps

1. ✅ Download AOSP source
2. ✅ Add MuseOS device tree
3. ✅ Build system.img
4. ✅ Build boot.img
5. ✅ Create flashable ZIP
6. ✅ Test in emulator
7. ✅ Flash to device
8. ✅ Create OTA updates

## Estimated Time

- AOSP Download: 4-8 hours
- First Build: 2-6 hours
- Subsequent Builds: 30-60 minutes
- Total: ~1 day for complete setup
