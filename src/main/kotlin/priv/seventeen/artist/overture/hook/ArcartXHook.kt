package priv.seventeen.artist.overture.hook

import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import priv.seventeen.artist.arcartx.util.ItemStackUtils.setDrop
import priv.seventeen.artist.blink.BlinkLog

/**
 * ArcartX 可选依赖钩子
 *
 * 在插件启动时检测一次 ArcartX 是否存在，后续所有模块通过此对象判断，
 * 避免每次调用都重复检测，也避免硬依赖导致无 ArcartX 时崩溃。
 */
object ArcartXHook {

    /** ArcartX 是否已加载 */
    val enabled: Boolean by lazy {
        Bukkit.getPluginManager().isPluginEnabled("ArcartX").also { found ->
            if (found) BlinkLog.info("检测到 ArcartX，已启用附加功能")
        }
    }

    /**
     * 设置掉落物附加模型。
     * 仅在 ArcartX 存在时调用，否则静默跳过。
     */
    fun setDrop(item: ItemStack, path: String) {
        if (!enabled) return
        item.setDrop(path)
    }
}
