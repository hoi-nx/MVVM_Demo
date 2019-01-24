
package com.example.mvvm_demo

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.annotation.MainThread
import androidx.annotation.NonNull
import androidx.annotation.WorkerThread
import androidx.lifecycle.Observer
import com.google.gson.stream.MalformedJsonException
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

abstract class NetworkBoundResource<T, V> @MainThread constructor(private val appExecutors: AppExecutors) {

    private val result = MediatorLiveData<Resource<T>>()

    init {
        result.value = Resource.loading(null)
        @Suppress("LeakingThis")
        val dbSource = loadFromDb()

//        result.addSource(dbSource,object : Observer<T> {
//            override fun onChanged(t: T) {
//                result.removeSource(dbSource)
//                if(shouldFetch(t)){
//                    fetchFromNetwork(dbSource)
//                }else{
//                    result.addSource(dbSource,object :Observer<T>{
//                        override fun onChanged(newData: T) {
//                            setValue(Resource.success(newData))
//                        }
//
//                    })
//                }
//            }
//        })

        result.addSource(dbSource) { t ->
            result.removeSource(dbSource)
            if (shouldFetch(t)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(
                    dbSource
                ) { newData -> setValue(Resource.success(newData)) }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<T>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    @SuppressLint("CheckResult")
    private fun fetchFromNetwork(dbSource: LiveData<T>) {
        result.addSource(dbSource) { newData -> result.setValue(Resource.loading(newData)) }
        createCall().enqueue(object : Callback<V> {
            override fun onResponse(@NonNull call: Call<V>, @NonNull response: Response<V>) {
                result.removeSource(dbSource)
                Log.d("Success",""+response.body().toString())
                saveResultAndReInit(response.body())
            }

            override fun onFailure(@NonNull call: Call<V>, @NonNull t: Throwable) {
                Log.d("ERROR",t.message.toString())
                onFetchFailed(t)
                result.removeSource(dbSource)
                result.addSource(dbSource) { newData ->
                    result.setValue(Resource.error(getCustomErrorMessage(t), newData))
                }
            }
        })

//       BaseThread.interact(createObser(),object :MBack<V>{
//           override fun call(e: V) {
//               result.removeSource(dbSource)
//               saveResultAndReInit(e)
//           }
//       },object :MBack<Throwable>{
//           override fun call(error: Throwable) {
//               onFetchFailed(error)
//               result.removeSource(dbSource)
//               result.addSource(dbSource) { newData ->
//                   result.setValue(Resource.error(getCustomErrorMessage(error), newData))
//               }
//           }
//       })

//        createObser().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).
//            subscribe({body->
//                result.removeSource(dbSource)
//                saveResultAndReInit(body)
//        },{error->
//                onFetchFailed(error)
//                result.removeSource(dbSource)
//                result.addSource(dbSource) { newData ->
//                    result.setValue(Resource.error(getCustomErrorMessage(error), newData))
//                }
//        })


//        val apiCall = createCallLive()
//        result.addSource(apiCall) { t ->
//            appExecutors.diskIO().execute {
//                saveCallResult(t)
//                appExecutors.mainThread().execute {
//                    result.addSource(loadFromDb()) { newData ->
//                        setValue(Resource.success(newData))
//                    }
//                }
//            }
//        }

    }

    private fun getCustomErrorMessage(error: Throwable): String {

        return if (error is SocketTimeoutException) {
            Log.d("ERROR","SocketTimeoutException")
            "SocketTimeoutException"

        } else if (error is MalformedJsonException) {
            Log.d("ERROR","MalformedJsonException")
            "MalformedJsonException"
        } else if (error is IOException) {
            Log.d("ERROR","IOException")
            "IOException"
        } else if (error is HttpException) {
            Log.d("ERROR",error.response().message())
            error.response().message()
        } else {
            "Something went wrong!"
        }

    }

    @SuppressLint("StaticFieldLeak")
    @MainThread
    private fun saveResultAndReInit(response: V?) {
//        object : AsyncTask<Void, Void, Void>() {
//
//            @SuppressLint("WrongThread")
//            override fun doInBackground(vararg voids: Void): Void? {
//                saveCallResult(response!!)
//                return null
//            }
//
//            override fun onPostExecute(aVoid: Void) {
//                result.addSource(loadFromDb()) { newData ->
//                    if (null != newData)
//                        result.setValue(Resource.success(newData))
//                }
//            }
//        }.execute()
        appExecutors.diskIO().execute {
            saveCallResult(response!!)
            appExecutors.mainThread().execute {
                result.addSource(loadFromDb()) { newData ->
                    Log.d("Sucessscs",newData.toString())
                    setValue(Resource.success(newData))

                }
            }
        }
    }

    protected abstract fun onFetchFailed(t:Throwable)

    fun getAsLiveData() = result as LiveData<Resource<T>>

    @NonNull
    @MainThread
    protected abstract fun createCall(): Call<V>

    @NonNull
    @MainThread
    protected abstract fun createObser(): Observable<V>

    @NonNull
    @MainThread
    protected abstract fun createCallLive(): LiveData<V>

    @WorkerThread
    protected abstract fun saveCallResult(item: V)

    @MainThread
    protected abstract fun shouldFetch(data: T?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveData<T>

}
