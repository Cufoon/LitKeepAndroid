package cufoon.litkeep.android.store

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel


sealed class AppStartState {
    object CheckLogin : AppStartState()
    object InProgress : AppStartState()
    object Error : AppStartState()
    object SignIn : AppStartState()
}

class MainViewModel : ViewModel() {
    val appState by mutableStateOf<AppStartState>(AppStartState.CheckLogin)
    var tokenChecked by mutableStateOf(false)
    var tokenVerified by mutableStateOf(false)
    var launchAppReady by mutableStateOf(false)
    var bottomBarNowAt by mutableStateOf(0)
    var userNickName by mutableStateOf("")
    var userIcon by mutableStateOf("")
}
