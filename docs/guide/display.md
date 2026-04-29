# 展示方案

展示方案定义物品的名称和描述模板，在 `displays/` 目录下的 `.yml` 文件中配置。

## 普通展示

```yaml
weapon_display:
  name: "<item_name> <item_level>"
  lore:
    - "<item_type>"
    - ""
    - "<item_desc...>"
    - ""
    - "&7攻击力: &f+<damage_display>"
    - "&7耐久: <durability_bar>"
    - ""
    - "<item_extra...>"
```

## 变量语法

### `<var>` — 单值替换

从物品的 `name`/`lore` 配置或 `data-mapper` 映射结果中取值。找不到时替换为空字符串。

### `<var...>` — 列表展开

逐行消费列表中的值。如果列表有多个值，当前模板行会重复输出直到列表消费完毕。

**空列表跳过整行**：如果展开变量对应的列表为空，该模板行不会输出。这实现了条件行 — 当变量无数据时整行消失。

### 示例

物品配置：
```yaml
my_item:
  lore:
    item_desc:
      - "&f第一行描述"
      - "&f第二行描述"
    item_extra: []           # 空列表
```

展示模板：
```yaml
my_display:
  lore:
    - "<item_desc...>"       # 输出两行
    - ""
    - "<item_extra...>"      # 空列表 → 整行跳过
```

最终输出：
```
&f第一行描述
&f第二行描述
                             ← 空行保留
                             ← item_extra 行被跳过
```

## 条件展示

根据 Aria 表达式动态选择不同的展示方案：

```yaml
weapon_display_smart:
  conditions:
    - condition: "item.durability() <= 10"
      display: weapon_display_broken
    - condition: "player.hasPermission('vip.display')"
      display: weapon_display_vip
  default: weapon_display
```

- `conditions` 按顺序评估，第一个为 `true` 的条件生效
- `condition` 是 Aria 表达式，上下文中可访问 `player` 和 `item`
- `default` 是所有条件都不满足时的默认展示方案
