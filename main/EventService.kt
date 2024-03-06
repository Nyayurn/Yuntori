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

@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.github.nyayurn.yuntori

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Satori 事件服务的 WebHook 实现
 * @param container 监听器容器
 * @param properties Satori WebHook 配置
 * @param name 用于区分不同 Satori 事件服务的名称
 */
class WebHookEventService(
    val container: ListenersContainer,
    val properties: WebHookProperties,
    val name: String = "Satori"
) : AutoCloseable {
    private var client: ApplicationEngine? = null
    private val logger = GlobalLoggerFactory.getLogger(this::class.java)

    @OptIn(DelicateCoroutinesApi::class)
    fun connect(): WebHookEventService {
        GlobalScope.launch {
            client = embeddedServer(CIO, properties.serverPort, properties.serverHost) {
                routing {
                    post("/") {
                        val authorization = call.request.headers["Authorization"]
                        if (authorization != properties.server.token) {
                            call.response.status(HttpStatusCode.Unauthorized)
                            return@post
                        }
                        val body = call.receiveText()
                        try {
                            val mapper = jacksonObjectMapper().configure(
                                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false
                            )
                            val event = mapper.readValue<Event>(body)
                            launch {
                                when (event) {
                                    is MessageEvent -> logger.info(name, buildString {
                                        append("${event.platform}(${event.selfId}) 接收事件(${event.type}): ")
                                        append("\u001B[38;5;4m").append("${event.channel.name}(${event.channel.id})")
                                        append("\u001B[38;5;6m")
                                        append(event.member?.nick ?: event.user.nick ?: event.user.name)
                                        append("(${event.user.id})")
                                        append("\u001B[0m").append(": ").append(event.message.content)
                                    })

                                    else -> logger.info(
                                        name, "${event.platform}(${event.selfId}) 接收事件: ${event.type}"
                                    )
                                }
                                logger.debug(name, "事件详细信息: $body")
                                container.runEvent(event, properties.server, name)
                            }
                            call.response.status(HttpStatusCode.OK)
                        } catch (e: Exception) {
                            logger.warn(name, "处理事件时出错(${body}): ${e.localizedMessage}")
                            e.printStackTrace()
                            call.response.status(HttpStatusCode.InternalServerError)
                        }
                    }
                }
            }.start()
            logger.info(name, "成功启动 HTTP 服务器")
        }
        return this
    }

    override fun close() {
        client?.stop()
    }

    companion object {
        fun of(
            container: ListenersContainer,
            properties: WebHookProperties = WebHookProperties(server = YunhuProperties()),
            name: String = "Satori",
        ) = WebHookEventService(container, properties, name)

        fun of(name: String = "Satori", dsl: Builder.() -> Unit) = Builder(name).apply(dsl).build()

        fun connect(
            container: ListenersContainer,
            properties: WebHookProperties = WebHookProperties(server = YunhuProperties()),
            name: String = "Satori",
        ) = WebHookEventService(container, properties, name).connect()

        fun connect(name: String = "Satori", dsl: Builder.() -> Unit) = Builder(name).apply(dsl).build().connect()
    }

    class Builder(var name: String) {
        var container: ListenersContainer = ListenersContainer.of()
        var properties: WebHookProperties = WebHookProperties(server = YunhuProperties())

        fun listeners(lambda: ListenersContainer.() -> Unit) {
            container = ListenersContainer.of(lambda)
        }

        fun properties(lambda: PropertiesBuilder.() -> Unit) {
            properties = PropertiesBuilder().apply(lambda).build()
        }

        fun properties(lambda: () -> WebHookProperties) {
            this.properties = lambda()
        }

        fun build() = WebHookEventService(container, properties, name)

        class PropertiesBuilder {
            var server: Server = Server()
            var host: String = "127.0.0.1"
            var port: Int = 5500
            var path: String = ""
            var token: String? = null
            var version: String = "v1"
            fun build() =
                WebHookProperties(server.host, server.port, YunhuProperties(host, port, path, token, version))

            class Server {
                var host: String = "0.0.0.0"
                var port: Int = 8080
            }
        }
    }
}