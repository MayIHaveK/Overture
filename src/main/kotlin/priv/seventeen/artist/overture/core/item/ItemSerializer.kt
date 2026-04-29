package priv.seventeen.artist.overture.core.item

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.bukkit.inventory.ItemStack
import priv.seventeen.artist.asteroid.AsteroidAPI
import priv.seventeen.artist.asteroid.item.ItemTag
import priv.seventeen.artist.overture.core.manager.ItemManager

/**
 * 物品序列化器
 * ItemStack ↔ JSON 格式
 */
object ItemSerializer {

    private val gson = Gson()

    /**
     * 序列化 ItemStack 为 JSON
     *
     * Overture 物品格式:
     * ```json
     * {
     *   "id": "diamond_sword_1",
     *   "amount": 1,
     *   "data": { ... },
     *   "unique": { ... }
     * }
     * ```
     *
     * 原版物品格式:
     * ```json
     * {
     *   "id": "minecraft:diamond_sword",
     *   "amount": 1,
     *   "nbt": "..."
     * }
     * ```
     */
    fun serialize(itemStack: ItemStack): String {
        val stream = ItemStream(itemStack)
        val json = JsonObject()

        if (stream.isOverture) {
            json.addProperty("id", stream.overtureId)
            json.addProperty("amount", itemStack.amount)

            // 序列化活跃数据
            val data = stream.overtureData
            if (!data.isEmpty()) {
                json.addProperty("data", data.toString())
            }

            // 序列化唯一数据
            val unique = stream.overtureUnique
            if (!unique.isEmpty()) {
                val uniqueJson = JsonObject()
                if (unique.containsKey("uuid")) uniqueJson.addProperty("uuid", unique.getString("uuid"))
                if (unique.containsKey("player")) uniqueJson.addProperty("player", unique.getString("player"))
                if (unique.containsKey("date")) uniqueJson.addProperty("date", unique.getLong("date"))
                if (unique.containsKey("date-formatted")) uniqueJson.addProperty("date-formatted", unique.getString("date-formatted"))
                json.add("unique", uniqueJson)
            }
        } else {
            json.addProperty("id", "minecraft:${itemStack.type.name.lowercase()}")
            json.addProperty("amount", itemStack.amount)
            // 使用 Asteroid 序列化原版物品
            try {
                val nmsJson = AsteroidAPI.getItemStackNMS().item2Json(itemStack)
                json.addProperty("nbt", nmsJson)
            } catch (_: Exception) {
            }
        }

        return gson.toJson(json)
    }

    /**
     * 从 JSON 反序列化为 ItemStack
     */
    fun deserialize(json: String): ItemStack? {
        return try {
            val obj = JsonParser.parseString(json).asJsonObject
            val id = obj.get("id")?.asString ?: return null
            val amount = obj.get("amount")?.asInt ?: 1

            if (id.startsWith("minecraft:")) {
                // 原版物品
                val nbt = obj.get("nbt")?.asString
                if (nbt != null) {
                    val item = AsteroidAPI.getItemStackNMS().json2Item(nbt)
                    item.amount = amount
                    return item
                }
                val material = org.bukkit.Material.matchMaterial(id.removePrefix("minecraft:").uppercase())
                    ?: return null
                return ItemStack(material, amount)
            } else {
                // Overture 物品
                val item = ItemManager.generate(id) ?: return null
                item.amount = amount

                // 恢复活跃数据
                val dataStr = obj.get("data")?.asString
                if (dataStr != null) {
                    // TODO: 从字符串恢复 ItemTag 数据
                }

                // 恢复唯一数据
                val uniqueObj = obj.getAsJsonObject("unique")
                if (uniqueObj != null) {
                    val tag = ItemTag.fromItemStack(item)
                    val root = tag.getCompound("overture")
                    val uniqueTag = root.getCompound("unique")
                    uniqueObj.get("uuid")?.asString?.let { uniqueTag.putString("uuid", it) }
                    uniqueObj.get("player")?.asString?.let { uniqueTag.putString("player", it) }
                    uniqueObj.get("date")?.asLong?.let { uniqueTag.putLong("date", it) }
                    uniqueObj.get("date-formatted")?.asString?.let { uniqueTag.putString("date-formatted", it) }
                    root.putCompound("unique", uniqueTag)
                    tag.putCompound("overture", root)
                    return tag.saveTo(item)
                }

                return item
            }
        } catch (e: Exception) {
            null
        }
    }
}
