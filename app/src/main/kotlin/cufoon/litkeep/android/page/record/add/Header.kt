package cufoon.litkeep.android.page.record.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cufoon.litkeep.android.component.StatusBarHolder
import cufoon.litkeep.android.component.Switch
import cufoon.litkeep.android.component.TopBarWithBack


@Composable
internal fun AddRecordHeader(selected: () -> Int, onChanged: (Int) -> Unit) {
    val switchBg = MaterialTheme.colorScheme.primary
    Box(
        Modifier
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        TopBarWithBack({ "返回" }) {}
        Column {
            StatusBarHolder()
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Switch({ listOf("支出", "收入") }, selected, onChanged, { switchBg })
            }
        }
    }
}
