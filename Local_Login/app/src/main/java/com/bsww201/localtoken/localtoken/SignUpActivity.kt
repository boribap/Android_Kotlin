package com.bsww201.localtoken.localtoken

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
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
                        Log.e("retrofit 들어옴", "retrofit 들어옴")

                        var result1 : Int? = response?.code()
                        Log.e("결과 값 1 ", result1.toString())

                        if (response!!.isSuccessful()){
                            var result : String = response.body().toString()
                            Log.e("결과 값 2 ", result.toString())
                            var jsonobj : responseJSON = response.body()
                            var resmsg : String? = jsonobj.getID()
                            Log.e("결과 값 3 ", resmsg)

                            if(resmsg == "NO"){
                                Toast.makeText(this@SignUpActivity,"중복되는 이메일이 존재합니다.", Toast.LENGTH_SHORT).show()
                            }else {
                                Toast.makeText(this@SignUpActivity,"토큰을 전달하겠습니다.", Toast.LENGTH_SHORT).show()
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

    private fun checkNameForm(name : String?) : Boolean {
        if (name == null){
            return false
        }
        val regex = "^[_a-zA-Z가-힣0-9]{2,6}"
        val patten = Pattern.compile(regex)
        return patten.matcher(name).matches()
    }

    private fun checkEmailForm(email : String?) : Boolean {
        if (email == null){
            return false
        }
        val regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$"
        val pattern = Pattern.compile(regex)
        return pattern.matcher(email).matches()
    }

    private fun checkPasswordForm(password : String?) : Boolean {
        if (password == null){
            return false
        }
        //val regex = "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$"
        val regex = "^[_a-zA-Z가-힣0-9]{2,6}"
        val pattern = Pattern.compile(regex)
        return pattern.matcher(password).matches()
    }

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
    var response : String? = null

    public fun getID() : String?{
        return response
    }

    public fun setID(response : String?) {
        this.response = response
    }

    override fun toString(): String {
        return "response: " + response
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