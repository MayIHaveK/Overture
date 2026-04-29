package priv.seventeen.artist.overture.core.display

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import priv.seventeen.artist.overture.core.item.ItemStream

/**
 * 条件展示
 * 根据 Aria 表达式判断结果选择不同的展示方案
 */
class ConditionalDisplay(
    val id: String,
    val config: ConfigurationSection
) {

    /** 条件列表 */
    val conditions: List<DisplayCondition> = loadConditions()

    /** 默认展示方案 ID */
    val defaultDisplay: String? = config.getString("default")

    /**
     * 评估条件，返回应使用的展示方案 ID
     * @param player 当前玩家
     * @param stream 物品流
     * @param evaluator Aria 表达式求值器
     */
    fun evaluate(
        player: Player?,
        stream: ItemStream,
        evaluator: (String, Player?, ItemStream) -> Boolean
    ): String? {
        for (condition in conditions) {
            if (evaluator(condition.expression, player, stream)) {
                return condition.displayId
            }
        }
        return defaultDisplay
    }

    private fun loadConditions(): List<DisplayCondition> {
        val list = config.getMapList("conditions")
        return list.mapNotNull { map ->
            val condition = map["condition"]?.toString() ?: return@mapNotNull null
            val display = map["display"]?.toString() ?: return@mapNotNull null
            DisplayCondition(condition, display)
        }
    }

    data class DisplayCondition(
        val expression: String,
        val displayId: String
    )
}
