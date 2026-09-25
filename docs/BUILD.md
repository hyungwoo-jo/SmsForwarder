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
