package cufoon.litkeep.android.page.app

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import cufoon.litkeep.android.R
import cufoon.litkeep.android.ROUTE_RECORD_ADD
import cufoon.litkeep.android.ROUTE_RECORD_MANAGE
import cufoon.litkeep.android.component.BillRecordLine
import cufoon.litkeep.android.component.StatusBarHolder
import cufoon.litkeep.android.component.Sticky
import cufoon.litkeep.android.component.Title
import cufoon.litkeep.android.rememberAppNavController
import cufoon.litkeep.android.service.BillRecordService
import cufoon.litkeep.android.service.ReqBillRecordQuery
import cufoon.litkeep.android.theme.CurveCornerShape
import cufoon.litkeep.android.theme.LitColors
import cufoon.litkeep.android.util.ifNotNullOrElse
import java.time.OffsetDateTime


internal data class DataItem(
    val Type: Int? = null,
    val Kind: String? = null,
    val Value: Double? = null,
    val Mark: String? = null
)

internal fun fmtTime(t: OffsetDateTime?): String {
    if (t == null) {
        return ""
    }
    return "${t.year}年${"%02d".format(t.monthValue)}月${
        t.dayOfMonth.toString().padStart(2, '0')
    }日 ${"%02d".format(t.hour)}:${t.minute.toString().padStart(2, '0')}"
}

private suspend fun queryRecentWeek(
    startTime: Int, endTime: Int? = null, setValue: (List<DataItem>) -> Unit
) {
    var ndt = OffsetDateTime.now().plusDays(1)
    ndt = ndt.minusHours(ndt.hour.toLong()).minusMinutes(ndt.minute.toLong())
        .minusSeconds(ndt.second.toLong()).minusNanos(ndt.nano.toLong())
    val startTimeQuery = ndt.minusDays(startTime.toLong() + 1)
    val endTimeQuery =
        if (endTime == null) ndt.minusNanos(1) else ndt.minusDays(endTime.toLong()).minusNanos(1)
    val (err, data) = BillRecordService.queryAndKind(
        ReqBillRecordQuery("", startTimeQuery, endTimeQuery)
    )
    err.ifNotNullOrElse({
        Log.d("lit", it.info)
    }) {
        data?.let {
            val kindMap = it.kinds.associateBy({ k -> k.KindID }, { k -> k.Name })
            val fmtList = mutableListOf<DataItem>()
            it.record.forEach { record ->
                val kn = kindMap[record.Kind]
                var mark = record.Mark
                if (mark.isNullOrEmpty()) {
                    mark = kn
                }
                fmtList.add(
                    DataItem(
                        record.Type, "$kn ${fmtTime(record.Time)}", record.Value, mark
                    )
                )
            }
            setValue(fmtList)
        }
    }
}

