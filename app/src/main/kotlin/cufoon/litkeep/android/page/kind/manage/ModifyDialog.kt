package cufoon.litkeep.android.page.kind.manage

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cufoon.litkeep.android.component.Dialog
import cufoon.litkeep.android.component.DialogOption
import cufoon.litkeep.android.service.BillKind
import cufoon.litkeep.android.service.BillKindService
import cufoon.litkeep.android.theme.CurveCornerShape
import cufoon.litkeep.android.util.ifNotNullOrElse
import kotlinx.coroutines.launch


@Composable
fun DialogModify(visible: () -> Boolean, closer: () -> Unit, item: () -> BillKind) {
    val coroutineScope = rememberCoroutineScope()

    val itemToEdit by rememberUpdatedState(item())

    var inputKindName by remember(itemToEdit) {
        mutableStateOf(itemToEdit.name)
    }

    var inputKindDescription by remember(itemToEdit) {
        mutableStateOf(itemToEdit.description)
    }

    val context = LocalContext.current

    suspend fun modifyKind(time: Int) {
        val (err, data) = BillKindService.modify(
            BillKind(
                itemToEdit.kindID, inputKindName, inputKindDescription
            )
        )
        err.ifNotNullOrElse({
            Log.d("lit", it.info)
        }) {
            data?.let {
                Log.d("lit", "query $time completed")
                if (it.modified) {
                    Toast.makeText(context, "成功", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "失败", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Dialog(visible = visible, optionsProvider = {
        DialogOption(title = "修改分类", onCancel = {
            inputKindName = itemToEdit.name
            closer()
        }, onConfirm = {
            coroutineScope.launch {
                modifyKind(2)
                closer()
            }
        })
    }) {
        Text(
            "分类名称",
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp), fontSize = 16.sp
        )
        OutlinedTextField(
            value = inputKindName,
            onValueChange = { inputKindName = it },
            singleLine = true,
            shape = CurveCornerShape(16.dp),
            modifier = Modifier.focusable()
        )
        Text(
            "分类描述",
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .padding(top = 12.dp),
            fontSize = 16.sp
        )
        OutlinedTextField(value = inputKindDescription,
            onValueChange = { inputKindDescription = it },
            singleLine = true,
            shape = CurveCornerShape(16.dp),
            modifier = Modifier.focusable(),
            placeholder = { Text("暂无描述") })
    }
}