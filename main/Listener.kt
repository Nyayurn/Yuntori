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

package com.github.nyayurn.yutori.next

fun interface Listener<T : Event> {
    operator fun invoke(actions: Actions, event: T)
}

class ListenersContainer private constructor() {
    val any = mutableListOf<Listener<Event>>()
    val guild = GuildContainer()
    val interaction = InteractionContainer()
    val login = LoginContainer()
    val message = MessageContainer()
    val reaction = ReactionContainer()
    val friend = FriendContainer()
    private val logger = GlobalLoggerFactory.getLogger(this::class.java)

    fun any(listener: Listener<Event>) {
        any += listener
    }

    companion object {
        fun of(apply: ListenersContainer.() -> Unit = {}) = ListenersContainer().apply(apply)
    }

    fun runEvent(event: Event, properties: SatoriProperties, name: String) {
        try {
            val actions = Actions.of(event, properties, name)
            for (listener in this.any) listener(actions, event)
            when (event) {
                is GuildEvent -> guild.runEvent(actions, event, name)
                is GuildMemberEvent -> guild.member.runEvent(actions, event, name)
                is GuildRoleEvent -> guild.role.runEvent(actions, event, name)
                is InteractionButtonEvent, is InteractionCommandEvent -> interaction.runEvent(actions, event, name)
                is LoginEvent -> login.runEvent(actions, event, name)
                is MessageEvent -> message.runEvent(actions, event, name)
                is ReactionEvent -> reaction.runEvent(actions, event, name)
                is UserEvent -> friend.runEvent(actions, event, name)
            }
        } catch (e: EventParsingException) {
            logger.error(name, "$e, event: $event")
        }
    }

    class GuildContainer {
        val added = mutableListOf<Listener<GuildEvent>>()
        val updated = mutableListOf<Listener<GuildEvent>>()
        val removed = mutableListOf<Listener<GuildEvent>>()
        val request = mutableListOf<Listener<GuildEvent>>()
        val member = MemberContainer()
        val role = RoleContainer()
        private val logger = GlobalLoggerFactory.getLogger(this::class.java)

        fun added(listener: Listener<GuildEvent>) {
            added += listener
        }

        fun updated(listener: Listener<GuildEvent>) {
            updated += listener
        }

        fun removed(listener: Listener<GuildEvent>) {
            removed += listener
        }

        fun request(listener: Listener<GuildEvent>) {
            request += listener
        }

        fun runEvent(actions: Actions, event: GuildEvent, name: String) = when (event.type) {
            GuildEvents.ADDED -> added.forEach { it(actions, event) }
            GuildEvents.UPDATED -> updated.forEach { it(actions, event) }
            GuildEvents.REMOVED -> removed.forEach { it(actions, event) }
            GuildEvents.REQUEST -> request.forEach { it(actions, event) }
            else -> logger.warn(name, "Unsupported event: $event")
        }

        class MemberContainer {
            val added = mutableListOf<Listener<GuildMemberEvent>>()
            val updated = mutableListOf<Listener<GuildMemberEvent>>()
            val removed = mutableListOf<Listener<GuildMemberEvent>>()
            val request = mutableListOf<Listener<GuildMemberEvent>>()
            private val logger = GlobalLoggerFactory.getLogger(this::class.java)

            fun added(listener: Listener<GuildMemberEvent>) {
                added += listener
            }

            fun updated(listener: Listener<GuildMemberEvent>) {
                updated += listener
            }

            fun removed(listener: Listener<GuildMemberEvent>) {
                removed += listener
            }

            fun request(listener: Listener<GuildMemberEvent>) {
                request += listener
            }

            fun runEvent(actions: Actions, event: GuildMemberEvent, name: String) = when (event.type) {
                GuildMemberEvents.ADDED -> added.forEach { it(actions, event) }
                GuildMemberEvents.UPDATED -> updated.forEach { it(actions, event) }
                GuildMemberEvents.REMOVED -> removed.forEach { it(actions, event) }
                GuildMemberEvents.REQUEST -> request.forEach { it(actions, event) }
                else -> logger.warn(name, "Unsupported event: $event")
            }
        }

        class RoleContainer {
            val created = mutableListOf<Listener<GuildRoleEvent>>()
            val updated = mutableListOf<Listener<GuildRoleEvent>>()
            val deleted = mutableListOf<Listener<GuildRoleEvent>>()
            private val logger = GlobalLoggerFactory.getLogger(this::class.java)

            fun created(listener: Listener<GuildRoleEvent>) {
                created += listener
            }

