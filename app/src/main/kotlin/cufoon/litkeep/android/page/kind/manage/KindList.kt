package cufoon.litkeep.android.page.kind.manage

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import cufoon.litkeep.android.component.Title
import cufoon.litkeep.android.service.BillKind
import cufoon.litkeep.android.service.BillKindParent
import cufoon.litkeep.android.service.BillKindService
import cufoon.litkeep.android.theme.CurveCornerShape
import cufoon.litkeep.android.util.ifNotNullOrElse


data class DataItem(
    val id: String, val type: Int, val origin: BillKind
)

fun convertData(origin: List<BillKindParent>?): List<DataItem> {
    val result = mutableListOf<DataItem>()
    origin?.forEach {
        result.add(
            DataItem(
                it.KindID, 0, BillKind(it.UserID, it.KindID, it.Name, it.Description)
            )
        )
        it.Children?.let { children ->
            if (children.size == 1) {
                val item = children[0]
                result.add(DataItem(item.KindID, 1, item))
            } else {
                children.forEachIndexed { index, child ->
                    when (index) {
                        0 -> result.add(DataItem(child.KindID, 2, child))
                        children.lastIndex -> result.add(DataItem(child.KindID, 4, child))
                        else -> result.add(DataItem(child.KindID, 3, child))
                    }
                }
            }
        }
    }
    return result
}

@Composable
fun RenderListItem(
    paddingProvider: () -> PaddingValues,
    shapeProvider: () -> Shape,
    onItemClick: () -> Unit,
    item: BillKind
) {
    val padding by rememberUpdatedState(paddingProvider())
    val shape by rememberUpdatedState(shapeProvider())
    Row(Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp)
        .shadow(
            25.dp, ambientColor = Color(0x20000000), spotColor = Color(0x10000000)
        )
        .clip(shape)
        .background(Color.White)
        .padding(padding)
        .clip(CurveCornerShape(12.dp))
        .clickable {
            onItemClick()
        }
        .padding(6.dp), Arrangement.SpaceBetween) {
        Column {
            Text(item.Name)
            Text(
                item.Description.ifEmpty { "暂时还没有描述" }, color = Color(0xFFC2C2C2)
            )
        }
        Button(onClick = onItemClick, shape = CurveCornerShape(10.dp)) {
            Text("编辑", color = Color(0xFFFFFFFF))
        }
    }
}

@Composable
fun Divider() {
    Row(
        Modifier
            .fillMaxWidth()
            .height(1.dp)
            .padding(horizontal = 12.dp)
            .background(Color.White)
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(1.dp))
            .background(Color(0xFFF0F0F0))
    ) {

    }
}

@Composable
fun RenderItem(dataProvider: () -> DataItem, onItemClick: (BillKind) -> Unit) {
    val data by rememberUpdatedState(dataProvider())
    when (data.type) {
        0 -> Row {
            Title(Modifier.padding(vertical = 20.dp), { data.origin.Name })
        }

        1 -> RenderListItem(
            paddingProvider = { PaddingValues(6.dp, 12.dp) },
            shapeProvider = { CurveCornerShape(12.dp) },
            onItemClick = { onItemClick(data.origin) },
            item = data.origin
        )

        2 -> {
            RenderListItem(
                paddingProvider = { PaddingValues(6.dp, 12.dp, 6.dp, 3.dp) },
                shapeProvider = { CurveCornerShape(12.dp, 12.dp) },
                onItemClick = { onItemClick(data.origin) },
                item = data.origin
            )
            Divider()
        }

        3 -> {
            RenderListItem(
                paddingProvider = { PaddingValues(6.dp, 3.dp, 6.dp, 3.dp) },
                shapeProvider = { CurveCornerShape() },
                onItemClick = { onItemClick(data.origin) },
                item = data.origin
            )
            Divider()
        }

        else -> RenderListItem(
            paddingProvider = { PaddingValues(6.dp, 3.dp, 6.dp, 12.dp) },
            shapeProvider = { CurveCornerShape(br = 12.dp, bl = 12.dp) },
            onItemClick = { onItemClick(data.origin) },
            item = data.origin
        )
    }
}

@Composable
fun KindListMain(onItemClick: (BillKind) -> Unit) {
    val (kindList, setKindList) = remember { mutableStateOf<List<DataItem>>(listOf()) }

    suspend fun queryKinds(time: Int, setValue: (List<DataItem>) -> Unit) {
        val (err, data) = BillKindService.query()
        err.ifNotNullOrElse({
            Log.d("lit", it.info)
        }) {
            data?.let {
                Log.d("lit", "query $time completed")
                if (it.kind.isNotEmpty()) {
                    setValue(convertData(it.kind))
                }
            }
        }
    }

    LaunchedEffect(true) {
        queryKinds(2, setKindList)
    }

    if (kindList.isEmpty()) {
        Text(text = "没有分类")
    } else {
        LazyColumn(
            Modifier.fillMaxHeight()
        ) {
            items(items = kindList, key = { it.id }) {
                RenderItem(dataProvider = { it }, onItemClick = onItemClick)
            }
            item {
                Row(
                    Modifier
                        .height(24.dp)
                        .fillMaxWidth()
                ) {
                    // just placeholder
                }
            }
        }
    }
}
