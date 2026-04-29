package priv.seventeen.artist.overture.listener

import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.player.*
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import priv.seventeen.artist.blink.bukkitPlugin
import priv.seventeen.artist.blink.event.AutoListener
import priv.seventeen.artist.overture.core.action.ActionExecutor
import priv.seventeen.artist.overture.core.action.ActionTrigger
import priv.seventeen.artist.overture.core.action.ItemAction
import priv.seventeen.artist.overture.core.item.ItemSignal
import priv.seventeen.artist.overture.core.item.ItemStream
import priv.seventeen.artist.overture.core.item.OvertureItem
import priv.seventeen.artist.overture.core.manager.ItemManager
import priv.seventeen.artist.overture.core.manager.UpdateManager
import priv.seventeen.artist.overture.core.meta.impl.MetaDurability
import priv.seventeen.artist.overture.feature.ItemCooldown

/**
 * 物品交互事件监听器
 */
object ItemListener {

    @AutoListener(priority = EventPriority.HIGH)
    fun onInteract(event: PlayerInteractEvent) {
        val item = event.item ?: return
        val stream = ItemStream(item)
        if (!stream.isOverture) return

        val itemDef = ItemManager.getItem(stream.overtureId ?: return) ?: return
        val trigger = when {
            event.action.name.contains("LEFT") -> ActionTrigger.ON_LEFT_CLICK
            event.action.name.contains("RIGHT") -> ActionTrigger.ON_RIGHT_CLICK
            else -> return
        }

        val action = resolveAction(itemDef, trigger) ?: return
        ActionExecutor.execute(action, event.player, stream, event, itemDef.eventVars)

        if (stream.signals.isNotEmpty()) {
            rebuildItem(event.player, stream, item)
        }
    }

    @AutoListener(priority = EventPriority.HIGH)
    fun onInteractEntity(event: PlayerInteractEntityEvent) {
        if (event.hand != EquipmentSlot.HAND) return
        val item = event.player.inventory.itemInMainHand
        if (item.type.isAir) return

        val stream = ItemStream(item)
        if (!stream.isOverture) return

        val itemDef = ItemManager.getItem(stream.overtureId ?: return) ?: return
        val action = resolveAction(itemDef, ActionTrigger.ON_RIGHT_CLICK_ENTITY) ?: return
        ActionExecutor.execute(action, event.player, stream, event, itemDef.eventVars)

        if (stream.signals.isNotEmpty()) {
            rebuildItem(event.player, stream, item)
        }
    }

    @AutoListener(priority = EventPriority.HIGH)
    fun onAttack(event: EntityDamageByEntityEvent) {
        val player = event.damager as? Player ?: return
        val item = player.inventory.itemInMainHand
        if (item.type.isAir) return

        val stream = ItemStream(item)
        if (!stream.isOverture) return

        val itemDef = ItemManager.getItem(stream.overtureId ?: return) ?: return
        val action = resolveAction(itemDef, ActionTrigger.ON_ATTACK) ?: return
        ActionExecutor.execute(action, player, stream, event, itemDef.eventVars)

        if (stream.signals.isNotEmpty()) {
            rebuildItem(player, stream, item)
        }
    }

    @AutoListener(priority = EventPriority.HIGH)
    fun onDrop(event: PlayerDropItemEvent) {
        val item = event.itemDrop.itemStack
        val stream = ItemStream(item)
        if (!stream.isOverture) return

        val itemDef = ItemManager.getItem(stream.overtureId ?: return) ?: return
        val action = resolveAction(itemDef, ActionTrigger.ON_DROP) ?: return
        ActionExecutor.execute(action, event.player, stream, event, itemDef.eventVars)
    }

    @AutoListener(priority = EventPriority.HIGH)
    fun onPickup(event: EntityPickupItemEvent) {
        val player = event.entity as? Player ?: return
        val item = event.item.itemStack
        val stream = ItemStream(item)
        if (!stream.isOverture) return

        val itemDef = ItemManager.getItem(stream.overtureId ?: return) ?: return
        val action = resolveAction(itemDef, ActionTrigger.ON_PICK)
        if (action != null) {
            ActionExecutor.execute(action, player, stream, event, itemDef.eventVars)
        }

        // 拾取时检查更新
        val updated = UpdateManager.checkUpdate(player, item)
        if (updated != null) {
            event.item.itemStack = updated
        }
    }

    @AutoListener(priority = EventPriority.HIGH)
    fun onConsume(event: PlayerItemConsumeEvent) {
        val item = event.item
        val stream = ItemStream(item)
        if (!stream.isOverture) return

        val itemDef = ItemManager.getItem(stream.overtureId ?: return) ?: return
        val action = resolveAction(itemDef, ActionTrigger.ON_CONSUME) ?: return
        ActionExecutor.execute(action, event.player, stream, event, itemDef.eventVars)
    }

