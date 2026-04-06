# MuseOS Version Configuration

# Version info
MUSEOS_VERSION_MAJOR := 1
MUSEOS_VERSION_MINOR := 0
MUSEOS_VERSION_MAINTENANCE := 0
MUSEOS_VERSION := $(MUSEOS_VERSION_MAJOR).$(MUSEOS_VERSION_MINOR).$(MUSEOS_VERSION_MAINTENANCE)

# Build type
MUSEOS_BUILD_TYPE ?= OFFICIAL

# InfiniteUI version
INFINITEUI_VERSION := 1.0.0

# GaxialAI version
GAXIALAI_VERSION := 1.0.0

# Build date
MUSEOS_BUILD_DATE := $(shell date -u +%Y%m%d)

# Build properties
PRODUCT_PROPERTY_OVERRIDES += \
    ro.museos.version=$(MUSEOS_VERSION) \
    ro.museos.build.type=$(MUSEOS_BUILD_TYPE) \
    ro.museos.build.date=$(MUSEOS_BUILD_DATE) \
    ro.infiniteui.version=$(INFINITEUI_VERSION) \
    ro.gaxialai.version=$(GAXIALAI_VERSION) \
    ro.modversion=MuseOS-$(MUSEOS_VERSION)-$(MUSEOS_BUILD_DATE)-$(MUSEOS_BUILD_TYPE)

# Display version
MUSEOS_DISPLAY_VERSION := MuseOS $(MUSEOS_VERSION)

PRODUCT_PROPERTY_OVERRIDES += \
    ro.museos.display.version=$(MUSEOS_DISPLAY_VERSION)
