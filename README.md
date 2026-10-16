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

## New3 UI refresh

- Added a modern quick-action strip on the register dashboard for Barcode Scan, Held Orders, Customers, and Shift Report.
- Quick actions use compact icon tiles with clear color hierarchy and remain reachable before product search.
- Version 3.0.0 / versionCode 3.

## New3-2 reliability update

- Full-sale Return/Refund action from Sales History with confirmation and stock restoration.
- Atomic stock movement ledger with Room migration 4→5 for manual adjustments.
- SKU conflicts are reported instead of silently dropping a duplicate barcode.
- Backup export/import runs SQLite integrity validation and retains the newest three backup files.
- Version 3.2.0 / versionCode 4.

## New4 repair update

- Replaced the default in-app store logo fallback with the newly supplied logo image.
- Added product-line partial returns from Sales History: choose a return quantity for each product in a sale.
- Full-sale returns remain available through the same dialog by selecting all remaining quantities.
- Added Room migration 5→6 with per-line `returnedQuantity` tracking and `PARTIALLY_REFUNDED` sale status.
- Return operations restore stock atomically and write an audit event for partial or full returns.
- Version 4.0.0 / versionCode 5.

## New4-2 accounting and reliability update

- Added return transaction records with refund amount, method, reference, reason, actor, and timestamp.
- Return operations now write `SALE_RETURN` stock movement ledger entries atomically.
- Sales History now shows net revenue after returned amounts.
- Partial/full return metadata is shown on reprinted PDF receipts.
- Added Room migration 6→7 and corrected the migration test to match the actual schema.
- Updated legacy launcher PNG icons to use the supplied store image.

## New5 operations and reporting update

- Registered `sync_outbox` in Room with migration 8→9.
- Checkout now enqueues idempotent SALE operations; WorkManager retries delivery to the local `/api/sync` endpoint.
- Added idempotent local server sync acknowledgement and retry state tracking.
- Added UTF-8 CSV product export/import from Inventory, including price and stock bulk updates by SKU.
- Added customer credit due-date entry and overdue badges.
- Improved reports with exact product-cost COGS, return-aware payment settlement, and supplier purchase summaries.
- Added CSV upload/download actions to the Inventory top bar.
- Version 5.0.0 / versionCode 7.

## New5-2 compact dashboard UI update

- Reduced the three sales dashboard KPI cards to compact equal-width cards.
- Changed KPI card text to concise English labels: `Today Sales`, `Outstanding Credit`, and `Low Stock`.
- Reduced icon, padding, corner radius, typography, and inter-card spacing.
- Reduced Barcode / Held / Customer / Shift quick-action tiles to compact 88dp tiles with smaller icons and labels.
- Preserved accessibility content descriptions and Material 3 touch targets.
- Version 5.0.1 / versionCode 8.

## New5-3 database import update

- Restore now asks the user to choose between `Replace Current Database` and `Merge Products Only`.
- Replace mode preserves the existing full-database restore behavior and creates a pre-restore backup.
- Merge mode reads products from the selected SQLite backup and inserts only new products into the current database.
- Merge matching rules: SKU first, then barcode; duplicates are skipped and existing sales/customers/stock movements are not replaced.
- The merge result reports inserted and skipped product counts.
- Version 5.0.2 / versionCode 9.

### Known scope not included in New5

- Supplier debt/payment ledger is not yet a separate accounting module; purchase supplier totals and invoice references are available.
- Barcode label printing and Excel `.xlsx` support are not included; CSV is supported.
- Product-level profit and exportable PDF/CSV report files are not yet a dedicated report-export workflow.
- Remote multi-device sync beyond the local POS server still needs a persistent server-side database and authentication contract.

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
