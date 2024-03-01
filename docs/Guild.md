# 快速开始

## 基础信息

- 本项目不依赖也不建议配合 Spring 进行开发

## 项目创建

1. 创建项目
2. [引入依赖](#依赖引入)
3. [基础使用](#基础使用)

## 依赖引入

### Maven

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

```xml
<dependency>
    <groupId>com.github.Nyayurn</groupId>
    <artifactId>Yutori</artifactId>
    <version>final</version>
</dependency>
```

### Gradle Kotlin DSL

```kotlin
maven { url = URI("https://jitpack.io") }
```

```kotlin
implementation("com.github.Nyayurn:Yutori:final")
```

### Gradle Groovy DSL


```groovy
maven { url 'https://jitpack.io' }
```

```groovy
implementation 'com.github.Nyayurn:Yutori:final'
```

## 基础使用

### Kotlin

```kotlin
fun main() {
    WebSocketEventService.connect {
        listeners {
            message.created { actions, event ->
                if (event.message.content == "在吗") {
                    actions.message.create(event.channel.id) {
                        at { id = event.user.id }
                        text { " 我在!" }
                    }
                }
            }
        }
        properties {
            token = "token"
        }
    }
}
```

## 其他

Yutori: 作者名称 Yurn 与 Satori 协议名称结合而来