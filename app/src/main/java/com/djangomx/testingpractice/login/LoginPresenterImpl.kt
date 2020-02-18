package com.djangomx.testingpractice.login

import com.djangomx.testingpractice.R
import kotlinx.coroutines.*


class LoginPresenterImpl : LoginContract.LoginPresenter {

    private var mView: LoginContract.LoginView? = null
    private lateinit var mModel: LoginContract.LoginModel

    private val completableJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + completableJob)

    override fun attachView(view: LoginContract.LoginView, model: LoginContract.LoginModel) {
        this.mView = view
        mModel = model
    }

    override fun detachView() {
        this.mView = null
    }

    override fun onLoginButtonClick() {
        val user = mView?.getUserName() ?: ""
        val password = mView?.getPassword() ?: ""

        if (user.isEmpty()) {
            mView?.showUserEmpty(R.string.user_empty)
            return
        }

        if (password.isEmpty()) {
            mView?.showPasswordEmpty(R.string.password_empty)
            return
        }

        mView?.showLoading()
        val result = mModel.validateUserCredentials(user, password)
        mView?.dismissLoading()

        if (result) mView?.successAuth() else mView?.errorAuth()
    }

    override fun onLoginAsyncButtonClick() {
        val user = mView?.getUserName() ?: ""
        val password = mView?.getPassword() ?: ""

        if (user.isEmpty()) {
            mView?.showUserEmpty(R.string.user_empty)
            return
        }

        if (password.isEmpty()) {
            mView?.showPasswordEmpty(R.string.password_empty)
            return
        }

        mView?.showLoading()

        mModel.validateUserCredentialsWithCallback(user, password, object : CallbackLogin {
            override fun result(success: Boolean) {
                mView?.dismissLoading()
                if (success) mView?.successAuth() else mView?.errorAuth()
            }

        })
    }

    override fun onLoginAsyncCoroutineButtonClick() {
        val user = mView?.getUserName() ?: ""
        val password = mView?.getPassword() ?: ""

        if (user.isEmpty()) {
            mView?.showUserEmpty(R.string.user_empty)
            return
        }

        if (password.isEmpty()) {
            mView?.showPasswordEmpty(R.string.password_empty)
            return
        }

        mView?.showLoading()

        coroutineScope.launch {
            val result = mModel.validateUserCredentialsSuspend(user, password)
            withContext(Dispatchers.Main){
                mView?.dismissLoading()
                if (result) mView?.successAuth() else mView?.errorAuth()
            }
        }


    }


}