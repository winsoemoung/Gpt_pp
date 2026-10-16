# OfflinePOS

Offline-first Android POS application built with Kotlin, Jetpack Compose, Room, CameraX, Bluetooth receipt printing, PDF receipts, and a local HTML5 POS portal.

## Features

- Product inventory with Active/Inactive status and category filtering
- Camera and gallery product images
- Custom product units such as `အိတ်`, `ပိဿာ`, `ဗူး`, `ခု`, or any user-defined unit
- Room database export/restore with migration support
- Daily and monthly sales candlestick chart
- Recent product searches
- Checkout support for cash, credit, split payments, KPay/WavePay QR flows
- Optional transport/delivery fee included in the sale total
- PDF and Bluetooth thermal receipt output
- Local POS Server for staff devices over Wi-Fi, including browser camera/gallery image upload
- Delete confirmations for destructive actions

## Modern UI refresh

The current `main` branch includes a premium industrial-materials visual refresh:

- Slate grey, charcoal, and safety amber branded Material 3 light/dark colors
- Responsive dashboard KPI cards for daily revenue, customer credit, and low-stock alerts
- Animated daily revenue counter and elevated metric surfaces
- Interactive daily/monthly candlestick sales analytics with selectable detail cards
- Category chips, recent searches, multi-unit product labels, and a modern cart/checkout flow
- Delivery-fee-aware receipts and Unicode-safe Burmese labels for PDF and Bluetooth output

## new2 shop mode

- No PIN system, default PIN, or role-switching UI is included.
- Discounts can be used directly by the single-shop operator.
- Tax is permanently disabled at 0%; tax controls and tax rows are omitted from checkout and receipts.
- The Main POS Server starts **OFF** when the app opens and starts only after the Settings toggle is turned ON.
- The register header uses a clean store title without an `Offline` badge.
- Database destructive-migration fallback is disabled so an unsupported schema change cannot silently erase shop data.

## Requirements

- Android Studio with Android SDK 36
- JDK 17
- Android device or emulator with API 26 or newer
- Internet access on the first Gradle sync to download dependencies

The project uses the Gradle wrapper, so a separate Gradle installation is not required.

## Open in Android Studio

1. Extract or clone this repository.
2. Open the project root (`OfflinePOS`) in Android Studio.
3. Allow Gradle sync to complete.
4. Select the `app` configuration.
5. Run on an API 26+ device or emulator.

## Build from a terminal

Linux/macOS:

```bash
./gradlew testDebugUnitTest assembleDebug
```

Windows PowerShell:

```powershell
.\\gradlew.bat testDebugUnitTest assembleDebug
```

The debug APK is generated at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Database migrations

Room schemas are kept under `app/schemas/`. Before changing an entity, add a corresponding migration in `app/src/main/java/com/example/offlinepos/data/local/Migrations.kt` and increment the database version in `AppDatabase.kt`.

## Permissions and device features

The manifest declares optional camera, Bluetooth, network, and vibration permissions. Camera and Bluetooth features are marked optional so the application can still install on devices without those hardware features.

## GitHub notes

- APKs, build output, Gradle caches, IDE state, local SDK paths, and signing keystores are excluded by `.gitignore`.
- Do not commit production signing keys or passwords. Use Android Studio's release signing configuration or CI secrets for release builds.
- The included debug build uses Android's standard debug signing behavior and does not require a keystore in the repository.

## Package identity

```text
Application ID: com.example.offlinepos.knzeso
Namespace: com.example.offlinepos
```

## License

Add the license and ownership terms appropriate for your project before publishing publicly.
