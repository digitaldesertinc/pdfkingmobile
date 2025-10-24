PDF King (Mobile) — Android MVP Scaffold

This workspace contains a minimal Android (Kotlin + Jetpack Compose) scaffold to start the "PDF King" mobile app (based on the provided spec).

What is included
- Top-level Gradle/Kotlin configuration (Kotlin + Compose basics)
- `app` module with a `MainActivity` and a `PdfViewerScreen` using `android-pdf-viewer`
- Basic Compose theme and resources
- An `assets/` folder with a placeholder; place a real `sample.pdf` there to test the viewer

How to open
1. Open Android Studio.
2. Choose "Open" and select this folder (the project root).
3. Let Android Studio sync Gradle and download required SDKs/plugins.

Notes
- This is a MVP scaffold: viewer + a simple annotation overlay (UI). Persisting annotations into the PDF bytes is a next step (we added PDFBox dependency as an option).
- Some libraries (iText) are omitted here due to license; for advanced editing/signing consider iText7 (commercial) or configure PDFBox properly.

If you want, I can now:
- implement annotation-to-PDF saving (merge overlays into pages using PDFBox);
- add Merge/Split screens and simple unit tests;
- wire OCR (tess-two) as a follow-up.

CI / Build on GitHub Actions
---------------------------------
I added a GitHub Actions workflow (`.github/workflows/android-build.yml`) that will build a debug APK and upload it as an artifact when you push this project to GitHub.

How to get the APK via Actions:
1. Initialize a git repository in the project root (if you haven't already):
	```powershell
	cd "e:\PDF King\Mobile"
	git init
	git add .
	git commit -m "Initial PDF King scaffold with CI workflow"
	git branch -M main
	git remote add origin <your-git-repo-url>
	git push -u origin main
	```
2. After pushing, open the project's GitHub page → Actions → select the "Android CI - Build APK" run and wait until it completes.
3. When finished, open the workflow run and download the `app-debug-apk` artifact (contains `app-debug.apk`).

Notes & troubleshooting:
- The workflow attempts to use the Gradle wrapper (`./gradlew`) if present. If you didn't add the wrapper, it will try to install system Gradle on the runner (may be slower or fail). To avoid issues, generate and commit the Gradle wrapper locally from Android Studio (Build → Generate Signed Bundle / APK will create wrapper files), or run `gradle wrapper` locally and commit the generated `gradlew`, `gradlew.bat`, and `gradle/wrapper` files.
- Ensure `app/src/main/assets/sample.pdf` exists before running the app on a device. The CI build doesn't require it to produce the APK, but the app will try to load it at runtime.
- If the workflow fails due to SDK/Build Tools mismatch, update `api-level` / `build-tools` in the workflow to match your `compileSdk` / configured build tools.

