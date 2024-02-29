package cufoon.litkeep.android.page.record.add

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import cufoon.litkeep.android.R
import cufoon.litkeep.android.component.DateItem
import cufoon.litkeep.android.component.TimeItem
import cufoon.litkeep.android.component.rememberDatePickerDialog
import cufoon.litkeep.android.component.rememberTimePickerDialog
import cufoon.litkeep.android.theme.CurveCornerShape
import cufoon.litkeep.android.theme.LitColors
import java.util.Calendar


@Composable
internal fun AddRecordDateTime(
    modifier: Modifier = Modifier, timeSetter: (DateItem, TimeItem) -> Unit
) {
    val (selectedDate, openDatePickerDialog) = rememberDatePickerDialog(Calendar.getInstance())
    val (selectedTime, openTimePickerDialog) = rememberTimePickerDialog(Calendar.getInstance())

    LaunchedEffect(selectedDate, selectedTime) {
        timeSetter(selectedDate, selectedTime)
    }

    Box(
        modifier
            .clip(CurveCornerShape(12.dp))
            .background(LitColors.LightPink)
            .padding(8.dp)
    ) {
        Row(
            Modifier.align(Alignment.BottomEnd), verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = R.drawable.icons8_time,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .alpha(0.2f)
            )
        }
        Column(
            Modifier.background(Color(0x0cFFFFFF)), Arrangement.Center
        ) {
            Text("日期", fontSize = 16.sp)
            Text("$selectedDate", Modifier.clickable { openDatePickerDialog() })
            Text("$selectedTime", Modifier.clickable { openTimePickerDialog() })
        }
    }
}
