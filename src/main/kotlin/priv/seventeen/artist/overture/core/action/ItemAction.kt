package priv.seventeen.artist.overture.core.action

import priv.seventeen.artist.aria.Aria
import priv.seventeen.artist.aria.api.AriaCompiledRoutine

/**
 * 物品动作定义
 * 封装一个触发器对应的 Aria 脚本
 */
class ItemAction(
    val trigger: ActionTrigger,
    val script: String,
    val cancelEvent: Boolean = false
) {
    /** 预编译的 Aria 脚本（不绑定 Context，每次执行传入新 Context） */
    val compiled: AriaCompiledRoutine? = try {
        Aria.compile("${trigger.key}_action", script)
    } catch (e: Exception) {
        null
    }
}
