# SleepPlugin Changelog

## Version 1.0.2 (2025-05-25)

### New Features
- Added smooth time transition from night to morning
- Added ActionBar progress notifications (reduces chat spam)
- Added storm skipping functionality
- Added intelligent sleep tracking (doesn't cancel if enough players still sleeping)
- Added configuration update system that preserves user settings
- Added support for Nether/End player exclusion

### Improvements
- Improved message display system with three modes: normal, minimal, silent
- Enhanced configuration with more customization options
- Improved sleep mechanics with smart counting for odd player counts
- Better performance with optimized code
- Reduced chat spam with cooldown system for notifications

### Configuration Changes
- Added `version` field to track configuration versions
- Added `ignore-nether-end-players` option
- Added `smooth-time-transition` section with customization options
- Added `storm-settings` section for storm-related options
- Added `min-players-required` to set minimum players for activation

### Bug Fixes
- Fixed issue with sleep being canceled when sufficient players still sleeping
- Fixed unnecessary sleep notifications when playing alone
- Fixed incorrect message display for skip delay time
