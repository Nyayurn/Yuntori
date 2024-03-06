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

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.nyayurn.yuntori

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.nyayurn.yuntori.message.MessageDslBuilder
import com.github.nyayurn.yuntori.message.MessageSegment
import com.github.nyayurn.yuntori.message.elements.*
import com.github.nyayurn.yuntori.message.message
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking

/**
 * 封装所有 Action, 应通过本类对云湖 Server 发送 Action
 * @property message 消息 API
 * @property board 看板 API
 * @property properties 配置信息, 供使用者获取
 * @property name 属于哪个 Event Service
 */
class Actions private constructor(
    val message: MessageAction,
    val board: BoardAction,
    val properties: YunhuProperties,
    val name: String
) {
    companion object {
        /**
         * 工厂方法
         * @param properties 配置
         */
        fun of(properties: YunhuProperties, name: String) = Actions(
            MessageAction.of(properties, name),
            BoardAction.of(properties, name),
            properties,
            name
        )

        /**
         * 工厂方法
         * @param dsl 配置 DSL
         */
        inline fun of(
            name: String,
            dsl: WebHookEventService.Builder.PropertiesBuilder.() -> Unit
        ) = of(WebHookEventService.Builder.PropertiesBuilder().apply(dsl).build().server, name)
    }
}

class MessageAction private constructor(val user: UserAction, private val generalAction: GeneralAction) {
    /**
     * 发送消息
     * @param channelId 频道 ID
     * @param content 消息内容
     */
    fun create(channelId: String, content: MessageSegment): List<Message> {
        val list = mutableListOf<YunhuContent>(TextContent())
        for (element in content) {
            val last = list.last()
            when (element) {
                is Text -> (last as? TextContent ?: TextContent().apply { list.add(this) }).text.append(element)

                is Href -> when (last) {
                    is TextContent -> last.text.append(
                        "[${element.children.joinToString("") { it.toString() }}](${element.href})"
                    )

                    else -> MarkdownContent().apply {
                        list.add(this)
                    }.text.append("[${element.children.joinToString("") { it.toString() }}](${element.href})")
                }

                is Image -> when (last) {
                    is MarkdownContent -> last.text.append("![${element.title}](${element.src})")
                    else -> list.add(ImageContent(element.src))
                }

                is File -> list.add(FileContent(element.title ?: "这是一个文件", element.src))
                is Bold, is Strong -> (last as? MarkdownContent)?.text?.append("**$element**")
                is Idiomatic, is Em -> (last as? MarkdownContent)?.text?.append("*$element*")
                is Underline, is Delete -> (last as? MarkdownContent)?.text?.append("~~$element~~")
                is Code -> (last as? MarkdownContent)?.text?.append("```${element.lang}\n$element\n```")
                is Br -> (last as? TextContent)?.text?.append("\n")
                is Paragraph -> (last as? TextContent)?.text?.append(
                    "\n${element.children.joinToString("") { it.toString() }}\n"
                )

                is com.github.nyayurn.yuntori.message.elements.Message -> if (element.markdown) MarkdownContent() else TextContent().apply {
                    list.add(this)
                }.text.append(element.children.joinToString("") { it.toString() })

                is Quote -> (last as? MarkdownContent)?.text?.append(
                    "> ${element.children.joinToString("") { it.toString() }}"
                )

                is Button -> last.buttons += YunhuButton(
                    element.children.joinToString("") { it.toString() }, when (element.type) {
                        "action" -> 3
                        "link" -> 1
                        "copy" -> 2
                        else -> throw UnknownError("未知 Button type: ${element.type}")
                    }, element.href, element.text
                )
            }
        }
        val result = mutableListOf<Message>()
        for (yunhuContent in list) println(generalAction.send("send") {
            val split = channelId.split(":")
            put("recvId", split[1])
            put("recvType", split[0])
            put(
                "contentType", when (yunhuContent) {
                    is MarkdownContent -> "markdown"
                    is TextContent -> "text"
                    is ImageContent -> "image"
                    is FileContent -> "file"
                    else -> throw UnknownError("Unknown content")
                }
            )
            put("content", jsonObj {
                when (yunhuContent) {
                    is TextContent -> put("text", yunhuContent.text.toString())
                    is ImageContent -> put("imageUrl", yunhuContent.imageUrl)
                    is FileContent -> {
                        put("fileName", yunhuContent.fileName)
                        put("fileUrl", yunhuContent.fileUrl)
                    }
                }
                if (yunhuContent.buttons.isNotEmpty()) putJsonArr("buttons") {
                    for (button in yunhuContent.buttons) addJsonObj {
                        put("text", button.text)
                        put("actionType", button.actionType)
                        if (button.url != null) put("url", button.url)
                        if (button.value != null) put("value", button.value)
                    }
                }
            })
        })
        return result
    }

    /**
     * 使用纯文本发送消息
     * @param channelId 频道 ID
     * @param text 消息内容
     */
    fun create(channelId: String, text: String) = create(channelId, MessageSegment.of(text))

