package priv.seventeen.artist.overture.core.meta.impl

import org.bukkit.inventory.meta.ItemMeta
import priv.seventeen.artist.asteroid.AsteroidAPI
import priv.seventeen.artist.overture.core.meta.Meta
import priv.seventeen.artist.overture.core.meta.MetaKey

/**
 * 发光效果 Meta
 *
 * 配置格式:
 * ```yaml
 * shiny: true
 * ```
 */
@MetaKey("shiny")
class MetaShiny(
    private val value: Any?,
    override var locked: Boolean = false
) : Meta() {

    override val key: String = "shiny"

    val enabled: Boolean = value == true || value?.toString() == "true"

    override fun buildMeta(itemMeta: ItemMeta) {
        if (!enabled) return
        AsteroidAPI.getGlintNMS().setGlint(itemMeta, true)
    }

    override fun dropMeta(itemMeta: ItemMeta) {
        AsteroidAPI.getGlintNMS().removeGlint(itemMeta)
    }
}
