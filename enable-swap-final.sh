#!/bin/bash
# Enable swap with full paths

set -e

echo "💾 Enabling Swap Space"
echo "======================"
echo ""

SWAP_FILE="$HOME/swapfile"
SWAP_SIZE=16  # 16GB

if [ ! -f "$SWAP_FILE" ]; then
    echo "Creating ${SWAP_SIZE}GB swap file..."
    dd if=/dev/zero of=$SWAP_FILE bs=1G count=$SWAP_SIZE status=progress
    chmod 600 $SWAP_FILE
fi

echo "Setting up swap with mkswap..."
/sbin/mkswap $SWAP_FILE

echo "Enabling swap with swapon..."
sudo /sbin/swapon $SWAP_FILE

echo ""
echo "✅ Swap enabled successfully!"
echo ""
echo "Memory status:"
free -h
echo ""
echo "Swap devices:"
cat /proc/swaps
echo ""
