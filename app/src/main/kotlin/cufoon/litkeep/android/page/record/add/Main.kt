package cufoon.litkeep.android.page.record.add

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cufoon.litkeep.android.component.DateItem
import cufoon.litkeep.android.component.TimeItem
import cufoon.litkeep.android.rememberAppNavController
import cufoon.litkeep.android.service.BillRecordService
import cufoon.litkeep.android.service.ReqBillRecordCreate
import cufoon.litkeep.android.theme.LitColors
import cufoon.litkeep.android.util.ifNotNullOrElse
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.ZoneOffset


suspend fun createRecord(
    recordType: Int, kindId: String, money: Double, Time: OffsetDateTime, Mark: String
): Boolean {
    val (err, data) = BillRecordService.create(
        ReqBillRecordCreate(
            type = recordType, kindID = kindId, value = money, time = Time, mark = Mark
        )
    )
    err.ifNotNullOrElse({
        Log.d("lit", it.info)
    }) {
        data?.let {
            if (it.created) {
                return true
            }
        }
    }
    return false
}

@Composable
fun RecordAddPage() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val navigator = rememberAppNavController()
    var comment by remember { mutableStateOf("") }
    var money by remember { mutableStateOf("") }
    var kindId by remember { mutableStateOf("") }
    var recordType by remember { mutableStateOf(0) }
    var date by remember { mutableStateOf(DateItem(2018, 5, 12)) }
    var time by remember { mutableStateOf(TimeItem(13, 14)) }

    fun onBack() {
        navigator {
            navigate("index") {
                popBackStack()
                popBackStack()
            }
        }
    }

    fun onSubmit() {
        val type = when (recordType) {
            0 -> 1
            else -> 0
        }
        scope.launch {
            Log.d("lit", "kindId <$kindId>")
            val result = createRecord(
                type, kindId, money.toDouble(), OffsetDateTime.of(
                    date.year,
                    date.month + 1,
                    date.day,
                    time.hour,
                    time.minute,
                    0,
                    0,
                    ZoneOffset.ofHours(8)
                ), comment
            )
            if (result) {
                Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show()
                onBack()
            } else {
                Toast.makeText(context, "添加失败", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(LitColors.WhiteBackground)
            .padding(bottom = 8.dp)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        AddRecordHeader({ recordType }, { recordType = it })
        KindSelector(Modifier.weight(1f), { kindId }, { kindId = it })
        Row(
            Modifier
                .padding(8.dp, 8.dp, 8.dp, 0.dp)
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AddRecordDateTime(Modifier.fillMaxHeight()) { d, t ->
                date = d
                time = t
            }
            AddRecordComment(
                { comment },
                Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
                    .fillMaxHeight()
            ) { comment = it }
        }
        KeyBoardArea({ money }, { money = it }, { onSubmit() }, { onBack() })
    }
}
