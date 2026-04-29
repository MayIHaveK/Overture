# 事件系统

Overture 通过 Bukkit 事件系统暴露物品生命周期的各个阶段。

## ItemBuildEvent

### Pre — 物品构建前

```kotlin
@AutoListener
fun onBuildPre(event: ItemBuildEvent.Pre) {
    event.player       // Player? — 当前玩家
    event.itemId       // String — 物品 ID
    event.stream       // ItemStreamGenerated — 可修改 nameVars/loreVars
    event.isCancelled  // 可取消
}
```

### Post — 物品构建后

```kotlin
@AutoListener
fun onBuildPost(event: ItemBuildEvent.Post) {
    event.player       // Player?
    event.itemId       // String
    event.stream       // ItemStreamGenerated
    // 不可取消。Meta build/drop 在此阶段执行。
}
```

## ItemReleaseEvent

### Release — 物品释放为 ItemStack

```kotlin
@AutoListener
fun onRelease(event: ItemReleaseEvent.Release) {
    event.player       // Player?
    event.stream       // ItemStream
    event.itemMeta     // ItemMeta — 可修改
}
```

### SelectDisplay — 展示方案选择

```kotlin
@AutoListener
fun onSelectDisplay(event: ItemReleaseEvent.SelectDisplay) {
    event.player       // Player?
    event.stream       // ItemStream
    event.displayId    // String? — 可修改，替换展示方案
}
```

### Display — 展示变量生成

```kotlin
@AutoListener
fun onDisplay(event: ItemReleaseEvent.Display) {
    event.player       // Player?
    event.stream       // ItemStream
    event.nameVars     // MutableMap<String, String> — 可注入变量
    event.loreVars     // MutableMap<String, MutableList<String>>
}
```

### Final — 最终修改

```kotlin
@AutoListener
fun onFinal(event: ItemReleaseEvent.Final) {
    event.player       // Player?
    event.stream       // ItemStream
    event.itemStack    // ItemStack — 可替换
}
```

## ItemUpdateEvent

物品更新检查时触发，可取消以阻止更新。

```kotlin
@AutoListener
fun onUpdate(event: ItemUpdateEvent) {
    event.player       // Player
    event.itemStack    // ItemStack
    event.item         // OvertureItem — 物品定义
    event.isCancelled  // 可取消
}
```

## ItemGiveEvent

物品发放时触发，可取消或修改物品。

```kotlin
@AutoListener
fun onGive(event: ItemGiveEvent) {
    event.player       // Player
    event.itemId       // String
    event.itemStack    // ItemStack — 可修改
    event.isCancelled  // 可取消
}
```

## PluginReloadEvent

配置重载完成后触发。

```kotlin
@AutoListener
fun onReload(event: PluginReloadEvent) {
    // 重新加载依赖 Overture 数据的缓存
}
```
