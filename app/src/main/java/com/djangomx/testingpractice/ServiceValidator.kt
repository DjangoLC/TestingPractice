package com.djangomx.testingpractice

import android.os.Handler
import android.os.Looper
import com.djangomx.testingpractice.login.CallbackLogin

class ServiceValidator {

    fun validateCredentials(userName: String,password: String, callback: CallbackLogin){

        Handler(Looper.getMainLooper()).postDelayed({
            val result = userName == "kike" && password == "123"
            callback.result(result)
        },2000)

    }

}