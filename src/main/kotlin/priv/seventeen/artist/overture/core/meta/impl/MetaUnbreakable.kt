package priv.seventeen.artist.overture.core.meta.impl

import org.bukkit.inventory.meta.ItemMeta
import priv.seventeen.artist.overture.core.meta.Meta
import priv.seventeen.artist.overture.core.meta.MetaKey

/**
 * 不可破坏 Meta
 *
 * 配置格式:
 * ```yaml
 * unbreakable: true
 * ```
 */
@MetaKey("unbreakable")
class MetaUnbreakable(
    private val value: Any?,
    override var locked: Boolean = false
) : Meta() {

    override val key: String = "unbreakable"

    val enabled: Boolean = value == true || value?.toString() == "true"

    override fun buildMeta(itemMeta: ItemMeta) {
        if (enabled) {
            itemMeta.isUnbreakable = true
        }
    }

    override fun dropMeta(itemMeta: ItemMeta) {
        itemMeta.isUnbreakable = false
    }
}
