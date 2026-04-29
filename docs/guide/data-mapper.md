# 数据映射

`data-mapper` 将物品 NBT 中的活跃数据映射为展示变量，在 Display 构建时自动注入。

## 配置格式

```yaml
my_item:
  data:
    damage: 7
    durability: 100
    custom:
      level: 3
  data-mapper:
    damage_display: "{damage}"
    durability_bar: "bar({durability_current}, {durability})"
    level_stars: "repeat('★', {custom.level})"
    level_text: "roman({custom.level})"
```

## 表达式语法

### 变量引用

`{key}` — 从展平的 NBT 数据中取值。支持嵌套路径 `{custom.level}`。

### 内置函数

`funcName(arg1, arg2, ...)` — 调用内置映射函数。

参数类型：
- `'text'` 或 `"text"` — 字符串字面量
- `{key}` — NBT 数据引用
- `123` — 数字

### Aria 表达式

复杂逻辑可以直接写 Aria 表达式，变量引用 `{key}` 会先被替换为实际值。

完整函数列表见 [映射函数参考](/reference/mapper-functions)。
