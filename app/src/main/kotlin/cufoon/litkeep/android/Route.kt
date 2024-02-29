package cufoon.litkeep.android

import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cufoon.litkeep.android.component.DialogGlobal
import cufoon.litkeep.android.page.app.AboutPage
import cufoon.litkeep.android.page.app.AppMain
import cufoon.litkeep.android.page.kind.manage.BillKindManagePage
import cufoon.litkeep.android.page.record.add.RecordAddPage
import cufoon.litkeep.android.page.record.manage.RecordManagePage
import cufoon.litkeep.android.page.user.login.LoginPage
import cufoon.litkeep.android.page.user.manage.ChangeAvatarPage
import cufoon.litkeep.android.page.user.manage.ChangeNickNamePage
import cufoon.litkeep.android.page.user.manage.UserManagePage
import cufoon.litkeep.android.store.MainViewModel
import cufoon.litkeep.android.store.Navigator
import cufoon.litkeep.android.theme.LitKeepTheme

const val ROUTE_INDEX = "index"
const val ROUTE_LOGIN = "login"
const val ROUTE_RECORD_ADD = "record_add"
const val ROUTE_RECORD_MANAGE = "record_manage"
const val ROUTE_RECORD_KIND_MANAGE = "record_kind_manage"
const val ROUTE_USER_MANAGE = "user_manage"
const val ROUTE_USER_PROFILE_EDIT_AVATAR = "user_profile_edit_avatar"
const val ROUTE_USER_PROFILE_EDIT_NICKNAME = "user_profile_edit_nickname"
const val ROUTE_ABOUT = "about"


val LocalAppNavContext = compositionLocalOf<NavHostController?> { null }
val LocalAppViewModel = compositionLocalOf<MainViewModel?> { null }

@Composable
fun rememberAppNavController(): (NavHostController.() -> Unit) -> Boolean {
    val anc = LocalAppNavContext.current
    return fun(block: NavHostController.() -> Unit): Boolean {
        anc?.let {
            it.block()
            return true
        }
        return false
    }
}

@Composable
fun rememberAppViewModel(): MainViewModel {
    LocalAppViewModel.current?.let {
        return it
    }
    return viewModel()
}

@Composable
fun AppRoot(vm: MainViewModel) {
    val navController = rememberNavController()
    val context = LocalContext.current

    LaunchedEffect(true) {
        vm.launchAppReady = true
    }

    val token by Navigator.token.collectAsState()
    val isLoginExpire by remember { derivedStateOf { token.isEmpty() } }
    LaunchedEffect(isLoginExpire) {
        if (isLoginExpire) {
            Toast.makeText(context, "登录过期！", Toast.LENGTH_SHORT).show()
            navController.navigate(ROUTE_LOGIN) {
                popUpTo("index") { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    CompositionLocalProvider(
        LocalAppNavContext provides navController, LocalAppViewModel provides vm
    ) {
        LitKeepTheme {
            DialogGlobal {
                if (vm.tokenChecked) {
                    NavHost(navController = navController,
                        startDestination = if (vm.tokenVerified) ROUTE_INDEX else ROUTE_LOGIN,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background),
                        enterTransition = {
                            fadeIn(animationSpec = tween(220, delayMillis = 90)) + scaleIn(
                                initialScale = 0.92f, animationSpec = tween(220, delayMillis = 90)
                            )
                        },
                        exitTransition = {
                            fadeOut(animationSpec = tween(90))
                        },
                        popEnterTransition = {
                            fadeIn(animationSpec = tween(220, delayMillis = 90)) + scaleIn(
                                initialScale = 0.92f, animationSpec = tween(220, delayMillis = 90)
                            )
                        },
                        popExitTransition = {
                            fadeOut(animationSpec = tween(90))
                        }) {
                        composable(ROUTE_INDEX) { AppMain() }
                        composable(ROUTE_LOGIN) { LoginPage() }
                        composable(ROUTE_RECORD_ADD) { RecordAddPage() }
                        composable(ROUTE_RECORD_MANAGE) { RecordManagePage() }
                        composable(ROUTE_RECORD_KIND_MANAGE) { BillKindManagePage() }
                        composable(ROUTE_USER_MANAGE) { UserManagePage() }
                        composable(ROUTE_USER_PROFILE_EDIT_AVATAR) { ChangeAvatarPage() }
                        composable(ROUTE_USER_PROFILE_EDIT_NICKNAME) { ChangeNickNamePage() }
                        composable(ROUTE_ABOUT) { AboutPage() }
                    }
                }
            }
        }
    }
}
