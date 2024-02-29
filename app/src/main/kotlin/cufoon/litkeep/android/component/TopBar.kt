package cufoon.litkeep.android.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cufoon.litkeep.android.R
import cufoon.litkeep.android.rememberAppNavController


@Composable
fun TopBar(
    title: () -> String = { "" }, content: @Composable () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .windowInsetsPadding(WindowInsets.statusBars)
            .requiredHeight(48.dp)
            .padding(horizontal = 10.dp),
        Arrangement.SpaceBetween,
        Alignment.CenterVertically
    ) {
        Text(title(), fontSize = 24.sp, color = Color(0xFF000000))
        content()
    }
}

@Composable
fun TopBarWithBack(
    title: () -> String = { "" }, content: (@Composable () -> Unit)? = null
) {
    val navigator = rememberAppNavController()
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        Modifier
            .fillMaxWidth()
            .background(Color(0x10000000))
            .padding(bottom = 1.dp)
            .background(Color.White)
            .windowInsetsPadding(WindowInsets.statusBars)
            .wrapContentHeight()
            .padding(10.dp),
        Arrangement.SpaceBetween,
        Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painterResource(R.drawable.icons8_back_arrow_120),
                null,
                Modifier
                    .clickable(interactionSource = interactionSource, indication = null) {
                        navigator {
                            navigateUp()
                        }
                    }
                    .padding(end = 5.dp)
                    .width(28.dp)
                    .height(28.dp))
            Text(
                title(), fontSize = 18.sp, color = Color(0xFF000000)
            )
        }
        content?.let {
            it()
        }
    }
}

@Composable
fun StatusBarHolder() {
    Row(
        Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars),
        Arrangement.SpaceBetween,
        Alignment.CenterVertically
    ) {}
}
