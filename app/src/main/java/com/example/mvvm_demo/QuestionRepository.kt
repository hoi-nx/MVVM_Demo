package com.example.mvvm_demo

import Client.service
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.mvvm_demo.database.DatabaseManager
import com.example.mvvm_demo.database.entity.QuestionEntity
import io.reactivex.Observable


class QuestionRepository constructor(
    private val appExecutors: AppExecutors) {
    fun loadQuestion(): LiveData<Resource<List<QuestionEntity>>> {
        return object : NetworkBoundResource<List<QuestionEntity>, List<QuestionEntity>>(appExecutors) {
            override fun createObser(): Observable<List<QuestionEntity>> {
                return Client.service.loadQuestion3()

            }

            override fun createCallLive(): LiveData<List<QuestionEntity>> {
                return Client.service.loadQuestion2()
            }

            override fun saveCallResult(item: List<QuestionEntity>) {
                Log.d("Sucesss",item.size.toString())
                for(i in item){
                    Log.d("Sucesss",i.avatar_url)
                }
                DatabaseManager.appDatabase.questionDao().saveArticles(item)
            }

            override fun shouldFetch(data: List<QuestionEntity>?): Boolean {
                return true //data == null || data.isEmpty()

                //
            }

            override fun loadFromDb(): LiveData<List<QuestionEntity>> = DatabaseManager.appDatabase.questionDao().getAllWords()

            override fun createCall() = Client.service.loadQuestion()
            override fun onFetchFailed(t:Throwable) {

            }
        }.getAsLiveData()
    }

}
