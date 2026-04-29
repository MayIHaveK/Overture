package priv.seventeen.artist.overture.core.group

import priv.seventeen.artist.overture.core.item.OvertureItem

/**
 * 物品分组
 * 支持层级结构，文件夹即分组
 */
class ItemGroup(
    /** 分组名称 */
    val name: String,
    /** 父分组 */
    val parent: ItemGroup? = null,
    /** 层级深度 */
    val level: Int = 0,
    /** 排序优先级 */
    val priority: Int = 0
) {

    /** 子分组 */
    val children: MutableList<ItemGroup> = mutableListOf()

    /** 分组路径 */
    val path: String
        get() = if (parent != null) "${parent.path}/$name" else name

    /**
     * 获取该分组下的所有物品
     */
    fun getItems(allItems: Map<String, OvertureItem>): List<OvertureItem> {
        return allItems.values.filter { it.group == this }
    }

    /**
     * 获取直接子分组
     */
    fun getSubGroups(): List<ItemGroup> {
        return children.sortedBy { it.priority }
    }

    override fun toString(): String = "ItemGroup(path=$path)"
    override fun equals(other: Any?): Boolean = other is ItemGroup && path == other.path
    override fun hashCode(): Int = path.hashCode()

    companion object {
        /** 无分组（根级别物品） */
        val NO_GROUP = ItemGroup("", null, 0)
    }
}
