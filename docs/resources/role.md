# 群组角色 (GuildRole)

## 动作 (Action)

### 设置群组成员角色

> actions.guild.member.role.set(guildId: String, userId: String, roleId: String)

### 取消群组成员角色

> actions.guild.member.role.unset(guildId: String, userId: String, roleId: String)

### 获取群组角色列表

> actions.guild.role.list(guildId: String, next: String? = null): List\<PaginatedData\<GuildRole\>\>

### 创建群组角色

> actions.guild.role.create(guildId: String, role: GuildRole): GuildRole

### 修改群组角色

> actions.guild.role.update(guildId: String, roleId: String, role: GuildRole)

### 删除群组角色

> actions.guild.role.delete(guildId: String, roleId: String)

## 事件 (Event)

### 群组角色被创建

> container.guild.role.created(listener: Listener\<GuildRoleEvent\>)

### 群组角色被修改

> container.guild.role.updated(listener: Listener\<GuildRoleEvent\>)

### 群组角色被删除

> container.guild.role.deleted(listener: Listener\<GuildRoleEvent\>)