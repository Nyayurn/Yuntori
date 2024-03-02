# 消息 (Message)

## 动作 (Action)

### 发送消息

> actions.message.create(channelId: String, content: String): List\<Message\>
> actions.message.create(channelId: String, block: MessageDslBuilder.() -\> Unit): List\<Message\>

### 获取消息

> actions.message.get(channelId: String, messageId: String): Message

### 撤回消息

> actions.message.delete(channelId: String, messageId: String)

### 编辑消息

> actions.message.update(channelId: String, messageId: String, content: String)
> actions.message.update(channelId: String, messageId: String, block: MessageDslBuilder.() -\> Unit)

### 获取消息列表

> actions.message.list(channelId: String, next: String? = null): List\<PaginatedData\<Message\>\>

## 事件 (Event)

### 消息被创建

> container.message.created(listener: Listener\<MessageEvent\>)

### 消息被编辑

> container.message.updated(listener: Listener\<MessageEvent\>)

### 消息被删除

> container.message.deleted(listener: Listener\<MessageEvent\>)