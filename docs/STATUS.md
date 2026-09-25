# Implementation status

| Stage | Status | Scope | Evidence / next action |
|---|---|---|---|
| A | DONE | Fork, source pin, project records | `origin` is `hyungwoo-jo/SmsForwarder`; `upstream` is `pppscn/SmsForwarder`; branch `personal/ko-local` starts at `a3d2302`. |
| B | DONE | JDK 11, Android SDK, app ID, debug build | JDK 11.0.32.1, API 33, Build Tools 33.0.1, and a verified universal debug APK. |
| C | TODO | Umeng, automatic network traffic, TLS, log secrets | Implement after the first repeatable debug build. |
| D | TODO | Korean resources and language selection | Start after source/build structure is stable. |
| E | TODO | Automated regression checks | Add meaningful localization and transport tests. |
| F | TODO | Local personal signing and release APK | Requires a private signing key after release build works. |
| G | TODO | Optional GitHub Actions | Create a safe manual build workflow after local release succeeds. |
| H | TODO | Acceptance report | Record verified facts and device-pending checks. |

## Stage A record

- Personal fork: https://github.com/hyungwoo-jo/SmsForwarder
- Upstream: https://github.com/pppscn/SmsForwarder
- Base commit: `a3d23026f0058420869163c1d5dfb463ce52fc15`
- Working branch: `personal/ko-local`
- The investigation checkout at `/tmp/SmsForwarder` was not modified.

## Stage B record

- Isolated JDK: Eclipse Temurin 11.0.32.1; archive SHA-256 verified against
  the official Adoptium API response.
- Isolated Android SDK: `platform-tools` 37.0.1, `platforms;android-33`, and
  `build-tools;33.0.1`.
- Gradle wrapper: 7.3.3, running on the isolated JDK 11.
- Debug application ID: `com.hwserve.smsforwarder.debug`.
- First successful command: `bash ./gradlew :app:assembleDebug --no-daemon
  --console=plain --stacktrace -PisNeedClean=false -PisNeedPackage=false`.
- Baseline universal debug APK: `SmsF_3.5.0.260925_100055_universal_debug.apk`.
- The debug APK uses the standard Android debug certificate. Release signing is
  intentionally deferred to stage F.
