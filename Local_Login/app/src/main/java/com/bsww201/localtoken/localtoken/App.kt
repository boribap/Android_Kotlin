package com.bsww201.localtoken.localtoken

import android.app.Application

// shared preferences를 전역에서 사용하기 위한 클래스
class App : Application() {

    // prefs 라는 이름의 MySharedPreferences 하나만 생성할 수 있도록 설정
    // 싱글 톤으로 가야하기 때문에
    companion object {
        lateinit var prefs : MySharedPreferences
    }

    override fun onCreate() {
        prefs = MySharedPreferences(applicationContext)
        super.onCreate()
    }
}