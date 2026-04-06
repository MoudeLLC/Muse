# MuseOS Build Configuration

PRODUCT_NAME := museos
PRODUCT_DEVICE := generic_x86_64
PRODUCT_BRAND := Muse
PRODUCT_MODEL := MuseOS
PRODUCT_MANUFACTURER := Muse

# MuseOS version
PRODUCT_PROPERTY_OVERRIDES += \
    ro.museos.version=1.0 \
    ro.museos.codename=Infinity \
    ro.museos.build.date=$(shell date +%Y%m%d) \
    ro.build.display.id="MuseOS 1.0 Infinity"

# Enable blur effects
PRODUCT_PROPERTY_OVERRIDES += \
    persist.museos.blur.enabled=true \
    persist.museos.blur.radius=64 \
    persist.museos.glass.opacity=0.2

# Custom launcher
PRODUCT_PACKAGES += \
    MuseLauncher \
    MuseSystemUI \
    MuseSettings

# Remove stock apps
PRODUCT_PACKAGES_REMOVE += \
    Launcher3 \
    Launcher3QuickStep
