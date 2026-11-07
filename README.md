# Scout-a-Lot

**Scout a lot. Know a lot.**

Scout-a-Lot is open-source scouting software for *FIRST* Robotics Competition (FRC) teams, built by [FRC Team 1939, The Kuhnights](https://www.frcteam1939.com/). Scouts collect match and pit data on their phones, sync it between devices without venue Wi-Fi, and turn it into stats a drive team can use.

[Website](https://www.scoutalot.com) · [Docs](https://docs.scoutalot.com)

## Features

- **Match scouting:** record each robot's performance in auto, teleop and endgame.
- **Pit scouting:** capture robot details from teams in the pits, with automatic pit assignments for each scouting device.
- **Custom forms:** build forms that fit each season's game.
- **Bluetooth and file sync:** move data between devices without venue Wi-Fi.
- **Team stats:** see how each team performs across an event, sorted by any tracked metric.
- **Match reports:** prepare for upcoming matches with reports built from your data.

## Availability

| Platform | Status |
| --- | --- |
| Android (build from source) | Available now |
| Google Play | Planned for December 2026 |
| Documentation site | Planned for December 2026 |
| iOS | Date to be announced |

## Building from source

### Requirements

- **JDK 21**
- **Android Studio**, a version that supports Android Gradle Plugin 9.3
- **Android SDK** with API level 36 (Android Studio can install it)
- An Android device or emulator running Android 7.0 (API 24) or newer.

## Using the app at an event

1. **Before the event (needs internet):** go to **Settings → Edit Events → Add Event**, enter a name, the event code and the year, then select it as the current event.
2. **On each scouting device:** in **Settings**, choose its robot assignment (Red 1 to Blue 3). Match and pit assignments follow from it.
3. **Build your own form: decide what data your team wants to collect by customizing template fields.**
4. **Scout:** use **Scout Matches** during matches and **Scout Pits** in the pits.
5. **Review:** use **Data Checklist**, **Team Stats** and **Match Reports** on the collecting device.

## Tech stack

- Kotlin and Kotlin Multiplatform
- Jetpack Compose with Material 3
- MVVM with AndroidX ViewModel and Kotlin Flow
- Room (bundled SQLite) for scouting data, DataStore for settings
- Ktor and kotlinx.serialization for API requests and JSON

## Contributing

Scout-a-Lot is free to copy and edit. We are not currently taking community contributions, but we plan to allow contributions in the future.

## Credits

Built by students of [FRC Team 1939, The Kuhnights](https://www.frcteam1939.com/).

Event data is provided by [The Blue Alliance](https://www.thebluealliance.com/) and *FIRST* through the FRC Events API.

*FIRST*® and *FIRST*® Robotics Competition are registered trademarks of *FIRST*®, which does not oversee, get involved with, or take responsibility for this project.
