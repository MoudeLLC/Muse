# MuseOS Generic Device Configuration

# Inherit device configuration
$(call inherit-product, device/muse/generic/device.mk)

# Inherit MuseOS configuration
$(call inherit-product, vendor/muse/config/common.mk)

# Device identifier
PRODUCT_NAME := museos_generic
PRODUCT_DEVICE := generic
PRODUCT_BRAND := Muse
PRODUCT_MODEL := MuseOS Generic
PRODUCT_MANUFACTURER := Muse Technologies

# Build type
MUSEOS_BUILD_TYPE := OFFICIAL
