# 群组成员 (GuildMember)

## 动作 (Action)

### 获取群组成员

> actions.guild.member.get(guildId: String, userId: String): GuildMember

### 获取群组成员列表

> actions.guild.member.list(guildId: String, next: String? = null): List\<PaginatedData\<GuildMember\>\>

### 踢出群组成员

> actions.guild.member.kick(guildId: String, userId: String, permanent: Boolean? = null)

### 通过群组成员申请

> actions.guild.member.approve(messageId: String, approve: Boolean, comment: String? = null)

## 事件 (Event)

### 群组成员增加

> container.guild.member.added(listener: Listener\<GuildMemberEvent\>)

### 群组成员信息更新

> container.guild.member.updated(listener: Listener\<GuildMemberEvent\>)

### 群组成员移除

> container.guild.member.removed(listener: Listener\<GuildMemberEvent\>)

### 接收到新的加群请求

> container.guild.member.request(listener: Listener\<GuildMemberEvent\>)