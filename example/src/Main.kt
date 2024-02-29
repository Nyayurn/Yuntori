package com.github.nyayurn.qbot

import com.github.nyayurn.yutori.*
import java.security.cert.X509Certificate
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

fun main() {
    GlobalLoggerFactory.factory = MyLoggerFactory(Level.INFO)
    val server = System.getenv("SatoriServer") ?: "Chronocat"
    val client = WebSocketEventService.connect(server) {
        listeners {
            message.created += CommandListener
            message.created += OpenGraphListener
            message.created += AtListener
        }
        properties {
            when (server) {
                "Chronocat" -> token = "Chronocat"
                "Koishi" -> {
                    port = 5140
                    path = "/satori"
                    token = "Koishi"
                }
            }

            System.getenv("SatoriHost")?.let {
                host = it
            }
            System.getenv("SatoriPort")?.toIntOrNull()?.let {
                port = it
            }
            System.getenv("SatoriPath")?.let {
                path = it
            }
            System.getenv("SatoriToken")?.let {
                token = it
            }
        }
    }
    while (readln() != "exit") continue
    client.close()
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

class MyLogger(clazz: Class<*>, private val useLevel: Level) : Logger {
    private val defaultLogger = DefaultLogger(clazz, useLevel)
    override fun log(level: Level, service: String, msg: String) {
        defaultLogger.log(level, service, msg)
        if (level.num < useLevel.num) return
        println()
    }
}

class MyLoggerFactory(private val level: Level = Level.INFO) : LoggerFactory {
    override fun getLogger(clazz: Class<*>) = MyLogger(clazz, level)
}