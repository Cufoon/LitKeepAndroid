package cufoon.litkeep.android.page.record.add

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cufoon.litkeep.android.theme.CurveCornerShape
import cufoon.litkeep.android.theme.LitColors


@Composable
internal fun AddRecordComment(
    comment: () -> String, modifier: Modifier = Modifier, setComment: (String) -> Unit
) {
    val hasValue by remember {
        derivedStateOf { comment().isNotEmpty() }
    }

    Column(
        modifier
            .clip(CurveCornerShape(12.dp))
            .background(LitColors.LightPink)
            .padding(8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("备注", fontSize = 16.sp)
        BasicTextField(value = comment(),
            onValueChange = setComment,
            textStyle = TextStyle(fontSize = 16.sp, color = Color.Gray),
            modifier = Modifier
                .focusable()
                .fillMaxWidth()
                .padding(top = 8.dp),
            cursorBrush = SolidColor(Color(0xFFE0909D)),
            decorationBox = { innerTextField ->
                Box {
                    if (!hasValue) {
                        Text("添加个备注吧", color = Color.LightGray)
                    }
                    innerTextField()
                }
            })
    }
}
