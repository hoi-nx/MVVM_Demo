package com.example.mvvm_demo.webservices

import com.facebook.stetho.okhttp3.BuildConfig
import com.facebook.stetho.okhttp3.StethoInterceptor
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


abstract class BaseRestClient : IRetrofit {
    private var mRetrofit: Retrofit? = null
    private val TIMEOUT = 6L

    override fun <T> createService(baseUrl: String, mClass: Class<T>): T {
        val okHttpClient = createOkHttpClient()
        mRetrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .client(okHttpClient)
            .build()
        return mRetrofit!!.create(mClass)
    }

    private fun createOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
//
        //.addInterceptor(AuthInterceptor(mToken!!))
        val builder = OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)

//        if (BuildConfig.DEBUG)
//            builder.addNetworkInterceptor(StethoInterceptor())

        return builder.build()
    }

    override fun destory() {
        mRetrofit = null
    }

}
