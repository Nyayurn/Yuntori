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

package com.github.nyayurn.yutori.next

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * 事件, 参考 https://satori.chat/zh-CN/protocol/events.html#event
 * @property id 事件 ID
 * @property type 事件类型
 * @property platform 接收者的平台名称
 * @property selfId 接收者的平台账号
 * @property timestamp 事件的时间戳
 * @property argv 交互指令
 * @property button 交互按钮
 * @property channel 事件所属的频道
 * @property guild 事件所属的群组
 * @property login 事件的登录信息
 * @property member 事件的目标成员
 * @property message 事件的消息
 * @property operator 事件的操作者
 * @property role 事件的目标角色
 * @property user 事件的目标用户
 */
open class Event(
    val id: Number,
    val type: String,
    val platform: String,
    @JsonProperty("self_id") val selfId: String,
    val timestamp: Number,
    open val argv: Interaction.Argv? = null,
    open val button: Interaction.Button? = null,
    open val channel: Channel? = null,
    open val guild: Guild? = null,
    open val login: Login? = null,
    open val member: GuildMember? = null,
    open val message: Message? = null,
    open val operator: User? = null,
    open val role: GuildRole? = null,
    open val user: User? = null,
    val raw: String
) : Signaling.Body {
    override fun toString(): String {
        return "Event(id=$id, type='$type', platform='$platform', selfId='$selfId', timestamp=$timestamp, argv=$argv, button=$button, channel=$channel, guild=$guild, login=$login, member=$member, message=$message, operator=$operator, role=$role, user=$user)"
    }
}

/**
 * 群组事件列表, 参考 https://satori.chat/zh-CN/resources/guild.html#%E4%BA%8B%E4%BB%B6
 */
object GuildEvents {
    const val ADDED = "guild-added"
    const val UPDATED = "guild-updated"
    const val REMOVED = "guild-removed"
    const val REQUEST = "guild-request"
}

/**
 * 群组事件实体类
 */
class GuildEvent(
    id: Number,
    type: String,
    platform: String,
    @JsonProperty("self_id") selfId: String,
    timestamp: Number,
    argv: Interaction.Argv? = null,
    button: Interaction.Button? = null,
    channel: Channel? = null,
    override val guild: Guild,
    login: Login? = null,
    member: GuildMember? = null,
    message: Message? = null,
    operator: User? = null,
    role: GuildRole? = null,
    user: User? = null,
    raw: String
) : Event(
    id,
    type,
    platform,
    selfId,
    timestamp,
    argv,
    button,
    channel,
    guild,
    login,
    member,
    message,
    operator,
    role,
    user,
    raw
) {
    companion object {
        @JvmStatic
        fun parse(event: Event) = GuildEvent(
            event.id,
            event.type,
            event.platform,
            event.selfId,
            event.timestamp,
            event.argv,
            event.button,
            event.channel,
            event.guild!!,
            event.login,
            event.member,
            event.message,
            event.operator,
            event.role,
            event.user,
            event.raw
        )
    }
}

/**
 * 群组成员事件列表, 参考 https://satori.chat/zh-CN/resources/member.html#%E4%BA%8B%E4%BB%B6
 */
object GuildMemberEvents {
    const val ADDED = "guild-member-added"
    const val UPDATED = "guild-member-updated"
    const val REMOVED = "guild-member-removed"
    const val REQUEST = "guild-member-request"
}

/**
 * 群组成员事件实体类
 */
class GuildMemberEvent(
    id: Number,
    type: String,
    platform: String,
    @JsonProperty("self_id") selfId: String,
    timestamp: Number,
    argv: Interaction.Argv? = null,
    button: Interaction.Button? = null,
    channel: Channel? = null,
    override val guild: Guild,
    login: Login? = null,
    override val member: GuildMember,
    message: Message? = null,
    operator: User? = null,
    role: GuildRole? = null,
    override val user: User,
    raw: String
) : Event(
    id,
    type,
    platform,
    selfId,
    timestamp,
    argv,
    button,
    channel,
    guild,
    login,
    member,
    message,
    operator,
    role,
    user,
    raw
) {
    companion object {
        @JvmStatic
        fun parse(event: Event) = GuildMemberEvent(
            event.id,
            event.type,
            event.platform,
            event.selfId,
            event.timestamp,
            event.argv,
            event.button,
            event.channel,
            event.guild!!,
            event.login,
            event.member!!,
            event.message,
            event.operator,
            event.role,
            event.user!!,
            event.raw
        )
    }
}

/**
 * 群组角色事件列表, 参考 https://satori.chat/zh-CN/resources/role.html#%E4%BA%8B%E4%BB%B6
 */
