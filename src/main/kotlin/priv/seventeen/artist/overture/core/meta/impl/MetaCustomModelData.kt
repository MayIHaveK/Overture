package priv.seventeen.artist.overture.core.meta.impl

import org.bukkit.inventory.meta.ItemMeta
import priv.seventeen.artist.overture.core.meta.Meta
import priv.seventeen.artist.overture.core.meta.MetaKey

/**
 * 自定义模型数据 Meta
 *
 * 配置格式:
 * ```yaml
 * custom_model_data: 10001
 * ```
 */
@MetaKey("custom_model_data")
class MetaCustomModelData(
    private val value: Any?,
    override var locked: Boolean = false
) : Meta() {

    override val key: String = "custom_model_data"

    val modelData: Int = (value as? Number)?.toInt() ?: value?.toString()?.toIntOrNull() ?: 0

    override fun buildMeta(itemMeta: ItemMeta) {
        if (modelData > 0) {
            itemMeta.setCustomModelData(modelData)
        }
    }

    override fun dropMeta(itemMeta: ItemMeta) {
        itemMeta.setCustomModelData(null)
    }
}
