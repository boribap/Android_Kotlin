package com.bsww201.localtoken.localtoken

import android.content.Context
import android.content.SharedPreferences

// shared preferences 사용 클래스
class MySharedPreferences(context: Context) {
    val PREFS_FILENAME = "prefs"
    val PREF_KEY_TOKEN = "token"
    val prefs : SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    var token : String
    // get() 실행시 저장된 값을 반환하며 default값은 ""
        get() = prefs.getString(PREF_KEY_TOKEN, "")
    // set(value) 실행 시 value로 값을 대체한 후 저장
        set(value) = prefs.edit().putString(PREF_KEY_TOKEN, value).apply()
}