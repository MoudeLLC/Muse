# MuseOS ROM Distribution Guide

## Distribution Formats

MuseOS can be distributed in multiple formats for different use cases:

### 1. Flashable ZIP (Primary Distribution)
**Target**: Android phones and tablets  
**Format**: `.zip` file  
**Installation**: Via custom recovery (TWRP/CWM)

**Contents**:
- `system.img` - System partition
- `boot.img` - Kernel and ramdisk
- `vendor.img` - Vendor binaries
- `META-INF/` - Installation scripts

**File naming**: `museos-1.0.0-20260405-OFFICIAL-generic.zip`

**Installation steps**:
1. Boot into TWRP recovery
2. Wipe System, Data, Cache
3. Flash ZIP file
4. Reboot

### 2. Fastboot Images
**Target**: Direct device flashing  
**Format**: Individual `.img` files  
**Installation**: Via fastboot commands

**Files**:
- `system.img`
- `boot.img`
- `vendor.img`
- `recovery.img`
- `userdata.img`

**Installation**:
```bash
fastboot flash system system.img
fastboot flash boot boot.img
fastboot flash vendor vendor.img
fastboot reboot
```

### 3. ISO Image (for PCs/Laptops)
**Target**: x86_64 computers  
**Format**: `.iso` file  
**Installation**: Burn to USB/DVD and boot

**Creation**:
```bash
lunch museos_x86_64-userdebug
make iso_img
```

**Output**: `museos-x86_64-1.0.0.iso`

**Installation**:
1. Burn ISO to USB: `dd if=museos.iso of=/dev/sdX bs=4M`
2. Boot from USB
3. Install to hard drive

### 4. System IMG (for Emulator)
**Target**: Android emulator/testing  
**Format**: `.img` files  
**Installation**: Load in emulator

**Usage**:
```bash
emulator -system system.img -data userdata.img
```

### 5. OTA Update Package
**Target**: Over-the-air updates  
**Format**: `.zip` file (signed)  
**Installation**: Via system updater

**Creation**:
```bash
make otapackage
```

## Build Variants

### For ARM64 Devices (Phones/Tablets)
```bash
lunch museos_generic-userdebug
make -j$(nproc) bacon
# Output: museos-*.zip (flashable)
```

### For x86_64 PCs
```bash
lunch museos_x86_64-userdebug
make -j$(nproc) iso_img
# Output: museos-x86_64.iso
```

### For Emulator
```bash
lunch museos_emulator-userdebug
make -j$(nproc)
emulator
```

## File Structure

### Flashable ZIP Structure
```
museos-1.0.0-20260405-OFFICIAL-generic.zip
├── META-INF/
│   └── com/google/android/
│       ├── update-binary
│       └── updater-script
├── system.img
├── boot.img
└── vendor.img
```

### ISO Structure
```
museos-x86_64.iso
├── boot/
│   ├── grub/
│   └── kernel
├── system/
│   └── system.img
└── install/
    └── installer
```

## Distribution Checklist

### Pre-Release
- [ ] Build signed release variant
- [ ] Test on multiple devices
- [ ] Verify all features work
- [ ] Create changelog
- [ ] Generate MD5/SHA256 checksums

### Release Files
- [ ] Flashable ZIP
- [ ] Fastboot images (optional)
- [ ] ISO image (for PC variant)
- [ ] OTA package
- [ ] Installation guide
- [ ] Changelog

### Post-Release
- [ ] Upload to distribution server
- [ ] Update download page
- [ ] Announce on social media
- [ ] Monitor bug reports

## Checksums

Always provide checksums for verification:

```bash
# Generate MD5
md5sum museos-*.zip > museos-*.zip.md5

# Generate SHA256
sha256sum museos-*.zip > museos-*.zip.sha256
```

## Signing

For official releases, sign with release keys:

```bash
# Sign ZIP
java -jar signapk.jar \
    testkey.x509.pem \
    testkey.pk8 \
    museos-unsigned.zip \
    museos-signed.zip
```

## Distribution Platforms

### Official Channels
- Official website download page
- GitHub Releases
- SourceForge
- XDA Developers forum

### Community Channels
- Telegram groups
- Discord servers
- Reddit communities

## File Naming Convention

```
museos-<version>-<date>-<type>-<device>.zip

Components:
- version: 1.0.0
- date: 20260405 (YYYYMMDD)
- type: OFFICIAL, BETA, NIGHTLY
- device: generic, pixel7, etc.

Examples:
- museos-1.0.0-20260405-OFFICIAL-generic.zip
- museos-1.0.0-20260405-BETA-pixel7.zip
- museos-1.1.0-20260410-NIGHTLY-generic.zip
```

## Size Estimates

- Flashable ZIP: 1.5-2.5 GB
- System IMG: 2-3 GB
- ISO (x86_64): 2-3 GB
- OTA Update: 500MB-1.5GB (delta)

## Bandwidth Considerations

For large-scale distribution:
- Use CDN for downloads
- Provide torrent files
- Mirror on multiple servers
- Implement download resume support
