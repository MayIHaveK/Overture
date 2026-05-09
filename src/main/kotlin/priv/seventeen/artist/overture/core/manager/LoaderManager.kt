package priv.seventeen.artist.overture.core.manager

import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import priv.seventeen.artist.blink.BlinkLog
import priv.seventeen.artist.overture.api.ItemProvider
import priv.seventeen.artist.overture.core.group.ItemGroup
import priv.seventeen.artist.overture.core.item.OvertureItem
import priv.seventeen.artist.overture.core.model.ItemModel
import priv.seventeen.artist.overture.util.ColorUtil
import java.io.File

/**
 * 配置加载器
 * 递归扫描 items/ 和 displays/ 目录
 */
object LoaderManager {

    private val groups = mutableMapOf<String, ItemGroup>()

    /**
     * 加载物品目录
     */
    fun loadItems(directory: File): Map<String, OvertureItem> {
        val result = mutableMapOf<String, OvertureItem>()
        if (!directory.exists()) {
            directory.mkdirs()
            return result
        }
        loadItemsRecursive(directory, null, result)
        return result
    }

    /**
     * 加载展示方案目录
     */
    fun loadDisplays(directory: File) {
        if (!directory.exists()) {
            directory.mkdirs()
            return
        }
        loadDisplaysRecursive(directory)
    }

    /**
     * 获取所有分组
     */
    fun getGroups(): Map<String, ItemGroup> = groups.toMap()

    /**
     * 获取分组
     */
    fun getGroup(path: String): ItemGroup? = groups[path]

    /**
     * 获取根级别分组
     */
    fun getRootGroups(): List<ItemGroup> {
        return groups.values.filter { it.parent == null }.sortedBy { it.priority }
    }

    /**
     * 重载
     */
    fun reload() {
        groups.clear()
    }

    // ==================== 内部实现 ====================

    private fun loadItemsRecursive(
        directory: File,
        parentGroup: ItemGroup?,
        result: MutableMap<String, OvertureItem>
    ) {
        val files = directory.listFiles() ?: return

        // 读取 __group__ 配置
        val groupConfig = files.find { it.name == "__group__" || it.name == "__group__.yml" }
        val groupYaml = groupConfig?.let { YamlConfiguration.loadConfiguration(it) }
        val groupPriority = groupYaml?.getInt("priority", 0) ?: 0
        val groupIconName = groupYaml?.getString("icon", "CHEST") ?: "CHEST"
        val groupIcon = Material.matchMaterial(groupIconName.uppercase()) ?: Material.CHEST
        val groupDisplayName = groupYaml?.getString("name")?.let { ColorUtil.colored(it) }
        val groupLore = groupYaml?.getStringList("lore")?.map { ColorUtil.colored(it) } ?: emptyList()

        // 创建当前目录的分组（如果不是根目录）
        val currentGroup = if (parentGroup != null || directory.name != "items") {
            ItemGroup(
                name = directory.name,
                parent = parentGroup,
                level = (parentGroup?.level ?: -1) + 1,
                priority = groupPriority,
                icon = groupIcon,
                displayName = groupDisplayName,
                description = groupLore
            ).also {
                parentGroup?.children?.add(it)
                groups[it.path] = it
            }
        } else null

        // 处理文件
        for (file in files.sortedBy { it.name }) {
            if (file.name.startsWith("__")) continue

            if (file.isDirectory) {
                loadItemsRecursive(file, currentGroup, result)
            } else if (file.extension == "yml" || file.extension == "yaml") {
                loadItemFile(file, currentGroup, result)
            }
        }
    }

    private fun loadItemFile(
        file: File,
        group: ItemGroup?,
        result: MutableMap<String, OvertureItem>
    ) {
        try {
            val yaml = YamlConfiguration.loadConfiguration(file)
            for (key in yaml.getKeys(false)) {
                if (key.startsWith("__")) continue

                // 事件模型（$ 后缀）
                if (key.endsWith("$")) {
                    val modelId = key.removeSuffix("$")
                    val section = yaml.getConfigurationSection(key) ?: continue
                    val model = ItemModel(modelId, section)
                    ItemManager.registerModel(model)
                    continue
                }

                val section = yaml.getConfigurationSection(key) ?: continue
                val item = OvertureItem(key, section)
                item.group = group
                result[key] = item
            }
        } catch (e: Exception) {
            BlinkLog.error("加载物品文件失败 ${file.name}: ${e.message}")
        }
    }

    private fun loadDisplaysRecursive(directory: File) {
        val files = directory.listFiles() ?: return
        for (file in files) {
            if (file.isDirectory) {
                loadDisplaysRecursive(file)
            } else if (file.extension == "yml" || file.extension == "yaml") {
                loadDisplayFile(file)
            }
        }
    }

    private fun loadDisplayFile(file: File) {
        try {
            val yaml = YamlConfiguration.loadConfiguration(file)
            for (key in yaml.getKeys(false)) {
                val section = yaml.getConfigurationSection(key) ?: continue
                DisplayManager.loadFromSection(key, section)
            }
        } catch (e: Exception) {
            BlinkLog.error("加载展示文件失败 ${file.name}: ${e.message}")
        }
    }
}

/**
 * YAML 文件物品提供者（默认实现）
 */
class YamlItemProvider(private val dataFolder: File) : ItemProvider {
    override val id: String = "yaml"
    override val priority: Int = 0

    override fun load(): Map<String, OvertureItem> {
        val itemsDir = File(dataFolder, "items")
        return LoaderManager.loadItems(itemsDir)
    }

    override fun reload() {
        LoaderManager.reload()
        val displaysDir = File(dataFolder, "displays")
        DisplayManager.reload()
        LoaderManager.loadDisplays(displaysDir)
    }
}
