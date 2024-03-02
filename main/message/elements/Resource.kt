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

package com.github.nyayurn.yutori.next.message.elements

/**
 * 资源元素
 * @property src 资源的 URL
 * @property cache 是否使用已缓存的文件
 * @property timeout 下载文件的最长时间 (毫秒)
 */
abstract class ResourceElement : NodeMessageElement {
    var src: String by super.properties
    var title: String? by super.properties
    var cache: Boolean? by super.properties
    var timeout: String? by super.properties

    constructor(
        name: String,
        src: String,
        title: String?,
        cache: Boolean?,
        timeout: String?
    ) : super(name) {
        this.src = src
        this.title = title
        this.cache = cache
        this.timeout = timeout
    }
}

/**
 * 图片
 * @property width 图片的宽度
 * @property height 图片的高度
 */
class Image : ResourceElement {
    var width: Number? by super.properties
    var height: Number? by super.properties

    constructor(
        src: String,
        title: String? = null,
        cache: Boolean? = null,
        timeout: String? = null,
        width: Number? = null,
        height: Number? = null
    ) : super("img", src, title, cache, timeout) {
        this.width = width
        this.height = height
    }
}

/**
 * 语音
 */
class Audio : ResourceElement {
    var duration: Number? by super.properties
    var poster: String? by super.properties

    constructor(
        src: String,
        title: String? = null,
        cache: Boolean? = null,
        timeout: String? = null,
        duration: Number? = null,
        poster: String? = null
    ): super("audio", src, title, cache, timeout) {
        this.duration = duration
        this.poster = poster
    }
}

/**
 * 视频
 */
class Video : ResourceElement {
    var width: Number? by super.properties
    var height: Number? by super.properties
    var duration: Number? by super.properties
    var poster: String? by super.properties
    constructor(
        src: String,
        title: String? = null,
        cache: Boolean? = null,
        timeout: String? = null,
        width: Number? = null,
        height: Number? = null,
        duration: Number? = null,
        poster: String? = null
    ): super("video", src, title, cache, timeout) {
        this.width = width
        this.height = height
        this.duration = duration
        this.poster = poster
    }
}

/**
 * 文件
 */
class File : ResourceElement {
    var poster: String? by super.properties

    constructor(
        src: String,
        title: String? = null,
        cache: Boolean? = null,
        timeout: String? = null,
        poster: String? = null
    ): super("file", src, title, cache, timeout) {
        this.poster = poster
    }
}