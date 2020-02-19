package com.djangomx.testingpractice.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.djangomx.testingpractice.R
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.mockito.MockitoAnnotations

class LoginPresenterImplTest {

    private lateinit var mPresenter: LoginContract.LoginPresenter
    private lateinit var mView: LoginContract.LoginView
    private lateinit var mModel: LoginContract.LoginModel

    private lateinit var userCorrect: String
    private lateinit var userFail: String

    private lateinit var passworCorrect: String
    private lateinit var paswordFail: String

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)

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
    fun validateCredentialsWithSuspendSuccess() = runBlockingTest {
        whenever(mView.getUserName()).thenReturn(userCorrect)
        whenever(mView.getPassword()).thenReturn(passworCorrect)


        whenever(
            mModel.validateUserCredentialsSuspend(
                userCorrect,
                passworCorrect
            )
        ).thenReturn(true)

        mPresenter.onLoginAsyncCoroutineButtonClick()
        verify(mView, times(1)).successAuth()

    }

    @Test
    fun validateCredentialsWithCallbackSuccess() {

        whenever(mView.getUserName()).thenReturn(userCorrect)
        whenever(mView.getPassword()).thenReturn(passworCorrect)
        val response = true

        doAnswer {
            (it.getArgument(0) as String)
            (it.getArgument(1) as String)
            (it.getArgument(2) as CallbackLogin).result(response)
            return@doAnswer null
        }.whenever(mModel).validateUserCredentialsWithCallback(any(), any(), any())

        mPresenter.onLoginAsyncButtonClick()
        verify(mView).successAuth()
    }

    @Test
    fun validateCredentialsWithCallbackError() {

        whenever(mView.getUserName()).thenReturn(userCorrect)
        whenever(mView.getPassword()).thenReturn(passworCorrect)
        val response = false

        doAnswer {
            (it.getArgument(0) as String)
            (it.getArgument(1) as String)
            (it.getArgument(2) as CallbackLogin).result(response)
            return@doAnswer null
        }.whenever(mModel).validateUserCredentialsWithCallback(any(), any(), any())

        mPresenter.onLoginAsyncButtonClick()
        verify(mView).errorAuth()
    }


    @Test
    fun validateCredentialsWithSuspendError() = runBlockingTest {
        whenever(mView.getUserName()).thenReturn(userCorrect)
        whenever(mView.getPassword()).thenReturn(paswordFail)

        whenever(mModel.validateUserCredentialsSuspend(userCorrect, paswordFail)).thenReturn(false)

        mPresenter.onLoginAsyncCoroutineButtonClick()
        verify(mView).errorAuth()

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
        mPresenter.detachView()
    }
}