# SleepPlugin

Minecraft Paper 1.21.5 plugin for enhanced sleep mechanics - only half of online players need to sleep to skip the night.

## Features

- Half of players needed to skip night
- Smart counting for odd player counts
- Storm and night skipping
- Progress notifications
- Cancel when players wake up
- Multi-world support

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

1. Download `SleepPlugin-1.0.0.jar`
2. Place in server's `plugins` folder
3. Restart server

## Building

1. Install Java 21+
2. Clone repository
3. Run: `./gradlew build`
4. JAR file will be in `build/libs/`

## License

MIT License