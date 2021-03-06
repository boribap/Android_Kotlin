package com.bsww201.localtoken.localtoken

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Pattern

// 로그인 하는 화면 (메인화면)
class MainActivity : AppCompatActivity() {

    private var emailEditText : String = ""
    private var passwordEditText : String = ""

    // retrofit : 서버로 전달할 데이터의 클래스 객체 생성
    private var user : User = User()

    // 전환할 intent
    val calenderIntent = Intent(this, CalenderActivity::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.e("저장된 값", App.prefs.refresh_token)

        if (JWTUtils().compareExp(App.prefs.refresh_token)){
            // exp 유효
            Log.e("유효 : ", "0")

            // 달력 페이지로
            startActivity(calenderIntent)

        }
        else {
            // exp 만료
            Log.e("유효 : ", "X")

            // 로그인 로직 실행
            sign_up_Button.setOnClickListener{
                val signupIntent = Intent(this, SignUpActivity::class.java)
                startActivity(signupIntent)
            }

            // 로그인 버튼 누르면 verify 가 성공하면 다음 인텐트로 전환되도록
            login_Button.setOnClickListener{

                emailEditText = email_Text.text.toString()
                passwordEditText = password_Text.text.toString()

                if (!isValid()){
                    // 로그인 형식 틀려서 로그인 실패
                }
                else {
                    var baseurl = "http://192.168.0.4:7260/"
                    var retrofit : Retrofit = Retrofit.Builder()
                            .baseUrl(baseurl)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()

                    var serviceAPI : ServiceAPI = retrofit.create(ServiceAPI::class.java)
                    user = user.User(emailEditText, passwordEditText)
                    var call : Call<List<responseJSON>> = serviceAPI.loginData(user)

                    call.enqueue(object : Callback<List<responseJSON>>{
                        override fun onResponse(call: Call<List<responseJSON>>?, response: Response<List<responseJSON>>?) {

                            var result1 : Int? = response?.code()
                            Log.e("응답 코드 ", result1.toString())

                            if (response!!.isSuccessful()){
                                // 서버에서 문자열을 받은 뒤에 그것을 가지고 어떤 행동을 할지 코딩
                                var jsonobj : List<responseJSON> = response.body()
                                var res_status : String? = jsonobj[0].getstatus()
                                Log.e("응답 메세지", res_status)

                                if (res_status == "LOGIN_FAIL_EMAIL"){
                                    // 응답에 LOGIN_FAIL_EMAIL이 올 때 --> email 이 없다고 Toast & 칸 비우기
                                    Toast.makeText(this@MainActivity, "회원이 아닙니다. 이메일을 확인해주세요.", Toast.LENGTH_SHORT).show()
                                    email_Text.text.clear()
                                    password_Text.text.clear()
                                }
                                else if (res_status == "LOGIN_FAIL_PASSWORD"){
                                    // 응답에 LOGIN_FAIL_PASSWORD가 올 때 --> password 가 다르다고 Toast & 칸 비우기
                                    Toast.makeText(this@MainActivity, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                                    password_Text.text.clear()
                                }
                                else if (res_status == "LOGIN_PERMIT"){
                                    // 응답에 토큰이 올 때 --> 토큰을 prefs에 저장하기 & 화면을 calender 화면으로 변경시키기
                                    Toast.makeText(this@MainActivity, "로그인 성공", Toast.LENGTH_SHORT).show()

                                    var res_accessToken = jsonobj[1].getAccessToken()
                                    var res_refreshToken = jsonobj[2].getRefreshToken()
                                    Log.e("access 토큰 값", res_accessToken)
                                    Log.e("refresh 토큰 값", res_refreshToken)

                                    // sharedpreferences 에서 키값이 token, value가 resmsg인 값을 저장
                                    App.prefs.access_token = res_accessToken.toString()
                                    App.prefs.refresh_token = res_refreshToken.toString()

                                    // 해당 위치에 저장된 값을 가져옴
                                    val access = App.prefs.access_token
                                    val refresh = App.prefs.refresh_token
                                    Log.e("저장된 access 토큰 값", access)
                                    Log.e("저장된 refresh 토큰 값", refresh)

                                    // calender화면으로 전환
                                    startActivity(calenderIntent)
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<responseJSON>>?, t: Throwable?) {
                            Log.e("에러 남", t.toString())
                        }
                    })

                }

            }
        }
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