    /**
     * 使用 DSL 发送消息
     * @param channelId 频道 ID
     * @param block 消息内容 DSL
     */
    inline fun create(channelId: String, block: MessageDslBuilder.() -> Unit) = create(channelId, message(block))

    fun get(channelId: String, messageId: String): Message {
        throw NotImplementedError("channel_id: $channelId, message_id: $messageId")
    }

    /**
     * 批量发送消息
     * @param channelsId 频道 ID 列表
     * @param content 消息内容
     */
    fun batch(channelsId: List<String>, content: MessageSegment): List<Message> {
        throw NotImplementedError("channels_id: $channelsId, content: $content")
    }

    /**
     * 使用纯文本批量发送消息
     * @param channelsId 频道 ID 列表
     * @param text 消息内容
     */
    fun batch(channelsId: List<String>, text: String) = batch(channelsId, MessageSegment.of(text))

    /**
     * 使用 DSL 批量发送消息
     * @param channelsId 频道 ID 列表
     * @param block 消息内容 DSL
     */
    inline fun batch(channelsId: List<String>, block: MessageDslBuilder.() -> Unit) = batch(channelsId, message(block))

    /**
     * 编辑消息
     * @param channelId 频道 ID
     * @param messageId 消息 ID
     * @param content 消息内容
     */
    fun update(channelId: String, messageId: String, content: MessageSegment) {
        throw NotImplementedError("channel_id: $channelId, message_id: $messageId, content: $content")
    }

    /**
     * 使用纯文本编辑消息
     * @param channelId 频道 ID
     * @param messageId 消息 ID
     * @param text 消息内容
     */
    fun update(channelId: String, messageId: String, text: String) =
        update(channelId, messageId, MessageSegment.of(text))

    /**
     * 使用 DSL 编辑消息
     * @param channelId 频道 ID
     * @param messageId 消息 ID
     * @param block 消息内容 DSL
     */
    inline fun update(channelId: String, messageId: String, block: MessageDslBuilder.() -> Unit) =
        update(channelId, messageId, MessageDslBuilder().apply(block).build())

    companion object {
        fun of(properties: YunhuProperties, name: String) = MessageAction(
            UserAction.of(properties, name), GeneralAction(properties, name)
        )
    }

    class UserAction private constructor(private val generalAction: GeneralAction) {
        /**
         * 获取用户发送的消息列表
         * @param channelId 频道 ID
         * @param messageId 消息 ID
         * @param range 获取范围
         */
        fun list(channelId: String, messageId: String, range: IntRange): List<Message> {
            throw NotImplementedError("channel_id: $channelId, message_id: $messageId, range: $range")
        }

        companion object {
            fun of(properties: YunhuProperties, name: String) = UserAction(GeneralAction(properties, name))
        }
    }
}

class BoardAction private constructor(private val generalAction: GeneralAction) {
    /**
     * 设置看板
     * @param channelId 频道 ID, 非 null 设置用户看板, null 设置全局看板
     * @param content 看板内容
     */
    fun set(channelId: String? = null, content: MessageSegment) {
        throw NotImplementedError("channel_id: $channelId, content: $content")
    }

    /**
     * 使用纯文本设置看板
     * @param channelId 频道 ID, 非 null 设置用户看板, null 设置全局看板
     * @param text 看板内容
     */
    fun set(channelId: String? = null, text: String) = set(channelId, MessageSegment.of(text))


    /**
     * 使用 DSL 设置看板
     * @param channelId 频道 ID, 非 null 设置用户看板, null 设置全局看板
     * @param block 看板内容 DSL
     */
    fun set(channelId: String? = null, block: MessageDslBuilder.() -> Unit) = set(channelId, message(block))

    /**
     * 取消看板
     * @param channelId 频道 ID, 非 null 取消用户看板, null 取消全局看板
     */
    fun unset(channelId: String? = null) {
        throw NotImplementedError("channel_id: $channelId")
    }

    companion object {
        fun of(properties: YunhuProperties, name: String) = BoardAction(GeneralAction(properties, name))
    }
}

class GeneralAction(val properties: YunhuProperties, val name: String) {
    val mapper: ObjectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    val logger = GlobalLoggerFactory.getLogger(this::class.java)

    fun send(resource: String, block: JsonObjectDSLBuilder.() -> Unit): String = runBlocking {
        HttpClient(CIO).use { client ->
            val response = client.post {
                url {
                    host = properties.host
                    port = properties.port
                    appendPathSegments(properties.path, properties.version, "bot", resource)
                    parameters {
                        set("token", properties.token)
                    }
                }
                contentType(ContentType.Application.Json)
                setBody(jsonObj(block))
                logger.debug(
                    name, """
                    Yunhu Action: url: ${this.url},
                        headers: ${this.headers.build()},
                        body: ${this.body}
                    """.trimIndent()
                )
            }
            logger.debug(name, "Yunhu Action Response: $response")
            response.body()
        }
    }

    inline fun <reified T> sendWithSerialize(resource: String, noinline block: JsonObjectDSLBuilder.() -> Unit): T =
        try {
            mapper.readValue<T>(send(resource, block))
        } catch (e: Exception) {
            logger.warn(name, e.localizedMessage)
            throw e
        }
}