# Local build

The project is pinned to upstream commit `a3d23026f0058420869163c1d5dfb463ce52fc15`.

The upstream build uses Android Gradle Plugin 7.2.2, Gradle 7.3.3, Kotlin
1.7.21, compile/target SDK 33, Build Tools 33.0.1, and JDK 11. The system Java
must not be changed; the project will use an isolated JDK 11 and Android SDK.

## Planned local locations

```text
/home/hyungwoo/codespace/.smsforwarder-local/jdk-11/
/home/hyungwoo/codespace/.smsforwarder-local/android-sdk/
/home/hyungwoo/codespace/.smsforwarder-local/gradle-home/
/home/hyungwoo/codespace/.smsforwarder-local/signing/
```

`scripts/env.local.sh`, `local.properties`, private key files, passwords, and
build outputs are local-only. This document will be updated with actual tool
versions, commands, output paths, certificate fingerprint, and APK hashes once
the build environment is ready.

## Verified debug build

- JDK: Eclipse Temurin 11.0.32.1.
- Gradle: 7.3.3.
- Android SDK: Platform 33 and Build Tools 33.0.1.
- Command:

  ```bash
  source scripts/env.local.sh
  bash ./gradlew :app:assembleDebug --no-daemon --console=plain --stacktrace \
    -PisNeedClean=false -PisNeedPackage=false
  ```

- Universal output:
  `build/app/outputs/apk/debug/SmsF_3.5.0.260925_100055_universal_debug.apk`.
- Stage C clean-build APK SHA-256:
  `6f3fa0f3dd7e9124b688106b4119c54b16472f39c34fa11f58c6abc41284dceb`.
- Package ID: `com.hwserve.smsforwarder.debug`.
- Label: `SMS 자동전달 · 개인판`.
- Minimum/target SDK: 19 / 33.
- Verification: APK Signature Scheme v1 and v2 succeeded using the standard
  Android debug certificate. The `apksigner` warnings concern legacy
  unprotected `META-INF` entries and will be addressed before release signing.
- Stage C packaging: the universal debug APK is 47,712,267 bytes and includes
  `libgojni.so` for arm64-v8a, armeabi-v7a, x86, and x86_64. A clean build
  contains no `libumeng-spy.so` artifact.

## Dependency repositories

The upstream Aliyun Maven mirrors time out from this server. The project uses
Google Maven, Maven Central, JitPack, and the Huawei Cloud public Maven mirror.
The Huawei mirror is required only for seven fixed legacy artifacts that Maven
Central no longer serves. It is used while Gradle builds the APK; it is not an
application runtime endpoint.
