#!/bin/bash
# Create Flashable ZIP for MuseOS ROM

set -e

PRODUCT_OUT="$1"
VERSION="$2"
BUILD_DATE=$(date +%Y%m%d)
BUILD_TYPE="OFFICIAL"

if [ -z "$PRODUCT_OUT" ] || [ -z "$VERSION" ]; then
    echo "Usage: $0 <product_out_dir> <version>"
    exit 1
fi

ZIP_NAME="museos-${VERSION}-${BUILD_DATE}-${BUILD_TYPE}-generic.zip"
TEMP_DIR=$(mktemp -d)

echo "Creating flashable ZIP: $ZIP_NAME"

# Create directory structure
mkdir -p "$TEMP_DIR/META-INF/com/google/android"
mkdir -p "$TEMP_DIR/system"
mkdir -p "$TEMP_DIR/boot"
mkdir -p "$TEMP_DIR/vendor"

# Copy system image
echo "Copying system.img..."
cp "$PRODUCT_OUT/system.img" "$TEMP_DIR/system.img"

# Copy boot image
echo "Copying boot.img..."
cp "$PRODUCT_OUT/boot.img" "$TEMP_DIR/boot.img"

# Copy vendor image
echo "Copying vendor.img..."
cp "$PRODUCT_OUT/vendor.img" "$TEMP_DIR/vendor.img"

# Create updater-script
cat > "$TEMP_DIR/META-INF/com/google/android/updater-script" << 'EOF'
ui_print("========================================");
ui_print("  MuseOS Installation");
ui_print("  Version: @VERSION@");
ui_print("  Build: @BUILD_DATE@");
ui_print("========================================");

ui_print("Mounting partitions...");
mount("ext4", "EMMC", "/dev/block/bootdevice/by-name/system", "/system");
mount("ext4", "EMMC", "/dev/block/bootdevice/by-name/vendor", "/vendor");

ui_print("Formatting system...");
format("ext4", "EMMC", "/dev/block/bootdevice/by-name/system");

ui_print("Installing MuseOS...");
package_extract_file("system.img", "/dev/block/bootdevice/by-name/system");
package_extract_file("boot.img", "/dev/block/bootdevice/by-name/boot");
package_extract_file("vendor.img", "/dev/block/bootdevice/by-name/vendor");

ui_print("Setting permissions...");
set_metadata_recursive("/system", "uid", 0, "gid", 0, "dmode", 0755, "fmode", 0644);

ui_print("Unmounting partitions...");
unmount("/system");
unmount("/vendor");

ui_print("========================================");
ui_print("  MuseOS installed successfully!");
ui_print("  Reboot to experience InfiniteUI");
ui_print("========================================");
EOF

# Replace placeholders
sed -i "s/@VERSION@/$VERSION/g" "$TEMP_DIR/META-INF/com/google/android/updater-script"
sed -i "s/@BUILD_DATE@/$BUILD_DATE/g" "$TEMP_DIR/META-INF/com/google/android/updater-script"

# Create update-binary (recovery executable)
cp "$PRODUCT_OUT/../../host/linux-x86/bin/updater" "$TEMP_DIR/META-INF/com/google/android/update-binary"

# Create ZIP
echo "Creating ZIP archive..."
cd "$TEMP_DIR"
zip -r "$PRODUCT_OUT/$ZIP_NAME" ./*

echo "Flashable ZIP created: $PRODUCT_OUT/$ZIP_NAME"
echo "Size: $(du -h "$PRODUCT_OUT/$ZIP_NAME" | cut -f1)"

# Cleanup
rm -rf "$TEMP_DIR"

echo "Done!"
