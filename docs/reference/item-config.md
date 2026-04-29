# 物品配置参考

## 完整字段表

| 字段            | 类型     | 必填 | 说明                               |
|---------------|--------|----|----------------------------------|
| `display`     | String | 否  | 引用的展示方案 ID                       |
| `icon`        | String | 是  | Bukkit Material 枚举名              |
| `name`        | Map    | 否  | 名称变量 `Map<String, String>`       |
| `lore`        | Map    | 否  | 描述变量 `Map<String, String\|List>` |
| `data`        | Map    | 否  | 活跃数据，写入 NBT `overture.data`      |
| `data-mapper` | Map    | 否  | 数据→展示变量映射                        |
| `meta`        | Map    | 否  | 元数据配置                            |
| `event`       | Map    | 否  | 事件脚本配置                           |

## !! 锁定后缀

| 位置                  | 效果               |
|---------------------|------------------|
| `icon!!: MATERIAL`  | 更新时强制覆盖材质        |
| `name!!:`           | 更新时强制覆盖名称        |
| `lore!!:`           | 更新时强制覆盖描述        |
| `data.key!!: value` | 更新时强制覆盖该数据       |
| `meta.key!!:`       | 更新时强制重新应用该 Meta  |
| `event.on_xxx!!:`   | 自动取消原始 Bukkit 事件 |

## 数据类型标注

| 后缀      | 类型       | 示例             | NBT 类型       |
|---------|----------|----------------|--------------|
| `s`     | Short    | `"10s"`        | TAG_Short    |
| `L`     | Long     | `"10L"`        | TAG_Long     |
| `f`     | Float    | `"10f"`        | TAG_Float    |
| `b`     | Byte     | `"10b"`        | TAG_Byte     |
| 无 (整数)  | Int      | `10`           | TAG_Int      |
| 无 (小数)  | Double   | `10.0`         | TAG_Double   |
| 无 (布尔)  | Byte     | `true`         | TAG_Byte(1)  |
| 无 (字符串) | String   | `"hello"`      | TAG_String   |
| 列表      | List     | `[1, 2, 3]`    | TAG_List     |
| 嵌套      | Compound | `key: { ... }` | TAG_Compound |
