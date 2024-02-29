package cufoon.litkeep.android.page.record.add

import android.icu.text.DecimalFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import cufoon.litkeep.android.R
import cufoon.litkeep.android.theme.CurveCornerShape
import cufoon.litkeep.android.theme.LitColors
import cufoon.litkeep.android.util.Calculator


val cKeyLabelMap = mapOf(
    "c" to "AC",
    "d" to "DEL",
    "0" to "0",
    "1" to "1",
    "2" to "2",
    "3" to "3",
    "4" to "4",
    "5" to "5",
    "6" to "6",
    "7" to "7",
    "8" to "8",
    "9" to "9",
    "." to ".",
    "(" to "(",
    ")" to ")",
    "+" to "+",
    "-" to "-",
    "*" to "×",
    "/" to "÷",
    "=" to "=",
    "y" to "✔"
)

val n147 = listOf("c", "1", "4", "7", ".")
val n258 = listOf("(", "2", "5", "8", "0")
val n369 = listOf(")", "3", "6", "9", "/")
val n000 = listOf("d", "*", "-", "+")

@Composable
internal fun KeyBoardArea(
    valueProvider: () -> String,
    updateInput: (String) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit
) {
    val value by rememberUpdatedState(valueProvider())
    var inputInternal by remember { mutableStateOf("") }
    val noneInputYet by remember { derivedStateOf { inputInternal.isEmpty() } }
    val needCalculate by remember {
        derivedStateOf {
            inputInternal.contains(Regex("[+\\-*/()]")) && !(inputInternal.startsWith(
                "-"
            ) && inputInternal.isEmpty())
        }
    }

    val calculate = lit@{
        val xxx = Calculator.exec(inputInternal)
        var r = ""
        xxx?.let {
            val df = DecimalFormat("#.00")
            r = df.format(it)
        }
        inputInternal = if (r.isEmpty()) {
            updateInput("计算出错")
            ""
        } else {
            updateInput(r)
            r
        }
    }

    val onKeyInput = lit@{ key: String ->
        if (key == "y") {
            onSubmit()
            return@lit
        }
        if (key == "=") {
            return@lit calculate()
        }
        if (key == "c") {
            updateInput("")
            inputInternal = ""
            return@lit
        }
        if (key == "d") {
            if (inputInternal.isNotEmpty()) {
                updateInput(value.substring(0, value.length - 1))
                inputInternal = inputInternal.substring(0, inputInternal.length - 1)
            }
            return@lit
        }
        if (inputInternal.isEmpty()) {
            updateInput("")
        }
        inputInternal += key
        val preValue = if (value == "计算出错") "" else value
        updateInput("$preValue${cKeyLabelMap[key]}")
    }

    Row(
        Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(8.dp)
            .shadow(
                10.dp, ambientColor = Color(0x20000000), spotColor = Color(0x20000000)
            )
            .clip(CurveCornerShape(13.dp))
            .background(LitColors.LightPink)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = R.drawable.icons8_yuan_96,
            contentDescription = null,
            modifier = Modifier
                .padding(end = 8.dp)
                .size(32.dp)
                .alpha(0.7f)
        )
        Text(text = value, fontSize = 20.sp)
    }
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp)
    ) {
        Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            n147.forEach {
                cKeyLabelMap[it]?.let { it1 -> AKey({ it1 }) { onKeyInput(it) } }
            }
        }
        Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            n258.forEach {
                cKeyLabelMap[it]?.let { it1 -> AKey({ it1 }) { onKeyInput(it) } }
            }
        }
        Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            n369.forEach {
                cKeyLabelMap[it]?.let { it1 -> AKey({ it1 }) { onKeyInput(it) } }
            }
        }
        Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            n000.forEach {
                cKeyLabelMap[it]?.let { it1 -> AKey({ it1 }) { onKeyInput(it) } }
            }
            AKey(label = { if (noneInputYet) "取消" else (if (needCalculate) "=" else "✔") }) {
                if (noneInputYet) onBack() else onKeyInput(
                    if (needCalculate) "=" else "y"
                )
            }
        }
    }
}

@Composable
private fun AKey(
    label: () -> String, onClick: () -> Unit
) {
    Row(
        Modifier
            .padding(5.dp, 3.dp)
            .shadow(
                10.dp, ambientColor = Color(0x20000000), spotColor = Color(0x20000000)
            )
            .clip(CurveCornerShape(16.dp))
            .clickable { onClick() }
            .fillMaxWidth()
            .aspectRatio(1.5f)
            .background(LitColors.LightPink),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label(), fontSize = 20.sp)
    }
}
