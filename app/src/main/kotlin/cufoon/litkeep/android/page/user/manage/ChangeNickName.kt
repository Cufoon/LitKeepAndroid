package cufoon.litkeep.android.page.user.manage

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cufoon.litkeep.android.component.TopBarWithBack
import cufoon.litkeep.android.rememberAppNavController
import cufoon.litkeep.android.rememberAppViewModel
import cufoon.litkeep.android.service.UserService
import cufoon.litkeep.android.store.MainViewModel
import cufoon.litkeep.android.theme.CurveCornerShape
import cufoon.litkeep.android.util.ifNotNullOrElse
import kotlinx.coroutines.launch

private suspend fun changeUserNickName(nextNickName: String, cb: (Boolean) -> Unit) {
    val (err, data) = UserService.changeNickName(nextNickName)
    err.ifNotNullOrElse({
        Log.d("lit", it.info)
    }) {
        data?.let {
            cb(it.changed)
        }
    }
}


@Composable
fun ChangeNickNamePage() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val navController = rememberAppNavController()
    val vm: MainViewModel = rememberAppViewModel()
    val interactionSource = remember { MutableInteractionSource() }
    var nickname by remember { mutableStateOf("") }
    val hasValue by remember { derivedStateOf { nickname.isNotEmpty() } }

    Column {
        TopBarWithBack({ "修改昵称" }) {
            Text(text = "保存", fontSize = 18.sp, modifier = Modifier.clickable(
                interactionSource = interactionSource, indication = null
            ) {
                scope.launch {
                    changeUserNickName(nickname) {
                        if (it) {
                            navController { navigateUp() }
                        } else {
                            Toast.makeText(context, "修改失败", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 28.dp)
                .padding(18.dp, 4.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (vm.userNickName.isEmpty()) "你还没有昵称！" else "你现在的昵称：${vm.userNickName}",
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 28.dp)
                .padding(18.dp, 4.dp)
                .shadow(
                    25.dp, ambientColor = Color(0x20000000), spotColor = Color(0x10000000)
                )
                .clip(CurveCornerShape(20.dp))
                .requiredHeight(60.dp)
                .background(Color.White)
                .padding(6.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Text("昵称", modifier = Modifier.padding(horizontal = 12.dp))
            BasicTextField(value = nickname,
                onValueChange = { nickname = it },
                textStyle = TextStyle(fontSize = 16.sp, color = Color.DarkGray),
                modifier = Modifier
                    .focusable()
                    .fillMaxWidth(),
                cursorBrush = SolidColor(Color(0xFFE0909D)),
                decorationBox = { innerTextField ->
                    Box {
                        if (!hasValue) {
                            Text("请输入新昵称", color = Color.LightGray)
                        }
                        innerTextField()
                    }
                })
        }
    }
}
