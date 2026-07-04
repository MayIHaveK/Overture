package priv.seventeen.artist.overture.api

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import priv.seventeen.artist.blink.bukkitPlugin
import priv.seventeen.artist.overture.core.item.ItemStream
import priv.seventeen.artist.overture.core.item.ItemSerializer
import priv.seventeen.artist.overture.core.item.OvertureItem
import priv.seventeen.artist.overture.core.manager.ItemManager
import priv.seventeen.artist.overture.core.manager.RarityGlowManager
import java.io.File

/**
 * Overture 公共 API 门面
 */
object OvertureAPI {

    /**
     * 获取物品定义
     */
    fun getItem(id: String): OvertureItem? = ItemManager.getItem(id)

    /**
     * 获取所有物品
     */
    fun getItems(): Map<String, OvertureItem> = ItemManager.getItems()

    /**
     * 获取所有物品 ID
     */
    fun getItemIds(): List<String> = ItemManager.getItemIds()

    /**
     * 生成物品
     */
    fun generateItem(id: String, player: Player? = null): ItemStack? = ItemManager.generate(id, player)

    /**
     * 从 ItemStack 读取物品流
     */
    fun readStream(item: ItemStack): ItemStream = ItemManager.read(item)

    /**
     * 判断是否为 Overture 物品
     */
    fun isOvertureItem(item: ItemStack): Boolean = ItemManager.isOvertureItem(item)

    /**
     * 获取物品 ID
     */
    fun getOvertureId(item: ItemStack): String? = ItemManager.getOvertureId(item)

    /**
     * 序列化物品为 JSON
     */
    fun serialize(item: ItemStack): String = ItemSerializer.serialize(item)

    /**
     * 从 JSON 反序列化物品
     */
    fun deserialize(json: String): ItemStack? = ItemSerializer.deserialize(json)

    /**
     * 注册物品提供者
     */
    fun registerProvider(provider: ItemProvider) = ItemManager.registerProvider(provider)

    /**
     * 重载所有配置
     */
    fun reload() {
        ItemManager.reload()
        RarityGlowManager.load(File(bukkitPlugin.dataFolder, "rarity.yml"))
    }
}
