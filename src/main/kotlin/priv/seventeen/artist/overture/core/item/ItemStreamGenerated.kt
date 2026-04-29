package priv.seventeen.artist.overture.core.item

import org.bukkit.inventory.ItemStack

/**
 * 首次生成的物品流
 * 携带名称和描述变量映射，用于 Display 构建
 */
class ItemStreamGenerated(
    sourceItem: ItemStack,
    /** 名称变量 Map<变量名, 值> */
    val nameVars: MutableMap<String, String>,
    /** 描述变量 Map<变量名, 值列表> */
    val loreVars: MutableMap<String, MutableList<String>>
) : ItemStream(sourceItem) {

    /**
     * 添加名称变量
     */
    fun addName(key: String, value: String) {
        nameVars[key] = value
    }

    /**
     * 添加描述变量（单值）
     */
    fun addLore(key: String, value: String) {
        loreVars.getOrPut(key) { mutableListOf() }.also {
            it.clear()
            it.add(value)
        }
    }

    /**
     * 添加描述变量（多值）
     */
    fun addLore(key: String, values: List<String>) {
        loreVars[key] = values.toMutableList()
    }

    /**
     * 同时添加到名称和描述变量
     */
    fun addVariable(key: String, value: String) {
        addName(key, value)
        addLore(key, value)
    }

    /**
     * 同时添加到名称和描述变量（多值）
     */
    fun addVariable(key: String, values: List<String>) {
        addName(key, values.firstOrNull() ?: "")
        addLore(key, values)
    }
}
