package cufoon.litkeep.android.component

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cufoon.litkeep.android.theme.CurveCornerShape
import cufoon.litkeep.android.util.createContext
import cufoon.litkeep.android.util.useContext


data class DialogOption(
    val rmConfirm: Boolean = false,
    val rmCancel: Boolean = false,
    val title: String? = null,
    val confirmText: String = "确定",
    val cancelText: String = "取消",
    var onCancel: () -> Unit = {},
    var onConfirm: () -> Unit = {}
)

interface DialogController {
    fun openDialog(options: DialogOption, content: @Composable () -> Unit)
}

val dialogContext = createContext<DialogController>()

@Composable
fun rememberGlobalDialogController(): DialogController? {
    return useContext(dialogContext)
//    return fun(block: DialogController.() -> Unit) {
//        r?.let(block)
//    }
}

@Composable
fun DialogGlobal(wrapped: @Composable () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    var content by remember { mutableStateOf<@Composable () -> Unit>({ }) }
    var options by remember { mutableStateOf(DialogOption()) }

    fun clear() {
        visible = false
        content = {}
    }

    val openDialog = fun(option: DialogOption, c: @Composable () -> Unit) {
        content = c
        options = DialogOption(option.rmConfirm,
            option.rmCancel,
            option.title,
            option.confirmText,
            option.cancelText,
            {
                option.onCancel()
                clear()
            },
            {
                option.onConfirm()
                clear()
            })
        visible = true
    }

    dialogContext.Provider(value = object : DialogController {
        override fun openDialog(options: DialogOption, content: @Composable () -> Unit) {
            openDialog(options, content)
        }
    }) {
        Box {
            wrapped()
            Dialog(visible = { visible }, content = content, optionsProvider = { options })
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Dialog(
    visible: () -> Boolean, optionsProvider: () -> DialogOption, content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val options = optionsProvider()
    val isVisible by rememberUpdatedState(visible())

    BackHandler(isVisible) {
        options.onCancel()
    }

    AnimatedVisibility(
        visible = isVisible, enter = fadeIn(tween(150)), exit = fadeOut(tween(150))
    ) {
        Row(
            Modifier
                .clickable(interactionSource, null, onClick = options.onCancel)
                .fillMaxSize()
                .background(Color(0x25000000))
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(bottom = 8.dp, start = 10.dp, end = 10.dp),
            Arrangement.Center,
            Alignment.Bottom
        ) {
            Column(Modifier
                .animateEnterExit(enter = slideInVertically(tween(250)) { it },
                    exit = slideOutVertically(tween(200)) { it })
                .clickable(false) {}
                .clip(CurveCornerShape(28.dp))
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.White)
                .padding(12.dp, 24.dp, 12.dp, 18.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                if (options.title != null) {
                    Row(
                        Modifier.padding(bottom = 32.dp), Arrangement.Center
                    ) {
                        Title(text = { options.title })
                    }
                }
                Column(Modifier.padding(bottom = 40.dp, start = 20.dp, end = 20.dp)) {
                    content()
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    if (!options.rmCancel) {
                        Button(
                            onClick = options.onCancel,
                            Modifier
                                .clip(CurveCornerShape(28.dp))
                                .height(48.dp)
                                .weight(1f)
                        ) {
                            Text(options.cancelText, color = Color.White, fontSize = 18.sp)
                        }
                    }
                    if (!options.rmCancel and !options.rmConfirm) {
                        Text(text = "", Modifier.width(12.dp))
                    }
                    if (!options.rmConfirm) {
                        Button(
                            onClick = options.onConfirm,
                            Modifier
                                .clip(CurveCornerShape(28.dp))
                                .height(48.dp)
                                .weight(1f)
                        ) {
                            Text(options.confirmText, color = Color.White, fontSize = 18.sp)
                        }
                    }
                }
            }
        }
    }
}
