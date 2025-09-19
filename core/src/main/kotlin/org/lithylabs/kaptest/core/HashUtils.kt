package org.lithylabs.kaptest.core

import java.security.*

object HashUtils {

    fun sha1(input: String) = hashString("SHA-1", input)
    fun sha256(input: String) = hashString("SHA-256", input)
    fun md5(input: String) = hashString("MD5", input)

    private fun hashString(type: String, input: String): String {
        val bytes = MessageDigest
                .getInstance(type)
                .digest(input.toByteArray())
        return bytes.toHexString().uppercase()
    }
}

fun String.sha256() = HashUtils.sha256(this)
fun String.md5() = HashUtils.md5(this)
fun ByteArray.toHexString() = joinToString("") { (0xFF and it.toInt()).toString(16).padStart(2, '0') }
