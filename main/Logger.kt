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

import com.github.nyayurn.yutori.next.Level.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class Level(val num: Int) { ERROR(5), WARN(4), INFO(3), DEBUG(2), TRACE(1) }

interface Logger {
    fun log(level: Level, service: String, msg: String)
    fun error(name: String, msg: String) = log(ERROR, name, msg)
    fun warn(name: String, msg: String) = log(WARN, name, msg)
    fun info(name: String, msg: String) = log(INFO, name, msg)
    fun debug(name: String, msg: String) = log(DEBUG, name, msg)
    @Suppress("unused")
    fun trace(name: String, msg: String) = log(TRACE, name, msg)
}

fun interface LoggerFactory {
    fun getLogger(clazz: Class<*>): Logger
}

class DefaultLogger(private val clazz: Class<*>, private val useLevel: Level) : Logger {
    private infix fun String.deco(context: String) = "\u001b[${this}m$context\u001b[0m"

    override fun log(level: Level, service: String, msg: String) {
        if (level.num < useLevel.num) return
        val time = "38;5;8" deco "[${LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd HH:mm:ss"))}]"
        val className = "38;5;2" deco "[${clazz.simpleName}]"
        val levelAndMsg = when (level) {
            ERROR -> "38;5;9"
            WARN -> "38;5;11"
            INFO -> "0"
            DEBUG -> "38;5;8"
            TRACE -> "38;5;7"
        } deco "[${level.name}]: $msg"
        println("[$service]$time$className$levelAndMsg")
    }
}

class DefaultLoggerFactory(private val level: Level = INFO) : LoggerFactory {
    override fun getLogger(clazz: Class<*>): Logger = DefaultLogger(clazz, level)
}

object GlobalLoggerFactory : LoggerFactory {
    @Suppress("MemberVisibilityCanBePrivate")
    var factory: LoggerFactory = DefaultLoggerFactory()
    override fun getLogger(clazz: Class<*>) = factory.getLogger(clazz)
}