    @AutoListener(priority = EventPriority.HIGH)
    fun onBlockBreak(event: BlockBreakEvent) {
        val item = event.player.inventory.itemInMainHand
        if (item.type.isAir) return

        val stream = ItemStream(item)
        if (!stream.isOverture) return

        val itemDef = ItemManager.getItem(stream.overtureId ?: return) ?: return
        val action = resolveAction(itemDef, ActionTrigger.ON_BLOCK_BREAK) ?: return
        ActionExecutor.execute(action, event.player, stream, event, itemDef.eventVars)

        if (stream.signals.isNotEmpty()) {
            rebuildItem(event.player, stream, item)
        }
    }

    @AutoListener(priority = EventPriority.HIGH)
    fun onSwapHand(event: PlayerSwapHandItemsEvent) {
        // 主手 → 副手
        val mainItem = event.mainHandItem
        if (mainItem != null && !mainItem.type.isAir) {
            val stream = ItemStream(mainItem)
            if (stream.isOverture) {
                val itemDef = stream.overtureId?.let { ItemManager.getItem(it) }
                if (itemDef != null) {
                    val action = resolveAction(itemDef, ActionTrigger.ON_SWAP_TO_OFFHAND)
                    if (action != null) {
                        ActionExecutor.execute(action, event.player, stream, event, itemDef.eventVars)
                    }
                }
            }
        }

        // 副手 → 主手
        val offItem = event.offHandItem
        if (offItem != null && !offItem.type.isAir) {
            val stream = ItemStream(offItem)
            if (stream.isOverture) {
                val itemDef = stream.overtureId?.let { ItemManager.getItem(it) }
                if (itemDef != null) {
                    val action = resolveAction(itemDef, ActionTrigger.ON_SWAP_TO_MAINHAND)
                    if (action != null) {
                        ActionExecutor.execute(action, event.player, stream, event, itemDef.eventVars)
                    }
                }
            }
        }
    }

    @AutoListener
    fun onJoin(event: PlayerJoinEvent) {
        // 玩家加入时检查背包更新
        Bukkit.getScheduler().runTaskLater(
            bukkitPlugin, Runnable {
                UpdateManager.checkInventory(event.player)
            }, 20L
        )
    }

    @AutoListener
    fun onWorldChange(event: PlayerChangedWorldEvent) {
        UpdateManager.checkInventory(event.player)
    }

    @AutoListener
    fun onQuit(event: PlayerQuitEvent) {
        ItemCooldown.clear(event.player)
    }

    @AutoListener(priority = EventPriority.MONITOR)
    fun onItemDamage(event: PlayerItemDamageEvent) {
        val item = event.item
        val stream = ItemStream(item)
        if (!stream.isOverture) return

        val itemDef = ItemManager.getItem(stream.overtureId ?: return) ?: return
        // 有自定义耐久时拦截原版耐久损耗
        if (itemDef.metaList.any { it.key == "durability" }) {
            event.isCancelled = true
            // 触发 on_damage 脚本
            val action = resolveAction(itemDef, ActionTrigger.ON_DAMAGE)
            if (action != null) {
                ActionExecutor.execute(action, event.player, stream, event, itemDef.eventVars)
                if (stream.signals.isNotEmpty()) {
                    rebuildItem(event.player, stream, item)
                }
            }
        }
    }

    // ==================== 辅助方法 ====================

    private fun resolveAction(
        item: OvertureItem,
        trigger: ActionTrigger
    ): ItemAction? {
        // 优先物品自身的动作
        item.actions[trigger]?.let { return it }
        // 其次从模型中查找
        for (modelId in item.modelIds) {
            val model = ItemManager.getModel(modelId) ?: continue
            model.actions[trigger]?.let { return it }
        }
        return null
    }

    private fun rebuildItem(player: Player, stream: ItemStream, item: ItemStack) {
        if (stream.signals.contains(ItemSignal.DURABILITY_DESTROYED)) {
            // 物品损坏处理
            handleItemDestroy(player, stream, item)
            return
        }
        // 完整重构：重新构建物品（触发 Release 事件链，更新 Display）
        val itemDef = ItemManager.getItem(stream.overtureId ?: return) ?: return
        val rebuilt = itemDef.build(player, stream)
        val result = rebuilt.toItemStack(player)
        // 将结果写回原 ItemStack
        item.type = result.type
        item.amount = result.amount
        item.itemMeta = result.itemMeta
    }

    private fun handleItemDestroy(player: Player, stream: ItemStream, item: ItemStack) {
        val itemDef = ItemManager.getItem(stream.overtureId ?: return) ?: return
        val durabilityMeta = itemDef.metaList.filterIsInstance<MetaDurability>().firstOrNull()

        val remains = durabilityMeta?.getRemainsItem()
        if (remains != null) {
            item.type = remains.type
            item.amount = remains.amount
            item.itemMeta = remains.itemMeta
        } else {
            // 播放破碎效果
            player.playSound(player.location, Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f)
            item.amount = 0
        }
    }
}
