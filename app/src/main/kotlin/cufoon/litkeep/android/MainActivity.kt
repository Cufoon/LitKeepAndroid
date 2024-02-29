package cufoon.litkeep.android

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import cufoon.litkeep.android.service.TokenService
import cufoon.litkeep.android.store.MainViewModel
import cufoon.litkeep.android.util.ifNotNullOrElse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import com.tencent.mmkv.MMKV as tm


class MainActivity : ComponentActivity(), CoroutineScope by MainScope() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        val rootDir: String = tm.initialize(this)
        println("mmkv root: $rootDir")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val context = applicationContext
        val viewModel: MainViewModel by viewModels()
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                // Check if the initial data is ready.
                return if (viewModel.launchAppReady) {
                    // The content is ready; start drawing.
                    content.viewTreeObserver.removeOnPreDrawListener(this)
                    true
                } else {
                    // The content is not ready; suspend.
                    false
                }
            }
        })
        launch {
            val (err, data) = TokenService.verify()
            err.ifNotNullOrElse({
                Toast.makeText(context, "${it.code}---${it.info}", Toast.LENGTH_SHORT).show()
            }) {
                data?.let {
                    if (it.verified == 0) {
                        viewModel.tokenVerified = true
                    }
                }
            }
            viewModel.tokenChecked = true
            setContent(content = { AppRoot(viewModel) })
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    AppMain()
//}