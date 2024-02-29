package cufoon.litkeep.android.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf


class ReactContext<T>(val ctx: ProvidableCompositionLocal<T?>) {
    @Composable
    fun Provider(value: T?, content: @Composable () -> Unit) {
        CompositionLocalProvider(
            ctx provides value, content = content
        )
    }
}

fun <T> createContext(value: T? = null): ReactContext<T> {
    val ctx = compositionLocalOf { value }
    return ReactContext(ctx)
}

@Composable
fun <T> useContext(ctx: ReactContext<T>): T? = ctx.ctx.current
