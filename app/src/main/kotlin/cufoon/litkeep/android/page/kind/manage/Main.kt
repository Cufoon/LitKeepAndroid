package cufoon.litkeep.android.page.kind.manage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cufoon.litkeep.android.component.TopBarWithBack
import cufoon.litkeep.android.service.BillKind


@Composable
fun BillKindManagePage() {
    var isShowDialog by remember { mutableStateOf(false) }
    var itemToChange by remember { mutableStateOf(BillKind("", "", "")) }

    val launchDialog = { it: BillKind ->
        itemToChange = it
        isShowDialog = true
    }

    Box {
        Column {
            TopBarWithBack({ "账单分类管理" }) {}
            KindListMain(launchDialog)
        }
        DialogModify({ isShowDialog }, { isShowDialog = false }) {
            itemToChange
        }
    }
}
