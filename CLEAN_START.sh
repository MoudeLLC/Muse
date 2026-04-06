#!/bin/bash
# Clean up old attempts and start fresh

echo "🧹 Cleaning up old files..."

# Remove old build attempts
rm -rf museos-rom MuseOS-Dev MuseOS-System
rm -f build-museos-*.sh install-museos-*.sh setup-museos-*.sh
rm -f inject-museos-*.sh integrate-museos.sh launch-muse-aeb.sh

echo "✅ Cleanup complete. Ready for fresh start."
