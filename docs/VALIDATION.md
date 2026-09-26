# 검증 기록

## 현재 확인 범위

서버에서 실행 가능한 자동 검증과 APK 빌드를 확인한다. 휴대폰/워치 연결을 요구하는 동작은 `DEVICE_PENDING`이며, 실제 업무 알림·봇 토큰을 테스트에 사용하지 않았다.

| 항목 | 상태 | 증거 / 한계 |
|---|---|---|
| 원본 고정 및 개인 포크 | PASS | `docs/UPSTREAM.md`, upstream `a3d23026f0058420869163c1d5dfb463ce52fc15` |
| 한국어 리소스 | PASS | `python3 scripts/check_localization.py`: 1,186개 (기존 1,185 + 권한 거절 문구) |
| canonical 태그 유지 | PASS | `scripts/check_privacy.py`: 원본과 30개 동일, locale override 없음 |
| 권한 거절 문구 | PASS | `XBasicLibInit.kt`가 한국어 리소스 사용 |
| Umeng source/dependency/manifest | PASS | 제거 커밋 및 `scripts/check_privacy.py`; DEX 확인은 릴리스 수집 때 수행 |
| 자동 업데이트·원격 안내·FRPC 다운로드 | PASS | Stage C 제거; native FRPC 라이브러리 APK 포함 |
| 전화번호 조회 OFF | PASS | 기본 OFF 정적 검사, `disabledPhoneAreaLookupDoesNotInvokeNetworkLookup` 테스트에서 lookup 호출 0회 |
| 로그 인증정보 및 응답 보존 | PASS | `SensitiveLogRedactorTest`, `ResponseBodySnapshot`, 전송 결과 저장도 마스킹; HTTP 본문 로그 생략 |
| Telegram | PASS | GET/POST × TEXT/HTML/MarkdownV2 로컬 요청, 한국어·이모지, 401/429·연결 실패 테스트. XHttp/Android callback 전체의 기기 통합 검증은 별도 |
| Webhook | PASS | 실제 본문 renderer를 이용한 UTF-8 plain text/헤더/204 모의 서버 및 JSON 이스케이프 테스트 |
| TLS | PASS | 명시적으로 신뢰한 로컬 테스트 인증서 성공, 기본 trust store에서 미신뢰 인증서 거절; production trust-all 정적 검사 |
| JVM 테스트 | PASS | `build/app/test-results/testDebugUnitTest/`: 10 tests, failures/errors 0 |
| Debug APK | PASS | 최종 코드 `8ddd2812`의 `:app:assembleDebug` 성공 |
| Debug lint | PASS | 앞선 gate: errors 0, warnings 200. `VectorDrawableCompat` 기존 레이아웃 검사만 제외, 전체 abortOnError는 유지 |
| Release / 서명 / DEX | PASS | `8ddd2812`의 arm64-v8a/universal APK, `dist/BUILD-INFO.json`, 동일 인증서의 v1/v2 서명 검증, DEX 내 `com/umeng/` 문자열 없음, ABI별 FRPC 라이브러리 포함 |
| 기기·Galaxy Watch | DEVICE_PENDING | 설치·언어 변경·NAVER WORKS A/B 알림·재부팅/절전·갱신 확인 필요 |
| GitHub Actions | NOT RUN | 선택 단계 G, 개인 서명을 CI에 전송하지 않음 |

## 최종 릴리스

- applicationId: `com.hwserve.smsforwarder`
- versionName: `3.5.0.260926-personal-ko-8ddd2812`
- arm64-v8a: `dist/SmsF_3.5.0.260926-personal-ko-8ddd2812_300055_arm64-v8a_release.apk`
- universal: `dist/SmsF_3.5.0.260926-personal-ko-8ddd2812_100055_universal_release.apk`
- arm64-v8a SHA-256: `efdf0c1a7cfd605f4d33e5d9a45c6c31b614ff4e178aac6ddad79c04490438dc`
- universal SHA-256: `ccde63fd32c1402f1696ea2418c4579eca457ce3cf759bf93a6ec61529725701`
- signing certificate SHA-256: `e476e7b37e0119f0b01edd874c3cf3bc88547ec7d322988cfdf5d9813b90e710`
- debug/release lint: 각각 errors 0, warnings 200. 기존 vector 호환성 예외만 명시적으로 제외.
- 서명 보고서의 v1 `META-INF` mail/provider 리소스 경고는 기능 유지를 위해 남겼다. v2 서명은 검증됐다. Android 19~23의 실제 설치·동작은 기기에서 확인하지 않았다.
- 기기 확인 명령 `adb devices -l` 결과 연결 기기 없음.
- 최종 소스 빌드 당시 working tree clean. 이후 검증 문서와 빌드 스크립트의 캐시 처리만 보완했다.
- 키는 `.smsforwarder-local/signing/smsforwarder-personal.p12`에 보관하고, 디렉터리 0700/키·암호·서명 설정 0600을 확인했다. Git에 키를 올리지 않았다.

두 번째 릴리스 빌드에서 생성 리소스 병합 캐시의 잘못된 source path로 실패했다. 해당 생성 캐시를 `/tmp/smsforwarder-resource-cache-8ddd2812`에 보존한 뒤 재생성하여 빌드를 통과했다. 반복 빌드용 스크립트에는 `:app:mergeReleaseResources --rerun-tasks`를 추가해 전체 clean 없이 리소스 병합 상태를 다시 생성한다.

## 남아 있는 검토 범위

- 한국어 문자열 4~6번 청크는 자동 번역 초안을 사용했다. 전체 키·형식 검사는 통과했으나 자연스러운 UI 표현은 기기에서 추가 검토가 필요하다.
- 두 안내 문구의 중국어 중괄호 태그는 원본 템플릿 토큰 보존을 위해 유지했다.
- 기존 라이브러리 대화상자의 문구와 좁은 화면 배치는 기기에서 확인해야 한다.
- 여기의 정적 검사와 로컬 mock 테스트는 전체 Kotlin/네이티브 코드 보안 감사 또는 모든 기능의 무통신 보장을 의미하지 않는다.
- 날짜 기반 버전명·빌드 시간 필드 때문에 APK 바이트 단위 재현성을 보장하지 않는다.
