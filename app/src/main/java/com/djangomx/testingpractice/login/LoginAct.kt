package com.djangomx.testingpractice.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.djangomx.testingpractice.R
import kotlinx.android.synthetic.main.activity_main.*

class LoginAct : AppCompatActivity(),LoginContract.LoginView {

    private lateinit var mPresenter: LoginContract.LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mPresenter = LoginPresenterImpl()
        mPresenter.attachView(this, LoginModelImpl())

        btnLogin.setOnClickListener {
            mPresenter.onLoginButtonClick()
            val intent: Intent = Intent(this, LoginAct::class.java)
        }

        btnLoginAsync.setOnClickListener {
            mPresenter.onLoginAsyncButtonClick()
        }

        btnLoginAsyncCoroutine.setOnClickListener {
            mPresenter.onLoginAsyncCoroutineButtonClick()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        progressBar.visibility = View.GONE
    }

    override fun showUserEmpty(resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show()
    }

    override fun showPasswordEmpty(resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show()
    }

    override fun getUserName(): String {
        return tvName.text.toString()
    }

    override fun getPassword(): String {
        return tvPassword.text.toString()
    }

    override fun successAuth() {
        Toast.makeText(this, "succes Auth", Toast.LENGTH_LONG).show()
    }

    override fun errorAuth() {
        Toast.makeText(this, "something was wrong in auth", Toast.LENGTH_LONG).show()
    }
}