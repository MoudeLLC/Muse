# MuseOS Device Configuration

# Inherit from AOSP
$(call inherit-product, $(SRC_TARGET_DIR)/product/core_64_bit.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/full_base_telephony.mk)

# MuseOS System UI (InfiniteUI)
PRODUCT_PACKAGES += \
    InfiniteUILauncher \
    InfiniteUISettings \
    InfiniteUISystemUI \
    MuseOSFramework

# GaxialAI System Services
PRODUCT_PACKAGES += \
    GaxialAIService \
    GennaAssistant \
    OliviaCreativeStudio

# Pre-installed Apps
PRODUCT_PACKAGES += \
    MuseCamera \
    MuseGallery \
    MuseMusic \
    MuseContacts \
    MuseDialer \
    MuseMessages

# System Properties
PRODUCT_PROPERTY_OVERRIDES += \
    ro.museos.version=$(MUSEOS_VERSION) \
    ro.museos.build.type=$(MUSEOS_BUILD_TYPE) \
    ro.infiniteui.version=$(INFINITEUI_VERSION) \
    ro.gaxialai.enabled=true

# Device Info
PRODUCT_NAME := museos_generic
PRODUCT_DEVICE := generic
PRODUCT_BRAND := Muse
PRODUCT_MODEL := MuseOS
PRODUCT_MANUFACTURER := Muse Technologies

# Build fingerprint
PRODUCT_BUILD_PROP_OVERRIDES += \
    PRODUCT_NAME=museos_generic \
    PRIVATE_BUILD_DESC="museos_generic-userdebug 14 UQ1A.240105.004 eng.muse.20260405.114700 dev-keys"

BUILD_FINGERPRINT := Muse/museos_generic/generic:14/UQ1A.240105.004/eng.muse.20260405.114700:userdebug/dev-keys
