package cufoon.litkeep.android.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cufoon.litkeep.android.R
import cufoon.litkeep.android.theme.CurveCornerShape


@Composable
fun Divider() {
    Row(
        Modifier
            .fillMaxWidth()
            .height(1.dp)
            .padding(horizontal = 50.dp)
            .background(Color(0xFFF0F0F0))
    ) {}
}

@Composable
fun Option(
    name: String,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    hasArrow: Boolean = true,
    rightContent: (@Composable () -> Unit)? = null
) {
    Row(
        Modifier
            .clip(CurveCornerShape(20.dp))
            .then(modifier)
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            icon?.let {
                Column(Modifier.padding(end = 5.dp)) { it() }
            }
            Text(text = name, fontSize = 17.sp)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            rightContent?.let { it() }
            if (hasArrow) {
                Image(
                    painter = painterResource(id = R.drawable.config_option_arrow),
                    contentDescription = "option_arrow",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun Options(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(18.dp, 4.dp)
            .shadow(
                25.dp, ambientColor = Color(0x20000000), spotColor = Color(0x10000000)
            )
            .clip(CurveCornerShape(20.dp))
            .background(Color(0xFFFFFFFF))
            .padding(6.dp),
        content = content
    )
}
