package priv.seventeen.artist.overture.api.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * 插件重载事件
 */
class PluginReloadEvent : Event() {

    override fun getHandlers(): HandlerList = handlerList

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}
