package com.bsww201.localtoken.localtoken

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.regex.Pattern

class SignUpActivity:AppCompatActivity() {

    private var nameEditText:String = ""
    private var emailEditText:String = ""
    private var passwordEditText:String = ""

    // retrofit : 서버로 전달할 데이터의 클래스 객체 생성
    private var user : User = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        s_sign_up_Button.setOnClickListener{

            val signinIntent = Intent(this, MainActivity::class.java)

            nameEditText = s_name_Text.text.toString()
            emailEditText = s_email_Text.text.toString()
            passwordEditText = s_password_Text.text.toString()

            if (!isValid()){
                // 회원가입 실패
            }
            else{
                // 회원가입 성공 --> 정보를 서버로 전달
                Log.e("회원가입 정보 : " , nameEditText + " " + emailEditText + " " + passwordEditText)

                // retrofit : --> 정보를 서버로 전달
                var baseurl = "주소 적기"
                var retrofit : Retrofit = Retrofit.Builder()
                        .baseUrl(baseurl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                var serviceApi : ServiceApi = retrofit.create(ServiceApi::class.java)
                user = user.User(nameEditText, emailEditText, passwordEditText)
                var call : Call<responseJSON> = serviceApi.signUpData(user)

                call.enqueue(object : Callback<responseJSON>{
                    override fun onResponse(call: Call<responseJSON>?, response: Response<responseJSON>?) {

                        var result1 : Int? = response?.code()
                        Log.e("응답 코드 ", result1.toString())

                        if (response!!.isSuccessful()){
                            var result : String = response.body().toString()
                            Log.e("서버에서온 응답", result.toString())
                            var jsonobj : responseJSON = response.body()
                            var resmsg : String? = jsonobj.getID()
                            Log.e("토큰", resmsg)

                            if(resmsg == "NO"){
                                Toast.makeText(this@SignUpActivity,"중복되는 이메일이 존재합니다.", Toast.LENGTH_SHORT).show()
                                s_email_Text.text.clear()
                            }else {
                                Toast.makeText(this@SignUpActivity,"토큰을 저장하겠습니다.", Toast.LENGTH_SHORT).show()

                                // sharedpreferences 에서 키값이 token, value가 resmsg인 값을 저장
                                App.prefs.token = resmsg.toString()

                                // 해당 위치에 저장된 값을 가져옴
                                val msg = App.prefs.token

                                if (msg == ""){
                                    Toast.makeText(this@SignUpActivity, "토큰 없음", Toast.LENGTH_SHORT).show()
                                } else {
                                    // 토큰 원하는 위치에 저장까지 완료
                                    // 회원가입 끝
                                    Toast.makeText(this@SignUpActivity, "저장됨 : $msg", Toast.LENGTH_SHORT).show()
                                    // 로그인 페이지로 넘어가기
                                    startActivity(signinIntent)
                                }

                            }
                        }
                    }

                    override fun onFailure(call: Call<responseJSON>?, t: Throwable?) {
                        Log.e("에러 남", t.toString())
                    }
                })

            }
        }

        s_login_Button.setOnClickListener {
            val loginIntent = Intent(this, MainActivity::class.java)
            startActivity(loginIntent)
        }
    }

    // retrofit : API 설정을 위한 인터페이스
    public interface ServiceApi {

        @POST("sign_up")
        fun signUpData(
                @Body user: User
        ): Call<responseJSON>
    }

    // 회원가입 입력 폼에 대한 제한 (이름)
    private fun checkNameForm(name : String?) : Boolean {
        if (name == null){
            return false
        }
        val regex = "^[_a-zA-Z가-힣0-9]{2,6}"
        val patten = Pattern.compile(regex)
        return patten.matcher(name).matches()
    }

    // 회원가입 입력 폼에 대한 제한 (이메일)
    private fun checkEmailForm(email : String?) : Boolean {
        if (email == null){
            return false
        }
        val regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$"
        val pattern = Pattern.compile(regex)
        return pattern.matcher(email).matches()
    }

    // 회원가입 입력 폼에 대한 제한 (비밀번호)
    private fun checkPasswordForm(password : String?) : Boolean {
        if (password == null){
            return false
        }
        //val regex = "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$"
        val regex = "^[_a-zA-Z가-힣0-9]{2,6}"
        val pattern = Pattern.compile(regex)
        return pattern.matcher(password).matches()
    }

    // 로그인에 대한 유효성 검사 (전체)
    private fun isValid() : Boolean {
        if (!checkNameForm(name = nameEditText)) {
            Toast.makeText(this,"이름 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!checkEmailForm(email = emailEditText)){
            Toast.makeText(this,"이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!checkPasswordForm(password = passwordEditText)){
            Toast.makeText(this,"비밀번호 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}

// retrofit : 응답을 받기 위한 클래스
class responseJSON {
    var token : String? = null

    public fun getID() : String?{
        return token
    }

    public fun setID(response : String?) {
        this.token = response
    }

    override fun toString(): String {
        return "token : " + token
    }
}

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
}