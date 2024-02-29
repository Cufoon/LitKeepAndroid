package cufoon.litkeep.android.page.record.manage

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cufoon.litkeep.android.component.BillRecordLine
import cufoon.litkeep.android.component.Dialog
import cufoon.litkeep.android.component.DialogOption
import cufoon.litkeep.android.component.TopBarWithBack
import cufoon.litkeep.android.service.BillRecordService
import cufoon.litkeep.android.service.Err
import cufoon.litkeep.android.theme.CurveCornerShape
import cufoon.litkeep.android.theme.LitColors
import cufoon.litkeep.android.util.ifNotNullOrElse
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.time.OffsetDateTime


@Composable
fun InfiniteListHandler(
    listState: LazyListState, buffer: Int = 10, onLoadMore: suspend () -> Unit
) {
    val loadMore = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItemsNumber = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1

            lastVisibleItemIndex > (totalItemsNumber - buffer)
        }
    }

    LaunchedEffect(loadMore) {
        snapshotFlow { loadMore.value }.distinctUntilChanged().collect {
            onLoadMore()
        }
    }
}

private data class DataItem(
    val Id: Int? = null,
    val Type: Int? = null,
    val Kind: String? = null,
    val Value: Double? = null,
    val Mark: String? = null
)

private fun fmtTime(t: OffsetDateTime?): String {
    if (t == null) {
        return ""
    }
    return "${t.year}年${"%02d".format(t.monthValue)}月${
        t.dayOfMonth.toString().padStart(2, '0')
    }日 ${"%02d".format(t.hour)}:${t.minute.toString().padStart(2, '0')}"
}

private suspend fun getBillPage(page: Int, kindsMap: Map<String, String>): MutableList<DataItem>? {
    val (err, data) = BillRecordService.queryPage(page)
    err.ifNotNullOrElse({
        return null
    }) {
        data?.let {
            val fmtList = mutableListOf<DataItem>()
            it.record.forEach { record ->
                val kn = kindsMap[record.Kind]
                var mark = record.Mark
                if (mark.isNullOrEmpty()) {
                    mark = kn
                }
                fmtList.add(
                    DataItem(
                        record.ID, record.Type, "$kn\n${fmtTime(record.Time)}", record.Value, mark
                    )
                )
            }
            return fmtList
        }
    }
    return null
}

private suspend fun getBillPageData(): Triple<Err?, Map<String, String>, Int> {
    val (err, data) = BillRecordService.queryPageData()
    err.ifNotNullOrElse({
        return Triple(err, mapOf(), 0)
    }) {
        data?.let {
            val kindMap = it.kinds.associateBy({ k -> k.KindID }, { k -> k.Name })
            return Triple(null, kindMap, it.pageData.totalPages)
        }
    }
    return Triple(Err(1, "获取的信息为空"), mapOf(), 0)
}

private suspend fun deleteOneRecord(id: Int): Boolean {
    val (err, data) = BillRecordService.deleteOne(id)
    err.ifNotNullOrElse({
        return false
    }) {
        data?.let {
            if (it.notDeleted.isNullOrEmpty()) {
                return true
            }
        }
    }
    return false
}

@Composable
fun RecordManagePage() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    var kindsMap by remember { mutableStateOf(mapOf<String, String>()) }
    var dataPages by remember { mutableStateOf(0) }
    val items = remember { mutableStateListOf<DataItem>() }
    var loadPage by remember { mutableStateOf(0) }
    var isShowDialog by remember { mutableStateOf(false) }
    var idToDelete by remember { mutableStateOf<Int?>(null) }

    suspend fun onLoadMore() {
        Log.d("lit", "to load page ($loadPage)")
        if (loadPage < dataPages) {
            val nextPage = getBillPage(loadPage, kindsMap)
            nextPage?.let {
                items.addAll(it)
            }
            loadPage += 1
        }
    }

    LaunchedEffect(true) {
        val (err, kMap, allPages) = getBillPageData()
        err.ifNotNullOrElse({
            Toast.makeText(context, "信息获取错误", Toast.LENGTH_LONG).show()
        }) {
            kindsMap = kMap
            dataPages = allPages
            onLoadMore()
        }
    }

    Box {
        Column {
            TopBarWithBack({ "记录管理" }) {}
            LazyColumn(
                modifier = Modifier.fillMaxSize(), state = listState
            ) {
                item {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .height(22.dp)
                    ) {}
                }
                items(items) { userItem ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(12.dp, 6.dp)
                            .clip(CurveCornerShape(16.dp))
                            .background(Color.White)
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            Modifier.weight(1f)
                        ) {
                            BillRecordLine(kind = { userItem.Kind ?: "none" },
                                mark = { userItem.Mark ?: "none" },
                                money = { userItem.Value },
                                type = { userItem.Type ?: 0 },
                                color = { if (userItem.Type != 0) LitColors.Expenditure else LitColors.Income })
                        }
                        Column(
                            Modifier
                                .wrapContentWidth()
                                .padding(horizontal = 8.dp)
                        ) {
                            Button(onClick = {
                                idToDelete = userItem.Id
                                isShowDialog = true
                            }, shape = CurveCornerShape(10.dp)) {
                                Text("删除", color = Color(0xFFFFFFFF))
                            }
                        }
                    }
                }
                item {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .height(22.dp)
                    ) {}
                }
            }

            InfiniteListHandler(listState = listState) {
                onLoadMore()
            }
        }
        Dialog(visible = { isShowDialog }, optionsProvider = {
            DialogOption(title = "修改分类", onCancel = {
                isShowDialog = false
            }) {
                coroutineScope.launch {
                    idToDelete?.let {
                        val isDeleted = deleteOneRecord(it)
                        if (isDeleted) {
                            items.removeAll { dataItem -> dataItem.Id == it }
                        }
                        Toast.makeText(
                            context, if (isDeleted) "删除成功" else "删除失败", Toast.LENGTH_SHORT
                        ).show()
                    }
                    isShowDialog = false
                }
            }
        }) {
            Text(
                "确定要删除吗？",
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp), fontSize = 16.sp
            )
        }
    }
}
