package cufoon.litkeep.android.page.app

import android.os.Parcelable
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import cufoon.litkeep.android.R
import cufoon.litkeep.android.ROUTE_ABOUT
import cufoon.litkeep.android.ROUTE_RECORD_KIND_MANAGE
import cufoon.litkeep.android.ROUTE_RECORD_MANAGE
import cufoon.litkeep.android.ROUTE_USER_MANAGE
import cufoon.litkeep.android.component.Avatar
import cufoon.litkeep.android.component.Divider
import cufoon.litkeep.android.component.Option
import cufoon.litkeep.android.component.Options
import cufoon.litkeep.android.component.StatusBarHolder
import cufoon.litkeep.android.rememberAppNavController
import cufoon.litkeep.android.service.LITKEEP_BACKEND_URL
import cufoon.litkeep.android.service.UserService
import cufoon.litkeep.android.util.ifNotNullOrElse
import kotlinx.parcelize.Parcelize


@Parcelize
data class UserInfo(
    val nickname: String = "",
    val userId: String = "",
    val iconURL: String = "",
    val email: String = ""
) : Parcelable

suspend fun getInfo(setInfo: (UserInfo) -> Unit) {
    val (err, data) = UserService.getInfo()
    err.ifNotNullOrElse({
        Log.d("lit", it.info)
    }) {
        data?.let {

            setInfo(
                UserInfo(
                    it.nickname,
                    it.userId,
                    if (it.iconPath.isNotEmpty()) "$LITKEEP_BACKEND_URL${it.iconPath}" else "",
                    it.email
                )
            )
        }
    }
}

@Composable
fun MinePage() {
    val navigator = rememberAppNavController()
    val scrollState = rememberScrollState()
    var userInfo by rememberSaveable { mutableStateOf(UserInfo()) }

    LaunchedEffect(true) {
        getInfo {
            userInfo = it
        }
    }

    Column(Modifier.verticalScroll(scrollState)) {
        StatusBarHolder()
        Row(
            Modifier
                .fillMaxWidth()
                .padding(24.dp, 40.dp, 12.dp, 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(
                userInfo.iconURL.ifEmpty { R.drawable.user_icon },
                Modifier.padding(end = 18.dp)
            )
            Column {
                Text(
                    text = userInfo.nickname.ifEmpty { "这个人很懒，还没有设置昵称。" },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "id: ${userInfo.userId.ifEmpty { "获取失败" }}",
                    color = Color(0xFF808080)
                )
            }
        }
        Options(Modifier.padding(top = 32.dp)) {
            Option("账户管理", icon = {
                AsyncImage(
                    model = R.drawable.icons8_bolivian_girl,
                    contentDescription = "option_icon",
                    modifier = Modifier.size(28.dp)
                )
            }, modifier = Modifier.clickable {
                navigator {
                    navigate(ROUTE_USER_MANAGE)
                }
            })
            Divider()
            Option("分类管理", icon = {
                AsyncImage(
                    model = R.drawable.icons8_categorize,
                    contentDescription = "option_icon",
                    modifier = Modifier.size(28.dp)
                )
            }, modifier = Modifier.clickable {
                navigator {
                    navigate(ROUTE_RECORD_KIND_MANAGE)
                }
            })
            Divider()
            Option("记录管理", icon = {
                AsyncImage(
                    model = R.drawable.icons8_cash_in_hand_v1,
                    contentDescription = "option_icon",
                    modifier = Modifier.size(28.dp)
                )
            }, modifier = Modifier.clickable {
                navigator {
                    navigate(ROUTE_RECORD_MANAGE)
                }
            })
            Divider()
            Option("关于糖记", icon = {
                AsyncImage(
                    model = R.drawable.icons8_iron_man_v2,
                    contentDescription = "option_icon",
                    modifier = Modifier.size(28.dp)
                )
            }, modifier = Modifier.clickable {
                navigator {
                    navigate(ROUTE_ABOUT)
                }
            })
        }
    }
}
