package com.djangomx.testingpractice

interface BasePresenter<V,M> {

    fun attachView(view: V, model: M)
    fun detachView()

}