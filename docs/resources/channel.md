# 频道 (Channel)

## 动作 (Action)

### 获取群组频道

> actions.channel.get(channelId: String): Channel

### 获取群组频道列表

> actions.channel.list(guildId: String, next: String? = null): List\<PaginatedData\<Channel\>\>

### 创建群组频道

> actions.channel.create(guildId: String, data: Channel): Channel

### 修改群组频道

> actions.channel.update(channelId: String, data: Channel)

### 删除群组频道

> actions.channel.delete(channelId: String)

### 创建私聊频道

> actions.user.channel.create(userId: String, guildId: String? = null): Channel