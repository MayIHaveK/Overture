package priv.seventeen.artist.overture.core.item

/**
 * 物品信号枚举
 * 用于在物品构建/更新流程中传递状态信息
 */
enum class ItemSignal {

    /**
     * 物品经过了更新检查
     * 影响 Meta build 逻辑：仅 locked Meta 在此信号下重新 build
     */
    UPDATE_CHECKED,

    /**
     * 耐久值发生变化
     * 脚本执行完毕后如果存在此信号则触发 rebuild
     */
    DURABILITY_CHANGED,

    /**
     * 物品已损坏
     * rebuildToItemStack 中检测到此信号会跳过 rebuild
     */
    DURABILITY_DESTROYED
}
