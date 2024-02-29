package cufoon.litkeep.android.service

import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST


@JsonClass(generateAdapter = true)
data class BillKind(
    val UserID: String, val KindID: String, val Name: String, val Description: String
)

@JsonClass(generateAdapter = true)
data class BillKindParent(
    val UserID: String,
    val KindID: String,
    val Name: String,
    val Description: String,
    val Children: List<BillKind>?
)

@JsonClass(generateAdapter = true)
data class ResBillKindQuery(val kind: List<BillKindParent>)

@JsonClass(generateAdapter = true)
data class ResBillKindModify(val modified: Boolean)

interface DefBillKindService {
    @POST("Kind")
    suspend fun query(): Response<HttpResponse<ResBillKindQuery>>

    @PATCH("Kind")
    suspend fun modify(@Body data: BillKind): Response<HttpResponse<ResBillKindModify>>
}

val billKindRetrofit: DefBillKindService =
    retrofitWithTokenJSON.create(DefBillKindService::class.java)

object BillKindService {
    suspend fun query(): Pair<Err?, ResBillKindQuery?> {
        return request {
            billKindRetrofit.query()
        }
    }

    suspend fun modify(data: BillKind): Pair<Err?, ResBillKindModify?> {
        return request {
            billKindRetrofit.modify(data)
        }
    }
}
