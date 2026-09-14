package com.dshbox.app.common

import org.junit.Assert.assertEquals
import org.junit.Test

class DshUrlsTest {

    @Test
    fun appendsTokenAsQuery() {
        assertEquals(
            "http://127.0.0.1:3080?token=abc",
            DshUrls.withLaunchToken("http://127.0.0.1:3080", "abc"),
        )
    }

    @Test
    fun appendsTokenToExistingQuery() {
        assertEquals(
            "http://127.0.0.1:3080/?x=1&token=abc",
            DshUrls.withLaunchToken("http://127.0.0.1:3080/?x=1", "abc"),
        )
    }

    @Test
    fun leavesBaseWhenTokenMissing() {
        assertEquals("http://127.0.0.1:3080", DshUrls.withLaunchToken("http://127.0.0.1:3080", null))
        assertEquals("http://127.0.0.1:3080", DshUrls.withLaunchToken("http://127.0.0.1:3080", ""))
    }

    @Test
    fun doesNotDuplicateExistingToken() {
        val url = "http://127.0.0.1:3080?token=old"
        assertEquals(url, DshUrls.withLaunchToken(url, "new"))
    }
}
