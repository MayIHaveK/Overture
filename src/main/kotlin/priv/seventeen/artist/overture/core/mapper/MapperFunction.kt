package priv.seventeen.artist.overture.core.mapper


/**
 * 内置映射函数注册表
 */
object MapperFunction {

    private val functions = mutableMapOf<String, (List<Any>) -> String>()

    init {
        register("bar") { args ->
            val current = (args.getOrNull(0) as? Number)?.toInt() ?: 0
            val max = (args.getOrNull(1) as? Number)?.toInt() ?: 1
            val scale = (args.getOrNull(2) as? Number)?.toInt() ?: 20
            buildBar(current, max, scale)
        }

        register("repeat") { args ->
            val str = args.getOrNull(0)?.toString() ?: ""
            val n = (args.getOrNull(1) as? Number)?.toInt() ?: 0
            str.repeat(n.coerceIn(0, 100))
        }

        register("format") { args ->
            val pattern = args.getOrNull(0)?.toString() ?: "%s"
            val formatArgs = args.drop(1).toTypedArray()
            try {
                String.format(pattern, *formatArgs)
            } catch (_: Exception) {
                pattern
            }
        }

        register("color") { args ->
            val value = (args.getOrNull(0) as? Number)?.toDouble() ?: 0.0
            val min = (args.getOrNull(1) as? Number)?.toDouble() ?: 0.0
            val max = (args.getOrNull(2) as? Number)?.toDouble() ?: 100.0
            val percent = ((value - min) / (max - min)).coerceIn(0.0, 1.0)
            val color = when {
                percent <= 0.25 -> "§c"  // 红
                percent <= 0.5 -> "§e"   // 黄
                percent <= 0.75 -> "§a"  // 浅绿
                else -> "§2"             // 深绿
            }
            "$color${value.toInt()}"
        }

        register("percent") { args ->
            val current = (args.getOrNull(0) as? Number)?.toDouble() ?: 0.0
            val max = (args.getOrNull(1) as? Number)?.toDouble() ?: 1.0
            val percent = if (max > 0) (current / max * 100) else 0.0
            "%.1f%%".format(percent)
        }

        register("roman") { args ->
            val n = (args.getOrNull(0) as? Number)?.toInt() ?: 0
            toRoman(n)
        }

        register("fixed") { args ->
            val value = (args.getOrNull(0) as? Number)?.toDouble() ?: 0.0
            val decimals = (args.getOrNull(1) as? Number)?.toInt() ?: 1
            "%.${decimals}f".format(value)
        }

        register("condition") { args ->
            val cond = args.getOrNull(0)
            val trueVal = args.getOrNull(1)?.toString() ?: ""
            val falseVal = args.getOrNull(2)?.toString() ?: ""
            val result = when (cond) {
                is Boolean -> cond
                is Number -> cond.toInt() != 0
                is String -> cond.isNotEmpty() && cond != "false" && cond != "0"
                else -> false
            }
            if (result) trueVal else falseVal
        }
    }

    /**
     * 注册映射函数
     */
    fun register(name: String, handler: (List<Any>) -> String) {
        functions[name] = handler
    }

    /**
     * 获取映射函数
     */
    fun get(name: String): ((List<Any>) -> String)? = functions[name]

    // ==================== 内置实现 ====================

    private fun buildBar(current: Int, max: Int, scale: Int): String {
        val filled = if (max > 0) (current.toDouble() / max * scale).toInt().coerceIn(0, scale) else 0
        val sb = StringBuilder()
        for (i in 1..scale) {
            if (i <= filled) {
                sb.append("§f◆")
            } else {
                sb.append("§7◇")
            }
        }
        return sb.toString()
    }

    private fun toRoman(num: Int): String {
        if (num <= 0 || num > 3999) return num.toString()
        val values = intArrayOf(1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1)
        val symbols = arrayOf("M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I")
        val sb = StringBuilder()
        var n = num
        for (i in values.indices) {
            while (n >= values[i]) {
                sb.append(symbols[i])
                n -= values[i]
            }
        }
        return sb.toString()
    }
}
