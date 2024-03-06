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

package com.github.nyayurn.yuntori.message.elements

/**
 * 资源元素
 * @property src 资源的 URL
 * @property title 资源的标题
 */
abstract class ResourceElement(
    name: String,
    src: String,
    title: String?,
    vararg pairs: Pair<String, Any?>
) : NodeMessageElement(
    name,
    "src" to src,
    "title" to title,
    *pairs
) {
    var src: String by super.properties
    var title: String? by super.properties
}

/**
 * 图片
 */
class Image(src: String, title: String? = null) : ResourceElement("img", src, title)

/**
 * 文件
 */
class File(src: String, title: String? = null) : ResourceElement("file", src, title)