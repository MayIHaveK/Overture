# 物品配置

物品定义在 `items/` 目录下的 `.yml` 文件中，每个顶级键名即物品 ID。

## 完整格式

```yaml
diamond_sword_1:
  display: weapon_display          # 引用展示方案 ID
  icon: DIAMOND_SWORD              # 材质（Bukkit Material）
  # icon!!: DIAMOND_SWORD          # !! = 更新时强制覆盖材质

  name:                            # 名称变量 Map<String, String>
    item_name: "&b钻石剑"
    item_level: "&7Lv.1"
  # name!!:                        # !! = 更新时强制覆盖名称

  lore:                            # 描述变量 Map<String, List<String>>
    item_type: "&9武器"
    item_desc:                     # 多值列表（对应 <item_desc...> 展开）
      - "&f一把锋利的钻石剑"
      - "&f可以在铁匠铺购买"
    item_empty: []                 # 空列表 → 对应行不显示
  # lore!!:                        # !! = 更新时强制覆盖描述

  data:                            # 活跃数据 → NBT overture.data
    damage: 7                      # Int
    attack-speed: 1.6              # Double
    durability!!: 100              # !! = 更新时强制覆盖
    custom:                        # 支持嵌套
      level: 1
      exp: "0s"                    # 类型标注

  data-mapper:                     # 数据 → 展示变量映射
    damage_display: "{damage}"
    durability_bar: "bar({durability_current}, {durability})"

  meta:                            # 元数据（详见 Meta 系统）
    attribute:
      mainhand:
        generic_attack_damage: "+7"
    durability:
      max: 100
      remains: STICK
    unique: true
    shiny: true

  event:                           # 事件脚本（详见动作系统）
    from: [base_weapon_model]      # 引用事件模型
    data:                          # 事件变量
      base_damage: 7
    on_right_click!!: |            # !! = 自动取消原版事件
      sendMessage('&a右键！')
    on_attack: |
      item.damage(1)
```

## 字段说明

### display

引用展示方案的 ID。展示方案在 `displays/` 目录中定义。

### icon

Bukkit `Material` 枚举名，不区分大小写。加 `!!` 后缀表示更新时强制覆盖材质。

### name / lore

名称和描述的变量映射。这些变量会被展示方案中的 `<变量名>` 模板引用。

- `name` 中的值是单个字符串
- `lore` 中的值可以是字符串或字符串列表
- 支持 `&` 颜色代码和 `&#RRGGBB` hex 颜色

### data

写入 NBT `overture.data` 节点的活跃数据。支持嵌套结构。

**类型标注语法：**

| 后缀 | 类型 | 示例 |
|------|------|------|
| `s` | Short | `"10s"` |
| `L` | Long | `"10L"` |
| `f` | Float | `"10f"` |
| `b` | Byte | `"10b"` |
| 无 (整数) | Int | `10` |
| 无 (小数) | Double | `10.0` |
| 无 (布尔) | Byte(0/1) | `true` |

键名加 `!!` 后缀表示锁定数据，更新时强制覆盖。

### data-mapper

将 NBT data 映射为展示变量。详见 [数据映射](./data-mapper)。

### meta

物品元数据配置。详见 [Meta 系统](/reference/meta)。

### event

物品事件脚本。详见 [动作系统](./action)。
