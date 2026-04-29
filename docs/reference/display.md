# 展示方案参考

## 普通展示

```yaml
display_id:
  name: "<var1> <var2>"        # 名称模板
  lore:                        # 描述模板列表
    - "<var>"                  # 单值变量
    - "<var...>"               # 列表展开变量
    - "&7固定文本"              # 纯文本
    - ""                       # 空行
```

## 条件展示

```yaml
display_id:
  conditions:
    - condition: "Aria 表达式"   # 返回 Boolean
      display: target_display_id
  default: fallback_display_id
```

## 变量语法

| 语法         | 说明         |
|------------|------------|
| `<var>`    | 单值替换，取第一个值 |
| `<var...>` | 列表展开，逐行消费  |

### 变量来源

1. 物品 `name` 配置中的键值对
2. 物品 `lore` 配置中的键值对
3. `data-mapper` 映射结果
4. `ItemDurability` 注入的 `durability`、`durability_current`、`durability_max`
5. `ItemReleaseEvent.Display` 事件中外部插件注入的变量

### 空列表行为

当 `<var...>` 对应的列表为空（`[]`）时，该模板行整行跳过，不会输出空行。
