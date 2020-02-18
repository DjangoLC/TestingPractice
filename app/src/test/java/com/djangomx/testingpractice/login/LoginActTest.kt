package com.djangomx.testingpractice.login

import com.nhaarman.mockitokotlin2.*
import org.mockito.ArgumentCaptor
import org.mockito.Captor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.djangomx.testingpractice.R
import org.junit.*
import org.mockito.MockitoAnnotations

class LoginActTest {

    private lateinit var mPresenter: LoginContract.LoginPresenter
    private lateinit var mView: LoginContract.LoginView
    private lateinit var mModel: LoginContract.LoginModel

    private lateinit var userCorrect: String
    private lateinit var userFail: String

    private lateinit var passworCorrect: String
    private lateinit var paswordFail: String

    @Captor
    private lateinit var operationCallbackCaptor: ArgumentCaptor<CallbackLogin>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)

        mView = mock()
        mModel = mock()

        mPresenter = LoginPresenterImpl()
        mPresenter.attachView(mView, mModel)

        userCorrect = "kike"
        passworCorrect = "123"

        userFail = "anotherUser"
        paswordFail = "asd"
    }

    @Test
    fun validateCredentialsSuccess() {
        whenever(mView.getUserName()).thenReturn(userCorrect)
        whenever(mView.getPassword()).thenReturn(passworCorrect)

        whenever(mModel.validateUserCredentials(userCorrect, passworCorrect)).thenReturn(true)

        mPresenter.onLoginButtonClick()

        verify(mView).showLoading()
        verify(mView).dismissLoading()
        verify(mView).successAuth()
    }

    @Test
    fun validateCredentialsFails() {
        whenever(mView.getUserName()).thenReturn(userFail)
        whenever(mView.getPassword()).thenReturn(paswordFail)

        whenever(mModel.validateUserCredentials(userFail, paswordFail)).thenReturn(false)

        mPresenter.onLoginButtonClick()

        verify(mView).showLoading()
        verify(mView).dismissLoading()
        verify(mView).errorAuth()
    }

    @Test
    fun validateUserEmpty() {
        whenever(mView.getUserName()).thenReturn("")

        mPresenter.onLoginButtonClick()

        verify(mView).showUserEmpty(R.string.user_empty)
    }

    @Test
    fun validatePasswordEmpty() {
        whenever(mView.getUserName()).thenReturn(userCorrect)
        whenever(mView.getPassword()).thenReturn("")

        mPresenter.onLoginButtonClick()

        verify(mView).showPasswordEmpty(R.string.password_empty)
    }

    @Test
    fun validateCredentialsWithCallbackSuccess() {

        verify(mModel).validateUserCredentialsWithCallback(capture(operationCallbackCaptor))
        operationCallbackCaptor.value.result(false)

        mPresenter.onLoginAsyncButtonClick()
        verify(mView).errorAuth()


    }


    @After
    fun tearDown() {

    }
}