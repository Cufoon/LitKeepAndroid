package cufoon.litkeep.android.util

import android.graphics.Typeface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.component.lineComponent
import com.patrykandpatrick.vico.compose.component.overlayingComponent
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.core.component.marker.MarkerComponent
import com.patrykandpatrick.vico.core.component.shape.DashedShape
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.cornered.Corner
import com.patrykandpatrick.vico.core.component.shape.cornered.MarkerCorneredShape
import com.patrykandpatrick.vico.core.extension.copyColor
import com.patrykandpatrick.vico.core.marker.Marker


@Composable
internal fun rememberMarker(): Marker {
    val colorSecondaryContainer = MaterialTheme.colorScheme.secondaryContainer.copy(.2f)
    val labelBackgroundColor = MaterialTheme.colorScheme.secondaryContainer
    val labelBackground = remember(labelBackgroundColor) {
        ShapeComponent(
            MarkerCorneredShape(Corner.FullyRounded), labelBackgroundColor.toArgb()
        ).setShadow(
            color = colorSecondaryContainer.toArgb(),
            radius = 2f,
            dy = 2f,
            applyElevationOverlay = true
        )
    }
    val label = textComponent(
        color = MaterialTheme.colorScheme.onSecondary,
        background = labelBackground,
        lineCount = 1,
        padding = dimensionsOf(8.dp, 4.dp),
        typeface = Typeface.MONOSPACE,
    )

    val indicatorOuterComponent =
        shapeComponent(Shapes.pillShape, MaterialTheme.colorScheme.primary)
    val indicatorInnerComponent =
        shapeComponent(Shapes.pillShape, MaterialTheme.colorScheme.onPrimary)
    val indicator = overlayingComponent(
        outer = indicatorOuterComponent,
        inner = indicatorInnerComponent,
        innerPaddingAll = 4.dp,
    )

    val guideline = lineComponent(
        MaterialTheme.colorScheme.secondary.copy(.9f),
        5.dp,
        DashedShape(Shapes.pillShape, 6f, 4f),
        strokeWidth = 3.dp
    )

    return remember(label, indicator, guideline) {
        object : MarkerComponent(label, indicator, guideline) {
            init {
                indicatorSizeDp = 20f
                onApplyEntryColor = { entryColor ->
                    with(indicatorOuterComponent) {
                        color = entryColor.copyColor(80)
                        setShadow(
                            radius = 16f, color = entryColor
                        )
                    }
                }
            }
        }
    }
}
