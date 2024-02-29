package cufoon.litkeep.android.page.user.manage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cufoon.litkeep.android.R
import cufoon.litkeep.android.ROUTE_USER_PROFILE_EDIT_AVATAR
import cufoon.litkeep.android.ROUTE_USER_PROFILE_EDIT_NICKNAME
import cufoon.litkeep.android.component.Avatar
import cufoon.litkeep.android.component.DialogOption
import cufoon.litkeep.android.component.Option
import cufoon.litkeep.android.component.Options
import cufoon.litkeep.android.component.TopBarWithBack
import cufoon.litkeep.android.component.rememberGlobalDialogController
import cufoon.litkeep.android.page.app.UserInfo
import cufoon.litkeep.android.page.app.getInfo
import cufoon.litkeep.android.rememberAppNavController
import cufoon.litkeep.android.rememberAppViewModel
import cufoon.litkeep.android.store.MainViewModel
import cufoon.litkeep.android.util.MMKV


@Composable
fun UserManagePage() {
    val navigator = rememberAppNavController()
    val globalDialogController = rememberGlobalDialogController()
    val vm: MainViewModel = rememberAppViewModel()

    val interactionSource = remember { MutableInteractionSource() }
    var userInfo by remember { mutableStateOf(UserInfo()) }

    LaunchedEffect(true) {
        getInfo {
            userInfo = it
            vm.userIcon = it.iconURL
            vm.userNickName = it.nickname
        }
    }

    Column {
        TopBarWithBack({ "用户管理" })
        Row(
            Modifier
                .fillMaxWidth()
                .padding(32.dp), horizontalArrangement = Arrangement.Center
        ) {
            Avatar(userInfo.iconURL.ifEmpty { R.drawable.user_icon },
                Modifier
                    .shadow(
                        25.dp, ambientColor = Color(0x20000000), spotColor = Color(0x10000000)
                    )
                    .clickable(interactionSource = interactionSource, indication = null) {
                        navigator {
                            navigate(ROUTE_USER_PROFILE_EDIT_AVATAR)
                        }
                    })
        }
        Options(Modifier.padding(top = 12.dp)) {
            Option(
                "昵称", modifier = Modifier.clickable(
                    interactionSource = interactionSource, indication = null
                ) {
                    navigator {
                        navigate(ROUTE_USER_PROFILE_EDIT_NICKNAME)
                    }
                }, hasArrow = true
            ) { Text(userInfo.nickname.ifEmpty { "你还没有昵称" }) }
            Option("用户名", hasArrow = false) { Text(userInfo.userId) }
            Option("邮箱", hasArrow = false) { Text(userInfo.email) }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 24.dp), horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "注销登录", color = Color(0x80DD0000), modifier = Modifier.clickable(
                MutableInteractionSource(), null
            ) {
                globalDialogController?.openDialog(DialogOption(onConfirm = {
                    MMKV.token = ""
                    MMKV.password = ""
                    MMKV.username = ""
                    navigator {
                        navigate("login") {
                            popUpTo("index") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                })) {
                    Text("确定要退出吗？")
                }
            })
        }
    }
}
