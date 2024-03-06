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

@file:Suppress("unused")

package com.github.nyayurn.yuntori

/**
 * JsonObject 字符串 DSL 构建器
 */
inline fun jsonObj(dsl: JsonObjectDSLBuilder.() -> Unit) = JsonObjectDSLBuilder().apply(dsl).toString()

/**
 * JsonArray 字符串 DSL 构建器
 */
inline fun jsonArr(dsl: JsonArrayDSLBuilder.() -> Unit) = JsonArrayDSLBuilder().apply(dsl).toString()

class JsonObjectDSLBuilder {
    private val map = mutableMapOf<String, Any>()
    fun put(key: String, value: Any?) {
        value?.let { map[key] = it }
    }

    fun put(key: String, dsl: () -> Any?) {
        dsl()?.let { map[key] = it }
    }

    fun putJsonObj(key: String, dsl: JsonObjectDSLBuilder.() -> Unit) {
        map[key] = JsonObjectDSLBuilder().apply(dsl)
    }

    fun putJsonArr(key: String, dsl: JsonArrayDSLBuilder.() -> Unit) {
        map[key] = JsonArrayDSLBuilder().apply(dsl)
    }

    override fun toString() = map.entries.joinToString(",", "{", "}") { (key, value) ->
        buildString {
            append("\"$key\":")
            append(
                when (value) {
                    is String -> "\"$value\""
                    else -> value.toString()
                }
            )
        }
    }
}

class JsonArrayDSLBuilder {
    private val list = mutableListOf<Any>()
    fun add(value: Any?) {
        value?.let { list += it }
    }

    fun add(dsl: () -> Any?) {
        dsl()?.let { list += it }
    }

    fun addJsonArr(dsl: JsonArrayDSLBuilder.() -> Unit) {
        list += JsonArrayDSLBuilder().apply(dsl)
    }

    fun addJsonObj(dsl: JsonObjectDSLBuilder.() -> Unit) {
        list += JsonObjectDSLBuilder().apply(dsl)
    }

    override fun toString() = list.joinToString(",", "[", "]") { value ->
        buildString {
            append(
                when (value) {
                    is String -> "\"$value\""
                    else -> value.toString()
                }
            )
        }
    }
}