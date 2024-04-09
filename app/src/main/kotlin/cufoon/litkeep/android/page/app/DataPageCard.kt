package cufoon.litkeep.android.page.app

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
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
import cufoon.litkeep.android.service.BillRecordService
import cufoon.litkeep.android.service.ReqBillRecordQueryStatisticDay
import cufoon.litkeep.android.theme.CurveCornerShape
import cufoon.litkeep.android.util.ifNotNullOrElse
import cufoon.litkeep.android.util.rememberMarker
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.abs


private suspend fun queryRecentWeek(
    recordType: Int,
    startTime: Long,
    endTime: Long? = null,
    setValue1: (List<String>) -> Unit,
    setValue2: (List<FloatEntry>) -> Unit
) {
    // exchange when start after end.
    var startTimeMut = startTime
    var endTimeMut = endTime
    if (endTimeMut != null && startTimeMut > endTimeMut) {
        val tmp = startTimeMut
        startTimeMut = endTimeMut
        endTimeMut = tmp
    }
    // the zero time of today.
    val ztt = OffsetDateTime.now().truncatedTo(ChronoUnit.DAYS)
    val startTimeQuery = ztt.plusDays(startTimeMut)
    val endTimeQuery = if (endTimeMut == null) ztt.plusDays(1).minusNanos(1)
    else ztt.plusDays(endTimeMut + 1).minusNanos(1)

    val (err, data) = BillRecordService.queryStatisticDay(
        ReqBillRecordQueryStatisticDay(
            startTimeQuery.toInstant().toEpochMilli(),
            endTimeQuery.toInstant().toEpochMilli(),
            recordType
        )
    )
    err.ifNotNullOrElse({
        Log.d("lit", it.info)
    }) {
        data?.let {
            val fmtList1 = mutableListOf<String>()
            val fmtList2 = mutableListOf<FloatEntry>()
            it.statistic?.forEachIndexed { idx, sl ->
                Log.d("lit", "get data: $idx $sl")
                fmtList1.add(sl.day.takeLast(2))
                fmtList2.add(FloatEntry(idx.toFloat(), abs(sl.money)))
            }
            Log.d("lit", "fmtList1: $fmtList1")
            Log.d("lit", "fmtList2: $fmtList2")
            setValue1(fmtList1)
            setValue2(fmtList2)
        }
    }
}

@Composable
internal fun OneCard(
    recordType: () -> Int,
    startTime: () -> Long,
    endTime: () -> Long,
    empty: @Composable RowScope.() -> Unit
) {
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
    val bottomAxisValueFormatter =
        AxisValueFormatter<AxisPosition.Horizontal.Bottom> { x, _ -> cxList[x.toInt()] }
    val bottomAxis = rememberBottomAxis(
        guideline = null, tick = null, axis = null, valueFormatter = bottomAxisValueFormatter
    )
    val chartEntryProducer = {
        entryModelOf(recordList)
    }

    LaunchedEffect(true) {
        queryRecentWeek(
            recordType(), startTime(), endTime(), setValue1 = setCxList, setValue2 = setRecordList
        )
    }
    LaunchedEffect(recordList) {
        val tmpMap = mutableMapOf<Float, Marker>()
        recordList.forEach {
            tmpMap[it.x] = markerStyle
        }
        setMarker(tmpMap)
    }

    Row(
        Modifier
            .fillMaxWidth()
            .padding(18.dp)
            .shadow(
                25.dp, ambientColor = Color(0x20000000), spotColor = Color(0x10000000)
            )
            .clip(CurveCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(18.dp, 36.dp)
            .height(120.dp)
    ) {
        if (recordList.isNotEmpty()) {
            Chart(
                chart = columnChart(
                    persistentMarkers = marker, columns = listOf(
                        LineComponent(
                            color = MaterialTheme.colorScheme.tertiary.toArgb(),
                            thicknessDp = 8f,
                            shape = Shapes.roundedCornerShape(allPercent = 40)
                        )
                    )
                ),
                model = chartEntryProducer(),
                modifier = Modifier.fillMaxSize(),
                bottomAxis = bottomAxis
            )
        } else {
            empty()
        }
    }
}