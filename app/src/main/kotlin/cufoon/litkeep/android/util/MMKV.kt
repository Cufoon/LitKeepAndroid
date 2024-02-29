package cufoon.litkeep.android.util

import android.os.Parcelable
import com.tencent.mmkv.MMKV as tm


val kv: tm = tm.defaultMMKV()

object MMKV {
    var token: String
        get() {
            val r = kv.decodeString("token", "")
            return r ?: ""
        }
        set(value) {
            kv.encode("token", value)
        }
    var username: String
        get() {
            val r = kv.decodeString("username", "")
            return r ?: ""
        }
        set(value) {
            kv.encode("username", value)
        }
    var password: String
        get() {
            val r = kv.decodeString("password", "")
            return r ?: ""
        }
        set(value) {
            kv.encode("password", value)
        }

    fun setString(key: String, value: String?) {
        kv.encode(key, value)
    }

    fun setParcelable(key: String, value: Parcelable?) {
        kv.encode(key, value)
    }
}