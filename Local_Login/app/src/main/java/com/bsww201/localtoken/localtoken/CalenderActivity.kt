package com.bsww201.localtoken.localtoken

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_calender.*

class CalenderActivity : AppCompatActivity() {

    val signinIntent = Intent(this, MainActivity::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calender)

        logout_button.setOnClickListener{
            var pref : SharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE)
            var editor : SharedPreferences.Editor = pref.edit()
            editor.clear()
            editor.commit()

            Log.e("지우고 난 후 액세스 토큰", App.prefs.access_token)
            Log.e("지우고 난 후 리프레시 토큰", App.prefs.refresh_token)

            signinIntent
        }
    }

}