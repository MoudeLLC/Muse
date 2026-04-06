#!/bin/bash
# Fix MuseOS device configuration

set -e

DEVICE_DIR=~/aosp-full/device/muse/museos

echo "🔧 Fixing MuseOS Device Configuration"
echo "======================================"

# Create BoardConfig.mk
cat > $DEVICE_DIR/BoardConfig.mk << 'EOF'
# MuseOS Board Configuration

# Architecture
TARGET_ARCH := x86_64
TARGET_ARCH_VARIANT := x86_64
TARGET_CPU_ABI := x86_64
TARGET_CPU_VARIANT := generic

TARGET_2ND_ARCH := x86
TARGET_2ND_ARCH_VARIANT := x86
TARGET_2ND_CPU_ABI := x86
TARGET_2ND_CPU_VARIANT := generic

# Board
TARGET_BOARD_PLATFORM := museos
TARGET_BOOTLOADER_BOARD_NAME := museos

# Kernel
TARGET_NO_KERNEL := true

# Partitions
BOARD_FLASH_BLOCK_SIZE := 512
TARGET_USERIMAGES_USE_EXT4 := true
BOARD_SYSTEMIMAGE_PARTITION_SIZE := 3221225472
BOARD_USERDATAIMAGE_PARTITION_SIZE := 576716800
BOARD_CACHEIMAGE_PARTITION_SIZE := 69206016
BOARD_CACHEIMAGE_FILE_SYSTEM_TYPE := ext4
BOARD_VENDORIMAGE_PARTITION_SIZE := 314572800
BOARD_VENDORIMAGE_FILE_SYSTEM_TYPE := ext4

# SELinux
BOARD_SEPOLICY_DIRS += device/muse/museos/sepolicy

# Graphics
USE_OPENGL_RENDERER := true
TARGET_USES_HWC2 := true
NUM_FRAMEBUFFER_SURFACE_BUFFERS := 3

# Audio
USE_XML_AUDIO_POLICY_CONF := 1

# WiFi
BOARD_WLAN_DEVICE := emulator
BOARD_HOSTAPD_DRIVER := NL80211
WPA_SUPPLICANT_VERSION := VER_0_8_X
BOARD_WPA_SUPPLICANT_DRIVER := NL80211
EOF

echo "✅ Created BoardConfig.mk"

# Create device.mk
cat > $DEVICE_DIR/device.mk << 'EOF'
# MuseOS Device Configuration

# Inherit from generic x86_64 device
$(call inherit-product, $(SRC_TARGET_DIR)/product/generic_x86_64.mk)

# Device identifier
PRODUCT_DEVICE := museos
PRODUCT_NAME := museos
PRODUCT_BRAND := Muse
PRODUCT_MODEL := MuseOS
PRODUCT_MANUFACTURER := Muse

# MuseOS specific properties
PRODUCT_PROPERTY_OVERRIDES += \
    ro.museos.version=1.0 \
    ro.museos.codename=Infinity \
    ro.museos.ui.style=liquid_glass \
    ro.museos.ai.enabled=true

# Packages
PRODUCT_PACKAGES += \
    Launcher3
EOF

echo "✅ Created device.mk"

# Update museos.mk to use device.mk
cat > $DEVICE_DIR/museos.mk << 'EOF'
# MuseOS Product Configuration

$(call inherit-product, device/muse/museos/device.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/core_64_bit.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/aosp_base_telephony.mk)

PRODUCT_NAME := museos
PRODUCT_DEVICE := museos
PRODUCT_BRAND := Muse
PRODUCT_MODEL := MuseOS Infinity
PRODUCT_MANUFACTURER := Muse
EOF

echo "✅ Updated museos.mk"

# Create minimal sepolicy
mkdir -p $DEVICE_DIR/sepolicy
touch $DEVICE_DIR/sepolicy/file_contexts

echo "✅ Created SELinux policy"

echo ""
echo "✅ Device configuration fixed!"
echo ""
