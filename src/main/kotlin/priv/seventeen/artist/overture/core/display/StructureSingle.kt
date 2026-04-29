package priv.seventeen.artist.overture.core.display

import priv.seventeen.artist.overture.util.ColorUtil
import priv.seventeen.artist.overture.util.VariableReader

/**
 * 单行模板解析器
 * 处理 <var> 变量替换
 */
class StructureSingle(template: String?) {

    private val parts: List<VariableReader.Part>? = template?.let { reader.parse(it) }

    /**
     * 构建最终字符串
     * @param vars 变量映射 Map<变量名, 值>
     */
    fun build(vars: Map<String, String>): String? {
        val parts = this.parts ?: return null
        return parts.joinToString("") { part ->
            when (part) {
                is VariableReader.Part.Text -> part.content
                is VariableReader.Part.Variable -> vars[part.name] ?: ""
                is VariableReader.Part.ListVariable -> vars[part.name] ?: ""
            }
        }
    }

    companion object {
        private val reader = VariableReader("<", ">")
    }
}
