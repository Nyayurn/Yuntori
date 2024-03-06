package com.github.nyayurn.qbot

import com.github.nyayurn.yuntori.Actions
import com.github.nyayurn.yuntori.ListenersContainer
import com.github.nyayurn.yuntori.WebHookEventService
import com.github.nyayurn.yuntori.YunhuProperties
import java.security.cert.X509Certificate
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

fun main() {
    val client = WebHookEventService.connect("云湖") {
        listeners {
            message.created += CommandListener
            message.created += OpenGraphListener
        }
    }
    while (readln() != "exit") continue
    client.close()
}

fun action() {
    val actions = Actions.of(YunhuProperties(),"云湖")
    // 发送消息
    actions.apply {
        // 字符串, 私聊
        message.create("user:1", "你好")
        // DSL, 群聊
        message.create("group:1") {
            text { "你好" }
        }
    }
    // 批量发送消息
    actions.apply {
        message.batch(listOf("a")) {}
    }
    // 编辑消息
    actions.apply {
        message.update("user:1", "消息id", "updated")
        message.update("group:1", "消息id") {
            text { "updated" }
        }
    }
    // 看板
    actions.apply {
        board.set {}
        board.unset()
    }
}

fun event() {
    ListenersContainer.of {
        // 任意事件
        any { actions, event -> println("actions: $actions, event: $event") }
        // 普通消息事件(message.receive.normal)
        message.created { _, _ -> }
        // 指令消息事件(message.receive.instruction)
        interaction.command { _, _ -> }
        // 关注机器人事件(bot.followed) Coming soon
        bot.followed { _, _ -> }
        // 取消关注机器人事件(bot.unfollowed) Coming soon
        bot.unfollowed { _, _ -> }
        // 加入群事件(group.join)
        guild.member.added { _, _ -> }
        // 退出群事件(group.leave)
        guild.member.removed { _, _ -> }
        // 按钮事件(button.report.inline)
        interaction.button { _, _ -> }
    }
}

val trustAllCerts: TrustManager = object : X509TrustManager {
    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        // This method is empty
    }

    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
        // This method is empty
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
}