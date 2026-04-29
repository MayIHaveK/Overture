# API 参考

## OvertureAPI

公共 API 门面，所有方法均为静态调用。

```kotlin
import priv.seventeen.artist.overture.api.OvertureAPI
```

| 方法 | 返回类型 | 说明 |
|------|----------|------|
| `getItem(id)` | `OvertureItem?` | 获取物品定义 |
| `getItems()` | `Map<String, OvertureItem>` | 获取所有物品 |
| `getItemIds()` | `List<String>` | 获取所有物品 ID |
| `generateItem(id, player?)` | `ItemStack?` | 生成物品 |
| `readStream(item)` | `ItemStream` | 从 ItemStack 读取物品流 |
| `isOvertureItem(item)` | `Boolean` | 判断是否为 Overture 物品 |
| `getOvertureId(item)` | `String?` | 获取物品 ID |
| `serialize(item)` | `String` | 序列化物品为 JSON |
| `deserialize(json)` | `ItemStack?` | 从 JSON 反序列化 |
| `registerProvider(provider)` | `Unit` | 注册物品提供者 |
| `reload()` | `Unit` | 重载所有配置 |

## ItemProvider

物品提供者接口，支持多来源物品加载。

```kotlin
interface ItemProvider {
    val id: String           // 提供者标识
    val priority: Int        // 优先级（值越小越先加载）
    fun load(): Map<String, OvertureItem>
    fun reload()
}
```

### 自定义提供者示例

```kotlin
class DatabaseProvider : ItemProvider {
    override val id = "database"
    override val priority = 10

    override fun load(): Map<String, OvertureItem> {
        // 从数据库加载物品配置
        return mapOf()
    }

    override fun reload() {
        // 重新连接数据库
    }
}

// 注册
OvertureAPI.registerProvider(DatabaseProvider())
```

## ItemStream

物品流 — 运行时物品实例。

```kotlin
val stream = OvertureAPI.readStream(itemStack)

// 身份判断
stream.isOverture          // 是否为 Overture 物品
stream.overtureId          // 物品 ID

// 数据读写
stream.getData("damage")?.asInt()
stream.setData("damage", ItemTagData.of(10))
stream.removeData("temp_key")

// 版本检查
stream.isOutdated(item)    // 是否过时

// 保存
stream.save()              // 仅写入 NBT
stream.toItemStack(player) // 完整释放（触发事件链）
```

## MapperFunction

注册自定义映射函数：

```kotlin
import priv.seventeen.artist.overture.core.mapper.MapperFunction

MapperFunction.register("myFunc") { args ->
    val value = (args[0] as Number).toInt()
    "结果: $value"
}
```

## MetaRegistry

注册自定义 Meta 类型：

```kotlin
import priv.seventeen.artist.overture.core.meta.MetaRegistry

MetaRegistry.register("my_meta") { section, value, locked ->
    MyCustomMeta(section, locked)
}
```
