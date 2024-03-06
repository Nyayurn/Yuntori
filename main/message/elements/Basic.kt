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

@file:Suppress("MemberVisibilityCanBePrivate", "ConvertSecondaryConstructorToPrimary")

package com.github.nyayurn.yuntori.message.elements

/**
 * 消息元素
 */
fun interface MessageElement {
    override fun toString(): String
}

/**
 * 节点消息元素
 * @property nodeName 节点名称
 * @property properties 属性
 * @property children 子元素
 */
abstract class NodeMessageElement(val nodeName: String, vararg pairs: Pair<String, Any?>) : MessageElement {
    val properties: MutableMap<String, Any?> = mutableMapOf(*pairs)
    val children: MutableList<MessageElement> = mutableListOf()

    /**
     * 获取属性
     * @param key 属性名
     * @return 属性值
     */
    operator fun get(key: String) = properties[key]

    /**
     * 获取子元素
     * @param index 子元素索引
     * @return 消息元素
     */
    operator fun get(index: Int) = children[index]

    /**
     * 设置属性
     * @param key 属性名
     * @param value 属性值
     */
    operator fun set(key: String, value: Any) {
        properties[key] = value
    }

    /**
     * 设置子元素
     * @param index 索引
     * @param value 消息元素
     */
    operator fun set(index: Int, value: MessageElement) {
        children[index] = value
    }

    /**
     * 设置子元素
     * @param index 索引
     * @param text 文本
     */
    operator fun set(index: Int, text: String) {
        children[index] = Text(text)
    }

    /**
     * 添加子元素
     * @param element 消息元素
     */
    operator fun plusAssign(element: MessageElement) {
        children.add(element)
    }

    /**
     * 添加子元素
     * @param text 消息元素
     */
    operator fun plusAssign(text: String) {
        children.add(Text(text))
    }

    /**
     * 添加子元素
     * @param element 消息元素
     */
    fun add(element: MessageElement): NodeMessageElement {
        children.add(element)
        return this
    }

    /**
     * 添加子元素
     * @param text 消息元素
     */
    fun add(text: String): NodeMessageElement {
        children.add(Text(text))
        return this
    }

    override fun toString() = buildString {
        append("<$nodeName")
        for (item in properties) {
            val key = item.key
            val value = item.value ?: continue
            append(" ")
            append(
                when (value) {
                    is String -> "$key=\"$value\""
                    is Number -> "$key=$value"
                    is Boolean -> if (value) key else ""
                    else -> throw Exception("Invalid type")
                }
            )
        }
        if (children.isEmpty()) {
            append("/>")
        } else {
            append(">")
            for (item in children) append(item)
            append("</$nodeName>")
        }
    }
}

/**
 * 纯文本
 * @property text 文本
 */
class Text(var text: String) : MessageElement {
    override fun toString() = text
}

/**
 * 链接
 * @property href 链接的 URL
 */
class Href(href: String) : NodeMessageElement("a", "href" to href) {
    var href: String by super.properties
}