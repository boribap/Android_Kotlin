package com.bsww201.localtoken.localtoken

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.regex.Pattern

class SignUpActivity:AppCompatActivity() {

    private var nameEditText:String? = null
    private var emailEditText:String? = null
    private var passwordEditText:String? = null
    private var checkSum : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        login_Button.setOnClickListener{
            emailEditText = email_Text.text.toString()
            passwordEditText = password_Text.text.toString()

            if (!isValid()){
                // 회원가입 실패

            }
            else{
                // 회원가입 성공 --> 정보를 서버로 전달

            }
        }

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
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun checkPasswordForm(password : String?) : Boolean {
        if (password == null){
            return false
        }
        val regex = "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$"
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
            Toast.makeText(this,"이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}