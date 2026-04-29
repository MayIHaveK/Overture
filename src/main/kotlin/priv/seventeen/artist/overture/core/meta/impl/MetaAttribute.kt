package priv.seventeen.artist.overture.core.meta.impl

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.meta.ItemMeta
import priv.seventeen.artist.asteroid.AsteroidAPI
import priv.seventeen.artist.asteroid.item.ItemTag
import priv.seventeen.artist.overture.core.item.ItemSignal
import priv.seventeen.artist.overture.core.meta.Meta

/**
 * 属性修饰符 Meta
 *
 * 配置格式:
 * ```yaml
 * attribute:
 *   mainhand:
 *     generic_attack_damage: "+7"
 *     generic_attack_speed: "+10%"
 *   offhand:
 *     generic_armor: "+2~5"
 * ```
 *
 * 支持:
 * - "+n" 固定加成 (operation=0, ADD_VALUE)
 * - "+n%" 百分比加成 (operation=1, ADD_MULTIPLIED_BASE)
 * - "+min~max" 区间随机
 */
@priv.seventeen.artist.overture.core.meta.MetaKey("attribute")
class MetaAttribute(
    private val section: ConfigurationSection?,
    override var locked: Boolean = false
) : Meta() {

    override val key: String = "attribute"

    private val modifiers: List<AttributeEntry> = parseModifiers()

    override fun buildMeta(itemMeta: ItemMeta) {
        val nms = AsteroidAPI.getAttributeItemNMS()
        for (entry in modifiers) {
            try {
                nms.addModifier(
                    itemMeta,
                    entry.attribute,
                    "overture.${entry.attribute}",
                    entry.computeAmount(),
                    entry.operation,
                    entry.slot
                )
            } catch (_: Exception) {
            }
        }
    }

    override fun dropMeta(itemMeta: ItemMeta) {
        val nms = AsteroidAPI.getAttributeItemNMS()
        for (entry in modifiers) {
            try {
                nms.removeModifier(itemMeta, entry.attribute)
            } catch (_: Exception) {
            }
        }
    }

    private fun parseModifiers(): List<AttributeEntry> {
        val result = mutableListOf<AttributeEntry>()
        section ?: return result

        for (slotKey in section.getKeys(false)) {
            val slotSection = section.getConfigurationSection(slotKey) ?: continue
            val slot = normalizeSlot(slotKey)

            for (attrKey in slotSection.getKeys(false)) {
                val valueStr = slotSection.getString(attrKey) ?: continue
                val entry = parseAttributeValue(attrKey, valueStr, slot)
                if (entry != null) {
                    result.add(entry)
                }
            }
        }
        return result
    }

    private fun parseAttributeValue(attribute: String, value: String, slot: String?): AttributeEntry? {
        val cleanValue = value.removePrefix("+")

        // 百分比: "10%"  → operation=1 (ADD_MULTIPLIED_BASE)
        if (cleanValue.endsWith("%")) {
            val num = cleanValue.removeSuffix("%").toDoubleOrNull() ?: return null
            return AttributeEntry(attribute, num / 100.0, 1, slot)
        }

        // 区间随机: "2~5" → operation=0 (ADD_VALUE)
        if (cleanValue.contains("~")) {
            val parts = cleanValue.split("~")
            if (parts.size == 2) {
                val min = parts[0].toDoubleOrNull() ?: return null
                val max = parts[1].toDoubleOrNull() ?: return null
                return AttributeEntry(attribute, min, 0, slot, max)
            }
        }

        // 固定值: "7" → operation=0 (ADD_VALUE)
        val num = cleanValue.toDoubleOrNull() ?: return null
        return AttributeEntry(attribute, num, 0, slot)
    }

    /**
     * 标准化槽位名称，与 Asteroid IAttributeItemNMS 约定一致
     */
    private fun normalizeSlot(slot: String): String? {
        return when (slot.lowercase()) {
            "mainhand", "main_hand" -> "hand"
            "offhand", "off_hand" -> "offhand"
            "head", "helmet" -> "head"
            "chest", "chestplate" -> "chest"
            "legs", "leggings" -> "legs"
            "feet", "boots" -> "feet"
            "any", "all" -> null  // null = 所有槽位
            else -> null
        }
    }

    data class AttributeEntry(
        val attribute: String,
        val amount: Double,
        /** 0=ADD_VALUE, 1=ADD_MULTIPLIED_BASE, 2=ADD_MULTIPLIED_TOTAL */
        val operation: Int,
        val slot: String?,
        val maxAmount: Double? = null
    ) {
        fun computeAmount(): Double {
            return if (maxAmount != null) {
                amount + Math.random() * (maxAmount - amount)
            } else {
                amount
            }
        }
    }
}
