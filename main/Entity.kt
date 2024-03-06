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

@file:Suppress("unused", "UNUSED_PARAMETER")

package com.github.nyayurn.yuntori

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.nyayurn.yuntori.message.MessageSegment

/**
 * 频道, 参考 https://satori.chat/zh-CN/resources/channel.html#channel
 * @property id 频道 ID
 * @property type 频道类型
 * @property name 频道名称
 * @property parentId 父频道 ID
 */
data class Channel(
    val id: String,
    val type: Type,
    val name: String? = null,
    @JsonProperty("parent_id") val parentId: String? = null
) {
    /**
     * Channel.Type, 参考 https://satori.chat/zh-CN/resources/channel.html#channel-type
     */
    enum class Type(type: Number) {
        /**
         * 文本频道
         */
        TEXT(0),

        /**
         * 语音频道
         */
        VOICE(1),

        /**
         * 分类频道
         */
        CATEGORY(2),

        /**
         * 私聊频道
         */
        DIRECT(3)
    }
}

/**
 * 群组, 参考 https://satori.chat/zh-CN/resources/guild.html#guild
 * @property id 群组 ID
 * @property name 群组名称
 * @property avatar 群组头像
 */
data class Guild(
    val id: String,
    val name: String? = null,
    val avatar: String? = null,
)

/**
 * 群组成员, 参考 https://satori.chat/zh-CN/resources/member.html#guildmember
 * @property user 用户对象
 * @property nick 用户在群组中的名称
 * @property avatar 用户在群组中的头像
 * @property joinedAt 加入时间
 */
data class GuildMember(
    val user: User? = null,
    val nick: String? = null,
    val avatar: String? = null,
    @JsonProperty("joined_at") val joinedAt: Number?
)

/**
 * 群组角色, 参考 https://satori.chat/zh-CN/resources/role.html#guildrole
 * @property id 角色 ID
 * @property name 角色名称
 */
data class GuildRole(
    val id: String,
    val name: String? = null
)

/**
 * 交互, 参考 https://satori.chat/zh-CN/resources/interaction.html
 */
interface Interaction {
    /**
     * Argv, 参考 https://satori.chat/zh-CN/resources/interaction.html#argv
     * @property name 指令名称
     * @property arguments 参数
     * @property options 选项
     */
    data class Argv(
        val name: String,
        val arguments: List<Any>,
        val options: Any
    ) : Interaction

    /**
     * Button, 参考 https://satori.chat/zh-CN/resources/interaction.html#button
     * @property id 按钮 ID
     */
    data class Button(val id: String) : Interaction
}

/**
 * 登录信息, 参考 https://satori.chat/zh-CN/resources/login.html#login
 * @property user 用户对象
 * @property selfId 平台账号
 * @property platform 平台名称
 * @property status 登录状态
 */
data class Login(
    val user: User? = null,
    @JsonProperty("self_id") val selfId: String? = null,
    val platform: String? = null,
    val status: Status
) {
    /**
     * Status, 参考 https://satori.chat/zh-CN/resources/login.html#status
     */
    enum class Status(val value: Number) {
        /**
         * 离线
         */
        OFFLINE(0),

        /**
         * 在线
         */
        ONLINE(1),

        /**
         * 连接中
         */
        CONNECT(2),

        /**
         * 断开连接
         */
        DISCONNECT(3),

        /**
         * 重新连接
         */
        RECONNECT(4)
    }
}

/**
 * 消息, 参考 https://satori.chat/zh-CN/resources/message.html#message
 * @property id 消息 ID
 * @property content 消息内容
 * @property channel 频道对象
 * @property guild 群组对象
 * @property member 成员对象
 * @property user 用户对象
 * @property createdAt 消息发送的时间戳
 * @property updatedAt 消息修改的时间戳
 */
data class Message(
    val id: String,
    val content: MessageSegment,
    val channel: Channel? = null,
    val guild: Guild? = null,
    val member: GuildMember? = null,
    val user: User? = null,
    @JsonProperty("created_at") val createdAt: Number? = null,
    @JsonProperty("updated_at") val updatedAt: Number? = null
) {
    @JsonCreator
    constructor(
        @JsonProperty("id") id: String,
        @JsonProperty("content") content: String,
        @JsonProperty("channel") channel: Channel? = null,
        @JsonProperty("guild") guild: Guild? = null,
        @JsonProperty("member") member: GuildMember? = null,
        @JsonProperty("user") user: User? = null,
        @JsonProperty("created_at") createdAt: Number? = null,
        @JsonProperty("updated_at") updatedAt: Number? = null
    ): this(id, MessageSegment.of(content), channel, guild, member, user, createdAt, updatedAt)
}

