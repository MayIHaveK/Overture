package priv.seventeen.artist.overture.core.meta.impl

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import priv.seventeen.artist.overture.core.meta.Meta
import priv.seventeen.artist.overture.core.meta.MetaKey
import priv.seventeen.artist.overture.hook.ArcartXHook

/**
 * 掉落物附加模型 Meta（需要 ArcartX）
 *
 * 为物品指定在地面掉落时显示的附加 3D 模型路径，
 * 由 ArcartX 客户端渲染，服务端无 ArcartX 时此 Meta 静默无效。
 *
 * 配置格式:
 * ```yaml
 * meta:
 *   drop: "weapons/legendary_blade"
 *   # 或锁定
 *   drop!!: "weapons/legendary_blade"
 * ```
 */
@MetaKey("drop")
class MetaDrop(
    private val value: Any?,
    override var locked: Boolean = false
) : Meta() {

    override val key: String = "drop"

    /** 模型路径，为空则不生效 */
    val path: String = value?.toString()?.trim() ?: ""

    override fun buildRelease(itemStack: ItemStack, itemMeta: ItemMeta) {
        if (path.isBlank()) return
        ArcartXHook.setDrop(itemStack, path)
    }
}
