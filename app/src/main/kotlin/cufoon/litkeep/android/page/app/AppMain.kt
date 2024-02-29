package cufoon.litkeep.android.page.app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cufoon.litkeep.android.R
import cufoon.litkeep.android.component.TabItem
import cufoon.litkeep.android.rememberAppViewModel
import cufoon.litkeep.android.theme.LitColors


@Composable
fun AppMain() {
    val viewModel = rememberAppViewModel()

    val onBottomBarSelected = { index: Int ->
        viewModel.bottomBarNowAt = index
    }

    Surface {
        Column(
            Modifier.fillMaxSize(), Arrangement.SpaceBetween
        ) {
            Row(Modifier.weight(1f)) {
                when (viewModel.bottomBarNowAt) {
                    0 -> HomePage()
                    1 -> DataPage()
                    else -> MinePage()
                }
            }
            BottomBar({ viewModel.bottomBarNowAt }, onBottomBarSelected)
        }
    }
}

@Composable
fun BottomBar(nowAtProvider: () -> Int, whenSelect: (Int) -> Unit) {
    val nowAt = nowAtProvider()

    Row(
        Modifier
            .fillMaxWidth()
            .background(Color(255, 255, 255, 255))
            .windowInsetsPadding(WindowInsets.navigationBars)
            .height(52.dp),
        Arrangement.SpaceBetween
    ) {
        TabItem(
            Modifier
                .weight(1f)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(true, 26.dp, LitColors.MainPinkLight)
                ) { whenSelect(0) },
            { "首页" },
            { R.drawable.bottom_tab_home },
            { R.drawable.bottom_tab_home_on },
            { nowAt == 0 })
        TabItem(
            Modifier
                .weight(1f)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(true, 26.dp, LitColors.MainPinkLight)
                ) { whenSelect(1) },
            { "统计" },
            { R.drawable.bottom_tab_data },
            { R.drawable.bottom_tab_data_on },
            { nowAt == 1 })
        TabItem(
            Modifier
                .weight(1f)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(true, 26.dp, LitColors.MainPinkLight)
                ) { whenSelect(2) },
            { "我的" },
            { R.drawable.bottom_tab_mine },
            { R.drawable.bottom_tab_mine_on },
            { nowAt == 2 })
    }
}
