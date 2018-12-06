package com.bsww201.localtoken.localtoken

import android.util.Base64
import android.util.Log
import org.json.JSONObject
import java.util.*

class JWTUtils {

    // 안드로이드에 저장되어 있는 refresh 토큰을 받아 디코딩
    fun decoded(RefreshToken : String) : String {

        Log.e("저장된 값_1", App.prefs.refresh_token)

        var payload : String = RefreshToken.split(".")[1]

        return payload
    }

    // 토큰의 exp 부분 추출
    fun getExp(payload : String) : Long {

        var decodedByte  = Base64.decode(payload, Base64.URL_SAFE)
        var exp = JSONObject(String(decodedByte , Charsets.UTF_8)).getLong("exp") * 1000

        Log.e("디코딩된 exp", exp.toString())

        return exp
    }

    // 현재시간과 해당 refresh 토큰을 비교 (LocalTime() 사용 권장)
    fun compareExp(RefreshToken : String) : Boolean {

        if (RefreshToken == "") {
            return false
        }
        val nowDate = Date().time
        val refreshExp = getExp(decoded(RefreshToken))

        if (refreshExp > nowDate){
            Log.e("refreshExp : ", refreshExp.toString())
            Log.e("nowDate : ", nowDate.toString())
        }
        else{
            Log.e("refreshExp : ", refreshExp.toString())
            Log.e("nowDate : ", nowDate.toString())
        }

        return refreshExp > nowDate
    }

}