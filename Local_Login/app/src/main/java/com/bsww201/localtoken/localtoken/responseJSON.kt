package com.bsww201.localtoken.localtoken

// retrofit : 회원가입시 토큰 응답을 받기 위한 클래스
class responseJSON {
    var response : String? = null

    public fun getID() : String?{
        return response
    }

    public fun setID(response : String?) {
        this.response = response
    }

    override fun toString(): String {
        return "response : " + response
    }
}