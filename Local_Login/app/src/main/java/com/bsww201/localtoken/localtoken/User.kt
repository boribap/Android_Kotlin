package com.bsww201.localtoken.localtoken

// retrofit : 서버로 전달할 데이터의 클래스
class User {

    var user_name : String? = null
    var user_email : String? = null
    var user_password : String? = null

    public fun User() {

    }

    override fun toString(): String {
        return "User{" +
                "user_name=" + user_name + '\'' +
                "user_email=" + user_email + '\'' +
                "user_password=" + user_password + '\'' +
                '}'
    }

    public fun User(user_name : String, user_email : String, user_password : String) : User{
        this.user_name = user_name
        this.user_email = user_email
        this.user_password = user_password

        return this
    }

    public fun  User(user_email: String, user_password: String) : User{
        this.user_email = user_email
        this.user_password = user_password

        return this
    }
}