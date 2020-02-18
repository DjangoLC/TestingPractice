package com.djangomx.testingpractice.login


class LoginModelImpl : LoginContract.LoginModel {

    override fun validateUserCredentials(userName: String, password: String): Boolean {
        return userName == "kike" && password == "123"
    }

    override fun validateUserCredentialsWithCallback(
        callback: CallbackLogin) {


        callback.result(true)
    }

}