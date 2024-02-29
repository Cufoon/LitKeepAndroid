package cufoon.litkeep.android.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Dot(color: () -> Color) {
    Column(
        modifier = Modifier
            .padding(end = 3.dp)
            .size(12.dp)
            .background(color(), RoundedCornerShape(100))
    ) {}
}

@Composable
fun Money(money: () -> Double?, kind: () -> Int, color: () -> Color) {
    val moneyText = when (kind()) {
        1 -> "-${money()}"
        else -> "+${money()}"
    }
    Text(text = moneyText, color = color(), fontWeight = FontWeight.Bold, textAlign = TextAlign.End)
}

@Composable
fun BillRecordLine(
    mark: () -> String,
    kind: () -> String,
    money: () -> Double?,
    type: () -> Int,
    color: () -> Color
) {
    Row(
        Modifier
            .padding(8.dp, 4.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .sizeIn(minHeight = 48.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
            Dot(color)
            Column(Modifier.padding(horizontal = 6.dp)) {
                Text(mark())
                Text(kind(), color = Color(0xFF808080), fontSize = 12.sp)
            }
        }
        Row(Modifier.width(100.dp), horizontalArrangement = Arrangement.End) {
            Money(money = money, kind = type, color = color)
        }
    }
}
