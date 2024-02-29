package cufoon.litkeep.android.page.user.manage

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.scale
import coil.compose.AsyncImage
import cufoon.litkeep.android.R
import cufoon.litkeep.android.component.TopBarWithBack
import cufoon.litkeep.android.rememberAppNavController
import cufoon.litkeep.android.rememberAppViewModel
import cufoon.litkeep.android.service.UserService
import cufoon.litkeep.android.store.MainViewModel
import cufoon.litkeep.android.theme.CurveCornerShape
import cufoon.litkeep.android.theme.LitColors
import cufoon.litkeep.android.util.ifNotNullOrElse
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import java.io.ByteArrayOutputStream


suspend fun uploadPhoto(bitmap: Bitmap): Boolean {
    val bos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos)
    val bytes = bos.toByteArray()
    val (err, data) = UserService.changeIcon(bytes, "icon", "image/jpeg".toMediaType())
    err.ifNotNullOrElse({
        return false
    }) {
        data?.changed?.let { return it }
        return false
    }
}

@Composable
fun ChangeAvatarPage() {
    val context = LocalContext.current
    val navController = rememberAppNavController()
    val vm: MainViewModel = rememberAppViewModel()
    val ss = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var bitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { it1 ->
            val source = ImageDecoder.createSource(context.contentResolver, it1)
            val bitmapTmp = ImageDecoder.decodeBitmap(source)
            bitmapTmp.let {
                bitmap = if (it.width > it.height) {
                    val tb1 = it.scale(800 * it.width / it.height + 1, 800)
                    val pad = (tb1.width - tb1.height) / 2
                    Bitmap.createBitmap(tb1, pad, 0, 800, 800)
                } else {
                    val tb1 = it.scale(800, 800 * it.height / it.width + 1)
                    val pad = (tb1.height - tb1.width) / 2
                    Bitmap.createBitmap(tb1, 0, pad, 800, 800)
                }
            }
        }
    }

    Column {
        TopBarWithBack({ "更改头像" })
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(ss)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.0f)
                    .padding(18.dp)
                    .clip(CurveCornerShape(20.dp))
            ) {
                AsyncImage(
                    model = bitmap ?: vm.userIcon.ifEmpty { R.drawable.user_icon },
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
                    .height(64.dp)
                    .clip(CurveCornerShape(20.dp))
                    .clickable {
                        launcher.launch("image/*")
                    }
                    .background(LitColors.LightPink),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) { Text("选择一张图片") }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp)
                    .height(64.dp)
                    .clip(CurveCornerShape(20.dp))
                    .clickable {
                        coroutineScope.launch {
                            bitmap?.let {
                                val isSuccess = uploadPhoto(it)
                                if (!isSuccess) {
                                    Toast
                                        .makeText(context, "更换失败", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    Toast
                                        .makeText(context, "更换成功", Toast.LENGTH_SHORT)
                                        .show()
                                    navController { navigateUp() }
                                }
                            }
                        }
                    }
                    .background(LitColors.Pink),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "确认更改头像", color = Color.White
                )
            }
        }
    }
}
