package com.github.nyayurn.qbot

import com.github.nyayurn.yutori.*
import com.github.nyayurn.yutori.message.element.At
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import java.io.IOException
import java.util.regex.Pattern

object CommandListener : Listener<MessageEvent> {
    private val commands: Array<Command> = arrayOf(AiCommand, HelpCommand, EchoCommand)
    private val logger = GlobalLoggerFactory.getLogger(this::class.java)
    override fun invoke(actions: Actions, event: MessageEvent) {
        if (qqHelperFilter(event)) return
        var msg = event.message.content
        if (msg.startsWith("/") or msg.startsWith("!")) {
            msg = msg.substring(1)
            if (msg.isNotEmpty()) {
                commands.find { it.test(actions, event, msg) }?.let {
                    logger.info(
                        actions.name,
                        "User ${event.user.id} 触发命令: ${it::class.simpleName}(${event.message.content})"
                    )
                    it.command(actions, event, msg)
                } ?: actions.message.create(event.channel.id) {
                    quote { this["id"] = event.message.id }
                    text { "未知命令: ${msg.split(" ")[0]}" }
                }
            }
        }
    }
}

object OpenGraphListener : Listener<MessageEvent> {
    private val pattern = Pattern.compile("(http(s)?://[\\w./-]+)")
    private val logger = GlobalLoggerFactory.getLogger(this::class.java)
    override fun invoke(actions: Actions, event: MessageEvent) {
        if (qqHelperFilter(event)) return
        val msg = MessageUtil.extractTextChain(event.message.content).joinToString { it.toString() }
        val matcher = pattern.matcher(msg)
        if (matcher.find()) runBlocking {
            val request = HttpClient(CIO) {
                engine { https { trustManager = trustAllCerts } }
            }.use { it.get(matcher.group(0)) }
            try {
                val head = Jsoup.parse(request.body<String>()).head()
                val title = head.getElementsByAttributeValue("property", "og:title").first()?.attr("content")
                val desc = head.getElementsByAttributeValue("property", "og:description").first()?.attr("content")
                val img = head.getElementsByAttributeValue("property", "og:image").first()?.attr("content")
                val url = head.getElementsByAttributeValue("property", "og:url").first()?.attr("content")
                if (title != null || desc != null || img != null || url != null) {
                    logger.info(actions.name, "User ${event.user.id} 触发监听器: $OpenGraphListener")
                    actions.message.create(event.channel.id) {
                        quote { this["id"] = event.message.id }
                        title?.let {
                            text { title }
                        }
                        desc?.let {
                            if (title != null) {
                                text { "\n" }
                            }
                            text { desc }
                        }
                        img?.let {
                            img { src = img }
                        }
                        url?.let {
                            a {
                                href = url
                            }
                        }
                    }
                }
            } catch (e: IOException) {
                if (request.status.value != 200) {
                    actions.message.create(event.channel.id) {
                        quote { this["id"] = event.message.id }
                        text { "OpenGraph: 获取失败: ${request.status}" }
                    }
                }
                e.printStackTrace()
            }
        }
    }
}

object AtListener : Listener<MessageEvent> {
    override fun invoke(actions: Actions, event: MessageEvent) {
        if (qqHelperFilter(event)) return
        val msg = MessageUtil.extractTextChain(event.message.content).joinToString { it.toString() }
        val atBot =
            MessageUtil.parseElementChain(event.message.content).getOrNull(0).let { it is At && it.id == event.selfId }
        if (atBot && msg.isNotEmpty()) {
            GlobalLoggerFactory.getLogger(this::class.java)
                .info(actions.name, "User ${event.user.id} 触发命令: $AiCommand")
            AiCommand.run(actions, event, msg)
        }
    }
}