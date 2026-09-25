# Network audit

This is the baseline before changes. It distinguishes runtime requests from
repositories contacted only while Gradle resolves build dependencies.

| Feature | Source location | Observed baseline behaviour | Planned action |
|---|---|---|---|
| Umeng analytics | `utils/sdkinit/UMengInit.kt`, `App.kt` | Initializes release analytics after privacy consent | Remove SDK, initialization, calls, and rules. |
| App update | `utils/sdkinit/XUpdateInit.kt`, `MainActivity.kt` | Automatic and manual requests to upstream update service | Remove automatic checks; replace manual action after fork release policy is set. |
| Remote tips | `widget/GuideTipsDialog.kt` | Fetches a remote tips URL | Use packaged Korean content. |
| FRP library | `MainActivity.kt`, `Constants.kt` | Downloads `libgojni.so` from upstream service | Bundle and load the AAR library; remove runtime download. |
| Phone-area lookup | `PhoneUtils.kt`, `MsgInfo.kt` | Sends a phone number to `cx.shouji.360.cn` when template uses the tag | Add an explicit default-off setting. |
| Public IP lookup | `workers/NetworkWorker.kt` | Contacts ipify for a network feature | Verify trigger and disable unsolicited lookup. |
| Telegram | `utils/sender/TelegramUtils.kt` | Sends to user-selected Bot API or custom URL | Preserve with secret-safe logs. |
| Webhook and other channels | `utils/sender/*` | Send to user-configured endpoints | Preserve; restore normal TLS verification. |

No claim about full runtime traffic or bundled native-code behaviour is made
until the edited APK has been built and tested.
