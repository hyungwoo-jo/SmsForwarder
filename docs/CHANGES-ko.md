# 개인판 변경 및 라이선스

원작: pppscn/SmsForwarder, 기준 commit `a3d23026f0058420869163c1d5dfb463ce52fc15`.
개인 포크: https://github.com/hyungwoo-jo/SmsForwarder, `personal/ko-local`.

- 한국어 리소스와 언어 선택, 로컬 한국어 개인정보 안내.
- 개인 applicationId와 로컬 빌드·서명 스크립트.
- Umeng 분석 SDK, XUpdate, 원격 안내 및 FRPC 자동 다운로드 제거.
- FRPC 네이티브 라이브러리 APK 포함, 정상 TLS 인증서 검증 사용.
- 전화번호 외부 지역 조회 기본 OFF, 전송 로그의 인증정보 마스킹.
- Webhook plain text 본문 보존, Telegram TEXT 전송의 메시지 변형 방지.

원본 GPL-3.0 라이선스와 원작자 표시를 유지합니다. AndroidX, OkHttp, Gson, XUI/XHttp/XAOP, FRPC 및 원본에서 사용하던 의존성은 각각의 원본 라이선스를 따릅니다. 본 변경은 전체 의존성 또는 네이티브 코드의 보안 감사를 의미하지 않습니다.
