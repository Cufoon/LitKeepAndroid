package cufoon.litkeep.android.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.zIndex


@Composable
fun Sticky(
    shouldStick: () -> Boolean, content: @Composable RowScope.() -> Unit
) {
//    val statusBarHeight = with(LocalDensity.current) {
//        WindowInsets.statusBars.asPaddingValues().calculateTopPadding().toPx().toInt()
//    }
    val alphaAnimate by animateFloatAsState(if (shouldStick()) 1f else 0f)

    Row(
        Modifier
            .graphicsLayer {
                alpha = alphaAnimate
            }
            .zIndex(100f), content = content)
}
