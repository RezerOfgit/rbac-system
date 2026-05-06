## RBAC 权限管理系统

基于 Spring Boot + Spring Security + JWT + Vue 2 的企业级 RBAC 权限管理脚手架，实现从 API 接口到前端按钮的细粒度访问控制。

### 技术栈

| 层级 | 技术 | 版本 |
|------|------|------|
| 后端框架 | Spring Boot | 2.7.18 |
| 持久层框架 | MyBatis-Plus | 3.5.3.1 |
| 数据库连接池 | Druid | 1.2.8 |
| 认证机制 | JWT (jjwt) | 0.11.5 |
| 数据库 | MySQL | 8.0 |
| 前端框架 | Vue 2 + Element UI | 2.6 / 2.15.3 |
| 构建工具 | Maven / Vue CLI | 3.6.1 / 5.0.9 |

### 项目结构

```
rbac-system/
├── rbac-backend/         # Spring Boot 后端
│   ├── src/main/java/com/rbac/
│   │   ├── annotation/   # 自定义权限注解 (@RequirePermi)
│   │   ├── aspect/       # AOP 权限切面
│   │   ├── common/       # 统一响应封装 (R)、全局异常处理
│   │   ├── config/       # Spring Security 配置
│   │   ├── controller/   # 控制器层
│   │   ├── entity/       # 数据库实体
│   │   ├── filter/       # JWT 认证过滤器
│   │   ├── mapper/       # MyBatis-Plus Mapper
│   │   ├── security/     # 认证核心类 (LoginUser、JwtTokenProvider)
│   │   └── service/      # 业务逻辑层
│   └── src/main/resources/
│       ├── sql/          # 数据库初始化脚本
│       └── application-*.yml
└── rbac-frontend/        # Vue 2 前端
    └── src/
        ├── directive/    # 按钮级权限指令 (v-hasPermi)
        ├── router/       # 路由与守卫
        ├── utils/        # Axios 封装与 Token 拦截
        └── views/        # 登录、首页
```

### 核心功能

- **RBAC 五表模型** — `sys_user`、`sys_role`、`sys_menu`、`sys_user_role`、`sys_role_menu`，支持用户-角色-菜单多对多关联。
- **无状态 JWT 认证** — 深度整合 Spring Security 过滤链，无 Session 方案，适合前后端分离及分布式部署。
- **细粒度访问控制**
    - **接口级**：自定义 `@RequirePermi` 注解配合 Spring AOP 切面，精确拦截 Controller 方法。权限不足时返回 403。
    - **按钮级**：基于 Vue 自定义 `v-hasPermi` 指令，根据权限标识控制 DOM 元素的渲染。
    - **通配符放行**：支持 `*:*:*` 超级管理员权限，开发调试无需频繁赋权。
    - **数据级**：通过 `SecurityUtils` 工具类实现平行越权校验，普通用户只能操作自己的数据，超级管理员账号受保护禁止删除。
- **多环境日志隔离** — 开发环境全量 SQL 打印，生产环境仅 WARN 以上级别。预留 Logback 脱敏 Converter。
- **统一响应封装** — `R<T>` 统一返回 `code/message/data/timestamp`，语义化静态方法降低 Controller 层冗余代码。
- **数据库初始化脚本** — 包含 5 张表结构 + 默认管理员账号 + 测试数据，一键建库跑通全流程。

### 权限模型

系统中的最小授权单元是**权限标识**（如 `sys:user:add`）。每个接口和按钮都对应一个标识，用户拥有的标识集合决定其可访问的资源范围。具体流程如下：

1.  创建菜单资源，指定其 `perms` 标识；
2.  创建角色，将菜单权限授予角色（`sys_role_menu`）；
3.  将用户绑定到一个或多个角色（`sys_user_role`）；
4.  用户登录时，系统**动态地从数据库加载其所有权限标识**（跨 `sys_user_role`、`sys_role_menu` 和 `sys_menu` 三表查询）；
5.  每次 API 请求时，由 `@RequirePermi` 注解进行强制比对；
6.  前端页面渲染时，`v-hasPermi` 指令根据权限列表控制按钮显隐。

```
用户
│
▼
sys_user ──< sys_user_role >── sys_role ──< sys_role_menu >── sys_menu
▲                                    ▲
│                                    │
username / password              role_name / role_key
(身份凭证)                        (角色标识符)
```

### 快速开始

**1. 克隆项目**

```bash
git clone https://github.com/RezerOfgit/rbac-system.git
cd rbac-system
```

**2. 初始化数据库**

在 MySQL 中创建 `rbac_system` 库后，使用 DBeaver 或其他工具执行 `rbac-backend/src/main/resources/sql/rbac_init.sql`。

脚本会创建 5 张核心表并插入默认数据，包括两个测试账号：

| 账号 | 密码 | 角色 | 权限 |
|------|------|------|------|
| admin | 123456 | 超级管理员 | `*:*:*`（全部权限） |
| testuser | 123456 | 普通员工 | `sys:user:list`、`sys:role:list` |


**3. 配置环境变量**

在 IDE 中为 `RbacSystemApplication` 运行配置添加以下环境变量：

| 变量名 | 说明 | 示例值 |
|--------|------|--------|
| `DB_PASSWORD` | MySQL 连接密码 | `your_password` |
| `JWT_SECRET` | JWT 签名密钥 (≥256位) | `RbacDevSecret2026!` |

**4. 启动后端**

```bash
cd rbac-backend
mvn spring-boot:run
# 或直接在 IDE 中运行 RbacSystemApplication
```

后端运行在 `http://localhost:8080`。

**5. 启动前端**

```bash
cd rbac-frontend
npm install
npm run serve
```

前端运行在 `http://localhost:8081`，已配置代理将 `/api` 请求转发到后端。

**6. 登录测试**

访问 `http://localhost:8081`，使用上方测试账号登录。

### API 接口

所有接口统一返回格式：`{"code": 200, "message": "...", "data": {...}, "timestamp": ...}`。

| 方法 | 路径 | 所需权限 | 说明 |
|------|------|----------|------|
| POST | `/api/auth/login` | 无 | 登录，返回 JWT Token 及权限列表 |
| GET | `/api/user/list` | `sys:user:list` | 查询用户列表 |
| POST | `/api/user/add` | `sys:user:add` | 新增用户 |
| PUT | `/api/user/update` | `sys:user:update` | 修改用户信息（非管理员仅可修改自己） |
| DELETE | `/api/user/{id}` | `sys:user:delete` | 删除用户（禁止删除超级管理员，非管理员仅可删除自己） |
| POST | `/api/user/assignRole` | `sys:user:assign` | 为用户分配角色 |
| GET | `/api/role/list` | `sys:role:list` | 查询角色列表 |
| POST | `/api/role/add` | `sys:role:add` | 新增角色 |
| POST | `/api/role/assignMenu` | `sys:role:assign` | 为角色分配菜单权限 |
| GET | `/api/menu/tree` | `sys:menu:list` | 获取菜单树 |
| POST | `/api/menu/add` | `sys:menu:add` | 新增菜单 |

### 状态码说明

| code | 含义 | 触发场景 |
|------|------|----------|
| 200 | 成功 | 请求正常处理 |
| 400 | 参数错误 | 请求参数校验失败 |
| 401 | 未认证 | Token 缺失、过期或无效 |
| 403 | 无权限 | 用户无对应权限标识 |
| 500 | 系统异常 | 未预期的内部错误 |
