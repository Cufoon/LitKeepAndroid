package cufoon.litkeep.android.page.record.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cufoon.litkeep.android.component.StatusBarHolder
import cufoon.litkeep.android.component.Switch
import cufoon.litkeep.android.component.TopBarWithBack


@Composable
internal fun AddRecordHeader(selected: () -> Int, onChanged: (Int) -> Unit) {

    Box(
        Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart
    ) {
        TopBarWithBack({ "返回" }) {}
        Column {
            StatusBarHolder()
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Switch({ listOf("支出", "收入") }, selected, onChanged, { Color(0xFFD56374) })
            }
        }
    }
}
