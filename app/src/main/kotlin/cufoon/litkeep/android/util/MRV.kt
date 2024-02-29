package cufoon.litkeep.android.util


data class M4<out A, out B, out C, out D>(
    val first: A, val second: B, val third: C, val fourth: D
) {
    override fun toString(): String = "($first, $second, $third, $fourth)"
}

fun <T> M4<T, T, T, T>.toList(): List<T> = listOf(first, second, third, fourth)

data class M5<out A, out B, out C, out D, out E>(
    val first: A, val second: B, val third: C, val fourth: D, val fifth: E
) {
    override fun toString(): String = "($first, $second, $third, $fourth, $fifth)"
}

fun <T> M5<T, T, T, T, T>.toList(): List<T> = listOf(first, second, third, fourth, fifth)
