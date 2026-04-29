# 映射函数参考

`data-mapper` 中可用的内置映射函数。

## 内置函数

| 函数 | 参数 | 说明 | 示例 |
|------|------|------|------|
| `bar` | `(current, max, scale?)` | 生成进度条，scale 默认 20 | `bar({durability_current}, {durability})` |
| `repeat` | `(str, n)` | 重复字符串 n 次（最大 100） | `repeat('★', {level})` |
| `format` | `(pattern, args...)` | String.format 格式化 | `format('%.1f', {speed})` |
| `color` | `(value, min, max)` | 根据百分比着色 | `color({hp}, 0, 100)` |
| `percent` | `(current, max)` | 计算百分比文本 | `percent({exp}, {max_exp})` |
| `roman` | `(n)` | 数字转罗马数字 (1~3999) | `roman({level})` |
| `fixed` | `(value, decimals?)` | 固定小数位，默认 1 | `fixed({crit_rate}, 2)` |
| `condition` | `(cond, trueVal, falseVal)` | 条件选择 | `condition({active}, '&a启用', '&c禁用')` |

## color 函数着色规则

| 百分比 | 颜色 |
|--------|------|
| ≤ 25% | `§c` 红色 |
| ≤ 50% | `§e` 黄色 |
| ≤ 75% | `§a` 浅绿 |
| > 75% | `§2` 深绿 |

## 自定义函数

通过 API 注册自定义映射函数：

```kotlin
MapperFunction.register("myFunc") { args ->
    val value = args[0] as Number
    "自定义结果: $value"
}
```

配置中使用：
```yaml
data-mapper:
  my_var: "myFunc({some_data})"
```
