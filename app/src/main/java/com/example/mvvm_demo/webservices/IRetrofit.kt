package com.example.mvvm_demo.webservices

interface IRetrofit {
    fun <T> createService(baseUrl: String,mClass: Class<T>): T
    fun destory()

}
