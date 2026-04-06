# MuseOS - AOSP-Based Custom Android OS

This is a REAL custom Android operating system built from AOSP source code.

## What This Is

- ✅ Complete Android OS modification
- ✅ Custom framework (SystemServer, WindowManager, etc.)
- ✅ Native C++ components (blur compositor)
- ✅ Custom system UI and launcher
- ✅ Builds flashable system.img

## What This Is NOT

- ❌ Just apps running on Android
- ❌ A launcher replacement
- ❌ An Android skin

## Build Instructions

### 1. Install Dependencies
```bash
./install-dependencies.sh  # Run with sudo
```

### 2. Download AOSP Source (~50GB, 1-3 hours)
```bash
./download-aosp.sh
```

### 3. Build MuseOS (1-3 hours)
```bash
./build-museos.sh
```

### 4. Flash to Emulator
```bash
./flash-museos.sh
```

## System Requirements

- **Disk Space**: 100GB minimum
- **RAM**: 16GB minimum (32GB recommended)
- **CPU**: 8+ cores recommended
- **OS**: Linux (Ubuntu 20.04+ recommended)

## Cloud Build (Recommended)

For faster builds and more space:

### GitHub Codespaces
1. Fork this repo
2. Open in Codespaces (32GB RAM, 128GB storage)
3. Run build scripts

### Google Cloud / AWS
1. Create VM with 200GB+ storage
2. Clone repo
3. Run build scripts

## What Gets Modified

- `frameworks/base` - Core Android framework
- `frameworks/native` - Native system services
- `packages/apps/Launcher3` - Custom launcher
- `packages/apps/Settings` - Custom settings
- SystemUI - Custom system UI with blur

## Output

After building, you'll have:
- `system.img` - MuseOS system partition
- `vendor.img` - Vendor partition
- `boot.img` - Boot image

Flash these to create a complete MuseOS device!
