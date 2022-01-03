package com.example.hoinzeyshabits.utils

object StringUtils {

    fun nullOrEmpty(toCheck: String?): Boolean {
        return toCheck == null || toCheck.trim { it <= ' ' }.isEmpty()
    }
}