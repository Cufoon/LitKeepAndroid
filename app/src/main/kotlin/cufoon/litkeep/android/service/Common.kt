package cufoon.litkeep.android.service

import android.util.Log
import com.squareup.moshi.JsonClass
import cufoon.litkeep.android.store.Navigator
import cufoon.litkeep.android.util.MMKV
import retrofit2.Response


@JsonClass(generateAdapter = true)
data class HttpResponse<T>(
    val code: Int, val info: String, val data: T?
)

data class Err(val code: Int, val info: String)

suspend fun <T> request(run: suspend () -> Response<HttpResponse<T>>): Pair<Err?, T?> {
    try {
        var r = run()
        var reRun = false
        if (r.code() == 401) {
            if (MMKV.username.isEmpty() || MMKV.password.isEmpty()) {
                return Pair(Err(ErrorNoAuthorization, "未登录"), null)
            }
            val reLogin = userRetrofit.login(ReqUserLogin(MMKV.username, MMKV.password))
            val reLoginBody = reLogin.body()
            if (reLoginBody?.data?.logined == true) {
                val token = reLoginBody.data.token
                MMKV.token = token
                Navigator.token.value = token
                reRun = true
            } else {
                MMKV.username = ""
                MMKV.password = ""
                MMKV.token = ""
                Navigator.token.value = ""
                return Pair(Err(ErrorAccountPasswordWrong, "账号和密码不匹配"), null)
            }
        }
        if (reRun) {
            r = run()
        }
        r.body()?.let {
            if (it.code != 0) {
                return Pair(Err(it.code, it.info), null)
            }
            return Pair(null, it.data)
        }
        return Pair(null, null)
    } catch (e: Exception) {
        Log.e("lit", e.message ?: "未知错误")
        return Pair(Err(ErrorFromRetrofit, e.message ?: "未知错误"), null)
    }
}
