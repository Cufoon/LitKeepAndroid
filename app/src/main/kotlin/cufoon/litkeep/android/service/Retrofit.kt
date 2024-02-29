package cufoon.litkeep.android.service

import com.squareup.moshi.Moshi
import cufoon.litkeep.android.util.MMKV
import cufoon.litkeep.android.util.OffsetDateTimeAdapter
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


val moshi: Moshi = Moshi.Builder().add(OffsetDateTimeAdapter()).build()

val moshiConverter: MoshiConverterFactory = MoshiConverterFactory.create(moshi)

val okHttpClient = OkHttpClient.Builder().addInterceptor {
    it.proceed(
        it.request().newBuilder().addHeader("Authorization", MMKV.token).build()
    )
}.build()

const val LITKEEP_BACKEND_URL = "https://xxx.yyy/"

val retrofitJSON: Retrofit =
    Retrofit.Builder().baseUrl(LITKEEP_BACKEND_URL).addConverterFactory(moshiConverter).build()

val retrofitWithTokenJSON: Retrofit =
    Retrofit.Builder().baseUrl(LITKEEP_BACKEND_URL).addConverterFactory(moshiConverter)
        .client(okHttpClient).build()
