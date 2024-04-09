package cufoon.litkeep.android.service

import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


@JsonClass(generateAdapter = true)
data class BillKind(
    val kindID: String, val name: String, val description: String
)

@JsonClass(generateAdapter = true)
data class BillKindParent(
    val kindID: String,
    val name: String,
    val description: String,
    val children: List<BillKind>?
)

@JsonClass(generateAdapter = true)
data class ResBillKindQuery(val kind: List<BillKindParent>)

@JsonClass(generateAdapter = true)
data class ResBillKindModify(val modified: Boolean)

interface DefBillKindService {
    @POST("kind_query")
    suspend fun query(): Response<HttpResponse<ResBillKindQuery>>

    @POST("kind_update")
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
