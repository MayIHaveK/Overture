package priv.seventeen.artist.overture.core.item

/**
 * NBT 键常量
 * 物品 NBT 中 overture 根节点下的键定义
 */
object ItemKey {

    /** 根节点名 */
    const val ROOT = "overture"

    /** 物品 ID */
    const val ID = "id"

    /** 版本签名 (SHA-1) */
    const val VERSION = "version"

    /** 活跃数据 (Compound) */
    const val DATA = "data"

    /** 唯一数据 (Compound) */
    const val UNIQUE = "unique"

    /** Meta 历史记录 (List<String>) */
    const val META_HISTORY = "meta-history"

    // unique 子键
    const val UNIQUE_UUID = "uuid"
    const val UNIQUE_PLAYER = "player"
    const val UNIQUE_DATE = "date"
    const val UNIQUE_DATE_FORMATTED = "date-formatted"

    /** 获取完整路径 */
    fun path(key: String): String = "$ROOT.$key"
}
