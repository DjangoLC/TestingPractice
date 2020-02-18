package com.djangomx.testingpractice.login

import com.nhaarman.mockitokotlin2.*
import org.mockito.ArgumentCaptor
import org.mockito.Captor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.djangomx.testingpractice.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
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
    fun validateCredentialsWithCallbackSuccess() {

        verify(mModel).validateUserCredentialsWithCallback(userCorrect,passworCorrect,capture(operationCallbackCaptor))
        operationCallbackCaptor.value.result(false)

        mPresenter.onLoginAsyncButtonClick()
        verify(mView).errorAuth()

    }


    @Test
    fun validateCredentialsWithSuspendSuccess() {
        whenever(mView.getUserName()).thenReturn(userCorrect)
        whenever(mView.getPassword()).thenReturn(passworCorrect)

        runBlockingTest {
            whenever(mModel.validateUserCredentialsSuspend(userCorrect,passworCorrect)).thenReturn(true)

            mPresenter.onLoginAsyncCoroutineButtonClick()
            verify(mView, times(1)).successAuth()
        }
    }

    @Test
    fun validateCredentialsWithSuspendError() {
        whenever(mView.getUserName()).thenReturn(userCorrect)
        whenever(mView.getPassword()).thenReturn(paswordFail)

        runBlockingTest {
            whenever(mModel.validateUserCredentialsSuspend(userCorrect,paswordFail)).thenReturn(false)

            mPresenter.onLoginAsyncCoroutineButtonClick()
            verify(mView, times(1)).errorAuth()
        }
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}