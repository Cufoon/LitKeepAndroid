package cufoon.litkeep.android.page.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cufoon.litkeep.android.component.StatusBarHolder
import cufoon.litkeep.android.component.Title
import java.time.LocalDate


@Composable
fun DataPage() {
    val scrollState = rememberScrollState()
    val today = LocalDate.now().dayOfWeek.value

    Column(
        Modifier
            .verticalScroll(scrollState)
            .padding(bottom = 64.dp)
    ) {
        StatusBarHolder()
        Title(text = { "本周消费" }, modifier = Modifier.padding(12.dp))
        OneCard(
            recordType = { 1 },
            startTime = { (1 - today).toLong() },
            endTime = { (7 - today).toLong() }
        ) {
            Text(
                text = "你还没有任何消费!",
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Center
            )
        }
        Title(text = { "本周收入" }, modifier = Modifier.padding(12.dp))
        OneCard(
            recordType = { 0 },
            startTime = { (1 - today).toLong() },
            endTime = { (7 - today).toLong() }
        ) {
            Text(
                text = "你还没有任何收入!",
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Center
            )
        }
    }
}
