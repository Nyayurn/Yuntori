package com.github.nyayurn.yutori.next.message.elements

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Interaction {
    @Test
    fun button() {
        Assertions.assertEquals(
            "<button id=\"114514\" type=\"link\" href=\"www.baidu.com\" text=\"Hello\" theme=\"info\"/>",
            Button("114514", "link", "www.baidu.com", "Hello", "info").toString()
        )
    }
}