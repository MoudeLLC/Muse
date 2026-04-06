#!/bin/bash

# MuseOS Complete Installation Script
# Installs all MuseOS components

set -e

echo "========================================="
echo "  MuseOS Complete Installation"
echo "========================================="
echo ""
echo "This will install:"
echo "  • Muse Launcher"
echo "  • Muse SystemUI"
echo "  • Muse Settings"
echo ""
echo "Press Enter to continue or Ctrl+C to cancel..."
read

# Install Launcher
./install-launcher.sh

echo ""
echo "Waiting 3 seconds..."
sleep 3

# Install SystemUI
./install-systemui.sh

echo ""
echo "Waiting 3 seconds..."
sleep 3

# Install Settings
./install-settings.sh

echo ""
echo "========================================="
echo "  ✓ MuseOS Installation Complete!"
echo "========================================="
echo ""
echo "All MuseOS components are now installed:"
echo "  ✓ Muse Launcher (Home screen)"
echo "  ✓ Muse SystemUI (Status bar & notifications)"
echo "  ✓ Muse Settings (System settings)"
echo ""
echo "Press the home button to start using MuseOS!"
echo ""
