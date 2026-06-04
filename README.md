# keshe_server

## 项目简介

这是一个二手商品交易平台的后端服务，基于 Spring Boot 框架开发。

## 技术栈

- Java 21
- Spring Boot 3.2.x
- Spring Security + JWT
- Spring Data JPA
- MySQL

## 主要功能

### 用户模块
- 用户注册、登录
- 用户信息管理（查看、修改）
- 密码修改

### 商品模块
- 商品发布
- 待出售商品总览、分类查询
- 下单、接单
- （自己发布的）商品删除

### 评论模块
- 发布评论
- 查看商品评论
- 删除（自己发布的）评论

### 管理员模块
- 删除任意评论
- 删除任意商品
- 删除任意用户

## 项目结构

```
server/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/keshe/server/
│   │   │       ├── configs/           # 配置类（安全、JWT、异常处理等）
│   │   │       ├── controller/        # 控制层（REST API）
│   │   │       ├── data/              # 数据层
│   │   │       │   ├── dto/           # 数据传输对象
│   │   │       │   ├── po/            # 持久化对象(实体类)
│   │   │       │   └── vo/            # 视图对象
│   │   │       ├── repository/        # 数据访问层（JPA）
│   │   │       ├── service/           # 服务层（业务逻辑）
│   │   │       ├── utils/             # 工具类
│   │   │       └── ServerApplication.java  # 启动类
│   │   └── resources/                 # 配置文件
│   └── test/                          # 测试代码
├── uploads/                           # 上传文件存储
└── README.md
```

## API 接口示例

### 用户模块

```
POST /auth/register        # 用户注册
POST /auth/login           # 用户登录
POST /auth/change_password # 修改密码
GET  /user/getProfile      # 获取用户信息
POST /user/updateProfile   # 修改用户信息
GET  /user/list            # 获取所有用户列表（公开）
```

### 商品模块

```
GET  /product/list         # 获取待出售商品列表
GET  /product/detail/{productId}   # 获取商品详情
GET  /product/category/{category}  # 按分类查询商品
POST /product/sell         # 发布商品
POST /product/buy          # 购买商品
POST /product/accept       # 卖家接单
POST /product/delete       # 删除自己发布的商品
GET  /product/my-purchases # 获取用户购买的商品
GET  /product/my-products  # 获取用户发布的商品
POST /product/upload       # 上传商品图片
```

### 评论模块

```
GET  /comment/{productId}  # 获取商品评论列表
POST /comment/add          # 发布评论
POST /comment/delete       # 删除自己的评论
```

### 管理员模块

```
POST /admin/comment/delete # 删除任意评论
POST /admin/product/delete # 删除任意商品
POST /admin/user/delete    # 删除任意用户
```
