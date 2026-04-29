package priv.seventeen.artist.overture.core.display

import org.bukkit.configuration.ConfigurationSection
import priv.seventeen.artist.overture.util.ColorUtil

/**
 * 展示方案
 * 定义物品名称和描述的模板结构
 */
class Display(
    /** 展示方案 ID */
    val id: String,
    /** 原始配置 */
    val config: ConfigurationSection
) {

    /** 名称模板 */
    val structureName: StructureSingle = StructureSingle(config.getString("name")?.let { ColorUtil.colored(it) })

    /** 描述模板 */
    val structureLore: StructureList = StructureList(
        config.getStringList("lore").map { ColorUtil.colored(it) }
    )

    /**
     * 构建展示产物
     * @param nameVars 名称变量
     * @param loreVars 描述变量
     */
    fun build(nameVars: Map<String, String>, loreVars: Map<String, List<String>>): DisplayProduct {
        val name = structureName.build(nameVars)
        // 创建可变副本用于展开消费
        val mutableLoreVars = loreVars.mapValues { it.value.toMutableList() }.toMutableMap()
        val lore = structureLore.build(mutableLoreVars)
        return DisplayProduct(name, lore)
    }

    override fun toString(): String = "Display(id=$id)"
}
