package priv.seventeen.artist.overture.api.event

import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.inventory.ItemStack
import priv.seventeen.artist.overture.core.item.OvertureItem

/**
 * 物品更新事件（可取消）
 */
class ItemUpdateEvent(
    val player: Player,
    val itemStack: ItemStack,
    val item: OvertureItem
) : Event(), Cancellable {

    private var cancelled = false

    override fun isCancelled(): Boolean = cancelled
    override fun setCancelled(cancel: Boolean) { cancelled = cancel }
    override fun getHandlers(): HandlerList = handlerList

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}
