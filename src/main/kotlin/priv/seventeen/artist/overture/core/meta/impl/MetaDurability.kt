package priv.seventeen.artist.overture.core.meta.impl

import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.ItemMeta
import priv.seventeen.artist.asteroid.item.ItemTag
import priv.seventeen.artist.asteroid.item.ItemTagData
import priv.seventeen.artist.overture.core.item.ItemSignal
import priv.seventeen.artist.overture.core.meta.Meta
import priv.seventeen.artist.overture.core.meta.MetaKey

/**
 * 自定义耐久 Meta
 *
 * 配置格式:
 * ```yaml
 * durability:
 *   max: 100
 *   remains: STICK
 *   synchronous: true
 *   display: "&8[ &f%symbol% &8]"
 *   display-symbol:
 *     full: "◆"
 *     empty: "◇"
 * ```
 */
@MetaKey("durability")
class MetaDurability(
    private val section: ConfigurationSection?,
    override var locked: Boolean = false
) : Meta() {

    override val key: String = "durability"
    override val priority: Int = -10  // 优先构建

    /** 最大耐久 */
    val max: Int = section?.getInt("max", 100) ?: 100

    /** 残骸物品 */
    val remains: String? = section?.getString("remains")

    /** 是否同步原版耐久条 */
    val synchronous: Boolean = section?.getBoolean("synchronous", true) ?: true

    /** 自定义耐久条样式 */
    val displayFormat: String? = section?.getString("display")

    /** 满格符号 */
    val symbolFull: String = section?.getString("display-symbol.full", "◆") ?: "◆"

    /** 空格符号 */
    val symbolEmpty: String = section?.getString("display-symbol.empty", "◇") ?: "◇"

    override fun build(player: Player?, compound: ItemTag, sourceTag: ItemTag, signals: Set<ItemSignal>) {
        // 写入最大耐久到 data
        val dataTag = compound.getCompound("data")
        if (!dataTag.containsKey("durability")) {
            dataTag.putInt("durability", max)
            compound.putCompound("data", dataTag)
        }
        // 初始化当前耐久
        if (!dataTag.containsKey("durability_current")) {
            dataTag.putInt("durability_current", max)
            compound.putCompound("data", dataTag)
        }
    }

    override fun buildRelease(itemStack: ItemStack, itemMeta: ItemMeta) {
        if (!synchronous) return
        if (itemMeta !is Damageable) return

        val tag = ItemTag.fromItemStack(itemStack)
        val root = tag.getCompound("overture")
        val data = root.getCompound("data")
        val current = data.getInt("durability_current")
        val maxDur = data.getInt("durability")

        if (maxDur > 0) {
            val percent = current.toDouble() / maxDur.toDouble()
            val maxItemDur = itemStack.type.maxDurability.toInt()
            if (maxItemDur > 0) {
                itemMeta.damage = maxItemDur - (maxItemDur * percent).toInt()
            }
        }
    }

    /**
     * 获取残骸 ItemStack
     */
    fun getRemainsItem(): ItemStack? {
        val remainsStr = remains ?: return null
        val material = Material.matchMaterial(remainsStr.uppercase()) ?: return null
        return ItemStack(material)
    }

    companion object {
        const val DATA_KEY_CURRENT = "durability_current"
        const val DATA_KEY_MAX = "durability"
    }
}
