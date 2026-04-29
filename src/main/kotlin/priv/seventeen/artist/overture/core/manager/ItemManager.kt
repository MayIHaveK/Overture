package priv.seventeen.artist.overture.core.manager

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import priv.seventeen.artist.asteroid.item.ItemTag
import priv.seventeen.artist.blink.BlinkLog
import priv.seventeen.artist.overture.api.ItemProvider
import priv.seventeen.artist.overture.api.event.ItemGiveEvent
import priv.seventeen.artist.overture.api.event.PluginReloadEvent
import priv.seventeen.artist.overture.core.item.ItemStream
import priv.seventeen.artist.overture.core.item.OvertureItem
import priv.seventeen.artist.overture.core.model.ItemModel

/**
 * 物品管理器
 * 负责物品的注册、查询、生成
 */
object ItemManager {

    /** 已注册的物品 */
    private val items = mutableMapOf<String, OvertureItem>()

    /** 已注册的事件模型 */
    private val models = mutableMapOf<String, ItemModel>()

    /** 物品提供者 */
    private val providers = mutableListOf<ItemProvider>()

    /**
     * 注册物品
     */
    fun register(item: OvertureItem) {
        items[item.id] = item
    }

    /**
     * 注册事件模型
     */
    fun registerModel(model: ItemModel) {
        models[model.id] = model
    }

    /**
     * 注册物品提供者
     */
    fun registerProvider(provider: ItemProvider) {
        providers.add(provider)
        providers.sortBy { it.priority }
    }

    /**
     * 获取物品定义
     */
    fun getItem(id: String): OvertureItem? = items[id]

    /**
     * 获取所有物品
     */
    fun getItems(): Map<String, OvertureItem> = items.toMap()

    /**
     * 获取所有物品 ID
     */
    fun getItemIds(): List<String> = items.keys.toList()

    /**
     * 获取事件模型
     */
    fun getModel(id: String): ItemModel? = models[id]

    /**
     * 生成物品
     */
    fun generate(id: String, player: Player? = null): ItemStack? {
        val item = items[id] ?: return null
        val stream = item.build(player)
        return stream.toItemStack(player)
    }

    /**
     * 发放物品给玩家
     */
    fun give(player: Player, id: String, amount: Int = 1): Boolean {
        val itemStack = generate(id, player) ?: return false
        itemStack.amount = amount

        // 触发发放事件
        val event = ItemGiveEvent(player, id, itemStack)
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) return false

        val remaining = player.inventory.addItem(event.itemStack)
        // 背包满则掉落
        remaining.values.forEach { overflow ->
            player.world.dropItemNaturally(player.location, overflow)
        }
        return true
    }

    /**
     * 从 ItemStack 读取物品流
     */
    fun read(itemStack: ItemStack): ItemStream {
        return ItemStream(itemStack)
    }

    /**
     * 判断是否为 Overture 物品
     */
    fun isOvertureItem(itemStack: ItemStack): Boolean {
        val tag = ItemTag.fromItemStack(itemStack)
        return tag.containsKey("overture")
    }

    /**
     * 获取物品 ID
     */
    fun getOvertureId(itemStack: ItemStack): String? {
        val tag = ItemTag.fromItemStack(itemStack)
        if (!tag.containsKey("overture")) return null
        return tag.getCompound("overture").getString("id")
    }

    /**
     * 重载所有物品
     */
    fun reload() {
        items.clear()
        models.clear()

        // 从所有提供者加载
        for (provider in providers) {
            try {
                provider.reload()
                val loaded = provider.load()
                items.putAll(loaded)
                BlinkLog.info("从 ${provider.id} 加载了 ${loaded.size} 个物品")
            } catch (e: Exception) {
                BlinkLog.error("物品提供者 ${provider.id} 加载失败: ${e.message}")
            }
        }

        BlinkLog.success("共加载 ${items.size} 个物品, ${models.size} 个模型")

        // 触发重载事件
        Bukkit.getPluginManager().callEvent(
            PluginReloadEvent()
        )
    }

    /**
     * 清空所有数据
     */
    fun clear() {
        items.clear()
        models.clear()
    }
}
