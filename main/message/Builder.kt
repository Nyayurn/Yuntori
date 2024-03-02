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

package com.github.nyayurn.yutori.next.message

import com.github.nyayurn.yutori.next.message.elements.*

/**
 * 消息 DSL 构造器
 * @param dsl DSL
 * @return 消息
 */
inline fun message(dsl: MessageDslBuilder.() -> Unit) = MessageDslBuilder().apply(dsl).toString()

@DslMarker
annotation class MessageDSL

interface PropertiedBuilder {
    val properties: MutableMap<String, Any?>
    operator fun get(key: String) = properties[key]
    operator fun set(key: String, value: Any?) {
        properties[key] = value
    }

    fun build(): MessageElement
}

@MessageDSL
open class MessageDslBuilder {
    val list = mutableListOf<MessageElement>()

    fun text(element: Text) = element.apply { list += this }
    inline fun text(block: () -> String) = Text(block()).apply { list += this }

    fun custom(element: Custom) = element.apply { list += this }
    inline fun custom(block: () -> String) = Custom(block()).apply { list += this }

    fun at(element: At) = element.apply { list += this }
    inline fun at(block: AtBuilder.() -> Unit) = AtBuilder().apply(block).build().apply { list += this }

    fun sharp(element: Sharp) = element.apply { list += this }
    inline fun sharp(block: SharpBuilder.() -> Unit) = SharpBuilder().apply(block).build().apply { list += this }

    fun a(element: Href) = element.apply { list += this }
    inline fun a(block: HrefBuilder.() -> Unit) = HrefBuilder().apply(block).build().apply { list += this }

    fun img(element: Image) = element.apply { list += this }
    inline fun img(block: ImageBuilder.() -> Unit) = ImageBuilder().apply(block).build().apply { list += this }

    fun audio(element: Audio) = element.apply { list += this }
    inline fun audio(block: AudioBuilder.() -> Unit) = AudioBuilder().apply(block).build().apply { list += this }

    fun video(element: Video) = element.apply { list += this }
    inline fun video(block: VideoBuilder.() -> Unit) = VideoBuilder().apply(block).build().apply { list += this }

    fun file(element: File) = element.apply { list += this }
    inline fun file(block: FileBuilder.() -> Unit) = FileBuilder().apply(block).build().apply { list += this }

    fun b(element: Bold) = element.apply { list += this }
    inline fun b(block: BoldBuilder.() -> Unit) = BoldBuilder().apply(block).build().apply { list += this }

    fun strong(element: Strong) = element.apply { list += this }
    inline fun strong(block: BoldBuilder.() -> Unit) = BoldBuilder().apply(block).build().apply { list += this }

    fun i(element: Idiomatic) = element.apply { list += this }
    inline fun i(block: IdiomaticBuilder.() -> Unit) = IdiomaticBuilder().apply(block).build().apply { list += this }

    fun em(element: Em) = element.apply { list += this }
    inline fun em(block: IdiomaticBuilder.() -> Unit) = IdiomaticBuilder().apply(block).build().apply { list += this }

    fun u(element: Underline) = element.apply { list += this }
    inline fun u(block: UnderlineBuilder.() -> Unit) = UnderlineBuilder().apply(block).build().apply { list += this }

    fun ins(element: Ins) = element.apply { list += this }
    inline fun ins(block: UnderlineBuilder.() -> Unit) = UnderlineBuilder().apply(block).build().apply { list += this }

    fun s(element: Strikethrough) = element.apply { list += this }
    inline fun s(block: DeleteBuilder.() -> Unit) = DeleteBuilder().apply(block).build().apply { list += this }

    fun del(element: Delete) = element.apply { list += this }
    inline fun del(block: DeleteBuilder.() -> Unit) = DeleteBuilder().apply(block).build().apply { list += this }

    fun spl(element: Spl) = element.apply { list += this }
    inline fun spl(block: SplBuilder.() -> Unit) = SplBuilder().apply(block).build().apply { list += this }

    fun code(element: Code) = element.apply { list += this }
    inline fun code(block: CodeBuilder.() -> Unit) = CodeBuilder().apply(block).build().apply { list += this }

    fun sup(element: Sup) = element.apply { list += this }
    inline fun sup(block: SupBuilder.() -> Unit) = SupBuilder().apply(block).build().apply { list += this }

    fun sub(element: Sub) = element.apply { list += this }
    inline fun sub(block: SubBuilder.() -> Unit) = SubBuilder().apply(block).build().apply { list += this }

    fun br(element: Br) = element.apply { list += this }
    inline fun br(block: BrBuilder.() -> Unit) = BrBuilder().apply(block).build().apply { list += this }

    fun p(element: Paragraph) = element.apply { list += this }
    inline fun p(block: ParagraphBuilder.() -> Unit) = ParagraphBuilder().apply(block).build().apply { list += this }

    fun message(element: Message) = element.apply { list += this }
    inline fun message(block: MessageBuilder.() -> Unit) = MessageBuilder().apply(block).build().apply { list += this }

    fun quote(element: Quote) = element.apply { list += this }
    inline fun quote(block: QuoteBuilder.() -> Unit) = QuoteBuilder().apply(block).build().apply { list += this }

    fun author(element: Author) = element.apply { list += this }
    inline fun author(block: AuthorBuilder.() -> Unit) = AuthorBuilder().apply(block).build().apply { list += this }

    fun button(element: Button) = element.apply { list += this }
    inline fun button(block: ButtonBuilder.() -> Unit) = ButtonBuilder().apply(block).build().apply { list += this }

    override fun toString() = list.joinToString("") { it.toString() }

