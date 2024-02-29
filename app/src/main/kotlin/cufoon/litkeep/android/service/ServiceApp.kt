package cufoon.litkeep.android.service

import com.squareup.moshi.JsonClass
import cufoon.litkeep.android.BuildConfig
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


@JsonClass(generateAdapter = true)
data class ReqAppCheckUpdate(val now: Int)

@JsonClass(generateAdapter = true)
data class ResAppCheckUpdate(val update: Boolean, val url: String)

interface DefAppService {
    @POST("AndroidApp")
    suspend fun checkUpdate(@Body data: ReqAppCheckUpdate): Response<HttpResponse<ResAppCheckUpdate>>
}

val appRetrofit: DefAppService = retrofitJSON.create(DefAppService::class.java)

object AppService {
    suspend fun checkUpdate(): Pair<Err?, ResAppCheckUpdate?> {
        return request {
            appRetrofit.checkUpdate(ReqAppCheckUpdate(BuildConfig.VERSION_CODE))
        }
    }
}
