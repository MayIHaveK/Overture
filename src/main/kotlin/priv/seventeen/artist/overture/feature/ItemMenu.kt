package priv.seventeen.artist.overture.feature

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import priv.seventeen.artist.blink.event.AutoListener
import priv.seventeen.artist.overture.core.group.ItemGroup
import priv.seventeen.artist.overture.core.item.OvertureItem
import priv.seventeen.artist.overture.core.manager.ItemManager
import priv.seventeen.artist.overture.core.manager.LoaderManager
import priv.seventeen.artist.overture.util.ColorUtil

/**
 * 物品菜单 GUI
 *
 * 布局（6 行 × 9 列）：
 * - 第 0 行 (0-8)  : 面包屑导航
 * - 第 1-4 行 (9-44): 内容区（先展示子分组，后展示物品）
 * - 第 5 行 (45-53): 导航栏
 *   - 45: 上一页
 *   - 49: 返回上级 / 关闭
 *   - 53: 下一页
 */
object ItemMenu {

    private const val ROWS = 6
    private const val SIZE = ROWS * 9
    private const val CONTENT_START = 9
    private const val CONTENT_END = 45
    private const val CONTENT_SIZE = CONTENT_END - CONTENT_START  // 36
    private const val SLOT_PREV = 45
    private const val SLOT_BACK = 49
    private const val SLOT_NEXT = 53

    /**
     * 打开物品菜单
     */
    fun open(player: Player, group: ItemGroup? = null, page: Int = 0) {
        val entries = buildEntries(group)
        val totalPages = ((entries.size - 1).coerceAtLeast(0) / CONTENT_SIZE) + 1
        val safePage = page.coerceIn(0, totalPages - 1)

        val holder = ItemMenuHolder(group, safePage)
        val title = buildTitle(group, safePage + 1, totalPages)
        val inventory = Bukkit.createInventory(holder, SIZE, title)
        holder.attach(inventory)

        // 面包屑导航
        renderBreadcrumb(inventory, group)

        // 内容区
        val start = safePage * CONTENT_SIZE
        val end = (start + CONTENT_SIZE).coerceAtMost(entries.size)
        for (i in start until end) {
            val entry = entries[i]
            inventory.setItem(CONTENT_START + (i - start), entry.icon)
            holder.entries[CONTENT_START + (i - start)] = entry
        }

        // 功能栏
        if (safePage > 0) {
            inventory.setItem(SLOT_PREV, createNav("§a← 上一页", "§7第 ${safePage + 1}/$totalPages 页", Material.ARROW))
        }
        if (end < entries.size) {
            inventory.setItem(SLOT_NEXT, createNav("§a下一页 →", "§7第 ${safePage + 1}/$totalPages 页", Material.ARROW))
        }
        if (group != null) {
            val parentName = group.parent?.title ?: "根目录"
            inventory.setItem(SLOT_BACK, createNav("§e← 返回上级", "§7返回到 §f$parentName", Material.OAK_DOOR))
        } else {
            inventory.setItem(SLOT_BACK, createNav("§c关闭", "§7关闭菜单", Material.BARRIER))
        }

        player.openInventory(inventory)
    }

    @AutoListener
    fun onClick(event: InventoryClickEvent) {
        val holder = event.inventory.holder as? ItemMenuHolder ?: return
        event.isCancelled = true

        val player = event.whoClicked as? Player ?: return
        val slot = event.rawSlot
        if (slot < 0 || slot >= SIZE) return

        // 功能栏
        when (slot) {
            SLOT_PREV -> {
                if (holder.page > 0) open(player, holder.group, holder.page - 1)
                return
            }
            SLOT_NEXT -> {
                open(player, holder.group, holder.page + 1)
                return
            }
            SLOT_BACK -> {
                if (holder.group != null) {
                    open(player, holder.group.parent)
                } else {
                    player.closeInventory()
                }
                return
            }
        }

        // 面包屑
        if (holder.breadcrumb.containsKey(slot)) {
            val target = holder.breadcrumb[slot]
            open(player, target)
            return
        }

        // 内容区条目
        val entry = holder.entries[slot] ?: return
        when (entry) {
            is MenuEntry.GroupEntry -> open(player, entry.group)
            is MenuEntry.ItemEntry -> {
                ItemManager.give(player, entry.item.id)
                player.sendMessage(ColorUtil.colored("&a已获取物品: &f${entry.item.id}"))
            }
        }
    }

    // ==================== 内部实现 ====================

    private fun buildEntries(group: ItemGroup?): List<MenuEntry> {
        val result = mutableListOf<MenuEntry>()

        // 子分组
        val subGroups = if (group != null) {
            group.getSubGroups()
        } else {
            LoaderManager.getRootGroups()
        }
        for (sub in subGroups) {
            result.add(MenuEntry.GroupEntry(sub, buildGroupIcon(sub)))
        }

        // 物品
        val items = if (group != null) {
            group.getItems(ItemManager.getItems())
        } else {
            ItemManager.getItems().values.filter { it.group == null }
        }
        for (item in items.sortedBy { it.id }) {
            result.add(MenuEntry.ItemEntry(item, buildItemIcon(item)))
        }

        return result
    }

