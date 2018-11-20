package com.bsww201.kakaologin.kakao_login

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.kakao.auth.*
import android.content.Intent
import android.util.Log
import android.view.Window
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeResponseCallback
import com.kakao.usermgmt.response.model.UserProfile
import com.kakao.util.exception.KakaoException
import com.kakao.util.helper.log.Logger
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var callback: SessionCallback? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)

        callback = SessionCallback()
        Session.getCurrentSession().addCallback(callback)
        Session.getCurrentSession().checkAndImplicitOpen()

        com_kakao_login.setOnClickListener{
            var session = Session.getCurrentSession()
            session.addCallback(SessionCallback())
            session.open(AuthType.KAKAO_LOGIN_ALL, this)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            Log.v("boribap", "boribap")
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(callback)
    }

    // 세션관련 부분
    private inner class SessionCallback : ISessionCallback {

        // access token을 성공적으로 발급 받아 valid access token을 가지고 있는 상태.
        override fun onSessionOpened() {

            UserManagement.requestMe(object :MeResponseCallback(){

                // 앱연결이 실패한 경우로 에러 결과를 받습니다.
                override fun onFailure(errorResult: ErrorResult?) {
                    super.onFailure(errorResult)
                    Log.v("444", errorResult.toString())
                }

                // 앱연결을 성공한 경우로 앱연결된 사용자 ID를 받습니다.
                override fun onSuccess(result: UserProfile?) {
                    Log.e("UserProfile", result.toString())
                }

                // 세션이 닫혀 실패한 경우로 에러 결과를 받습니다.
                override fun onSessionClosed(errorResult: ErrorResult?) {

                }

                override fun onNotSignedUp() {

                }
            })

        }

        // memory와 cache에 session 정보가 전혀 없는 상태.
        override fun onSessionOpenFailed(exception: KakaoException?) {
            if (exception != null) {
                Logger.e(exception)
            }
            // 세션 연결 실패시 다시 로그인화면 불러옴
            setContentView(R.layout.activity_main)
        }
    }
}
