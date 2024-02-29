package cufoon.litkeep.android.service

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


@JsonClass(generateAdapter = true)
data class ReqUserLogin(@Json(name = "email") val username: String, val password: String)

@JsonClass(generateAdapter = true)
data class ResUserLogin(val logined: Boolean, val token: String)

@JsonClass(generateAdapter = true)
data class ResGetInfo(
    val nickname: String,
    @Json(name = "userID") val userId: String,
    @Json(name = "email") val email: String,
    @Json(name = "iconPath") val iconPath: String
)

@JsonClass(generateAdapter = true)
data class ReqUserChangeNickName(@Json(name = "nickname") val nickname: String)

@JsonClass(generateAdapter = true)
data class ResUserChangeNickName(val changed: Boolean)

@JsonClass(generateAdapter = true)
data class ResUserChangeIcon(val changed: Boolean)

interface DefUserService {
    @POST("User")
    suspend fun login(@Body data: ReqUserLogin): Response<HttpResponse<ResUserLogin>>

    @GET("UserInfo")
    suspend fun getInfo(): Response<HttpResponse<ResGetInfo>>

    @POST("UserInfoNickNameChange")
    suspend fun changeNickName(@Body data: ReqUserChangeNickName): Response<HttpResponse<ResUserChangeNickName>>

    @Multipart
    @POST("UserInfoIconChange")
    suspend fun changeIcon(@Part file: MultipartBody.Part): Response<HttpResponse<ResUserChangeIcon>>
}

val userRetrofit: DefUserService = retrofitJSON.create(DefUserService::class.java)
val userRetrofitWithToken: DefUserService = retrofitWithTokenJSON.create(DefUserService::class.java)

object UserService {
    suspend fun login(username: String, password: String): Pair<Err?, ResUserLogin?> {
        return request {
            userRetrofit.login(ReqUserLogin(username, password))
        }
    }

    suspend fun getInfo(): Pair<Err?, ResGetInfo?> {
        return request {
            userRetrofitWithToken.getInfo()
        }
    }

    suspend fun changeNickName(nickName: String): Pair<Err?, ResUserChangeNickName?> {
        return request {
            userRetrofitWithToken.changeNickName(ReqUserChangeNickName(nickName))
        }
    }

    suspend fun changeIcon(
        resourceData: ByteArray, filename: String, mediaType: MediaType
    ): Pair<Err?, ResUserChangeIcon?> {
        val file = MultipartBody.Part.createFormData(
            "file", filename, resourceData.toRequestBody(mediaType)
        )
        return request {
            userRetrofitWithToken.changeIcon(file)
        }
    }
}
