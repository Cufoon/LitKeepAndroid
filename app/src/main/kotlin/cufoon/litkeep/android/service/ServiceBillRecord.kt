package cufoon.litkeep.android.service

import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import java.time.OffsetDateTime


@JsonClass(generateAdapter = true)
data class ReqBillRecordQuery(
    val kindID: String, val startTime: Long, val endTime: Long
)

@JsonClass(generateAdapter = true)
data class ReqBillRecordQueryStatisticDay(
    val startTime: Long, val endTime: Long, val type: Int?
)

@JsonClass(generateAdapter = true)
data class ReqBillRecordPageQuery(
    val page: Int
)

@JsonClass(generateAdapter = true)
data class ReqBillRecordCreate(
    val kindID: String, val type: Int, val value: Double, val time: Long, val mark: String
)

@JsonClass(generateAdapter = true)
data class ReqBillRecordDelete(
    val ids: List<Int>
)

@JsonClass(generateAdapter = true)
data class BillRecord(
    val id: Int? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val deletedAt: String? = null,
    val userID: String? = null,
    val type: Int? = null,
    val kind: String? = null,
    val value: Double? = null,
    val time: OffsetDateTime? = null,
    val mark: String? = null
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
    @POST("bill_query")
    suspend fun query(@Body data: ReqBillRecordQuery): Response<HttpResponse<ResBillRecordQuery>>

    @POST("bill_create")
    suspend fun create(@Body data: ReqBillRecordCreate): Response<HttpResponse<ResBillRecordCreate>>

    @POST("bill_query_with_kind")
    suspend fun queryAndKind(@Body data: ReqBillRecordQuery): Response<HttpResponse<ResBillRecordQueryAndKind>>

    @POST("bill_query_page_info")
    suspend fun queryPageData(): Response<HttpResponse<ResBillRecordQueryPageData>>

    @POST("bill_query_page")
    suspend fun queryPage(@Body data: ReqBillRecordPageQuery): Response<HttpResponse<ResBillRecordQuery>>


    @POST("bill_query_statistic_day")
    suspend fun queryStatisticDay(@Body data: ReqBillRecordQueryStatisticDay): Response<HttpResponse<ResBillRecordQueryStatisticDay>>

    @POST("bill_delete")
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
