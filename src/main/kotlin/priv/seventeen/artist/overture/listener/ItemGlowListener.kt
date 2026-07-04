package priv.seventeen.artist.overture.listener

import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.entity.ItemDespawnEvent
import org.bukkit.event.entity.ItemSpawnEvent
import priv.seventeen.artist.blink.event.AutoListener
import priv.seventeen.artist.overture.OvertureConfig
import priv.seventeen.artist.overture.core.item.ItemStream
import priv.seventeen.artist.overture.core.manager.RarityGlowManager

/**
 * 掉落物发光监听器
 *
 * 全事件驱动，无定时扫描：
 * - ItemSpawnEvent   → 掉落时检测品质并应用发光
 * - EntityPickupItemEvent → 拾取时清理 Team entry
 * - ItemDespawnEvent → 自然消失时清理 Team entry
 */
object ItemGlowListener {

    @AutoListener
    fun onSpawn(event: ItemSpawnEvent) {
        if (!OvertureConfig.instance.rarity.enabled) return
        val stream = ItemStream(event.entity.itemStack)
        if (!stream.isOverture) return
        RarityGlowManager.applyGlow(event.entity)
    }

    @AutoListener
    fun onPickup(event: EntityPickupItemEvent) {
        RarityGlowManager.removeGlow(event.item)
    }

    @AutoListener
    fun onDespawn(event: ItemDespawnEvent) {
        RarityGlowManager.removeGlow(event.entity)
    }
}
