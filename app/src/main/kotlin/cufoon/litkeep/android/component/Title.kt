package cufoon.litkeep.android.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cufoon.litkeep.android.theme.CurveCornerShape


@Composable
fun Title(
    modifier: Modifier = Modifier,
    text: () -> String = { "" },
    lineColor: () -> Color = { Color(0xFFFFC4C4) }
) {
    Box(modifier) {
        Row(modifier = Modifier
            .width(50.dp)
            .height(15.dp)
            .offset(16.dp, 15.dp)
            .clip(CurveCornerShape(5.dp))
            .drawBehind { drawRect(lineColor()) }) {}
        Text(
            text = text(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp, 0.dp, 0.dp),
            color = Color(0xAF000000),
            fontSize = 20.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Default,
            letterSpacing = 2.sp,
            textDecoration = TextDecoration.None,
            textAlign = TextAlign.Left,
            lineHeight = 24.sp,
            overflow = TextOverflow.Ellipsis,
            softWrap = true,
            maxLines = 2,
            style = TextStyle(
                fontSize = 24.sp, shadow = Shadow(
                    color = Color(0x4F000000), offset = Offset(5f, 5f), blurRadius = 20f
                )
            )
        )
    }
}
