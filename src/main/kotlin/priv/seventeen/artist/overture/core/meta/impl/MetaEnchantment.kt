package priv.seventeen.artist.overture.core.meta.impl

import org.bukkit.NamespacedKey
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.meta.ItemMeta
import priv.seventeen.artist.overture.core.meta.Meta
import priv.seventeen.artist.overture.core.meta.MetaKey

/**
 * 附魔 Meta
 *
 * 配置格式:
 * ```yaml
 * enchantment:
 *   sharpness: 3
 *   unbreaking: 2
 * ```
 */
@MetaKey("enchantment")
class MetaEnchantment(
    private val section: ConfigurationSection?,
    override var locked: Boolean = false
) : Meta() {

    override val key: String = "enchantment"

    private val enchantments: Map<Enchantment, Int> = parseEnchantments()

    override fun buildMeta(itemMeta: ItemMeta) {
        for ((enchant, level) in enchantments) {
            itemMeta.addEnchant(enchant, level, true)
        }
    }

    override fun dropMeta(itemMeta: ItemMeta) {
        for ((enchant, _) in enchantments) {
            itemMeta.removeEnchant(enchant)
        }
    }

    @Suppress("DEPRECATION")
    private fun parseEnchantments(): Map<Enchantment, Int> {
        val result = mutableMapOf<Enchantment, Int>()
        section ?: return result

        for (key in section.getKeys(false)) {
            val level = section.getInt(key, 1)
            val enchant = resolveEnchantment(key)
            if (enchant != null) {
                result[enchant] = level
            }
        }
        return result
    }

    @Suppress("DEPRECATION")
    private fun resolveEnchantment(name: String): Enchantment? {
        // 优先尝试 NamespacedKey
        return try {
            Enchantment.getByKey(NamespacedKey.minecraft(name.lowercase()))
        } catch (_: Exception) {
            // 回退到旧 API
            try {
                Enchantment.getByName(name.uppercase())
            } catch (_: Exception) {
                null
            }
        }
    }
}
