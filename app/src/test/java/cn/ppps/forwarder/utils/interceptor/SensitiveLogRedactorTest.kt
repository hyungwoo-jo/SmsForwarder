package cn.ppps.forwarder.utils.interceptor

import org.junit.Assert.assertFalse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody

class SensitiveLogRedactorTest {
    @Test
    fun redactsDestinationCredentialsAndMessageContent() {
        val secretValues = listOf(
            "123456:telegram-token",
            "query-token",
            "Bearer proxy-secret",
            "custom-message",
            "proxy-password",
            "https://user:password@example.test/hook?token=query-token"
        )
        val log = """
            POST https://api.telegram.org/bot123456:telegram-token/sendMessage?token=query-token
            Authorization: Bearer proxy-secret
            Proxy-Authorization: Basic proxy-password
            {"text":"custom-message","chat_id":"12345"}
            https://user:password@example.test/hook?token=query-token
        """.trimIndent()

        val redacted = SensitiveLogRedactor.redact(log)

        secretValues.forEach { assertFalse("leaked: $it", redacted.contains(it)) }
        assertTrue(redacted.contains("[redacted-url]"))
        assertTrue(redacted.contains("Authorization: ***"))
    }

    @Test
    fun readingForLogsLeavesResponseBodyAvailableToSender() {
        val original = Response.Builder()
            .request(Request.Builder().url("https://example.test/").build())
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .body(ResponseBody.create(null, "응답 본문 ✅"))
            .build()

        val snapshot = ResponseBodySnapshot.read(original)

        assertEquals("응답 본문 ✅", snapshot.body)
        assertEquals("응답 본문 ✅", snapshot.response.body()!!.string())
    }
}
