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
| JVM 테스트 | PASS | `build/app/test-results/testDebugUnitTest/`: 9 tests, failures/errors 0 |
| Debug APK | PASS | Stage D build successful, Stage E 최종 코드 APK는 최종 빌드에서 확인 |
| Debug lint | PASS | 앞선 gate: errors 0, warnings 200. `VectorDrawableCompat` 기존 레이아웃 검사만 제외, 전체 abortOnError는 유지 |
| Release / 서명 / DEX | NOT RUN | Stage F 실행 예정 |
| 기기·Galaxy Watch | DEVICE_PENDING | 설치·언어 변경·NAVER WORKS A/B 알림·재부팅/절전·갱신 확인 필요 |
| GitHub Actions | NOT RUN | 선택 단계 G, 개인 서명을 CI에 전송하지 않음 |

## 남아 있는 검토 범위

- 한국어 문자열 4~6번 청크는 자동 번역 초안을 사용했다. 전체 키·형식 검사는 통과했으나 자연스러운 UI 표현은 기기에서 추가 검토가 필요하다.
- 두 안내 문구의 중국어 중괄호 태그는 원본 템플릿 토큰 보존을 위해 유지했다.
- 기존 라이브러리 대화상자의 문구와 좁은 화면 배치는 기기에서 확인해야 한다.
- 여기의 정적 검사와 로컬 mock 테스트는 전체 Kotlin/네이티브 코드 보안 감사 또는 모든 기능의 무통신 보장을 의미하지 않는다.
- 날짜 기반 버전명·빌드 시간 필드 때문에 APK 바이트 단위 재현성을 보장하지 않는다.