object GuildRoleEvents {
    const val CREATED = "guild-role-created"
    const val UPDATED = "guild-role-updated"
    const val DELETED = "guild-role-deleted"
}

/**
 * 群组角色事件实体类
 */
class GuildRoleEvent(
    id: Number,
    type: String,
    platform: String,
    @JsonProperty("self_id") selfId: String,
    timestamp: Number,
    argv: Interaction.Argv? = null,
    button: Interaction.Button? = null,
    channel: Channel? = null,
    override val guild: Guild,
    login: Login? = null,
    member: GuildMember? = null,
    message: Message? = null,
    operator: User? = null,
    override val role: GuildRole,
    user: User? = null,
    raw: String
) : Event(
    id,
    type,
    platform,
    selfId,
    timestamp,
    argv,
    button,
    channel,
    guild,
    login,
    member,
    message,
    operator,
    role,
    user,
    raw
) {
    companion object {
        @JvmStatic
        fun parse(event: Event) = GuildRoleEvent(
            event.id,
            event.type,
            event.platform,
            event.selfId,
            event.timestamp,
            event.argv,
            event.button,
            event.channel,
            event.guild!!,
            event.login,
            event.member,
            event.message,
            event.operator,
            event.role!!,
            event.user,
            event.raw
        )
    }
}

/**
 * 交互事件列表, 参考 https://satori.chat/zh-CN/resources/interaction.html#%E4%BA%8B%E4%BB%B6
 */
object InteractionEvents {
    const val BUTTON = "interaction/button"
    const val COMMAND = "interaction/command"
}

/**
 * 交互事件 interaction/button 实体类
 */
class InteractionButtonEvent(
    id: Number,
    type: String,
    platform: String,
    @JsonProperty("self_id") selfId: String,
    timestamp: Number,
    argv: Interaction.Argv? = null,
    override val button: Interaction.Button,
    channel: Channel? = null,
    guild: Guild? = null,
    login: Login? = null,
    member: GuildMember? = null,
    message: Message? = null,
    operator: User? = null,
    role: GuildRole? = null,
    user: User? = null,
    raw: String
) : Event(
    id,
    type,
    platform,
    selfId,
    timestamp,
    argv,
    button,
    channel,
    guild,
    login,
    member,
    message,
    operator,
    role,
    user,
    raw
) {
    companion object {
        @JvmStatic
        fun parse(event: Event) = InteractionButtonEvent(
            event.id,
            event.type,
            event.platform,
            event.selfId,
            event.timestamp,
            event.argv,
            event.button!!,
            event.channel,
            event.guild,
            event.login,
            event.member,
            event.message,
            event.operator,
            event.role,
            event.user,
            event.raw
        )
    }
}

/**
 * 交互事件 interaction/command 实体类
 */
class InteractionCommandEvent(
    id: Number,
    type: String,
    platform: String,
    @JsonProperty("self_id") selfId: String,
    timestamp: Number,
    argv: Interaction.Argv? = null,
    button: Interaction.Button? = null,
    channel: Channel? = null,
    guild: Guild? = null,
    login: Login? = null,
    member: GuildMember? = null,
    message: Message? = null,
    operator: User? = null,
    role: GuildRole? = null,
    user: User? = null,
    raw: String
) : Event(
    id,
    type,
    platform,
    selfId,
    timestamp,
    argv,
    button,
    channel,
    guild,
    login,
    member,
    message,
    operator,
    role,
    user,
    raw
) {
    companion object {
        @JvmStatic
        fun parse(event: Event) = InteractionCommandEvent(
            event.id,
            event.type,
            event.platform,
            event.selfId,
            event.timestamp,
            event.argv,
            event.button,
            event.channel,
            event.guild,
            event.login,
            event.member,
            event.message,
            event.operator,
            event.role,
            event.user,
            event.raw
        )
    }
}

/**
 * 登录事件列表, 参考 https://satori.chat/zh-CN/resources/login.html#%E4%BA%8B%E4%BB%B6
 */
object LoginEvents {
    const val ADDED = "login-added"
    const val REMOVED = "login-removed"
    const val UPDATED = "login-updated"
}

/**
 * 登录事件实体类
 */
