package com.github.nyayurn.qbot

import com.github.nyayurn.yutori.MessageEvent

val qqHelperFilter = { event: MessageEvent -> event.platform == "chronocat" && event.user.id == "2854196310" }