package priv.seventeen.artist.overture.core.meta.impl

import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.meta.ItemMeta
import priv.seventeen.artist.overture.core.meta.Meta
import priv.seventeen.artist.overture.core.meta.MetaKey

/**
 * 物品标志 Meta
 *
 * 配置格式:
 * ```yaml
 * item_flag:
 *   - HIDE_ATTRIBUTES
 *   - HIDE_ENCHANTS
 * ```
 */
@MetaKey("item_flag")
class MetaItemFlag(
    private val value: Any?,
    override var locked: Boolean = false
) : Meta() {

    override val key: String = "item_flag"

    val flags: List<ItemFlag> = parseFlags()

    override fun buildMeta(itemMeta: ItemMeta) {
        flags.forEach { itemMeta.addItemFlags(it) }
    }

    override fun dropMeta(itemMeta: ItemMeta) {
        flags.forEach { itemMeta.removeItemFlags(it) }
    }

    private fun parseFlags(): List<ItemFlag> {
        val list = when (value) {
            is List<*> -> value.filterIsInstance<String>()
            is String -> listOf(value)
            else -> return emptyList()
        }
        return list.mapNotNull { name ->
            try {
                ItemFlag.valueOf(name.uppercase())
            } catch (_: Exception) {
                null
            }
        }
    }
}
