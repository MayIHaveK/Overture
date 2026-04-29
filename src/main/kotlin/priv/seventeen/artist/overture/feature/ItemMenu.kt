package priv.seventeen.artist.overture.feature

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import priv.seventeen.artist.blink.event.AutoListener
import priv.seventeen.artist.overture.core.group.ItemGroup
import priv.seventeen.artist.overture.core.manager.ItemManager
import priv.seventeen.artist.overture.core.manager.LoaderManager
import priv.seventeen.artist.overture.util.ColorUtil

/**
 * 物品菜单 GUI
 */
object ItemMenu {

    private const val PAGE_SIZE = 45  // 5 行物品
    private const val ROWS = 6

    /**
     * 打开物品菜单
     */
    fun open(player: Player, group: ItemGroup? = null, page: Int = 0) {
        val title = if (group != null) {
            "§8Overture - ${group.name} (${page + 1})"
        } else {
            "§8Overture Items (${page + 1})"
        }

        val inventory = Bukkit.createInventory(null, ROWS * 9, title)
        val items = getMenuItems(group)

        // 填充物品
        val start = page * PAGE_SIZE
        val end = (start + PAGE_SIZE).coerceAtMost(items.size)
        for (i in start until end) {
            inventory.setItem(i - start, items[i])
        }

        // 导航按钮
        // 上一页
        if (page > 0) {
            inventory.setItem(45, createNavItem("§a上一页", Material.ARROW))
        }
        // 下一页
        if (end < items.size) {
            inventory.setItem(53, createNavItem("§a下一页", Material.ARROW))
        }
        // 返回上级
        if (group != null) {
            inventory.setItem(49, createNavItem("§c返回", Material.BARRIER))
        }

        player.openInventory(inventory)
    }

    @AutoListener
    fun onClick(event: InventoryClickEvent) {
        val title = event.view.title
        if (!title.startsWith("§8Overture")) return
        event.isCancelled = true

        val player = event.whoClicked as? Player ?: return
        val slot = event.rawSlot
        if (slot < 0 || slot >= ROWS * 9) return

        val clickedItem = event.currentItem ?: return
        if (clickedItem.type.isAir) return

        // 导航按钮处理
        when (slot) {
            45 -> { /* 上一页 */ }
            53 -> { /* 下一页 */ }
            49 -> { player.closeInventory() }
            else -> {
                // 点击物品 → 给予 / 点击分组 → 进入
                if (slot < PAGE_SIZE) {
                    val itemMeta = clickedItem.itemMeta ?: return
                    val lore = itemMeta.lore ?: return
                    val idLine = lore.lastOrNull() ?: return
                    val id = idLine.removePrefix("§8ID: ")
                    if (id.startsWith("__group__")) {
                        // 分组导航
                        val groupPath = id.removePrefix("__group__")
                        val group = LoaderManager.getGroup(groupPath)
                        if (group != null) open(player, group)
                    } else {
                        // 给予物品
                        ItemManager.give(player, id)
                        player.sendMessage(ColorUtil.colored("&a已获取物品: $id"))
                    }
                }
            }
        }
    }

    private fun getMenuItems(group: ItemGroup?): List<ItemStack> {
        val result = mutableListOf<ItemStack>()

        // 子分组
        val subGroups = if (group != null) {
            group.getSubGroups()
        } else {
            LoaderManager.getRootGroups()
        }

        for (subGroup in subGroups) {
            val icon = ItemStack(Material.CHEST_MINECART)
            val meta = icon.itemMeta!!
            meta.setDisplayName("§f${subGroup.name}")
            meta.lore = listOf("§7点击浏览", "§8ID: __group__${subGroup.path}")
            icon.itemMeta = meta
            result.add(icon)
        }

        // 物品
        val items = if (group != null) {
            group.getItems(ItemManager.getItems())
        } else {
            ItemManager.getItems().values.filter { it.group == null }.toList()
        }

        for (item in items) {
            val icon = try {
                item.buildItemStack()
            } catch (_: Exception) {
                ItemStack(item.material)
            }
            val meta = icon.itemMeta ?: continue
            val lore = (meta.lore ?: mutableListOf()).toMutableList()
            lore.add("")
            lore.add("§8ID: ${item.id}")
            meta.lore = lore
            icon.itemMeta = meta
            result.add(icon)
        }

        return result
    }

    private fun createNavItem(name: String, material: Material): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta!!
        meta.setDisplayName(name)
        item.itemMeta = meta
        return item
    }
}