@Composable
fun HomePage() {
    val navigator = rememberAppNavController()
    val scrollState = rememberScrollState()
    var title1position by remember { mutableStateOf(-1) }
    var title2position by remember { mutableStateOf(-1) }
    var title3position by remember { mutableStateOf(-1) }
    val density = LocalDensity.current
    val spacing = with(density) {
        18.dp.roundToPx()
    }

    val shouldSticky1 by remember {
        derivedStateOf {
            title1position >= 0 && scrollState.value >= title1position
        }
    }

    val shouldSticky2 by remember {
        derivedStateOf {
            title2position >= 0 && scrollState.value >= title2position - spacing
        }
    }

    val shouldSticky3 by remember {
        derivedStateOf {
            title3position >= 0 && scrollState.value >= title3position - spacing
        }
    }

    val alphaAnimate1 by animateFloatAsState(if (shouldSticky1) 0f else 1f)
    val alphaAnimate2 by animateFloatAsState(if (shouldSticky2) 0f else 1f)
    val alphaAnimate3 by animateFloatAsState(if (shouldSticky3) 0f else 1f)

    val (recordList, setRecordList) = remember {
        mutableStateOf<List<DataItem>>(listOf())
    }

    val (recordList1, setRecordList1) = remember {
        mutableStateOf<List<DataItem>>(listOf())
    }

    val (recordList2, setRecordList2) = remember {
        mutableStateOf<List<DataItem>>(listOf())
    }

    LaunchedEffect(true) {
        queryRecentWeek(0, setValue = setRecordList)
        queryRecentWeek(7, 1, setRecordList1)
        queryRecentWeek(OffsetDateTime.now().dayOfMonth - 1, setValue = setRecordList2)
    }

    Column {
        StatusBarHolder()
        Box(
            Modifier.fillMaxSize()
        ) {
            Sticky({ shouldSticky1 }) {
                Title(
                    Modifier
                        .background(LitColors.WhiteBackground)
                        .padding(vertical = 20.dp), { "今日消费" })
            }
            Sticky({ shouldSticky2 }) {
                Title(
                    Modifier
                        .background(LitColors.WhiteBackground)
                        .padding(vertical = 20.dp),
                    { "此前七天" },
                    { Color(0xFFFADB5F) })
            }
            Sticky({ shouldSticky3 }) {
                Title(
                    Modifier
                        .background(LitColors.WhiteBackground)
                        .padding(vertical = 20.dp),
                    { "本月消费" },
                    { Color(0xFFA3CFFF) })
            }
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(bottom = 120.dp)
            ) {
                ContentListWithTitle({ recordList }, {
                    BillRecordLine(kind = { it.Kind ?: "none" },
                        mark = { it.Mark ?: "none" },
                        money = { it.Value },
                        type = { it.Type ?: 0 },
                        color = { if (it.Type != 0) LitColors.Expenditure else LitColors.Income })
                }) {
                    Title(
                        Modifier
                            .alpha(alphaAnimate1)
                            .onGloballyPositioned {
                                title1position = it.positionInParent().y.toInt()
                            }
                            .onPlaced {
                                title1position = it.positionInParent().y.toInt()
                            }
                            .padding(vertical = 20.dp), { "今日消费" })
                }
                ContentListWithTitle({ recordList1 }, {
                    BillRecordLine(kind = { it.Kind ?: "none" },
                        mark = { it.Mark ?: "none" },
                        money = { it.Value },
                        type = { it.Type ?: 0 },
                        color = { if (it.Type != 0) LitColors.Expenditure else LitColors.Income })
                }) {
                    Title(Modifier
                        .alpha(alphaAnimate2)
                        .onGloballyPositioned {
                            title2position = it.positionInParent().y.toInt()
                        }
                        .onPlaced {
                            title2position = it.positionInParent().y.toInt()
                        }
                        .padding(vertical = 20.dp)
                        .padding(top = 10.dp),
                        { "此前七天" },
                        { Color(0xFFFADB5F) })
                }
                ContentListWithTitle({ recordList2 }, {
                    BillRecordLine(kind = { it.Kind ?: "none" },
                        mark = { it.Mark ?: "none" },
                        money = { it.Value },
                        type = { it.Type ?: 0 },
                        color = { if (it.Type != 0) LitColors.Expenditure else LitColors.Income })
                }) {
                    Title(Modifier
                        .alpha(alphaAnimate3)
                        .onGloballyPositioned {
                            title3position = it.positionInParent().y.toInt()
                        }
                        .onPlaced {
                            title3position = it.positionInParent().y.toInt()
                        }
                        .padding(vertical = 20.dp)
                        .padding(top = 10.dp),
                        { "本月消费" },
                        { Color(0xFFA3CFFF) })
                }
                Row(
                    Modifier
                        .padding(top = 48.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(Modifier
                        .shadow(
                            25.dp,
                            ambientColor = Color(0x20000000),
                            spotColor = Color(0x10000000)
                        )
                        .clip(CurveCornerShape(20.dp))
                        .clickable {
                            navigator {
                                navigate(ROUTE_RECORD_MANAGE)
                            }
                        }
                        .background(Color.White)
                        .padding(14.dp)
                        .wrapContentSize()) {
                        Text("全部记录➡")
                    }
                }
            }
            Row(
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 30.dp, bottom = 30.dp)
            ) {
                Button(
                    onClick = {
                        navigator {
                            navigate(ROUTE_RECORD_ADD)
                        }
                    },
                    Modifier
                        .size(56.dp)
                        .align(Alignment.CenterVertically),
                    shape = CurveCornerShape(18.dp),
                    contentPadding = PaddingValues.Absolute(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xEEFF99AD))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.home_add_button),
                        contentDescription = "add button",
                        Modifier
                            .size(26.dp)
                            .padding(),
                        Color(0xFFFFFFFF)
                    )
                }
            }
        }
    }
}

@Composable
private fun <T> ContentListWithTitle(
    recordList: () -> List<T>?, itemRender: @Composable (T) -> Unit, title: @Composable () -> Unit
) {
    title()
    Row(
        Modifier
            .defaultMinSize(minHeight = 150.dp)
            .fillMaxSize()
            .padding(18.dp, 4.dp)
            .shadow(
                25.dp, ambientColor = Color(0x20000000), spotColor = Color(0x10000000)
            )
            .clip(CurveCornerShape(20.dp))
            .background(Color(0xFFFFFFFF))
    ) {
        Column(Modifier.padding(12.dp)) {
            recordList().ifNotNullOrElse({ rList ->
                if (rList.isEmpty()) {
                    EmptyTip()
                } else {
                    rList.forEach {
                        itemRender(it)
                    }
                }
            }) {
                EmptyTip()
            }
        }
    }
}

@Composable
private fun EmptyTip() {
    Column(
        Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 110.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(R.drawable.icons8_empty_box_96, contentDescription = null)
        Text(text = "暂无记录")
    }
}
