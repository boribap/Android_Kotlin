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
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {

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
                var baseurl = "http://192.168.0.4:7260/"
                var retrofit : Retrofit = Retrofit.Builder()
                        .baseUrl(baseurl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                var serviceAPI : ServiceAPI = retrofit.create(ServiceAPI::class.java)
                user = user.User(nameEditText, emailEditText, passwordEditText)
                var call : Call<List<responseJSON>> = serviceAPI.signUpData(user)

                call.enqueue(object : Callback<List<responseJSON>>{
                    override fun onResponse(call: Call<List<responseJSON>>?, response: Response<List<responseJSON>>?) {

                        var result1 : Int? = response?.code()
                        Log.e("응답 코드 ", result1.toString())

                        if (response!!.isSuccessful()){
                            var jsonobj : List<responseJSON> = response.body()
                            var res_status : String? = jsonobj[0].getstatus()
                            Log.e("응답 메세지", res_status)

                            if(res_status == "DUPL_EMAIL"){
                                Toast.makeText(this@SignUpActivity,"중복되는 이메일이 존재합니다.", Toast.LENGTH_SHORT).show()
                                s_email_Text.text.clear()
                            }else {
                                // 회원가입 끝
                                Toast.makeText(this@SignUpActivity, "회원 가입 완료", Toast.LENGTH_SHORT).show()
                                // 로그인 페이지로 넘어가기
                                startActivity(signinIntent)
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<responseJSON>>?, t: Throwable?) {
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
        val regex = "^[_a-zA-Z가-힣0-9]{2,18}"
        val pattern = Pattern.compile(regex)
        return pattern.matcher(password).matches()
    }

    // 회원가입에 대한 유효성 검사 (전체)
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