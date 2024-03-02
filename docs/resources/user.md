# 用户 (User)

## 动作 (Action)

### 获取用户信息

> actions.user.get(userId: String): User

### 获取好友列表

> actions.friend.list(next: String? = null): List\<PaginatedData\<User\>\>

### 处理好友申请

> actions.friend.approve(messageId: String, approve: Boolean, comment: String? = null)

## 事件 (Event)

### 接收到新的好友申请

> container.friend.request(listener: Listener\<UserEvent\>)