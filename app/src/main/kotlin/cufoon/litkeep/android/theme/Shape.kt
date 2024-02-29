package cufoon.litkeep.android.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.min


val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp),
    extraSmall = RoundedCornerShape(4.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

class CurveCornerShape(
    private val tl: Dp = 0.dp,
    private val tr: Dp = 0.dp,
    private val br: Dp = 0.dp,
    private val bl: Dp = 0.dp
) : Shape {
    override fun createOutline(
        size: Size, layoutDirection: LayoutDirection, density: Density
    ): Outline {
        val w = size.width
        val w2 = w / 2
        val h = size.height
        val h2 = h / 2
        val maxR = min(w2, h2)
        val r1 = min(with(density) {
            this@CurveCornerShape.tl.roundToPx().toFloat()
        }, maxR)
        val r2 = min(with(density) {
            this@CurveCornerShape.tr.roundToPx().toFloat()
        }, maxR)
        val r3 = min(with(density) {
            this@CurveCornerShape.br.roundToPx().toFloat()
        }, maxR)
        val r4 = min(with(density) {
            this@CurveCornerShape.bl.roundToPx().toFloat()
        }, maxR)
        val rMagic1 = r1 * 2 / 5
        val rMagic2 = r2 * 2 / 5
        val rMagic3 = r3 * 2 / 5
        val rMagic4 = r4 * 2 / 5
        val path = Path().apply {
            moveTo(0f, r1)
            cubicTo(0f, rMagic1, rMagic1, 0f, r1, 0f)
            lineTo(w - r2, 0f)
            cubicTo(w - rMagic2, 0f, w, rMagic2, w, r2)
            lineTo(w, h - r3)
            cubicTo(w, h - rMagic3, w - rMagic3, h, w - r3, h)
            lineTo(r4, h)
            cubicTo(rMagic4, h, 0f, h - rMagic4, 0f, h - r4)
            close()
        }
        return Outline.Generic(path)
    }

    override fun toString(): String {
        return "CurveCornerShape(topStart = $tl, topEnd = $tr, bottomEnd = $br, bottomStart = $bl)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CurveCornerShape) return false

        if (tl != other.tl) return false
        if (tr != other.tr) return false
        if (br != other.br) return false
        if (bl != other.bl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = tl.hashCode()
        result = 31 * result + tr.hashCode()
        result = 31 * result + br.hashCode()
        result = 31 * result + bl.hashCode()
        return result
    }
}

fun CurveCornerShape(radius: Dp = 0.dp) = CurveCornerShape(radius, radius, radius, radius)
