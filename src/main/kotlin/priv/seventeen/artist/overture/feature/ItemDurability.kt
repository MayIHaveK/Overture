package priv.seventeen.artist.overture.feature

import org.bukkit.entity.Player
import priv.seventeen.artist.blink.event.AutoListener
import priv.seventeen.artist.overture.api.event.ItemReleaseEvent
import priv.seventeen.artist.overture.core.item.ItemStream
import priv.seventeen.artist.overture.core.manager.ItemManager
import priv.seventeen.artist.overture.core.meta.impl.MetaDurability
import priv.seventeen.artist.overture.util.ColorUtil

/**
 * 自定义耐久特性
 * 负责耐久条展示和同步原版耐久条
 */
object ItemDurability {

    /** 全局耐久条样式 */
    var displayFormat: String = "&8[ &f%symbol% &8]"
    var symbolFull: String = "◆"
    var symbolEmpty: String = "◇"
    var scale: Int = 20

    @AutoListener
    fun onReleaseDisplay(event: ItemReleaseEvent.Display) {
        val stream = event.stream
        if (!stream.isOverture) return

        val itemDef = ItemManager.getItem(stream.overtureId ?: return) ?: return
        val durMeta = itemDef.metaList.filterIsInstance<MetaDurability>().firstOrNull() ?: return

        val data = stream.overtureData
        val current = data.getInt("durability_current")
        val max = data.getInt("durability")

        if (max <= 0) return

        // 生成耐久条
        val bar = createBar(current, max, durMeta)
        val barText = ColorUtil.colored(bar)

        // 注入到展示变量
        event.nameVars["durability"] = barText
        event.nameVars["DURABILITY"] = barText
        event.loreVars["durability"] = mutableListOf(barText)
        event.loreVars["DURABILITY"] = mutableListOf(barText)

        // 额外变量
        event.nameVars["durability_current"] = current.toString()
        event.nameVars["durability_max"] = max.toString()
        event.loreVars["durability_current"] = mutableListOf(current.toString())
        event.loreVars["durability_max"] = mutableListOf(max.toString())
    }

    /**
     * 创建耐久条
     */
    private fun createBar(current: Int, max: Int, meta: MetaDurability): String {
        val format = meta.displayFormat ?: displayFormat
        val full = meta.symbolFull
        val empty = meta.symbolEmpty

        val filled = if (max > 0) (current.toDouble() / max * scale).toInt().coerceIn(0, scale) else 0
        val sb = StringBuilder()
        for (i in 1..scale) {
            sb.append(if (i <= filled) full else empty)
        }

        return format
            .replace("%symbol%", sb.toString())
            .replace("%current%", current.toString())
            .replace("%max%", max.toString())
            .replace("%percent%", if (max > 0) "${(current * 100 / max)}%" else "0%")
    }
}
