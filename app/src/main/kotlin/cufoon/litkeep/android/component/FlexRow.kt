package cufoon.litkeep.android.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import kotlin.math.roundToInt


@Composable
fun FlowRow(
    horizontalGap: Dp = 0.dp,
    verticalGap: Dp = 0.dp,
    alignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable () -> Unit,
) = Layout(content = content) { measurableList, constraints ->
    val horizontalGapPx = horizontalGap.toPx().roundToInt()
    val verticalGapPx = verticalGap.toPx().roundToInt()

    val rows = mutableListOf<Row>()
    var rowConstraints = constraints
    var rowPlaceableList = mutableListOf<Placeable>()

    measurableList.forEach { measurable ->
        val placeable = measurable.measure(Constraints())
        if (placeable.measuredWidth !in rowConstraints.minWidth..rowConstraints.maxWidth) {
            rows += Row(rowPlaceableList, horizontalGapPx)
            rowConstraints = constraints
            rowPlaceableList = mutableListOf()
        }
        val consumedWidth = placeable.measuredWidth + horizontalGapPx
        rowConstraints = rowConstraints.offset(horizontal = -consumedWidth)
        rowPlaceableList.add(placeable)
    }
    rows += Row(rowPlaceableList, horizontalGapPx)

    val width = constraints.maxWidth
    val height = (rows.sumOf { row -> row.height } + (rows.size - 1) * verticalGapPx).coerceAtMost(
        constraints.maxHeight
    )

    layout(width, height) {
        var y = 0
        rows.forEach { row ->
            val offset = alignment.align(row.width, width, layoutDirection)
            var x = offset
            row.placeableList.forEach { placeable ->
                placeable.placeRelative(x, y)
                x += placeable.width + horizontalGapPx
            }
            y += row.height + verticalGapPx
        }
    }
}

private class Row(
    val placeableList: List<Placeable>,
    val horizontalGapPx: Int,
) {
    val width by lazy(mode = LazyThreadSafetyMode.NONE) {
        placeableList.sumOf { it.width } + (placeableList.size - 1) * horizontalGapPx
    }

    val height by lazy(mode = LazyThreadSafetyMode.NONE) {
        placeableList.maxOfOrNull { it.height } ?: 0
    }
}

@Composable
private fun Preview(alignment: Alignment.Horizontal) {
    Box(Modifier.width(100.dp)) {
        FlowRow(
            horizontalGap = 8.dp,
            verticalGap = 8.dp,
            alignment = alignment,
        ) {
            repeat(17) { index ->
                Text(text = index.toString())
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAlignStart() = Preview(alignment = Alignment.Start)

@Preview
@Composable
private fun PreviewAlignCenter() = Preview(alignment = Alignment.CenterHorizontally)

@Preview
@Composable
private fun PreviewAlignEnd() = Preview(alignment = Alignment.End)