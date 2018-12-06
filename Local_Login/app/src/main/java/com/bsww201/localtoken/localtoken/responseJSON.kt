package com.bsww201.localtoken.localtoken

// retrofit : 회원가입시 토큰 응답을 받기 위한 클래스
class responseJSON {
    var status : String? = null
    var access_token : String? = null
    var refresh_token : String? = null
    var responseArray : Array<String?>? = null

    public fun getstatus() : String?{
        return status
    }

    public fun getAccessToken() : String? {
        return access_token
    }

    public fun getRefreshToken() : String? {
        return refresh_token
    }

//    public fun setID(response : String?) {
//        this.response = response
//    }

//    public fun toStringA(access_token : String): String {
//        return "access_token : " + access_token
//    }
//
//    public fun toStringR(refresh_token : String) : String {
//        return "refresh_token : " + refresh_token
//    }

}