# SleepPlugin

Minecraft Paper 1.21.x plugin for enhanced sleep mechanics - only half of online players need to sleep to skip the night.

## Features

- Half of players needed to skip night
- Smart counting for odd player counts
- Multiple message modes (normal, minimal, silent)
- Storm and night skipping
- Ignore players in Nether and End dimensions
- Smooth time transition from night to morning
- Configuration update system (preserves settings during updates)
- Multi-world support
- Multi-language support (English and Russian)

## Examples

- 2 players online: 1 player needs to sleep
- 3 players online: 1 player needs to sleep ((3-1)/2 = 1)
- 4 players online: 2 players need to sleep
- 5 players online: 2 players need to sleep ((5-1)/2 = 2)
- 6 players online: 3 players need to sleep

## Configuration

After first server start with the plugin, a configuration file will be created at `plugins/SleepPlugin/config.yml`:

```yaml
# SleepPlugin Configuration
# Do not change this version number manually
version: "1.0.2"

language: en_EN  
skip-delay: 3    
morning-time: 1000  
message-mode: normal  
min-players-required: 2  
ignore-nether-end-players: true  
smooth-time-transition:
  enabled: true  
  duration-ticks: 60 
  steps: 60 
storm-settings:
  skip-storms: true 
```

### Settings:

- `language`: Language for plugin messages (en_EN or ru_RU)
- `skip-delay`: Time in seconds before night is skipped
- `morning-time`: Minecraft time value to set when skipping to morning
- `message-mode`: Controls how verbose the plugin messages are
  - `normal`: Standard detailed messages
  - `minimal`: Short concise messages
  - `silent`: No messages at all
- `min-players-required`: Minimum number of players needed to activate sleep mechanics (plugin won't work with fewer players)
- `ignore-nether-end-players`: When true, players in Nether or End won't be counted for sleep calculations
- `smooth-time-transition`: Settings for the smooth time transition feature
  - `enabled`: Whether to enable smooth transition or use instant time change
  - `duration-ticks`: How long the transition should take (in ticks, 20 ticks = 1 second)
  - `steps`: Number of intermediate steps (higher = smoother)
- `storm-settings`: Settings for the storm skipping feature
  - `skip-storms`: When true, players can skip storms by sleeping