/**
 * 用户, 参考 https://satori.chat/zh-CN/resources/user.html#user
 * @property id 用户 ID
 * @property name 用户名称
 * @property nick 用户昵称
 * @property avatar 用户头像
 * @property isBot 是否为机器人
 */
data class User(
    val id: String,
    val name: String? = null,
    val nick: String? = null,
    val avatar: String? = null,
    @JsonProperty("is_bot") val isBot: Boolean? = null
)

/**
 * 信令, 参考 https://satori.chat/zh-CN/protocol/events.html#websocket
 * @property op 信令类型
 * @property body 信令数据
 */
data class Signaling(val op: Int, var body: Body? = null) {
    interface Body

    companion object {
        fun parse(json: String): Signaling {
            val mapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            val node = mapper.readTree(json)
            return when (val op = node["op"].asInt()) {
                EVENT -> Signaling(op, parseEvent(mapper.readValue<Event>((node["body"] as ObjectNode).apply {
                    set<ObjectNode>("raw", TextNode(this.toString()))
                }.toString())))

                READY -> Signaling(op, Ready(mapper.readValue(node["body"]["logins"].toString())))
                PONG -> Signaling(op)
                else -> throw NoSuchElementException()
            }
        }

        private fun parseEvent(event: Event) = try {
            val type = event.type
            when {
                type.startsWith("guild-member-") -> GuildMemberEvent.parse(event)
                type == InteractionEvents.BUTTON -> InteractionButtonEvent.parse(event)
                type == InteractionEvents.COMMAND -> InteractionCommandEvent.parse(event)
                type.startsWith("message-") -> MessageEvent.parse(event)
                else -> event
            }
        } catch (e: Throwable) {
            throw EventParsingException(e)
        }

        /**
         * 事件
         */
        const val EVENT = 0

        /**
         * 心跳
         */
        const val PING = 1

        /**
         * 心跳回复
         */
        const val PONG = 2

        /**
         * 鉴权
         */
        const val IDENTIFY = 3

        /**
         * 鉴权回复
         */
        const val READY = 4
    }
}

/**
 * 鉴权回复
 * @property logins 登录信息
 */
data class Ready(val logins: List<Login>) : Signaling.Body

/**
 * 鉴权
 * @property token 鉴权令牌
 * @property sequence 序列号
 */
data class Identify(
    var token: String? = null,
    var sequence: Number? = null
) : Signaling.Body

/**
 * 分页数据, 参考 https://satori.chat/zh-CN/protocol/api.html#%E5%88%86%E9%A1%B5
 * @param T 数据类型
 * @property data 数据
 * @property next 下一页的令牌
 */
data class PaginatedData<T>(
    val data: List<T>,
    val next: String? = null
)

/**
 * 云湖 Server 配置
 * @property host 云湖 Server 主机
 * @property port 云湖 Server 端口
 * @property path 云湖 Server 路径
 * @property token 云湖 Server 鉴权令牌
 * @property version 云湖 Server 协议版本
 */
data class YunhuProperties(
    val host: String = "chat-go.jwzhd.com",
    val port: Int = 5500,
    val path: String = "open-apis",
    val token: String? = null,
    val version: String = "v1"
)

/**
 * WebHook Server 配置
 * @property serverHost WebHook Server 监听主机
 * @property serverPort WebHook Server 监听端口
 * @property server 云湖 Server 配置
 */
data class WebHookProperties(
    val serverHost: String = "0.0.0.0",
    val serverPort: Int = 8080,
    val server: YunhuProperties
)

abstract class YunhuContent {
    val buttons = mutableListOf<YunhuButton>()
}

open class TextContent(text: String = "") : YunhuContent() {
    val text: StringBuilder = StringBuilder(text)
}
class ImageContent(val imageUrl: String) : YunhuContent()
class MarkdownContent(text: String = "") : TextContent(text)
class FileContent(val fileName: String, val fileUrl: String) : YunhuContent()

data class YunhuButton(
    val text: String,
    val actionType: Int,
    val url: String?,
    val value: String?
)