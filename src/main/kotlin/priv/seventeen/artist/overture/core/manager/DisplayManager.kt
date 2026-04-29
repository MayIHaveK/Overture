package priv.seventeen.artist.overture.core.manager

import org.bukkit.configuration.ConfigurationSection
import priv.seventeen.artist.blink.BlinkLog
import priv.seventeen.artist.overture.core.display.ConditionalDisplay
import priv.seventeen.artist.overture.core.display.Display

/**
 * 展示方案管理器
 */
object DisplayManager {

    /** 已注册的展示方案 */
    private val displays = mutableMapOf<String, Display>()

    /** 已注册的条件展示 */
    private val conditionalDisplays = mutableMapOf<String, ConditionalDisplay>()

    /**
     * 注册展示方案
     */
    fun register(display: Display) {
        displays[display.id] = display
    }

    /**
     * 注册条件展示
     */
    fun registerConditional(display: ConditionalDisplay) {
        conditionalDisplays[display.id] = display
    }

    /**
     * 获取展示方案
     */
    fun getDisplay(id: String): Display? = displays[id]

    /**
     * 获取条件展示
     */
    fun getConditionalDisplay(id: String): ConditionalDisplay? = conditionalDisplays[id]

    /**
     * 解析展示方案（优先条件展示，其次普通展示）
     */
    fun resolve(id: String): Any? {
        return conditionalDisplays[id] ?: displays[id]
    }

    /**
     * 从配置节加载展示方案
     */
    fun loadFromSection(id: String, section: ConfigurationSection) {
        if (section.contains("conditions")) {
            registerConditional(ConditionalDisplay(id, section))
        } else {
            register(Display(id, section))
        }
    }

    /**
     * 重载
     */
    fun reload() {
        displays.clear()
        conditionalDisplays.clear()
    }

    /**
     * 获取所有展示方案 ID
     */
    fun getDisplayIds(): List<String> = (displays.keys + conditionalDisplays.keys).toList()

    fun getDisplayCount(): Int = displays.size + conditionalDisplays.size
}
