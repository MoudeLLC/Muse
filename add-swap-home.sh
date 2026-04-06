#!/bin/bash
# Add swap in home directory (might work better in container)

set -e

echo "💾 Adding Swap Space (Home Directory)"
echo "======================================"
echo ""

SWAP_SIZE=16  # 16GB
SWAP_FILE="$HOME/swapfile"

echo "Creating ${SWAP_SIZE}GB swap file in home directory..."
echo "Location: $SWAP_FILE"
echo ""

# Create swap file
dd if=/dev/zero of=$SWAP_FILE bs=1G count=$SWAP_SIZE status=progress

echo ""
echo "Setting permissions..."
chmod 600 $SWAP_FILE

echo "Setting up swap..."
mkswap $SWAP_FILE

echo "Enabling swap..."
sudo swapon $SWAP_FILE

echo ""
echo "✅ Swap enabled!"
echo ""
echo "Current memory status:"
free -h
echo ""
cat /proc/swaps
echo ""