            fun updated(listener: Listener<GuildRoleEvent>) {
                updated += listener
            }

            fun deleted(listener: Listener<GuildRoleEvent>) {
                deleted += listener
            }

            fun runEvent(actions: Actions, event: GuildRoleEvent, name: String) = when (event.type) {
                GuildRoleEvents.CREATED -> created.forEach { it(actions, event) }
                GuildRoleEvents.UPDATED -> updated.forEach { it(actions, event) }
                GuildRoleEvents.DELETED -> deleted.forEach { it(actions, event) }
                else -> logger.warn(name, "Unsupported event: $event")
            }
        }
    }

    class InteractionContainer {
        val button = mutableListOf<Listener<InteractionButtonEvent>>()
        val command = mutableListOf<Listener<InteractionCommandEvent>>()
        private val logger = GlobalLoggerFactory.getLogger(this::class.java)

        fun button(listener: Listener<InteractionButtonEvent>) {
            button += listener
        }

        fun command(listener: Listener<InteractionCommandEvent>) {
            command += listener
        }

        fun runEvent(actions: Actions, event: Event, name: String) = when (event) {
            is InteractionButtonEvent -> button.forEach { it(actions, event) }
            is InteractionCommandEvent -> command.forEach { it(actions, event) }
            else -> logger.warn(name, "Unsupported event: $event")
        }
    }

    class LoginContainer {
        val added = mutableListOf<Listener<LoginEvent>>()
        val removed = mutableListOf<Listener<LoginEvent>>()
        val updated = mutableListOf<Listener<LoginEvent>>()
        private val logger = GlobalLoggerFactory.getLogger(this::class.java)

        fun added(listener: Listener<LoginEvent>) {
            added += listener
        }

        fun removed(listener: Listener<LoginEvent>) {
            removed += listener
        }

        fun updated(listener: Listener<LoginEvent>) {
            updated += listener
        }

        fun runEvent(actions: Actions, event: LoginEvent, name: String) = when (event.type) {
            LoginEvents.ADDED -> added.forEach { it(actions, event) }
            LoginEvents.REMOVED -> removed.forEach { it(actions, event) }
            LoginEvents.UPDATED -> updated.forEach { it(actions, event) }
            else -> logger.warn(name, "Unsupported event: $event")
        }
    }

    class MessageContainer {
        val created = mutableListOf<Listener<MessageEvent>>()
        val updated = mutableListOf<Listener<MessageEvent>>()
        val deleted = mutableListOf<Listener<MessageEvent>>()
        private val logger = GlobalLoggerFactory.getLogger(this::class.java)

        fun created(listener: Listener<MessageEvent>) {
            created += listener
        }

        fun updated(listener: Listener<MessageEvent>) {
            updated += listener
        }

        fun deleted(listener: Listener<MessageEvent>) {
            deleted += listener
        }

        fun runEvent(actions: Actions, event: MessageEvent, name: String) = when (event.type) {
            MessageEvents.CREATED -> created.forEach { it(actions, event) }
            MessageEvents.UPDATED -> updated.forEach { it(actions, event) }
            MessageEvents.DELETED -> deleted.forEach { it(actions, event) }
            else -> logger.warn(name, "Unsupported event: $event")
        }
    }

    class ReactionContainer {
        val added = mutableListOf<Listener<ReactionEvent>>()
        val removed = mutableListOf<Listener<ReactionEvent>>()
        private val logger = GlobalLoggerFactory.getLogger(this::class.java)

        fun added(listener: Listener<ReactionEvent>) {
            added += listener
        }

        fun removed(listener: Listener<ReactionEvent>) {
            removed += listener
        }

        fun runEvent(actions: Actions, event: ReactionEvent, name: String) = when (event.type) {
            ReactionEvents.ADDED -> added.forEach { it(actions, event) }
            ReactionEvents.REMOVED -> removed.forEach { it(actions, event) }
            else -> logger.warn(name, "Unsupported event: $event")
        }
    }

    class FriendContainer {
        val request = mutableListOf<Listener<UserEvent>>()
        private val logger = GlobalLoggerFactory.getLogger(this::class.java)

        fun request(listener: Listener<UserEvent>) {
            request += listener
        }

        fun runEvent(actions: Actions, event: UserEvent, name: String) = when (event.type) {
            UserEvents.FRIEND_REQUEST -> request.forEach { it(actions, event) }
            else -> logger.warn(name, "Unsupported event: $event")
        }
    }
}