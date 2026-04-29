package priv.seventeen.artist.overture.api

import priv.seventeen.artist.overture.core.item.OvertureItem

/**
 * 物品提供者接口
 * 支持多来源物品加载
 */
interface ItemProvider {

    /** 提供者标识 */
    val id: String

    /** 优先级（值越小越先加载） */
    val priority: Int

    /**
     * 加载物品
     * @return 物品 ID → 物品定义 映射
     */
    fun load(): Map<String, OvertureItem>

    /**
     * 重载
     */
    fun reload()
}
