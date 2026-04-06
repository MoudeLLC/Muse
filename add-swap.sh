#!/bin/bash
# Add swap space for AOSP build

set -e

echo "💾 Adding Swap Space"
echo "===================="
echo ""

SWAP_SIZE="16G"  # 16GB swap
SWAP_FILE="/swapfile"

echo "Creating ${SWAP_SIZE} swap file..."
echo "This will take a few minutes..."
echo ""

# Create swap file
sudo dd if=/dev/zero of=$SWAP_FILE bs=1G count=16 status=progress

echo ""
echo "Setting permissions..."
sudo chmod 600 $SWAP_FILE

echo "Setting up swap..."
sudo mkswap $SWAP_FILE

echo "Enabling swap..."
sudo swapon $SWAP_FILE

echo ""
echo "✅ Swap enabled!"
echo ""
echo "Current memory status:"
free -h

echo ""
echo "To make swap permanent (survives reboot):"
echo "  sudo bash -c 'echo \"$SWAP_FILE none swap sw 0 0\" >> /etc/fstab'"
echo ""
