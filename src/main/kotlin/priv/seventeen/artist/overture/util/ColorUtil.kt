package priv.seventeen.artist.overture.util

/**
 * 颜色代码处理工具
 */
object ColorUtil {

    /**
     * 将 & 颜色代码转换为 § 颜色代码
     */
    fun colored(text: String): String {
        return translateAlternateColorCodes('&', text)
    }

    /**
     * 批量转换颜色代码
     */
    fun colored(texts: List<String>): List<String> {
        return texts.map { colored(it) }
    }

    /**
     * 转换颜色代码（支持 hex 颜色 &#RRGGBB）
     */
    private fun translateAlternateColorCodes(altColorChar: Char, text: String): String {
        val sb = StringBuilder(text.length)
        var i = 0
        while (i < text.length) {
            if (text[i] == altColorChar) {
                // 检查 hex 颜色 &#RRGGBB
                if (i + 7 < text.length && text[i + 1] == '#') {
                    val hex = text.substring(i + 2, i + 8)
                    if (hex.all { it.isDigit() || it in 'a'..'f' || it in 'A'..'F' }) {
                        sb.append("§x")
                        for (c in hex) {
                            sb.append("§").append(c.lowercaseChar())
                        }
                        i += 8
                        continue
                    }
                }
                // 普通颜色代码
                if (i + 1 < text.length) {
                    val code = text[i + 1]
                    if ("0123456789AaBbCcDdEeFfKkLlMmNnOoRr".contains(code)) {
                        sb.append('§').append(code.lowercaseChar())
                        i += 2
                        continue
                    }
                }
            }
            sb.append(text[i])
            i++
        }
        return sb.toString()
    }

    /**
     * 去除所有颜色代码
     */
    fun stripColor(text: String): String {
        return text.replace(Regex("§[0-9a-fk-or]|§x(§[0-9a-f]){6}"), "")
    }
}
