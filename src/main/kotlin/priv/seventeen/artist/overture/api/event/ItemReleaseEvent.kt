package priv.seventeen.artist.overture.api.event

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import priv.seventeen.artist.overture.core.item.ItemStream

/**
 * 物品释放事件
 */
object ItemReleaseEvent {

    /**
     * 物品释放为 ItemStack 时触发
     */
    class Release(
        val player: Player?,
        val stream: ItemStream,
        val itemMeta: ItemMeta
    ) : Event() {

        override fun getHandlers(): HandlerList = handlerList

        companion object {
            @JvmStatic
            val handlerList = HandlerList()
        }
    }

    /**
     * 展示方案选择事件
     */
    class SelectDisplay(
        val player: Player?,
        val stream: ItemStream,
        var displayId: String?
    ) : Event() {

        override fun getHandlers(): HandlerList = handlerList

        companion object {
            @JvmStatic
            val handlerList = HandlerList()
        }
    }

    /**
     * 展示变量生成事件（data-mapper 注入）
     */
    class Display(
        val player: Player?,
        val stream: ItemStream,
        val nameVars: MutableMap<String, String>,
        val loreVars: MutableMap<String, MutableList<String>>
    ) : Event() {

        override fun getHandlers(): HandlerList = handlerList

        companion object {
            @JvmStatic
            val handlerList = HandlerList()
        }
    }

    /**
     * 最终修改事件
     */
    class Final(
        val player: Player?,
        val stream: ItemStream,
        var itemStack: ItemStack
    ) : Event() {

        override fun getHandlers(): HandlerList = handlerList

        companion object {
            @JvmStatic
            val handlerList = HandlerList()
        }
    }
}
