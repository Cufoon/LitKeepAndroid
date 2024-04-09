package cufoon.litkeep.android.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cufoon.litkeep.android.theme.CurveCornerShape


@Composable
fun Switch(
    items: () -> List<String>, selected: () -> Int, onChanged: (Int) -> Unit, color: () -> Color
) {
    Row(
        Modifier.border(2.dp, MaterialTheme.colorScheme.secondary, CurveCornerShape(16.dp))
    ) {
        items().forEachIndexed { index, item ->
            Item(nowAt = selected, idx = { index }, item = { item }, onClick = onChanged, color)
        }
    }
}

@Composable
private fun Item(
    nowAt: () -> Int, idx: () -> Int, item: () -> String, onClick: (Int) -> Unit, color: () -> Color
) {
    val selected by remember {
        derivedStateOf { nowAt() == idx() }
    }
    val interactionSource = remember { MutableInteractionSource() }

    Column(Modifier
        .clip(CurveCornerShape(16.dp))
        .clickable(interactionSource = interactionSource, indication = null) {
            onClick(idx())
        }
        .background(if (selected) MaterialTheme.colorScheme.secondary else Color.Transparent)
        .padding(12.dp, 6.dp)) {
        Text(
            item(),
            fontSize = 18.sp,
            color = if (selected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}