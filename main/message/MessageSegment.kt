/*
Copyright (c) 2024 Yurn
Yutori-Next is licensed under Mulan PSL v2.
You can use this software according to the terms and conditions of the Mulan PSL v2.
You may obtain a copy of Mulan PSL v2 at:
         http://license.coscl.org.cn/MulanPSL2
THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
See the Mulan PSL v2 for more details.
 */

package com.github.nyayurn.yuntori.message

import com.github.nyayurn.yuntori.message.elements.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode

class MessageSegment(private val elements: List<MessageElement>): List<MessageElement> by elements {
    override fun toString() = elements.joinToString("") { it.toString() }

    companion object {
        fun of(str: String): MessageSegment {
            val nodes = Jsoup.parse(str).body().childNodes()
            return MessageSegment(List(nodes.size) { i -> parseElement(nodes[i]) })
        }

        private fun parseElement(node: Node): MessageElement = when (node) {
            is TextNode -> Text(node.text())
            is Element -> when (node.tagName()) {
                              "a" -> Href(node.attr("href"))
                              "img" -> Image(node.attr("src"))
                              "file" -> File(node.attr("src"))
                              "b" -> Bold()
                              "strong" -> Strong()
                              "i" -> Idiomatic()
                              "em" -> Em()
                              "u" -> Underline()
                              "ins" -> Ins()
                              "s" -> Strikethrough()
                              "del" -> Delete()
                              "code" -> Code()
                              "br" -> Br()
                              "p" -> Paragraph()
                              "message" -> Message()
                              "quote" -> Quote()
                              "button" -> Button()
                              else -> null
                          }?.apply {
                for (attr in node.attributes()) this[attr.key] = attr.value
                for (child in node.childNodes()) this += parseElement(child)
            } ?: Text(node.toString())

            else -> Text(node.toString())
        }
    }
}