    private fun buildGroupIcon(group: ItemGroup): ItemStack {
        val icon = ItemStack(group.icon)
        val meta = icon.itemMeta ?: return icon
        meta.setDisplayName("§f§l${group.title}")
        val lore = mutableListOf<String>()
        lore.addAll(group.description)
        if (lore.isEmpty()) {
            lore.add("§7点击浏览分组")
        }
        lore.add("")
        val itemCount = group.getItems(ItemManager.getItems()).size
        val subCount = group.getSubGroups().size
        if (subCount > 0) lore.add("§7子分组: §f$subCount")
        if (itemCount > 0) lore.add("§7物品数: §f$itemCount")
        lore.add("§8» 点击进入")
        meta.lore = lore
        icon.itemMeta = meta
        return icon
    }

    private fun buildItemIcon(item: OvertureItem): ItemStack {
        val icon = try {
            item.buildItemStack()
        } catch (_: Exception) {
            ItemStack(item.material)
        }
        val meta = icon.itemMeta ?: return icon
        val lore = (meta.lore ?: mutableListOf()).toMutableList()
        if (lore.isNotEmpty()) lore.add("")
        lore.add("§8» 点击获取")
        lore.add("§8§o${item.id}")
        meta.lore = lore
        icon.itemMeta = meta
        return icon
    }

    private fun renderBreadcrumb(inventory: Inventory, group: ItemGroup?) {
        val holder = inventory.holder as? ItemMenuHolder ?: return

        // 根目录图标（槽位 0）
        val rootIcon = ItemStack(Material.COMPASS)
        val rootMeta = rootIcon.itemMeta
        rootMeta?.setDisplayName("§f§l全部物品")
        rootMeta?.lore = if (group == null) {
            listOf("§e当前位置")
        } else {
            listOf("§7点击返回根目录")
        }
        rootIcon.itemMeta = rootMeta
        inventory.setItem(0, rootIcon)
        if (group != null) holder.breadcrumb[0] = null // null 表示根

        if (group == null) return

        // 分组链（最多 7 层：槽位 1-7）
        val chain = group.getBreadcrumb()
        val maxSlots = 7
        val displayChain = if (chain.size <= maxSlots) {
            chain
        } else {
            chain.takeLast(maxSlots)
        }

        for ((index, node) in displayChain.withIndex()) {
            val slot = 1 + index
            if (slot > 7) break

            val isCurrent = node == group
            val icon = ItemStack(if (isCurrent) Material.NETHER_STAR else node.icon)
            val meta = icon.itemMeta ?: continue
            meta.setDisplayName("§f${node.title}")
            val lore = mutableListOf<String>()
            if (isCurrent) {
                lore.add("§e当前位置")
            } else {
                lore.add("§7点击跳转至此")
            }
            lore.add("§8路径: ${node.path}")
            meta.lore = lore
            icon.itemMeta = meta
            inventory.setItem(slot, icon)
            if (!isCurrent) {
                holder.breadcrumb[slot] = node
            }
        }

        // 分隔装饰（槽位 8）
        val filler = ItemStack(Material.BLACK_STAINED_GLASS_PANE)
        val fillerMeta = filler.itemMeta
        fillerMeta?.setDisplayName(" ")
        filler.itemMeta = fillerMeta
        inventory.setItem(8, filler)
    }

    private fun buildTitle(group: ItemGroup?, page: Int, totalPages: Int): String {
        val base = if (group != null) {
            "§8Overture » ${stripColor(group.title)}"
        } else {
            "§8Overture"
        }
        return if (totalPages > 1) "$base §7($page/$totalPages)" else base
    }

    private fun stripColor(s: String): String {
        return s.replace(Regex("§[0-9a-fk-orA-FK-OR]"), "")
    }

    private fun createNav(name: String, lore: String, material: Material): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta ?: return item
        meta.setDisplayName(name)
        meta.lore = listOf(lore)
        item.itemMeta = meta
        return item
    }

    // ==================== 菜单条目 ====================

    private sealed class MenuEntry {
        abstract val icon: ItemStack

        class GroupEntry(val group: ItemGroup, override val icon: ItemStack) : MenuEntry()
        class ItemEntry(val item: OvertureItem, override val icon: ItemStack) : MenuEntry()
    }

    /**
     * 菜单状态持有器
     * 通过 InventoryHolder 机制保存当前分组、页码、条目映射，避免依赖 title 字符串解析
     */
    private class ItemMenuHolder(
        val group: ItemGroup?,
        val page: Int
    ) : InventoryHolder {
        private var inv: Inventory? = null
        /** 槽位 → 菜单条目 */
        val entries: MutableMap<Int, MenuEntry> = mutableMapOf()
        /** 面包屑槽位 → 目标分组（null 表示根） */
        val breadcrumb: MutableMap<Int, ItemGroup?> = mutableMapOf()

        fun attach(inventory: Inventory) {
            this.inv = inventory
        }

        override fun getInventory(): Inventory = inv
            ?: throw IllegalStateException("ItemMenuHolder accessed before inventory attached")
    }
}
