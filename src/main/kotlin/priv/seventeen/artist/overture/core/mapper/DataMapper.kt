package priv.seventeen.artist.overture.core.mapper

import priv.seventeen.artist.aria.Aria
import priv.seventeen.artist.aria.context.VariableKey
import priv.seventeen.artist.aria.value.NumberValue
import priv.seventeen.artist.aria.value.StringValue
import priv.seventeen.artist.overture.core.item.ItemStream
import priv.seventeen.artist.overture.util.Translator

/**
 * 数据映射器
 * 将物品 NBT data 映射为展示变量
 */
object DataMapper {

    private val VARIABLE_PATTERN = Regex("\\{([^}]+)}")
    private val FUNCTION_PATTERN = Regex("^(\\w+)\\((.*)\\)$")

    /**
     * 执行数据映射
     * @param mapper data-mapper 配置 Map<目标变量名, 映射表达式>
     * @param stream 物品流
     * @return 映射结果 Map<变量名, 值>
     */
    fun map(mapper: Map<String, String>, stream: ItemStream): Map<String, String> {
        if (mapper.isEmpty()) return emptyMap()

        // 展平 NBT 数据
        val flatData = Translator.flatten(stream.overtureData)
        val result = mutableMapOf<String, String>()

        for ((key, expression) in mapper) {
            result[key] = evaluate(expression, flatData, stream)
        }

        return result
    }

    /**
     * 求值映射表达式
     */
    private fun evaluate(expression: String, data: Map<String, Any>, stream: ItemStream): String {
        // 尝试简单变量引用 "{key}"
        if (expression.startsWith("{") && expression.endsWith("}") && expression.count { it == '{' } == 1) {
            val key = expression.removeSurrounding("{", "}")
            return data[key]?.toString() ?: ""
        }

        // 尝试内置函数调用
        val funcMatch = FUNCTION_PATTERN.matchEntire(expression)
        if (funcMatch != null) {
            val funcName = funcMatch.groupValues[1]
            val argsStr = funcMatch.groupValues[2]
            val args = parseArgs(argsStr, data)
            val func = MapperFunction.get(funcName)
            if (func != null) {
                return func(args)
            }
        }

        // 替换变量后尝试作为 Aria 表达式
        val resolved = VARIABLE_PATTERN.replace(expression) { match ->
            val key = match.groupValues[1]
            data[key]?.toString() ?: "0"
        }

        // 如果替换后是纯文本（无函数调用），直接返回
        if (!resolved.contains("(") && !resolved.contains("if") && !resolved.contains("?")) {
            return resolved
        }

        // 作为 Aria 表达式执行
        return try {
            val ctx = Aria.createContext()
            // 注入数据变量
            data.forEach { (k, v) ->
                val varKey = VariableKey.of(k.replace(".", "_"))
                when (v) {
                    is Number -> ctx.globalStorage.getGlobalVariable(varKey).setValue(NumberValue(v.toDouble()))
                    else -> ctx.globalStorage.getGlobalVariable(varKey).setValue(StringValue(v.toString()))
                }
            }
            val result = Aria.eval("return $resolved", ctx)
            result?.stringValue() ?: resolved
        } catch (_: Exception) {
            resolved
        }
    }

    /**
     * 解析函数参数
     */
    private fun parseArgs(argsStr: String, data: Map<String, Any>): List<Any> {
        if (argsStr.isBlank()) return emptyList()
        return argsStr.split(",").map { arg ->
            val trimmed = arg.trim()
            // 字符串字面量
            if ((trimmed.startsWith("'") && trimmed.endsWith("'")) ||
                (trimmed.startsWith("\"") && trimmed.endsWith("\""))) {
                return@map trimmed.removeSurrounding("'").removeSurrounding("\"")
            }
            // 变量引用
            if (trimmed.startsWith("{") && trimmed.endsWith("}")) {
                val key = trimmed.removeSurrounding("{", "}")
                return@map data[key] ?: 0
            }
            // 数字
            trimmed.toDoubleOrNull() ?: trimmed
        }
    }
}
