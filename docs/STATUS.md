# Implementation status

| Stage | Status | Scope | Evidence / next action |
|---|---|---|---|
| A | DONE | Fork, source pin, project records | `origin` is `hyungwoo-jo/SmsForwarder`; `upstream` is `pppscn/SmsForwarder`; branch `personal/ko-local` starts at `a3d2302`. |
| B | DONE | JDK 11, Android SDK, app ID, debug build | JDK 11.0.32.1, API 33, Build Tools 33.0.1, and a verified universal debug APK. |
| C | DONE | Umeng, automatic network traffic, TLS, log secrets | Verified clean debug build with bundled FRPC libraries and no Umeng native artifact. |
| D | DONE | Korean resources, language selection, and local privacy notice | 1,185 `values-ko` resources pass the localization validator; a Korean privacy notice is bundled locally; verified debug APK build completed. |
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

## Stage C record

- Removed Umeng analytics, XUpdate, automatic startup checks, remote guide fetch,
  and the runtime FRPC download path.
- The About screen opens this fork's releases page only when chosen by the user.
- `libgojni.so` is stored in the Android native-library source set for all four
  supported ABIs and is loaded from the installed APK.
- Sender paths use normal TLS certificate verification. The shared transfer-log
  interceptor redacts credentials, tokens, message content, and authorization
  headers before persisting diagnostics.
- Phone-area lookup is disabled by default and now has an explicit settings
  control because it sends the queried phone number to a third-party service.
- Clean debug build: `:app:clean :app:assembleDebug`; universal APK SHA-256:
  `6f3fa0f3dd7e9124b688106b4119c54b16472f39c34fa11f58c6abc41284dceb`.
