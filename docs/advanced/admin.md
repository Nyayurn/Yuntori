# 管理接口

## 动作 (Action)

### 获取登录信息列表

> actions.admin.login.list(): List\<Login\>

### 创建 WebHook

> actions.admin.webhook.create(url: String, token: String? = null)

### 移除 WebHook

> actions.admin.webhook.delete(url: String)