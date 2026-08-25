# 进销存管理系统

基于前后端分离架构的进销存（进货 / 销售 / 库存）管理系统，集成 **支付宝沙箱支付**，支持订单支付轮询、商品图片上传等能力。

## 项目简介

本系统面向中小企业的进销存业务管理，包含商品管理、进货入库、销售出库、库存查询、订单与支付等模块。前端负责交互与支付状态轮询，后端提供业务 API 并接入支付宝沙箱支付网关。

## 项目结构

```
├── spring/    # 后端（Spring Boot 多模块 Maven 项目）
│   ├── ychs-base-common/  # 公共模块（工具类、通用组件）
│   └── ychs-base-web/     # Web 主模块（Controller、Service、Mapper）
└── vue/       # 前端（Vue 3 + Vite + TypeScript）
```

## 技术栈

### 前端
- **Vue 3** + **Vite** + **TypeScript**
- **Element Plus**（UI 组件库）
- **Pinia**（状态管理）+ **Vue Router**
- **Axios** + **ECharts**

### 后端
- **Spring Boot 3.1**（Java 17）
- **MyBatis-Plus**（ORM）
- **MySQL**（数据库）+ **Druid**（连接池）
- **JWT**（身份认证）
- **支付宝沙箱支付**（SDK 集成）

## 快速开始

### 环境要求
- JDK 17+、Maven 3.6+
- Node.js 18+
- MySQL 8+

### 后端启动
1. 复制 `spring/ychs-base-web/src/main/resources/application-example.yml` 为 `application-test.yml`（或 `application-active.yml`），填入真实数据库密码、JWT 密钥与支付宝沙箱密钥
2. 初始化数据库
3. 启动 `ychs-base-web` 模块

### 前端启动
```bash
cd vue
npm install
npm run dev
```

> 访问地址：前端 `http://localhost:5173`，后端 `http://localhost:8089`
