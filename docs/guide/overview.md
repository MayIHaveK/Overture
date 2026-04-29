# 概述

Overture 是一个基于 [Blink](https://github.com/17Artist/Blink) 框架的 Minecraft 物品库插件，提供完整的自定义物品定义、构建、展示、序列化和自动更新能力。

## 核心技术栈

| 组件 | 说明 |
|------|------|
| **Blink** | 插件框架 — 生命周期、命令、事件、配置 |
| **Asteroid** | 跨版本 NMS 桥接 — ItemTag / NBT 操作（1.18 ~ 1.26+） |
| **Aria** | 轻量级 JVM 脚本引擎 — 驱动物品动作和条件展示 |

## 核心设计

### 物品流 (ItemStream)

所有物品操作通过 `ItemStream` 中间层完成，不直接操作 `ItemStack`。物品流封装了 ItemStack + NBT 数据，提供类型安全的读写接口。

### 版本签名自动更新

每个物品定义的配置内容会计算 SHA-1 哈希作为版本签名。当配置变更时，签名自动变化，玩家背包中的物品会在下次检查时自动更新。

### !! 锁定机制

贯穿 `icon`、`name`、`lore`、`data`、`meta`、`event` 的锁定控制：

- `data` 中 `key!!` — 更新时强制覆盖该数据
- `icon!!` — 更新时强制覆盖材质
- `name!!` — 更新时强制覆盖名称
- `lore!!` — 更新时强制覆盖描述
- `meta` 中 `key!!` — 更新时强制重新应用该 Meta
- `event` 中 `on_xxx!!` — 自动取消原始 Bukkit 事件

### 展示方案分离

物品的展示（名称模板 + 描述模板）独立定义为 Display，多个物品可以共享同一个展示方案。支持条件展示 — 根据 Aria 表达式动态切换不同的展示方案。

## NBT 存储结构

```
ItemStack NBT:
└── overture (Compound)
    ├── id (String)              — 物品 ID
    ├── version (String)         — SHA-1 签名
    ├── data (Compound)          — 活跃数据
    ├── unique (Compound)        — 唯一数据
    │   ├── uuid (String)
    │   ├── player (String)
    │   ├── date (Long)
    │   └── date-formatted (String)
    └── meta-history (List)      — Meta 历史记录
```
