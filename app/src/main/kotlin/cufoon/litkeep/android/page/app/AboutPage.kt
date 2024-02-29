package cufoon.litkeep.android.page.app

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import cufoon.litkeep.android.R
import cufoon.litkeep.android.component.Divider
import cufoon.litkeep.android.component.Option
import cufoon.litkeep.android.component.Options
import cufoon.litkeep.android.component.TopBarWithBack
import cufoon.litkeep.android.service.AppService
import cufoon.litkeep.android.theme.CurveCornerShape
import cufoon.litkeep.android.theme.ForLiTFont
import cufoon.litkeep.android.theme.LitColors
import cufoon.litkeep.android.util.ifNotNullOrElse
import kotlinx.coroutines.launch


@Composable
fun AboutPage() {
    val uriHandler = LocalUriHandler.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        Modifier
            .fillMaxSize()
            .background(LitColors.WhiteBackground)
    ) {
        TopBarWithBack({ "关于糖记" }) {}
        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = 96.dp, bottom = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painterResource(R.drawable.logo), "logo image", Modifier.width(96.dp))
            Text(
                stringResource(R.string.app_name),
                Modifier.padding(vertical = 6.dp),
                textAlign = TextAlign.Center
            )
        }
        Text(
            "A way to manage your bill record lightly!",
            Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Options(Modifier.padding(top = 32.dp)) {
            Option("检查更新", icon = {
                AsyncImage(
                    model = R.drawable.icons8_connection_sync_v1,
                    contentDescription = "option_icon",
                    modifier = Modifier.size(29.dp)
                )
            }, hasArrow = false, modifier = Modifier.clickable {
                scope.launch {
                    val (err, data) = AppService.checkUpdate()
                    err.ifNotNullOrElse({
                        Toast.makeText(context, "检查失败", Toast.LENGTH_SHORT).show()
                    }) {
                        data?.let {
                            if (it.update) {
                                Toast.makeText(context, "有新版本！", Toast.LENGTH_SHORT).show()
                                uriHandler.openUri(it.url)
                            } else {
                                Toast.makeText(context, "已是最新版本", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

            }) {
                Text("v1.0.0")
            }
            Divider()
            Option("Github", icon = {
                AsyncImage(
                    model = R.drawable.icons8_github,
                    contentDescription = "option_icon",
                    modifier = Modifier.size(28.dp)
                )
            }, modifier = Modifier.clickable {
                uriHandler.openUri("https://github.com/Cufoon")
            })
            Divider()
            Option("作者首页", icon = {
                AsyncImage(
                    model = R.drawable.author_logo,
                    contentDescription = "option_icon",
                    modifier = Modifier.size(28.dp)
                )
            }, modifier = Modifier.clickable {
                uriHandler.openUri("https://cufoon.com")
            })
        }
        Box(
            Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = R.drawable.icons8_flower_96,
                contentDescription = "author icon",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .clip(CurveCornerShape(20.dp, 0.dp))
                    .alpha(0.8f)
            )
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 96.dp)
                    .padding(18.dp, 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "红豆生南国，",
                    fontFamily = ForLiTFont,
                    color = Color(0xFF888888),
                    modifier = Modifier.padding(3.dp)
                )
                Text(
                    text = "春来发几枝。",
                    fontFamily = ForLiTFont,
                    color = Color(0xFF888888),
                    modifier = Modifier.padding(3.dp)
                )
                Text(
                    text = "愿君多采撷，",
                    fontFamily = ForLiTFont,
                    color = Color(0xFF888888),
                    modifier = Modifier.padding(3.dp)
                )
                Text(
                    text = "此物最相思。",
                    fontFamily = ForLiTFont,
                    color = Color(0xFF888888),
                    modifier = Modifier.padding(3.dp)
                )
                Text(
                    "@Cufoon  ",
                    fontFamily = ForLiTFont,
                    color = Color(0xFF888888),
                    modifier = Modifier.padding(top = 32.dp, start = 3.dp, end = 3.dp)
                )
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {}
    }
}
