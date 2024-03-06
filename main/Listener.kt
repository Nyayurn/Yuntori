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

fun interface Listener<T : Event> {
    operator fun invoke(actions: Actions, event: T)
}

class ListenersContainer private constructor() {
    val any = mutableListOf<Listener<Event>>()
    val guild = GuildContainer()
    val interaction = InteractionContainer()
    val bot = BotContainer()
    val message = MessageContainer()
    private val logger = GlobalLoggerFactory.getLogger(this::class.java)

    fun any(listener: Listener<Event>) {
        any += listener
    }

    companion object {
        fun of(apply: ListenersContainer.() -> Unit = {}) = ListenersContainer().apply(apply)
    }

    fun runEvent(event: Event, properties: YunhuProperties, name: String) {
        try {
            val actions = Actions.of(properties, name)
            for (listener in this.any) listener(actions, event)
            when (event) {
                is GuildMemberEvent -> guild.member.runEvent(actions, event, name)
                is InteractionButtonEvent, is InteractionCommandEvent -> interaction.runEvent(actions, event, name)
                is MessageEvent -> message.runEvent(actions, event, name)
            }
        } catch (e: EventParsingException) {
            logger.error(name, "$e, event: $event")
        }
    }
}

class GuildContainer {
    val member = MemberContainer()

    class MemberContainer {
        val added = mutableListOf<Listener<GuildMemberEvent>>()
        val removed = mutableListOf<Listener<GuildMemberEvent>>()
        private val logger = GlobalLoggerFactory.getLogger(this::class.java)

        fun added(listener: Listener<GuildMemberEvent>) {
            added += listener
        }

        fun removed(listener: Listener<GuildMemberEvent>) {
            removed += listener
        }

        fun runEvent(actions: Actions, event: GuildMemberEvent, name: String) = when (event.type) {
            GuildMemberEvents.ADDED -> added.forEach { it(actions, event) }
            GuildMemberEvents.REMOVED -> removed.forEach { it(actions, event) }
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

class MessageContainer {
    val created = mutableListOf<Listener<MessageEvent>>()
    private val logger = GlobalLoggerFactory.getLogger(this::class.java)

    fun created(listener: Listener<MessageEvent>) {
        created += listener
    }

    fun runEvent(actions: Actions, event: MessageEvent, name: String) = when (event.type) {
        MessageEvents.CREATED -> created.forEach { it(actions, event) }
        else -> logger.warn(name, "Unsupported event: $event")
    }
}

class BotContainer {
    val followed = mutableListOf<Listener<Event>>()
    val unfollowed = mutableListOf<Listener<Event>>()
    private val logger = GlobalLoggerFactory.getLogger(this::class.java)

    fun followed(listener: Listener<Event>) {
        followed += listener
    }

    fun unfollowed(listener: Listener<Event>) {
        unfollowed += listener
    }

    fun runEvent(actions: Actions, event: Event, name: String) = when (event.type) {
        InternalEvents.BotEvents.FOLLOWED -> followed.forEach { it(actions, event) }
        InternalEvents.BotEvents.UNFOLLOWED -> unfollowed.forEach { it(actions, event) }
        else -> logger.warn(name, "Unsupported event: $event")
    }
}