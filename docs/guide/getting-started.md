# 快速开始

## 安装

1. 从 [GitHub Releases](https://github.com/17Artist/Overture/releases) 下载最新版 `Overture-x.x.x.jar`
2. 将 jar 文件放入服务端的 `plugins/` 目录
3. 启动（或重启）服务器

首次启动会自动在 `plugins/Overture/` 下生成默认配置与示例文件。

::: tip 依赖说明
Overture 已将 Blink 框架、Asteroid NMS 桥接、Aria 脚本引擎全部打包进单个 jar，**无需额外安装其他插件**。
:::

**支持的服务端**：Spigot / Paper / Purpur 等 Bukkit 派生端，支持 Minecraft 1.18 及以上版本。

## 文件结构

启动后会在 `plugins/Overture/` 下生成以下目录：

```
plugins/Overture/
├── config.yml           # 主配置
├── items/               # 物品定义（递归扫描，文件夹 = 分组）
│   ├── __group__.yml    # 分组配置（可选，详见分组系统）
│   ├── weapons/
│   │   └── swords.yml
│   └── consumables.yml
├── displays/            # 展示方案
│   └── weapon_display.yml
└── scripts/             # Aria 脚本文件（可选）
```

- `items/` 目录递归扫描，每个文件夹自动成为一个物品分组
- 分组支持自定义图标、显示名、描述，详见 [分组系统](./groups)
- `displays/` 目录递归扫描所有 `.yml` 文件
- 以 `__` 开头的文件名和键名会被跳过
- 以 `$` 结尾的键名作为事件模型注册

## 第一个物品

在 `plugins/Overture/items/` 下创建 `example.yml`：

```yaml
my_sword:
  display: simple_display
  icon: DIAMOND_SWORD
  name:
    item_name: "&b我的第一把剑"
  lore:
    item_type: "&9武器"
    item_desc:
      - "&f这是一把自定义剑"
  meta:
    shiny: true
```

在 `plugins/Overture/displays/` 下创建 `simple.yml`：

```yaml
simple_display:
  name: "<item_name>"
  lore:
    - "<item_type>"
    - ""
    - "<item_desc...>"
```

执行 `/ot reload` 重载配置，然后用 `/ot get my_sword` 领取物品。

## 命令速查

| 命令 | 说明 |
|------|------|
| `/ot list [group]` | 打开物品菜单 |
| `/ot give <item> [player] [amount]` | 发放物品 |
| `/ot get <item> [amount]` | 获取物品 |
| `/ot rebuild` | 强制重构手持物品 |
| `/ot serialize` | 序列化手持物品 |
| `/ot info` | 查看物品信息 |
| `/ot reload` | 重载配置 |

## 下一步

- [物品配置详解](./item-config) — 学习完整的物品定义格式
- [展示方案](./display) — 掌握名称和描述的模板系统
- [动作系统](./action) — 用 Aria 脚本实现物品的交互逻辑
- [分组系统](./groups) — 用文件夹组织物品并定制菜单外观
