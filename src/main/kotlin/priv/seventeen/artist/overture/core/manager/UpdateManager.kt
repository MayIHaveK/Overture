package priv.seventeen.artist.overture.core.manager

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import priv.seventeen.artist.blink.BlinkLog
import priv.seventeen.artist.overture.api.event.ItemUpdateEvent
import priv.seventeen.artist.overture.core.item.ItemSignal
import priv.seventeen.artist.overture.core.item.ItemStream

/**
 * 更新管理器
 * 负责检测和执行物品自动更新（主线程）
 */
object UpdateManager {

    /**
     * 检查并更新单个物品
     * @return 更新后的 ItemStack，如果不需要更新则返回 null
     */
    fun checkUpdate(player: Player, itemStack: ItemStack): ItemStack? {
        val stream = ItemStream(itemStack)
        if (!stream.isOverture) return null

        val itemId = stream.overtureId ?: return null
        val item = ItemManager.getItem(itemId) ?: return null

        if (!stream.isOutdated(item)) return null

        // 触发更新事件
        val event = ItemUpdateEvent(player, itemStack, item)
        org.bukkit.Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) return null

        // 执行更新
        stream.signals.add(ItemSignal.UPDATE_CHECKED)
        val updatedStream = item.build(player, stream)
        return updatedStream.toItemStack(player)
    }

    /**
     * 检查玩家背包中所有物品
     */
    fun checkInventory(player: Player) {
        val inventory = player.inventory
        for (i in 0 until inventory.size) {
            val item = inventory.getItem(i) ?: continue
            if (item.type.isAir) continue
            val updated = checkUpdate(player, item)
            if (updated != null) {
                inventory.setItem(i, updated)
            }
        }
    }
}
