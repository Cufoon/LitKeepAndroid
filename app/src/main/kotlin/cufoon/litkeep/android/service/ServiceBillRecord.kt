package cufoon.litkeep.android.service

import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import java.time.OffsetDateTime


@JsonClass(generateAdapter = true)
data class ReqBillRecordQuery(
    val kindID: String, val startTime: OffsetDateTime, val endTime: OffsetDateTime
)

@JsonClass(generateAdapter = true)
data class ReqBillRecordQueryStatisticDay(
    val startTime: OffsetDateTime, val endTime: OffsetDateTime
)

@JsonClass(generateAdapter = true)
data class ReqBillRecordPageQuery(
    val Page: Int
)

@JsonClass(generateAdapter = true)
data class ReqBillRecordCreate(
    val kindID: String,
    val type: Int,
    val value: Double,
    val time: OffsetDateTime,
    val mark: String? = null
)

@JsonClass(generateAdapter = true)
data class ReqBillRecordDelete(
    val ids: List<Int>
)

@JsonClass(generateAdapter = true)
data class BillRecord(
    val ID: Int? = null,
    val CreatedAt: String? = null,
    val UpdatedAt: String? = null,
    val DeletedAt: String? = null,
    val UserID: String? = null,
    val Type: Int? = null,
    val Kind: String? = null,
    val Value: Double? = null,
    val Time: OffsetDateTime? = null,
    val Mark: String? = null
)

@JsonClass(generateAdapter = true)
data class ResBillRecordQuery(val record: List<BillRecord>)

@JsonClass(generateAdapter = true)
data class ResBillRecordQueryAndKind(val record: List<BillRecord>, val kinds: List<BillKind>)

@JsonClass(generateAdapter = true)
data class BillRecordPageData(
    val total: Int, val totalPages: Int, val pageSize: Int
)

@JsonClass(generateAdapter = true)
data class ResBillRecordQueryPageData(val pageData: BillRecordPageData, val kinds: List<BillKind>)

@JsonClass(generateAdapter = true)
data class ResBillRecordCreate(val created: Boolean)

@JsonClass(generateAdapter = true)
data class ResBillRecordDelete(val notDeleted: List<Int>?)

@JsonClass(generateAdapter = true)
data class ResBillRecordQueryStatisticDayItem(val day: String, val money: Float)

@JsonClass(generateAdapter = true)
data class ResBillRecordQueryStatisticDay(val statistic: List<ResBillRecordQueryStatisticDayItem>?)

interface DefBillRecordService {
    @POST("Bill")
    suspend fun query(@Body data: ReqBillRecordQuery): Response<HttpResponse<ResBillRecordQuery>>

    @PUT("Bill")
    suspend fun create(@Body data: ReqBillRecordCreate): Response<HttpResponse<ResBillRecordCreate>>

    @POST("BillAndKind")
    suspend fun queryAndKind(@Body data: ReqBillRecordQuery): Response<HttpResponse<ResBillRecordQueryAndKind>>

    @POST("BillPageData")
    suspend fun queryPageData(): Response<HttpResponse<ResBillRecordQueryPageData>>

    @POST("BillPage")
    suspend fun queryPage(@Body data: ReqBillRecordPageQuery): Response<HttpResponse<ResBillRecordQuery>>


    @POST("BillStatisticDay")
    suspend fun queryStatisticDay(@Body data: ReqBillRecordQueryStatisticDay): Response<HttpResponse<ResBillRecordQueryStatisticDay>>

    @POST("BillDelete")
    suspend fun delete(@Body data: ReqBillRecordDelete): Response<HttpResponse<ResBillRecordDelete>>
}

val billRecordRetrofit: DefBillRecordService =
    retrofitWithTokenJSON.create(DefBillRecordService::class.java)

object BillRecordService {
    suspend fun query(data: ReqBillRecordQuery): Pair<Err?, ResBillRecordQuery?> {
        return request {
            billRecordRetrofit.query(data)
        }
    }

    suspend fun queryAndKind(data: ReqBillRecordQuery): Pair<Err?, ResBillRecordQueryAndKind?> {
        return request {
            billRecordRetrofit.queryAndKind(data)
        }
    }

    suspend fun queryPageData(): Pair<Err?, ResBillRecordQueryPageData?> {
        return request {
            billRecordRetrofit.queryPageData()
        }
    }

    suspend fun queryStatisticDay(data: ReqBillRecordQueryStatisticDay): Pair<Err?, ResBillRecordQueryStatisticDay?> {
        return request {
            billRecordRetrofit.queryStatisticDay(data)
        }
    }

    suspend fun queryPage(page: Int): Pair<Err?, ResBillRecordQuery?> {
        return request {
            billRecordRetrofit.queryPage(ReqBillRecordPageQuery(page))
        }
    }

    suspend fun create(data: ReqBillRecordCreate): Pair<Err?, ResBillRecordCreate?> {
        return request {
            billRecordRetrofit.create(data)
        }
    }

    suspend fun deleteOne(id: Int): Pair<Err?, ResBillRecordDelete?> {
        return request {
            billRecordRetrofit.delete(ReqBillRecordDelete(listOf(id)))
        }
    }

    suspend fun delete(ids: List<Int>): Pair<Err?, ResBillRecordDelete?> {
        return request {
            billRecordRetrofit.delete(ReqBillRecordDelete(ids))
        }
    }
}
