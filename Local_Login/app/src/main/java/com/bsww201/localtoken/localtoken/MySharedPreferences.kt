package com.bsww201.localtoken.localtoken

import android.content.Context
import android.content.SharedPreferences

// shared preferences 사용 클래스
class MySharedPreferences(context: Context) {
    val PREFS_FILENAME = "prefs"
    val PREF_KEY_ACCESS_TOKEN = "access_token"
    val PREF_KEY_REFRESH_TOKEN = "refresh_token"
    val prefs : SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    var access_token : String
    // get() 실행시 저장된 값을 반환하며 default값은 ""
        get() = prefs.getString(PREF_KEY_ACCESS_TOKEN, "")
    // set(value) 실행 시 value로 값을 대체한 후 저장
        set(value) = prefs.edit().putString(PREF_KEY_ACCESS_TOKEN, value).apply()

    var refresh_token : String
        get() = prefs.getString(PREF_KEY_REFRESH_TOKEN, "")
        set(value) = prefs.edit().putString(PREF_KEY_REFRESH_TOKEN, value).apply()
}