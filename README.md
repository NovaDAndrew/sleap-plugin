# SleepPlugin

Minecraft Paper 1.21.х plugin for enhanced sleep mechanics - only half of online players need to sleep to skip the night.

## Features

- Half of players needed to skip night
- Smart counting for odd player counts
- Storm and night skipping
- Progress notifications
- Cancel when players wake up
- Multi-world support
- Multi-language support (English and Russian)

## Examples

- 2 players online: 1 player needs to sleep
- 3 players online: 1 player needs to sleep ((3-1)/2 = 1)
- 4 players online: 2 players need to sleep
- 5 players online: 2 players need to sleep ((5-1)/2 = 2)
- 6 players online: 3 players need to sleep

## Requirements

- Minecraft: 1.21.5
- Server: Paper (or compatible)
- Java: 21+

## Installation

1. Download `SleepPlugin-1.0.1.jar`
2. Place in server's `plugins` folder
3. Restart server

## Configuration

After first server start with the plugin, a configuration file will be created at `plugins/SleepPlugin/config.yml`:

```yaml
# SleepPlugin Configuration
language: en_EN  # Default language, can be ru_RU or en_EN
skip-delay: 3    # Delay in seconds before skipping night
morning-time: 1000  # Time to set when skipping to morning
```

### Settings:

- `language`: Language for plugin messages (en_EN or ru_RU)
- `skip-delay`: Time in seconds before night is skipped
- `morning-time`: Minecraft time value to set when skipping to morning

## Building

1. Install Java 21+
2. Clone repository
3. Run: `./gradlew build`
4. JAR file will be in `build/libs/`

## License

MIT License
