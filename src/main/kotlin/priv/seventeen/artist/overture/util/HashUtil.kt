package priv.seventeen.artist.overture.util

import java.security.MessageDigest

/**
 * SHA-1 版本签名工具
 */
object HashUtil {

    private val digest = ThreadLocal.withInitial {
        MessageDigest.getInstance("SHA-1")
    }

    /**
     * 计算字符串的 SHA-1 哈希
     */
    fun sha1(input: String): String {
        val md = digest.get()
        md.reset()
        val bytes = md.digest(input.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }

    /**
     * 计算多个字符串拼接后的 SHA-1
     */
    fun sha1(vararg inputs: String): String {
        return sha1(inputs.joinToString("|"))
    }
}
