package com.github.nyayurn.qbot

import com.github.nyayurn.yuntori.Actions
import com.github.nyayurn.yuntori.GlobalLoggerFactory
import com.github.nyayurn.yuntori.Listener
import com.github.nyayurn.yuntori.MessageEvent
import com.github.nyayurn.yuntori.message.elements.Text
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
        val content = event.message.content.toString()
        if (content.startsWith("/") or content.startsWith("!")) {
            val msg = content.substring(1)
            if (msg.isNotEmpty()) {
                commands.find { it.test(actions, event, msg) }?.let {
                    logger.info(
                        actions.name,
                        "User ${event.user.id} 触发命令: ${it::class.simpleName}(${event.message.content})"
                    )
                    it.command(actions, event, msg)
                } ?: actions.message.create(event.channel.id) {
                    message {
                        markdown = true
                        quote { elements += event.message.content }
                        text { "未知命令: ${msg.split(" ")[0]}" }
                    }
                }
            }
        }
    }
}

object OpenGraphListener : Listener<MessageEvent> {
    private val pattern = Pattern.compile("(http(s)?://[\\w./-]+)")
    private val logger = GlobalLoggerFactory.getLogger(this::class.java)
    override fun invoke(actions: Actions, event: MessageEvent) {
        val msg = event.message.content.filterIsInstance<Text>().joinToString { it.toString() }
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
                        message {
                            markdown = true
                            quote { elements += event.message.content }
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
                }
            } catch (e: IOException) {
                if (request.status.value != 200) {
                    actions.message.create(event.channel.id) {
                        message {
                            markdown = true
                            quote { elements += event.message.content }
                            text { "OpenGraph: 获取失败: ${request.status}" }
                        }
                    }
                }
                e.printStackTrace()
            }
        }
    }
}