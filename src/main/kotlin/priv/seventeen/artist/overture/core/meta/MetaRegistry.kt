package priv.seventeen.artist.overture.core.meta

import org.bukkit.configuration.ConfigurationSection
import priv.seventeen.artist.overture.core.meta.impl.*

/**
 * Meta 注册表
 * 管理所有 Meta 类型的注册和创建
 */
object MetaRegistry {

    private val factories = mutableMapOf<String, MetaFactory>()

    init {
        // 注册所有内置 Meta
        register("attribute") { section, _, locked -> MetaAttribute(section, locked) }
        register("enchantment") { section, _, locked -> MetaEnchantment(section, locked) }
        register("unique") { _, value, locked -> MetaUnique(value, locked) }
        register("durability") { section, _, locked -> MetaDurability(section, locked) }
        register("unbreakable") { _, value, locked -> MetaUnbreakable(value, locked) }
        register("custom_model_data") { _, value, locked -> MetaCustomModelData(value, locked) }
        register("item_flag") { _, value, locked -> MetaItemFlag(value, locked) }
        register("color") { _, value, locked -> MetaColor(value, locked) }
        register("skull") { _, value, locked -> MetaSkull(value, locked) }
        register("potion") { _, value, locked -> MetaPotion(value, locked) }
        register("native") { section, _, locked -> MetaNative(section, locked) }
        register("shiny") { _, value, locked -> MetaShiny(value, locked) }
    }

    /**
     * 注册 Meta 工厂
     */
    fun register(key: String, factory: MetaFactory) {
        factories[key] = factory
    }

    /**
     * 创建 Meta 实例
     */
    fun create(key: String, section: ConfigurationSection?, value: Any?, locked: Boolean): Meta? {
        val factory = factories[key] ?: return null
        return try {
            factory.create(section, value, locked)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 获取所有已注册的 Meta 键
     */
    fun getRegisteredKeys(): Set<String> = factories.keys.toSet()

    fun interface MetaFactory {
        fun create(section: ConfigurationSection?, value: Any?, locked: Boolean): Meta
    }
}
