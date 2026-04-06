# Building MuseOS ROM - Complete Guide

## What We're Building
A complete Android-based operating system (like Samsung's One UI) that can be:
- Flashed to devices via recovery (TWRP)
- Installed via fastboot
- Distributed as OTA updates
- Packaged as system.img, boot.img, vendor.img

## Prerequisites

### Hardware Requirements
- **CPU**: 8+ cores recommended
- **RAM**: 16GB minimum, 32GB recommended
- **Storage**: 250GB+ free space
- **OS**: Ubuntu 20.04 LTS or newer

### Software Requirements
```bash
# Install build dependencies
sudo apt-get install -y \
    git-core gnupg flex bison build-essential \
    zip curl zlib1g-dev gcc-multilib g++-multilib \
    libc6-dev-i386 libncurses5 lib32ncurses5-dev \
    x11proto-core-dev libx11-dev lib32z1-dev \
    libgl1-mesa-dev libxml2-utils xsltproc unzip \
    fontconfig python3 python-is-python3

# Install repo tool
mkdir -p ~/bin
curl https://storage.googleapis.com/git-repo-downloads/repo > ~/bin/repo
chmod a+x ~/bin/repo
export PATH=~/bin:$PATH
```

## Step 1: Download AOSP Source

```bash
# Create working directory
mkdir -p ~/museos
cd ~/museos

# Initialize AOSP repository (Android 14)
repo init -u https://android.googlesource.com/platform/manifest -b android-14.0.0_r1

# Sync source code (this takes hours)
repo sync -c -j$(nproc) --force-sync --no-clone-bundle --no-tags
```

## Step 2: Add MuseOS Components

```bash
# Clone MuseOS device tree
git clone <museos-repo> device/muse/generic

# Clone MuseOS vendor files
git clone <museos-vendor> vendor/muse

# Copy InfiniteUI system apps
cp -r <infiniteui-source> packages/apps/InfiniteUI

# Copy MuseOS framework
cp -r <museos-framework> frameworks/base/museos
```

## Step 3: Configure Build

```bash
# Set up build environment
source build/envsetup.sh

# Choose build target
lunch museos_generic-userdebug

# Available variants:
# - museos_generic-eng (Engineering build)
# - museos_generic-userdebug (Debug build)
# - museos_generic-user (Production build)
```

## Step 4: Build ROM

```bash
# Full build (takes 2-6 hours)
make -j$(nproc) bacon

# Or build specific images
make systemimage    # system.img
make bootimage      # boot.img
make vendorimage    # vendor.img
make recoveryimage  # recovery.img

# Build OTA package
make otapackage
```

## Step 5: Build Output

After successful build, find files in:
```
out/target/product/generic/
├── museos-14.0-20260405-OFFICIAL-generic.zip  # Flashable ZIP
├── system.img                                  # System partition
├── boot.img                                    # Boot image
├── vendor.img                                  # Vendor partition
├── recovery.img                                # Recovery image
└── obj/PACKAGING/target_files_intermediates/  # OTA files
```

## Step 6: Create Flashable Package

```bash
# The build system automatically creates a flashable ZIP
# Located at: out/target/product/generic/museos-*.zip

# This ZIP contains:
# - system.img
# - boot.img
# - vendor.img
# - META-INF/com/google/android/updater-script
# - Installation scripts
```

## Installation Methods

### Method 1: Custom Recovery (TWRP)
```bash
# 1. Boot device into TWRP recovery
# 2. Wipe: System, Data, Cache, Dalvik
# 3. Install ZIP: museos-*.zip
# 4. Reboot system
```

### Method 2: Fastboot
```bash
# Boot device into fastboot mode
adb reboot bootloader

# Flash images
fastboot flash system system.img
fastboot flash boot boot.img
fastboot flash vendor vendor.img
fastboot flash recovery recovery.img

# Wipe data (optional, for clean install)
fastboot -w

# Reboot
fastboot reboot
```

### Method 3: System Image (for emulator/PC)
```bash
# Create system.img for x86_64
lunch museos_x86_64-userdebug
make -j$(nproc) systemimage

# Run in emulator
emulator -system system.img -data userdata.img
```

## Creating ISO/IMG for PC Installation

### For x86_64 PC Installation
```bash
# Build x86_64 variant
lunch museos_x86_64-userdebug
make -j$(nproc) iso_img

# Output: out/target/product/x86_64/museos-x86_64.iso

# Or create bootable IMG
make -j$(nproc) usb_img
# Output: out/target/product/x86_64/museos-x86_64.img

# Write to USB drive
sudo dd if=museos-x86_64.img of=/dev/sdX bs=4M status=progress
```

## Build Variants

### 1. Phone/Tablet ROM (ARM64)
```bash
lunch museos_generic-userdebug
make -j$(nproc) bacon
# Output: Flashable ZIP for ARM64 devices
```

### 2. PC/Laptop ISO (x86_64)
```bash
lunch museos_x86_64-userdebug
make -j$(nproc) iso_img
# Output: Bootable ISO for PCs
```

### 3. Emulator Build
```bash
lunch museos_emulator-userdebug
make -j$(nproc)
emulator
```

## Customization

### Modify InfiniteUI
```bash
# Edit launcher
vim packages/apps/InfiniteUI/Launcher/src/...

# Edit system UI
vim packages/apps/InfiniteUI/SystemUI/src/...

# Rebuild
make InfiniteUILauncher
make InfiniteUISystemUI
```

### Add Pre-installed Apps
```bash
# Edit device.mk
vim device/muse/generic/device.mk

# Add to PRODUCT_PACKAGES
PRODUCT_PACKAGES += YourApp
```

### Modify Framework
```bash
# Edit MuseOS framework
vim frameworks/base/museos/...

# Rebuild framework
make framework-res
```

## OTA Updates

### Create OTA Package
```bash
# Build target files
make -j$(nproc) target-files-package

# Generate OTA
./build/tools/releasetools/ota_from_target_files \
    out/target/product/generic/obj/PACKAGING/target_files_intermediates/*.zip \
    museos-ota-update.zip
```

### Sign OTA Package
```bash
# Sign with release keys
./build/tools/releasetools/sign_target_files_apks \
    -o -d ~/.android-certs \
    target_files.zip \
    signed_target_files.zip
```

## Troubleshooting

### Build Fails
```bash
# Clean build
make clean
make clobber

# Rebuild
make -j$(nproc) bacon
```

### Out of Memory
```bash
# Reduce parallel jobs
make -j4 bacon
```

### Missing Dependencies
```bash
# Install missing packages
sudo apt-get install <package-name>
```

## Distribution

### Release Checklist
- [ ] Build signed release variant
- [ ] Test on multiple devices
- [ ] Create changelog
- [ ] Generate OTA package
- [ ] Upload to distribution server
- [ ] Update download page

### File Naming Convention
```
museos-<version>-<date>-<type>-<device>.zip

Example:
museos-1.0.0-20260405-OFFICIAL-generic.zip
museos-1.0.0-20260405-BETA-pixel7.zip
```

## Support

For device-specific builds, create device tree in:
```
device/muse/<device-codename>/
```

Follow AOSP device tree structure and adapt for your device.
