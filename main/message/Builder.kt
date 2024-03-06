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

package com.github.nyayurn.yuntori.message

import com.github.nyayurn.yuntori.message.elements.*

/**
 * 消息 DSL 构造器
 * @param dsl DSL
 * @return 消息
 */
inline fun message(dsl: MessageDslBuilder.() -> Unit) = MessageDslBuilder().apply(dsl).toString()

@DslMarker
annotation class MessageDSL

interface ElementBuilder {
    fun buildElement(): MessageElement
}

@MessageDSL
open class MessageDslBuilder {
    val elements = mutableListOf<MessageElement>()

    fun text(element: Text) = element.apply { elements += this }
    inline fun text(block: () -> String) = Text(block()).apply { elements += this }

    fun a(element: Href) = element.apply { elements += this }
    inline fun a(block: HrefBuilder.() -> Unit) = HrefBuilder().apply(block).buildElement().apply { elements += this }

    fun img(element: Image) = element.apply { elements += this }
    inline fun img(block: ImageBuilder.() -> Unit) = ImageBuilder().apply(block).buildElement().apply { elements += this }

    fun file(element: File) = element.apply { elements += this }
    inline fun file(block: FileBuilder.() -> Unit) = FileBuilder().apply(block).buildElement().apply { elements += this }

    fun b(element: Bold) = element.apply { elements += this }
    inline fun b(block: BoldBuilder.() -> Unit) = BoldBuilder().apply(block).buildElement().apply { elements += this }

    fun strong(element: Strong) = element.apply { elements += this }
    inline fun strong(block: BoldBuilder.() -> Unit) = BoldBuilder().apply(block).buildElement().apply { elements += this }

    fun i(element: Idiomatic) = element.apply { elements += this }
    inline fun i(block: IdiomaticBuilder.() -> Unit) = IdiomaticBuilder().apply(block).buildElement().apply { elements += this }

    fun em(element: Em) = element.apply { elements += this }
    inline fun em(block: IdiomaticBuilder.() -> Unit) = IdiomaticBuilder().apply(block).buildElement().apply { elements += this }

    fun u(element: Underline) = element.apply { elements += this }
    inline fun u(block: UnderlineBuilder.() -> Unit) = UnderlineBuilder().apply(block).buildElement().apply { elements += this }

    fun ins(element: Ins) = element.apply { elements += this }
    inline fun ins(block: UnderlineBuilder.() -> Unit) = UnderlineBuilder().apply(block).buildElement().apply { elements += this }

    fun s(element: Strikethrough) = element.apply { elements += this }
    inline fun s(block: DeleteBuilder.() -> Unit) = DeleteBuilder().apply(block).buildElement().apply { elements += this }

    fun del(element: Delete) = element.apply { elements += this }
    inline fun del(block: DeleteBuilder.() -> Unit) = DeleteBuilder().apply(block).buildElement().apply { elements += this }

    fun code(element: Code) = element.apply { elements += this }
    inline fun code(block: CodeBuilder.() -> Unit) = CodeBuilder().apply(block).buildElement().apply { elements += this }

    fun br(element: Br) = element.apply { elements += this }
    inline fun br(block: Br.() -> Unit) = Br().apply(block).apply { elements += this }

    fun p(element: Paragraph) = element.apply { elements += this }
    inline fun p(block: ParagraphBuilder.() -> Unit) = ParagraphBuilder().apply(block).buildElement().apply { elements += this }

    fun message(element: Message) = element.apply { elements += this }
    inline fun message(block: MessageBuilder.() -> Unit) = MessageBuilder().apply(block).buildElement().apply { elements += this }

    fun quote(element: Quote) = element.apply { elements += this }
    inline fun quote(block: QuoteBuilder.() -> Unit) = QuoteBuilder().apply(block).buildElement().apply { elements += this }

    fun button(element: Button) = element.apply { elements += this }
    inline fun button(block: ButtonBuilder.() -> Unit) = ButtonBuilder().apply(block).buildElement().apply { elements += this }

    open fun build() = MessageSegment(elements)
    override fun toString() = elements.joinToString("") { it.toString() }

    @MessageDSL
    class HrefBuilder : MessageDslBuilder(), ElementBuilder {
        var href: String = ""
        override fun buildElement() = Href(href).apply { children.addAll(elements) }
    }

    @MessageDSL
    class ImageBuilder : MessageDslBuilder(), ElementBuilder {
        var src: String = ""
        var title: String? = null
        override fun buildElement() = Image(src, title).apply { children.addAll(elements) }
    }

    @MessageDSL
    class FileBuilder : MessageDslBuilder(), ElementBuilder {
        var src: String = ""
        var title: String? = null
        override fun buildElement() = File(src, title).apply { children.addAll(elements) }
    }

    @MessageDSL
    class BoldBuilder : MessageDslBuilder(), ElementBuilder {
        override fun buildElement() = Bold().apply { children.addAll(elements) }
    }

    @MessageDSL
    class IdiomaticBuilder : MessageDslBuilder(), ElementBuilder {
        override fun buildElement() = Idiomatic().apply { children.addAll(elements) }
    }

    @MessageDSL
    class UnderlineBuilder : MessageDslBuilder(), ElementBuilder {
        override fun buildElement() = Underline().apply { children.addAll(elements) }
    }

    @MessageDSL
    class DeleteBuilder : MessageDslBuilder(), ElementBuilder {
        override fun buildElement() = Strikethrough().apply { children.addAll(elements) }
    }

    @MessageDSL
    class CodeBuilder : MessageDslBuilder(), ElementBuilder {
        var lang: String = ""
        override fun buildElement() = Code(lang).apply { children.addAll(elements) }
    }

    @MessageDSL
    class ParagraphBuilder : MessageDslBuilder(), ElementBuilder {
        override fun buildElement() = Paragraph().apply { children.addAll(elements) }
    }

    @MessageDSL
    class MessageBuilder : MessageDslBuilder(), ElementBuilder {
        var markdown: Boolean = false
        override fun buildElement() = Message(markdown).apply { children.addAll(elements) }
    }

    @MessageDSL
    class QuoteBuilder : MessageDslBuilder(), ElementBuilder {
        override fun buildElement() = Quote().apply { children.addAll(elements) }
    }

    @MessageDSL
    class ButtonBuilder : MessageDslBuilder(), ElementBuilder {
        var id: String? = null
        var type: String? = null
        var href: String? = null
        var text: String? = null
        override fun buildElement() = Button(id, type, href, text).apply { children.addAll(elements) }
    }
}