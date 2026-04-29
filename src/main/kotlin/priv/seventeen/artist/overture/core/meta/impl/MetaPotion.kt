package priv.seventeen.artist.overture.core.meta.impl

import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import priv.seventeen.artist.overture.core.meta.Meta
import priv.seventeen.artist.overture.core.meta.MetaKey

/**
 * 药水效果 Meta
 *
 * 配置格式:
 * ```yaml
 * potion:
 *   - "SPEED,100,1"
 *   - "REGENERATION,200,2"
 * ```
 */
@MetaKey("potion")
class MetaPotion(
    private val value: Any?,
    override var locked: Boolean = false
) : Meta() {

    override val key: String = "potion"

    val effects: List<PotionEffect> = parseEffects()

    override fun buildMeta(itemMeta: ItemMeta) {
        if (itemMeta !is PotionMeta) return
        for (effect in effects) {
            itemMeta.addCustomEffect(effect, true)
        }
    }

    override fun dropMeta(itemMeta: ItemMeta) {
        if (itemMeta !is PotionMeta) return
        for (effect in effects) {
            itemMeta.removeCustomEffect(effect.type)
        }
    }

    private fun parseEffects(): List<PotionEffect> {
        val list = when (value) {
            is List<*> -> value.filterIsInstance<String>()
            is String -> listOf(value)
            else -> return emptyList()
        }
        return list.mapNotNull { str ->
            val parts = str.split(",").map { it.trim() }
            if (parts.size < 2) return@mapNotNull null
            val type = resolvePotionType(parts[0]) ?: return@mapNotNull null
            val duration = parts[1].toIntOrNull() ?: return@mapNotNull null
            val amplifier = parts.getOrNull(2)?.toIntOrNull() ?: 0
            PotionEffect(type, duration, amplifier)
        }
    }

    @Suppress("DEPRECATION")
    private fun resolvePotionType(name: String): PotionEffectType? {
        // getByName 在所有版本都可用
        return PotionEffectType.getByName(name.uppercase())
    }
}
