# 消息 (Message)

## 动作 (Action)

### 添加表态

> actions.reaction.create(channelId: String, messageId: String, emoji: String)

### 删除表态

> actions.reaction.delete(channelId: String, messageId: String, emoji: String, userId: String? = null)

### 清除表态

> actions.reaction.clear(channelId: String, messageId: String, emoji: String? = null)

### 获取表态列表

> actions.reaction.list(channelId: String, messageId: String, emoji: String, next: String? = null): 
> List\<PaginatedData\<User\>\>

## 事件 (Event)

### 表态被添加

> container.reaction.added(listener: Listener\<ReactionEvent\>)

### 表态被移除

> container.reaction.removed(listener: Listener\<ReactionEvent\>)