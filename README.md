
<img src="/assets/PaperBlockTuner.png" alt="PaperBlockTuner" width="128" height="128" style="vertical-align: middle; margin-right: 2px;" />


# PaperBlockTuner

**PaperBlockTuner** is a Minecraft plugin for [Paper](https://papermc.io/downloads/paper) and downstream servers that allows players to interactively edit note blocks using command and tool item.

## Features

- Tune note blocks using command and a stick
- Change instruments via GUI
- Scroll-tuning support for blocks and items
- Configurable settings per-player
- Support [BlockTuner Mod](https://github.com/Lumine1909/BlockTunerMod_Continue)

## Getting Started

### Requirements

- Minecraft 1.20.5+
- Paper and downstream server
- Java 21+
- Gradle (for building from source)

### Installation

1. Download the plugin JAR (or build it yourself, see below).
2. Place it in the `plugins/` directory of your Paper server.
3. Restart the server.
4. Configuration and data will be generated automatically.

### Building From Source

```bash
./gradlew build
```

The resulting `.jar` will be located in `build/libs/`.

## Usage

### Commands

| **Command**            | **Description**                                      | **Permission**                 |
|------------------------|------------------------------------------------------|--------------------------------|
| `/blocktuner`          | Main command.                                        | `blocktuner.command.base`      |
| `/blocktuner reload`   | Reload the plugin.                                   | `blocktuner.admin`             |
| `/blocktuner settings` | Opens the tuning settings GUI.                       | `blocktuner.command.settings`  |
| `/tune`                | Tunes the block or item the player is aiming.        | `blocktuner.command.tune`      |
| `/tunehand`            | Tunes the block or item the player is holding.       | `blocktuner.command.tunehand`  |
| `/tunestick`           | Get a stick for interactive tune (right/left click). | `blocktuner.command.tunestick` |

### Permissions

| **Permission**                          | **Description**                                                                |
|-----------------------------------------|--------------------------------------------------------------------------------|
| `blocktuner.command.base`               | Allows access to the main command `/blocktuner`.                               |
| `blocktuner.command.settings`           | Allows access to open the settings GUI.                                        |
| `blocktuner.command.tune`               | Allows use of the `/tune` command to tune the aiming noteblock.                |
| `blocktuner.command.tunehand`           | Allows use of `/tunehand` to tune the item/block in hand.                      |
| `blocktuner.command.tunestick`          | Allows get a tuning a stick for interactive tuning (left/right-click).         |
| `blocktuner.settings.note-tuning`       | Allows toggling note tuning in the settings GUI.                               |
| `blocktuner.settings.instrument-tuning` | Allows toggling instrument tuning in the settings GUI.                         |
| `blocktuner.settings.scroll-item`       | Allows enabling scroll-wheel tuning for held items.                            |
| `blocktuner.settings.scroll-block`      | Allows enabling scroll-wheel tuning for blocks.                                |
| `blocktuner.settings.sync-instrument`   | Allows enabling sync of block instrument type when tuning.                     |
| `blocktuner.playnote`                   | Allows a player to play note with a blaze rod.                                 |
| `blocktuner.edit`                       | General edit permission for block tuning actions.                              |
| `blocktuner.admin`                      | Allows access to administrative features and full control over plugin actions. |


### GUI
(All names are configurable)

![image](https://github.com/user-attachments/assets/b0a027b7-7437-4396-9b97-4fca8cdfdcfa)

![image](https://github.com/user-attachments/assets/b5d6d413-05cc-4ab7-a81f-45b06fbb0b09)

![image](https://github.com/user-attachments/assets/0727d817-3760-4d74-92e5-1665d4a6797e)





## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---