    @MessageDSL
    class AtBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties =
            mutableMapOf<String, Any?>("id" to null, "name" to null, "role" to null, "type" to null)
        var id: String? by properties
        var name: String? by properties
        var role: String? by properties
        var type: String? by properties
        override fun build() = At(id, name, role, type).apply {
            properties.putAll(this@AtBuilder.properties)
            children.addAll(list)
        }
    }

    @MessageDSL
    class SharpBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>("id" to "", "name" to null)
        var id: String by properties
        var name: String? by properties
        override fun build() = Sharp(id, name).apply {
            properties.putAll(this@SharpBuilder.properties)
            children.addAll(list)
        }
    }

    @MessageDSL
    class HrefBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>("href" to "")
        var href: String by properties
        override fun build() = Href(href).apply {
            properties.putAll(this@HrefBuilder.properties)
            children.addAll(list)
        }
    }

    @MessageDSL
    class ImageBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>(
            "src" to "", "cache" to null, "timeout" to null, "width" to null, "height" to null
        )
        var src: String by properties
        var title: String? by properties
        var cache: Boolean? by properties
        var timeout: String? by properties
        var width: Number? by properties
        var height: Number? by properties
        override fun build() = Image(src, title, cache, timeout, width, height).apply {
            properties.putAll(this@ImageBuilder.properties)
            children.addAll(list)
        }
    }

    @MessageDSL
    class AudioBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>("src" to "", "cache" to null, "timeout" to null)
        var src: String by properties
        var title: String? by properties
        var cache: Boolean? by properties
        var timeout: String? by properties
        var duration: Number? by properties
        var poster: String? by properties
        override fun build() = Audio(src, title, cache, timeout, duration, poster).apply {
            properties.putAll(this@AudioBuilder.properties)
            children.addAll(list)
        }
    }

    @MessageDSL
    class VideoBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>("src" to "", "cache" to null, "timeout" to null)
        var src: String by properties
        var title: String? by properties
        var cache: Boolean? by properties
        var timeout: String? by properties
        var width: Number? by properties
        var height: Number? by properties
        var duration: Number? by properties
        var poster: String? by properties
        override fun build() = Video(src, title, cache, timeout, width, height, duration, poster).apply {
            properties.putAll(this@VideoBuilder.properties)
            children.addAll(list)
        }
    }

    @MessageDSL
    class FileBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>("src" to "", "cache" to null, "timeout" to null)
        var src: String by properties
        var title: String? by properties
        var cache: Boolean? by properties
        var timeout: String? by properties
        var poster: String? by properties
        override fun build() = File(src, title, cache, timeout, poster).apply {
            properties.putAll(this@FileBuilder.properties)
            children.addAll(list)
        }
    }

    @MessageDSL
    class BoldBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun build() = Bold().apply {
            properties.putAll(this@BoldBuilder.properties)
            children.addAll(list)
        }
    }

    @MessageDSL
    class IdiomaticBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun build() = Idiomatic().apply {
            properties.putAll(this@IdiomaticBuilder.properties)
            children.addAll(list)
        }
    }

    @MessageDSL
    class UnderlineBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun build() = Underline().apply {
            properties.putAll(this@UnderlineBuilder.properties)
            children.addAll(list)
        }
    }

    @MessageDSL
    class DeleteBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun build() = Strikethrough().apply {
            properties.putAll(this@DeleteBuilder.properties)
            children.addAll(list)
        }
    }

    @MessageDSL
    class SplBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun build() = Spl().apply {
            properties.putAll(this@SplBuilder.properties)
            children.addAll(list)
        }
    }

    @MessageDSL
    class CodeBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun build() = Code().apply {
            properties.putAll(this@CodeBuilder.properties)
            children.addAll(list)
        }
    }

    @MessageDSL
    class SupBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun build() = Sup().apply {
            properties.putAll(this@SupBuilder.properties)
            children.addAll(list)
        }
    }

    @MessageDSL
    class SubBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun build() = Sub().apply {
            properties.putAll(this@SubBuilder.properties)
            children.addAll(list)
        }
    }

    @MessageDSL
    class BrBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun build() = Br().apply {
            properties.putAll(this@BrBuilder.properties)
            children.addAll(list)
        }
    }

    @MessageDSL
    class ParagraphBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun build() = Paragraph().apply {
            properties.putAll(this@ParagraphBuilder.properties)
            children.addAll(list)
        }
    }

    @MessageDSL
    class MessageBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>("id" to null, "forward" to null)
        var id: String? by properties
        var forward: Boolean? by properties
        override fun build() = Message(id, forward).apply {
            properties.putAll(this@MessageBuilder.properties)
            children.addAll(list)
        }
    }

    @MessageDSL
    class QuoteBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun build() = Quote().apply {
            properties.putAll(this@QuoteBuilder.properties)
            children.addAll(list)
        }
    }

    @MessageDSL
    class AuthorBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>("id" to null, "name" to null, "avatar" to null)
        var id: String? by properties
        var name: String? by properties
        var avatar: String? by properties
        override fun build() = Author(id, name, avatar).apply {
            properties.putAll(this@AuthorBuilder.properties)
            children.addAll(list)
        }
    }

    @MessageDSL
    class ButtonBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties =
            mutableMapOf<String, Any?>("id" to null, "type" to null, "href" to null, "text" to null, "theme" to null)
        var id: String? by properties
        var type: String? by properties
        var href: String? by properties
        var text: String? by properties
        var theme: String? by properties
        override fun build() = Button(id, type, href, text, theme).apply {
            properties.putAll(this@ButtonBuilder.properties)
            children.addAll(list)
        }
    }
}