package cufoon.litkeep.android.store

import cufoon.litkeep.android.util.MMKV
import kotlinx.coroutines.flow.MutableStateFlow


object Navigator {
    var token: MutableStateFlow<String> = MutableStateFlow(MMKV.token)
}
