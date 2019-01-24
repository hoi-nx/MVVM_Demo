package com.example.mvvm_demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.mvvm_demo.database.entity.QuestionEntity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val viewModel=ViewModelProviders.of(this).get(QuestionModel::class.java)
//        val nameObserver = Observer<Resource<List<QuestionEntity>>> { newName ->
//            Log.d("Main",""+newName.status.name)
//            if(newName==null){
//                Log.d("Main","Null rooif")
//            }else{
//              // Log.d("Main",""+newName.data!!.get(0).avatar_url)
//                Log.d("Main","Not Null rooif")
//            }
//
//
//        }
//
//        viewModel.loadQuestion().observe(this,nameObserver)

        viewModel.loadQuestion().observe(this,
            Observer<Resource<List<QuestionEntity>>> {
                Log.d("Main",""+it.status.toString())
                if (null != it && (it.status === Status.ERROR || it.status === Status.SUCCESS)) {
                    Log.d("Main",""+it.status.toString())
                    if(it.data!=null){
                        Log.d("Main",""+it.data.size.toString())
                    }

                }
            })

    }
}
