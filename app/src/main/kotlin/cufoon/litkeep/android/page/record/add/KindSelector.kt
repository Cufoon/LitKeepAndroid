package cufoon.litkeep.android.page.record.add

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cufoon.litkeep.android.R
import cufoon.litkeep.android.service.BillKind
import cufoon.litkeep.android.service.BillKindParent
import cufoon.litkeep.android.service.BillKindService
import cufoon.litkeep.android.theme.CurveCornerShape
import cufoon.litkeep.android.theme.LitColors
import cufoon.litkeep.android.util.ifNotNullOrElse


suspend fun queryKinds(time: Int, setValue: (List<BillKindParent>) -> Unit) {
    val (err, data) = BillKindService.query()
    err.ifNotNullOrElse({
        Log.d("lit", it.info)
    }) {
        data?.let {
            Log.d("lit", "query $time completed")
            if (it.kind.isNotEmpty()) {
                setValue(it.kind)
            }
        }
    }
}

@Composable
internal fun KindSelector(
    modifier: Modifier = Modifier, selectedProvider: () -> String, onSelectKind: (String) -> Unit
) {
    var kindList by remember {
        mutableStateOf<List<BillKindParent>>(listOf())
    }
    var selectedSuperItemIdx by remember {
        mutableStateOf(0)
    }
    val selectedSuperItem by remember(selectedSuperItemIdx, kindList) {
        mutableStateOf(kindList.getOrNull(selectedSuperItemIdx))
    }
    val selected by rememberUpdatedState(selectedProvider())

    LaunchedEffect(true) {
        queryKinds(1) {
            kindList = it
            if (selected.isEmpty() && kindList.isNotEmpty()) {
                kindList[0].Children?.get(0)?.let { it1 -> onSelectKind(it1.KindID) }
            }
        }
    }

    BoxWithConstraints(
        modifier
            .padding(8.dp, 8.dp, 8.dp, 0.dp)
            .clip(CurveCornerShape(12.dp))
            .background(LitColors.LightPink)
    ) {
        Log.d("lit", Arrangement.Start.spacing.toString())
        val columnNum = with(LocalDensity.current) {
            maxOf(constraints.maxWidth / 80.dp.roundToPx(), 1)
        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 80.dp)
        ) {
            kindList.forEachIndexed { idx, kind ->
                item(kind.KindID) {
                    AKindSuper({ idx }, { selectedSuperItemIdx }, {
                        selectedSuperItemIdx = idx
                        if (kind.Children.isNullOrEmpty()) {
                            onSelectKind(kind.KindID)
                        } else {
                            onSelectKind(kind.Children[0].KindID)
                        }
                    }, { kind.Name })
                }
                if ((idx > 0 && (idx + 1) % columnNum == 0) || idx >= kindList.lastIndex) {
                    item(span = { GridItemSpan(columnNum) }) {
                        KindChildren(
                            superItemProvider = { selectedSuperItem },
                            selectedSuperItemIdxProvider = { selectedSuperItemIdx },
                            rangeStartProvider = { idx - columnNum + 1 },
                            {
                                idx
                            },
                            selectedProvider = selectedProvider,
                            onSelectKind
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AKindSuper(
    kindIdx: () -> Int, selected: () -> Int, onSelectKind: () -> Unit, name: () -> String
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isSelected by remember { derivedStateOf { kindIdx() == selected() } }

    Column(
        Modifier
            .padding(5.dp)
            .clip(CurveCornerShape(16.dp))
            .clickable(
                interactionSource = interactionSource, indication = null, onClick = onSelectKind
            )
            .padding(5.dp)
            .defaultMinSize(minWidth = 80.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.bottom_tab_mine),
            contentDescription = "icon",
            modifier = Modifier
                .clip(CircleShape)
                .size(24.dp)
                .background(if (isSelected) Color(0xFFE0909D) else Color.Transparent)
        )
        Text(name(), fontSize = 14.sp, color = if (isSelected) Color(0xFFE0909D) else Color.Black)
    }
}

@Composable
private fun AKindSub(
    selectedProvider: () -> String,
    itemProvider: () -> BillKind,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {
    val item by rememberUpdatedState(itemProvider())
    val isSelected by remember {
        derivedStateOf { selectedProvider() == item.KindID }
    }
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier
            .clip(CurveCornerShape(12.dp))
            .clickable(interactionSource = interactionSource, indication = null) {
                onClick(item.KindID)
            }
            .width(80.dp)
            .background(if (isSelected) Color(0xFFE0909D) else Color(0xFFE7E7E7))
            .padding(8.dp), Arrangement.Center, Alignment.CenterHorizontally) {
        Text(item.Name)
    }
}

@Composable
private fun KindChildren(
    superItemProvider: () -> BillKindParent?,
    selectedSuperItemIdxProvider: () -> Int,
    rangeStartProvider: () -> Int,
    rangEndProvider: () -> Int,
    selectedProvider: () -> String,
    onSelectKind: (String) -> Unit
) {
    val superItem by rememberUpdatedState(superItemProvider())
    val selectedIdx by rememberUpdatedState(selectedSuperItemIdxProvider())
    val rangeStart by rememberUpdatedState(rangeStartProvider())
    val rangeEnd by rememberUpdatedState(rangEndProvider())

    val onMyStage by remember {
        derivedStateOf { selectedIdx in rangeStart..rangeEnd }
    }

    if (onMyStage && superItem?.Children?.isNotEmpty() == true) {
        BoxWithConstraints(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(8.dp)
                .shadow(
                    25.dp, ambientColor = Color(0x20000000), spotColor = Color(0x10000000)
                )
                .clip(CurveCornerShape(16.dp))
                .background(Color(0xFFF5F5F5))
                .padding(8.dp)
        ) {
            val columnNum = with(LocalDensity.current) {
                maxOf(constraints.maxWidth / 80.dp.roundToPx(), 1)
            }
            superItem?.Children?.let {
                // https://blog.csdn.net/qq_41437512/article/details/128243628
                val rowNum = it.lastIndex / columnNum + 1
                Column(Modifier.fillMaxWidth()) {
                    (0 until rowNum).map { rowIdx ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            (0 until columnNum).map { colIdx ->
                                val idx = rowIdx * columnNum + colIdx
                                if (idx < it.size) {
                                    AKindSub(
                                        selectedProvider = selectedProvider,
                                        itemProvider = { it[idx] },
                                        onClick = onSelectKind
                                    )
                                } else {
                                    Column(Modifier.width(80.dp)) {}
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
