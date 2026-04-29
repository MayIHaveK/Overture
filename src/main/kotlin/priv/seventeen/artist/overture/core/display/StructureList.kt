package priv.seventeen.artist.overture.core.display

import priv.seventeen.artist.overture.util.VariableReader

/**
 * 多行模板解析器
 * 处理 <var> 单值替换和 <var...> 列表展开
 *
 * 核心算法:
 * - <var> 普通变量: 取列表的第一个值
 * - <var...> 展开变量: 逐行消费列表中的值，列表未消费完则当前模板行不移除
 * - 空列表变量: 整行跳过（pass 机制）
 */
class StructureList(templates: List<String>) {

    private val parsedLines: List<List<VariableReader.Part>> = templates.map { reader.parse(it) }

    /**
     * 构建最终描述列表
     * @param vars 变量映射 Map<变量名, 值列表>（内部会消费列表展开变量）
     */
    fun build(vars: MutableMap<String, MutableList<String>>): List<String> {
        val result = mutableListOf<String>()
        // 使用可变队列处理模板行
        val queue = ArrayDeque(parsedLines)

        while (queue.isNotEmpty()) {
            val line = queue.first()
            var skip = false   // true = 当前行还有展开变量未消费完，不移除
            var pass = false   // true = 展开变量为空，跳过整行

            val built = StringBuilder()

            for (part in line) {
                when (part) {
                    is VariableReader.Part.Text -> {
                        built.append(part.content)
                    }
                    is VariableReader.Part.Variable -> {
                        // 普通变量: 取第一个值
                        val values = vars[part.name]
                        built.append(values?.firstOrNull() ?: "")
                    }
                    is VariableReader.Part.ListVariable -> {
                        // 展开变量
                        val values = vars[part.name]
                        if (values.isNullOrEmpty()) {
                            // 空列表 → 跳过整行
                            pass = true
                        } else {
                            // 消费第一个值
                            built.append(values.first())
                            if (values.size > 1) {
                                // 还有剩余，不移除当前模板行
                                skip = true
                                // 移除已消费的值
                                values.removeFirst()
                            } else {
                                // 最后一个值，清空列表
                                values.clear()
                            }
                        }
                    }
                }
                if (pass) break
            }

            if (!skip) {
                queue.removeFirst()
            }
            if (!pass) {
                result.add(built.toString())
            }
        }

        return result
    }

    companion object {
        private val reader = VariableReader("<", ">")
    }
}
