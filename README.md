# Sagrada Server

Java 17 server for the Sagrada Android client. It supports one human against 1–3 server-side bots and the original four-human LAN mode.

Run it from this directory with the Android project's Gradle wrapper:

```bash
bash ../sagrada-game/gradlew -p . :server:run
```

The console prints the IP address to enter in the Android app. TCP port `5000` must be reachable from the devices or emulators.
