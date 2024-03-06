/*
Copyright (c) 2024 Yurn
Yutori is licensed under Mulan PSL v2.
You can use this software according to the terms and conditions of the Mulan PSL v2.
You may obtain a copy of Mulan PSL v2 at:
         http://license.coscl.org.cn/MulanPSL2
THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
See the Mulan PSL v2 for more details.
 */

package com.github.nyayurn.yuntori.message.elements

/**
 * 换行
 */
class Br : NodeMessageElement("br")

/**
 * 段落
 */
class Paragraph : NodeMessageElement("p")

/**
 * 消息
 * @property markdown 是否使用 Markdown
 */
class Message(markdown: Boolean = false) : NodeMessageElement("message", "markdown" to markdown) {
    var markdown: Boolean by super.properties
}