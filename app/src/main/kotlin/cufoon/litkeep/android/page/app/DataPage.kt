package cufoon.litkeep.android.page.app

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.marker.Marker
import cufoon.litkeep.android.component.StatusBarHolder
import cufoon.litkeep.android.component.Title
import cufoon.litkeep.android.service.BillRecordService
import cufoon.litkeep.android.service.ReqBillRecordQueryStatisticDay
import cufoon.litkeep.android.theme.CurveCornerShape
import cufoon.litkeep.android.util.ifNotNullOrElse
import cufoon.litkeep.android.util.rememberMarker
import java.time.OffsetDateTime


private suspend fun queryRecentWeek(
    startTime: Int,
    endTime: Int? = null,
    setValue1: (List<String>) -> Unit,
    setValue2: (List<FloatEntry>) -> Unit
) {
    var ndt = OffsetDateTime.now().plusDays(1)
    ndt = ndt.minusHours(ndt.hour.toLong()).minusMinutes(ndt.minute.toLong())
        .minusSeconds(ndt.second.toLong()).minusNanos(ndt.nano.toLong())
    val startTimeQuery = ndt.minusDays(startTime.toLong() + 1)
    val endTimeQuery =
        if (endTime == null) ndt.minusNanos(1) else ndt.minusDays(endTime.toLong()).minusNanos(1)
    val (err, data) = BillRecordService.queryStatisticDay(
        ReqBillRecordQueryStatisticDay(startTimeQuery, endTimeQuery)
    )
    err.ifNotNullOrElse({
        Log.d("lit", it.info)
    }) {
        data?.let {
            val fmtList1 = mutableListOf<String>()
            val fmtList2 = mutableListOf<FloatEntry>()
            it.statistic?.forEachIndexed { idx, sl ->
                fmtList1.add(sl.day.takeLast(2))
                fmtList2.add(FloatEntry(idx.toFloat(), if (sl.money > 0f) 0f else -sl.money))
            }
            setValue1(fmtList1)
            setValue2(fmtList2)
        }
    }
}

@Composable
fun DataPage() {
    val scrollState = rememberScrollState()
    val markerStyle = rememberMarker()

    val (cxList, setCxList) = remember {
        mutableStateOf<List<String>>(listOf())
    }
    val (recordList, setRecordList) = remember {
        mutableStateOf<List<FloatEntry>>(listOf())
    }
    val (marker, setMarker) = remember {
        mutableStateOf(mapOf<Float, Marker>())
    }

    LaunchedEffect(true) {
        queryRecentWeek(7, setValue1 = setCxList, setValue2 = setRecordList)
    }

    val bottomAxisValueFormatter =
        AxisValueFormatter<AxisPosition.Horizontal.Bottom> { x, _ -> cxList[x.toInt()] }

    val bottomAxis = rememberBottomAxis(
        guideline = null, tick = null, axis = null, valueFormatter = bottomAxisValueFormatter
    )

    val chartEntryProducer = {
        entryModelOf(recordList)
    }

    LaunchedEffect(recordList) {
        val tmpMap = mutableMapOf<Float, Marker>()
        recordList.forEach {
            tmpMap[it.x] = markerStyle
        }
        setMarker(tmpMap)
    }

    Column(
        Modifier
            .verticalScroll(scrollState)
            .padding(bottom = 64.dp)
    ) {
        StatusBarHolder()
        Title(text = { "近5日消费" }, modifier = Modifier.padding(12.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .padding(18.dp)
                .shadow(
                    25.dp, ambientColor = Color(0x20000000), spotColor = Color(0x10000000)
                )
                .clip(CurveCornerShape(20.dp))
                .background(Color.White)
                .padding(18.dp, 36.dp)
                .height(120.dp)
        ) {
            if (recordList.isNotEmpty()) {
                Chart(
                    chart = columnChart(
                        persistentMarkers = marker, columns = listOf(
                            LineComponent(
                                color = Color(0xFFE69797).toArgb(),
                                thicknessDp = 8f,
                                shape = Shapes.roundedCornerShape(allPercent = 40),
                            )
                        )
                    ),
                    model = chartEntryProducer(),
                    modifier = Modifier.fillMaxSize(),
                    bottomAxis = bottomAxis
                )
            } else {
                Text(
                    text = "你还没有任何消费!",
                    modifier = Modifier.fillMaxSize(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
