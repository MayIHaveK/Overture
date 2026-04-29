package priv.seventeen.artist.overture.core.meta.impl

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import priv.seventeen.artist.asteroid.item.ItemTag
import priv.seventeen.artist.overture.core.item.ItemSignal
import priv.seventeen.artist.overture.core.meta.Meta
import priv.seventeen.artist.overture.core.meta.MetaKey
import priv.seventeen.artist.overture.util.Translator

/**
 * 原生 NBT 直写 Meta
 * 将配置中的数据直接写入物品 NBT
 *
 * 配置格式:
 * ```yaml
 * native:
 *   CustomTag: "hello"
 *   nested:
 *     key: 42
 * ```
 */
@MetaKey("native")
class MetaNative(
    private val section: ConfigurationSection?,
    override var locked: Boolean = false
) : Meta() {

    override val key: String = "native"

    override fun build(player: Player?, compound: ItemTag, sourceTag: ItemTag, signals: Set<ItemSignal>) {
        section ?: return
        val nativeTag = Translator.toItemTag(section)
        // 直接写入物品 NBT 根节点（不在 overture 节点下）
        for ((k, v) in nativeTag) {
            sourceTag.put(k, v)
        }
    }

    override fun drop(player: Player?, compound: ItemTag) {
        section ?: return
        // 注意：drop 时也应从 sourceTag 删除，但当前签名只有 compound
        // 这里从 compound 的父级（无法直接访问）删除，暂时标记为已知限制
        for (key in section.getKeys(false)) {
            compound.remove(key.removeSuffix("!!"))
        }
    }
}
