# 群组 (Guild)

## 动作 (Action)

### 获取群组

> actions.guild.get(guildId: String): Guild

### 获取群组列表

> actions.guild.list(next: String? = null): List\<PaginatedData\<Guild\>\>

### 处理群组邀请

> actions.guild.approve(messageId: String, approve: Boolean, comment: String)

## 事件 (Event)

### 加入群组

> container.guild.added(listener: Listener\<GuildEvent\>)

### 群组被修改

> container.guild.updated(listener: Listener\<GuildEvent\>)

### 退出群组

> container.guild.removed(listener: Listener\<GuildEvent\>)

### 接收到新的入群邀请

> container.guild.request(listener: Listener\<GuildEvent\>)