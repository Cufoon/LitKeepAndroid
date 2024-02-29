package cufoon.litkeep.android.service

import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.GET


@JsonClass(generateAdapter = true)
data class ResTokenVerify(val verified: Int)

interface DefServiceToken {
    @GET("TokenVerify")
    suspend fun verify(): Response<HttpResponse<ResTokenVerify>>
}

val tokenRetrofit: DefServiceToken = retrofitWithTokenJSON.create(DefServiceToken::class.java)

object TokenService {
    suspend fun verify(): Pair<Err?, ResTokenVerify?> {
        return request {
            tokenRetrofit.verify()
        }
    }
}
