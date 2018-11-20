package com.bsww201.kakaologin.kakao_login

import android.app.Activity
import android.app.Application
import android.content.Context
import com.kakao.auth.*

public class GlobalApplication:Application() {
    companion object {
        private var obj: GlobalApplication? = null
        private var currentActivity:Activity? = null
        lateinit var context: Context
        fun getGlobalApplicationContext(): GlobalApplication {
            return obj!!
        }
    }

    private class KakaoSDKAadpter:KakaoAdapter(){
        override fun getSessionConfig(): ISessionConfig {
            return object : ISessionConfig {
                // data를 저장할지 안할지의 여부 결정
                override fun isSaveFormData(): Boolean {
                    return true
                }

                // 로그인시 인증받을 타입 지정 (카카오스토리, 카카오톡, 웹뷰..)
                override fun getAuthTypes(): Array<AuthType> {
                    return arrayOf(AuthType.KAKAO_LOGIN_ALL)
                }

                // 로그인시 access token과 refresh token을 저장할 때의 암호화 여부를 결정
                fun isSecureMode(): Boolean {
                    return false
                }

                // 일반 사용자가 아닌 Kakao와 제휴된 앱에서 사용되는 값
                override fun getApprovalType(): ApprovalType {
                    return ApprovalType.INDIVIDUAL
                }

                // SDK 로그인시 사용되는 WebView에서 pause와 resume시에 Timer를 설정하여 CPU소모를 절약
                override fun isUsingWebviewTimer(): Boolean {
                    return false
                }
            }
        }

        override fun getApplicationConfig(): IApplicationConfig {

            return object :IApplicationConfig {
                // context를 가져옴
                override fun getApplicationContext(): Context? {
                    return GlobalApplication.getGlobalApplicationContext()
                }
                // activity를 가져옴
                override fun getTopActivity(): Activity? {
                    return GlobalApplication.currentActivity
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        obj = this
        context = this
        KakaoSDK.init(KakaoSDKAadpter())
    }
}