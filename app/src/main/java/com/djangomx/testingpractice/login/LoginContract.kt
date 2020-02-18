package com.djangomx.testingpractice.login

import androidx.annotation.StringRes
import com.djangomx.testingpractice.BasePresenter

interface LoginContract {

    interface LoginView {
        fun showLoading()
        fun dismissLoading()

        fun showUserEmpty(@StringRes resId: Int)
        fun showPasswordEmpty(@StringRes resId: Int)

        fun getUserName(): String
        fun getPassword(): String

        fun successAuth()
        fun errorAuth()
    }

    interface LoginPresenter : BasePresenter<LoginView, LoginModel> {
        fun onLoginButtonClick()
        fun onLoginAsyncButtonClick()
        fun onLoginAsyncCoroutineButtonClick()
    }

    interface LoginModel {
        fun validateUserCredentials(userName: String, password: String): Boolean
        fun validateUserCredentialsWithCallback(
            userName: String,
            password: String,
            callback: CallbackLogin
        )

        fun validateUserCredentialsWithCallback(callback: CallbackLogin)

        suspend fun validateUserCredentialsSuspend(
            userName: String,
            password: String
        ) : Boolean
    }

}