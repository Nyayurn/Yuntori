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

@file:Suppress("unused")

package com.github.nyayurn.yutori.next

import com.github.nyayurn.yutori.next.message.element.*
import com.github.nyayurn.yutori.next.message.element.Message
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode

/**
 * JsonObject 字符串 DSL 构建器
 */
inline fun jsonObj(dsl: JsonObjectDSLBuilder.() -> Unit) = JsonObjectDSLBuilder().apply(dsl).toString()

/**
 * JsonArray 字符串 DSL 构建器
 */
inline fun jsonArr(dsl: JsonArrayDSLBuilder.() -> Unit) = JsonArrayDSLBuilder().apply(dsl).toString()

class JsonObjectDSLBuilder {
    private val map = mutableMapOf<String, Any>()
    fun put(key: String, value: Any?) {
        value?.let { map[key] = it }
    }

    fun put(key: String, dsl: () -> Any?) {
        dsl()?.let { map[key] = it }
    }

    fun putJsonObj(key: String, dsl: JsonObjectDSLBuilder.() -> Unit) {
        map[key] = JsonObjectDSLBuilder().apply(dsl)
    }

    fun putJsonArr(key: String, dsl: JsonArrayDSLBuilder.() -> Unit) {
        map[key] = JsonArrayDSLBuilder().apply(dsl)
    }

    override fun toString() = map.entries.joinToString(",", "{", "}") { (key, value) ->
        buildString {
            append("\"$key\":")
            append(
                when (value) {
                    is String -> "\"$value\""
                    else -> value.toString()
                }
            )
        }
    }
}

class JsonArrayDSLBuilder {
    private val list = mutableListOf<Any>()
    fun add(value: Any?) {
        value?.let { list += it }
    }

    fun add(dsl: () -> Any?) {
        dsl()?.let { list += it }
    }

    fun addJsonArr(dsl: JsonArrayDSLBuilder.() -> Unit) {
        list += JsonArrayDSLBuilder().apply(dsl)
    }

    fun addJsonObj(dsl: JsonObjectDSLBuilder.() -> Unit) {
        list += JsonObjectDSLBuilder().apply(dsl)
    }

    override fun toString() = list.joinToString(",", "[", "]") { value ->
        buildString {
            append(
                when (value) {
                    is String -> "\"$value\""
                    else -> value.toString()
                }
            )
        }
    }
}

/**
 * 辅助类
 */
object MessageUtil {
    /**
     * 转义字符串
     * @return 转以后的字符串
     */
    fun String.encode() = replace("&", "&amp;").replace("\"", "&quot;").replace("<", "&lt;").replace(">", "&gt;")

    fun String.decode() = replace("&gt;", ">").replace("&lt;", "<").replace("&quot;", "\"").replace("&amp;", "&")

    /**
     * 提取出 Satori 消息字符串中的纯文本消息元素
     * @param raw 字符串
     * @return 元素链
     */
    fun extractTextChain(raw: String) = mutableListOf<Text>().apply {
        for (node in Jsoup.parse(raw).body().childNodes()) if (node is TextNode) this.add(Text(node.text()))
    }

    /**
     * 将 Satori 消息字符串转换为元素链
     * @param raw 字符串
     * @return 元素链
     */
    fun parseElementChain(raw: String) = mutableListOf<MessageElement>().apply {
        for (node in Jsoup.parse(raw).body().childNodes()) parseMessageElement(node)?.let { this.add(it) }
    }

    /**
     * 解析消息元素
     * @param node 节点
     * @return 消息元素
     */
    private fun parseMessageElement(node: Node): MessageElement? = when (node) {
        is TextNode -> Text(node.text())
        is Element -> when (node.tagName()) {
            "at" -> At()
            "sharp" -> Sharp(node.attr("id"))
            "a" -> Href(node.attr("href"))
            "img" -> Image(node.attr("src"))
            "audio" -> Audio(node.attr("src"))
            "video" -> Video(node.attr("src"))
            "file" -> File(node.attr("src"))
            "b" -> Bold()
            "strong" -> Strong()
            "i" -> Idiomatic()
            "em" -> Em()
            "u" -> Underline()
            "ins" -> Ins()
            "s" -> Strikethrough()
            "del" -> Delete()
            "spl" -> Spl()
            "code" -> Code()
            "sup" -> Sup()
            "sub" -> Sub()
            "br" -> Br()
            "p" -> Paragraph()
            "message" -> Message()
            "quote" -> Quote()
            "author" -> Author()
            "button" -> Button()
            else -> null
        }?.apply {
            for (attr in node.attributes()) this[attr.key] = attr.value
            for (child in node.childNodes()) parseMessageElement(child)?.let { this += it }
        }

        else -> null
    }
}