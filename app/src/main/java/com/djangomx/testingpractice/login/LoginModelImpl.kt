package com.djangomx.testingpractice.login

import android.os.Handler
import com.djangomx.testingpractice.ServiceValidator
import kotlin.coroutines.suspendCoroutine
import kotlin.coroutines.resume


class LoginModelImpl : LoginContract.LoginModel {

    val serviceValidator = ServiceValidator()

    override fun validateUserCredentials(userName: String, password: String): Boolean {
        return userName == "kike" && password == "123"
    }

    override fun validateUserCredentialsWithCallback(
        userName: String,
        password: String,
        callback: CallbackLogin
    ) {
        Handler().postDelayed({
            val result = (userName == "kike" && password == "123")
            callback.result(result)
        }, 2000)
    }

    override suspend fun validateUserCredentialsSuspend(
        userName: String,
        password: String
    ): Boolean = suspendCoroutine { continuation ->
        serviceValidator.validateCredentials(userName, password, object : CallbackLogin {
            override fun result(success: Boolean) {
                continuation.resume(success)
            }
        })
    }

}