package com.example.mvvm_demo

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

object BaseThread{
    private var disposables: MutableList<Disposable> = ArrayList()
    fun <O> interact(ob: Observable<O>, onNext: MBack<O>, onError: MBack<Throwable>) {
        val ob = ob.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
        checkDisposable()
        //disposable kich hoat phuong thuc subscribe trong Observable
        val disposable = ob.subscribe({ response ->
            //callback interface. dung interface de chuyen du lieu
            onNext.call(response)
        }, { error -> onError.call(error) })
        disposables.add(disposable)
    }
    fun <O> single(ob: Single<O>, onNext: MBack<O>, onError: MBack<Throwable>) {
        var ob = ob.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
        checkDisposable()
        val disposable = ob.subscribe({ response ->
            onNext.call(response)
        }, { error -> onError.call(error) })
        disposables.add(disposable)
    }
    fun <O> interactRetrofit(call: Call<O>, onNext: MBack<Response<O>>, onError: MBack<Throwable>) {
        call.enqueue(object : Callback<O> {
            override fun onResponse(call: Call<O>, response: Response<O>) {
                onNext.call(response)
            }
            override fun onFailure(call: Call<O>, t: Throwable) {
                onError.call(t)
            }
        })
    }
    @SuppressLint("NewApi")
    fun onDestroy() {
        //giai phong' ex:thread.....
        disposables.forEach { item -> item.dispose() }
        Log.d(TAG, "onDestroy: " + "=======")

    }
     fun checkDisposable() {
        for (i in disposables.indices.reversed()) {
            if (disposables[i].isDisposed) {
                disposables.removeAt(i)
            }
        }
    }
}