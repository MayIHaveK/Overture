package priv.seventeen.artist.overture.core.action

/**
 * 动作触发器枚举
 */
enum class ActionTrigger(val key: String) {
    ON_LEFT_CLICK("on_left_click"),
    ON_RIGHT_CLICK("on_right_click"),
    ON_RIGHT_CLICK_ENTITY("on_right_click_entity"),
    ON_ATTACK("on_attack"),
    ON_DAMAGE("on_damage"),
    ON_CONSUME("on_consume"),
    ON_DROP("on_drop"),
    ON_PICK("on_pick"),
    ON_BLOCK_BREAK("on_block_break"),
    ON_ITEM_BREAK("on_item_break"),
    ON_SWAP_TO_OFFHAND("on_swap_to_offhand"),
    ON_SWAP_TO_MAINHAND("on_swap_to_mainhand"),
    ON_BUILD("on_build"),
    ON_RELEASE("on_release"),
    ON_RELEASE_DISPLAY("on_release_display");

    companion object {
        private val keyMap = values().associateBy { it.key }
        private val camelMap = values().associateBy { it.key.replace("_", "") }

        fun fromKey(key: String): ActionTrigger? {
            val lower = key.lowercase()
            return keyMap[lower] ?: camelMap[lower.replace("_", "")]
        }
    }
}
