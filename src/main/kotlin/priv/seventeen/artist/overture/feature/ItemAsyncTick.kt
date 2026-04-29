package priv.seventeen.artist.overture.feature

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import priv.seventeen.artist.blink.bukkitPlugin
import priv.seventeen.artist.blink.lifecycle.Awake
import priv.seventeen.artist.blink.lifecycle.LifeCycle
import priv.seventeen.artist.overture.core.manager.UpdateManager

/**
 * 异步 Tick 定时任务
 * 定期检查在线玩家背包中的物品更新
 */
object ItemAsyncTick {

    var period: Long = 100L  // tick

    private var taskId: Int = -1

    @Awake(LifeCycle.ENABLE)
    fun start() {
        taskId = Bukkit.getScheduler().runTaskTimer(bukkitPlugin, Runnable {
            for (player in Bukkit.getOnlinePlayers()) {
                try {
                    UpdateManager.checkInventory(player)
                } catch (_: Exception) {
                }
            }
        }, period, period).taskId
    }

    @Awake(LifeCycle.DISABLE)
    fun stop() {
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId)
            taskId = -1
        }
    }
}
