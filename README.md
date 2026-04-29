<p align="center">
  <img src="docs/public/logo.svg" alt="Overture Logo" width="180" height="180">
</p>

<h1 align="center">Overture</h1>

<p align="center">
  <strong>下一代 Minecraft 物品库插件</strong><br>
  基于 Blink 框架 · Asteroid NMS · Aria 脚本引擎
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Minecraft-1.18.2--1.26+-green?style=flat-square" alt="MC Version">
  <img src="https://img.shields.io/badge/API-1.18-blue?style=flat-square" alt="API">
  <img src="https://img.shields.io/badge/Language-Kotlin-purple?style=flat-square" alt="Kotlin">
  <img src="https://img.shields.io/badge/License-Apache%202.0-orange?style=flat-square" alt="License">
</p>

---

## 特性

- **物品流设计** — 所有物品操作通过 `ItemStream` 中间层，避免直接操作 ItemStack
- **版本签名自动更新** — 配置变更自动检测，玩家背包物品实时同步
- **!! 锁定机制** — 精细控制更新时哪些数据保留、哪些强制覆盖
- **展示方案分离** — Display 独立定义，多物品共享同一展示模板
- **条件展示** — 根据 Aria 表达式动态切换展示方案
- **12 种内置 Meta** — 属性、附魔、耐久、唯一、模型数据、发光等
- **Aria 脚本驱动** — 15 种事件触发点，22 个内置函数
- **数据映射器** — 8 种内置函数将 NBT 数据转换为展示变量
- **多源物品提供** — `ItemProvider` 接口支持 YAML、数据库等任意来源
- **事件模型复用** — `$` 后缀定义可复用的事件脚本模板

## 快速开始


### 文件结构

```
plugins/Overture/
├── config.yml           # 主配置
├── items/               # 物品定义（递归扫描，文件夹 = 分组）
│   ├── weapons/
│   │   └── swords.yml
│   └── consumables.yml
├── displays/            # 展示方案
│   └── weapon_display.yml
└── scripts/             # Aria 脚本（可选）
```

### 示例物品

```yaml
diamond_sword:
  display: weapon_display
  icon: DIAMOND_SWORD
  name:
    item_name: "&b钻石剑"
    item_level: "&7Lv.1"
  lore:
    item_type: "&9武器"
    item_desc:
      - "&f一把锋利的钻石剑"
  data:
    damage: 7
    durability!!: 100
  data-mapper:
    damage_display: "{damage}"
    durability_bar: "bar({durability_current}, {durability})"
  meta:
    attribute:
      mainhand:
        generic_attack_damage: "+7"
    durability:
      max: 100
      remains: STICK
    unique: true
    shiny: true
  event:
    on_right_click!!: |
      if (cooldown.check('skill', 3000)) {
        cooldown.set('skill')
        sendMessage('&a技能释放！')
        item.damage(5)
      }
```

## 命令

| 命令              | 参数                         | 说明      |
|-----------------|----------------------------|---------|
| `/ot list`      | `[group]`                  | 打开物品菜单  |
| `/ot give`      | `<item> [player] [amount]` | 发放物品    |
| `/ot get`       | `<item> [amount]`          | 获取物品    |
| `/ot rebuild`   | —                          | 重构手持物品  |
| `/ot serialize` | —                          | 序列化手持物品 |
| `/ot info`      | —                          | 查看物品信息  |
| `/ot reload`    | —                          | 重载配置    |

## 开发者 API

```kotlin
// 获取物品
val item = OvertureAPI.getItem("diamond_sword")

// 生成物品
val itemStack = OvertureAPI.generateItem("diamond_sword", player)

// 判断是否为 Overture 物品
val isOverture = OvertureAPI.isOvertureItem(itemStack)

// 读取物品流
val stream = OvertureAPI.readStream(itemStack)
val durability = stream.getData("durability_current")?.asInt()

// 注册自定义物品提供者
OvertureAPI.registerProvider(MyDatabaseProvider())

// 监听物品事件
@AutoListener
fun onBuild(event: ItemBuildEvent.Post) {
    // 物品构建后处理
}
```

## 文档

完整文档请访问: [Overture Documentation](https://17artist.github.io/Overture/)

## 技术栈

| 组件                                               | 说明                         |
|--------------------------------------------------|----------------------------|
| [Blink](https://github.com/17Artist/Blink)       | 插件框架（生命周期、命令、事件、配置）        |
| [Asteroid](https://github.com/17Artist/Asteroid) | 跨版本 NMS 桥接（ItemTag、数据包、实体） |
| [Aria](https://github.com/17Artist/Aria)         | 轻量级 JVM 脚本引擎               |

## 许可证

[Apache License 2.0](LICENSE)
