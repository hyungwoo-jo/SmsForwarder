package cn.ppps.forwarder.utils.interceptor

import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Test
import java.nio.charset.StandardCharsets

class WebhookTransportTest {
    @Test
    fun plainTextWebhookPreservesUtf8BodyAndHeaders() {
        val server = MockWebServer()
        server.enqueue(MockResponse().setResponseCode(204))
        server.start()
        try {
            val body = "한국어 줄바꿈\\n이모지 ✅"
            val request = Request.Builder().url(server.url("/ntfy"))
                .header("X-Topic", "sms")
                .post(RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), body))
                .build()
            OkHttpClient().newCall(request).execute().use { assertEquals(204, it.code()) }
            val received = server.takeRequest()
            assertEquals("sms", received.getHeader("X-Topic"))
            assertEquals(body, received.body.readString(StandardCharsets.UTF_8))
            assertEquals("text/plain; charset=utf-8", received.getHeader("Content-Type"))
        } finally {
            server.shutdown()
        }
    }
}