class LoginEvent(
    id: Number,
    type: String,
    platform: String,
    @JsonProperty("self_id") selfId: String,
    timestamp: Number,
    argv: Interaction.Argv? = null,
    button: Interaction.Button? = null,
    channel: Channel? = null,
    guild: Guild? = null,
    override val login: Login,
    member: GuildMember? = null,
    message: Message? = null,
    operator: User? = null,
    role: GuildRole? = null,
    user: User? = null,
    raw: String
) : Event(
    id,
    type,
    platform,
    selfId,
    timestamp,
    argv,
    button,
    channel,
    guild,
    login,
    member,
    message,
    operator,
    role,
    user,
    raw
) {
    companion object {
        @JvmStatic
        fun parse(event: Event) = LoginEvent(
            event.id,
            event.type,
            event.platform,
            event.selfId,
            event.timestamp,
            event.argv,
            event.button,
            event.channel,
            event.guild,
            event.login!!,
            event.member,
            event.message,
            event.operator,
            event.role,
            event.user,
            event.raw
        )
    }
}

/**
 * 消息事件列表, 参考 https://satori.chat/zh-CN/resources/message.html#%E4%BA%8B%E4%BB%B6
 */
object MessageEvents {
    const val CREATED = "message-created"
    const val UPDATED = "message-updated"
    const val DELETED = "message-deleted"
}

/**
 * 消息事件实体类
 */
class MessageEvent(
    id: Number,
    type: String,
    platform: String,
    @JsonProperty("self_id") selfId: String,
    timestamp: Number,
    argv: Interaction.Argv? = null,
    button: Interaction.Button? = null,
    override val channel: Channel,
    guild: Guild? = null,
    login: Login? = null,
    member: GuildMember? = null,
    override val message: Message,
    operator: User? = null,
    role: GuildRole? = null,
    override val user: User,
    raw: String
) : Event(
    id,
    type,
    platform,
    selfId,
    timestamp,
    argv,
    button,
    channel,
    guild,
    login,
    member,
    message,
    operator,
    role,
    user,
    raw
) {
    companion object {
        @JvmStatic
        fun parse(event: Event) = MessageEvent(
            event.id,
            event.type,
            event.platform,
            event.selfId,
            event.timestamp,
            event.argv,
            event.button,
            event.channel!!,
            event.guild,
            event.login,
            event.member,
            event.message!!,
            event.operator,
            event.role,
            event.user!!,
            event.raw
        )
    }
}

/**
 * 表态事件列表, 参考 https://satori.chat/zh-CN/resources/reaction.html#%E4%BA%8B%E4%BB%B6
 */
object ReactionEvents {
    const val ADDED = "reaction-added"
    const val REMOVED = "reaction-removed"
}

/**
 * 表态事件实体类
 */
class ReactionEvent(
    id: Number,
    type: String,
    platform: String,
    @JsonProperty("self_id") selfId: String,
    timestamp: Number,
    argv: Interaction.Argv? = null,
    button: Interaction.Button? = null,
    channel: Channel? = null,
    guild: Guild? = null,
    login: Login? = null,
    member: GuildMember? = null,
    message: Message? = null,
    operator: User? = null,
    role: GuildRole? = null,
    user: User? = null,
    raw: String
) : Event(
    id,
    type,
    platform,
    selfId,
    timestamp,
    argv,
    button,
    channel,
    guild,
    login,
    member,
    message,
    operator,
    role,
    user,
    raw
) {
    companion object {
        @JvmStatic
        fun parse(event: Event) = ReactionEvent(
            event.id,
            event.type,
            event.platform,
            event.selfId,
            event.timestamp,
            event.argv,
            event.button,
            event.channel,
            event.guild,
            event.login,
            event.member,
            event.message,
            event.operator,
            event.role,
            event.user,
            event.raw
        )
    }
}

/**
 * 用户事件列表, 参考 https://satori.chat/zh-CN/resources/user.html#%E4%BA%8B%E4%BB%B6
 */
object UserEvents {
    const val FRIEND_REQUEST = "friend-request"
}

/**
 * 用户事件实体类
 */
class UserEvent(
    id: Number,
    type: String,
    platform: String,
    @JsonProperty("self_id") selfId: String,
    timestamp: Number,
    argv: Interaction.Argv? = null,
    button: Interaction.Button? = null,
    channel: Channel? = null,
    guild: Guild? = null,
    login: Login? = null,
    member: GuildMember? = null,
    message: Message? = null,
    operator: User? = null,
    role: GuildRole? = null,
    override val user: User,
    raw: String
) : Event(
    id,
    type,
    platform,
    selfId,
    timestamp,
    argv,
    button,
    channel,
    guild,
    login,
    member,
    message,
    operator,
    role,
    user,
    raw
) {
    companion object {
        @JvmStatic
        fun parse(event: Event) = UserEvent(
            event.id,
            event.type,
            event.platform,
            event.selfId,
            event.timestamp,
            event.argv,
            event.button,
            event.channel,
            event.guild,
            event.login,
            event.member,
            event.message,
            event.operator,
            event.role,
            event.user!!,
            event.raw
        )
    }
}