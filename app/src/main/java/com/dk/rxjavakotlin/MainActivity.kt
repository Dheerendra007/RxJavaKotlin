package com.dk.rxjavakotlin

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dk.rxjavakotlin.databinding.ActivityMainBinding
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
       // simpleObserver()
        //createObserable()
       // binding.fab.click
        implementNetworkCall()
    }

    @SuppressLint("CheckResult")
    private fun implementNetworkCall() {
        val retrofit = Retrofit.Builder().baseUrl("https://fakestoreapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()

        val productService = retrofit.create(ProductService::class.java)
        productService.getProducts()
            .subscribeOn(Schedulers.io())// up stream / all above code apply
            .observeOn(AndroidSchedulers.mainThread())// down stream / all below code apply
            .subscribe{
                Log.d("SampleObserver productService : ",it.toString())
        }
    }

    private fun createObserable() {
        val observable = Observable.create<String>{
            it.onNext("1")
            it.onError(IllegalAccessError("IllegalAccessError"))
            it.onNext("2")
            it.onComplete()
        }

        observable.subscribe(object :Observer<String>{
            override fun onSubscribe(d: Disposable) {
                Log.d("SampleObserver","onSubscribe $d")
            }

            override fun onError(e: Throwable) {
                Log.d("SampleObserver","onError $e")
            }

            override fun onComplete() {
                Log.d("SampleObserver","onComplete")
            }

            override fun onNext(t: String) {
                Log.d("SampleObserver","onNext $t")
            }

        })
    }

    fun simpleObserver(){
      //  Log.d("SampleObserver","doAction")
        val list = listOf<String>("A","B","C")
        val observable = Observable.fromIterable(list)
        observable.subscribe(/* observer = */ object :Observer<String>{
            override fun onSubscribe(d: Disposable) {
                Log.d("SampleObserver","onSubscribe $d")
            }

            override fun onError(e: Throwable) {
                Log.d("SampleObserver","onError $e")
            }

            override fun onComplete() {
                Log.d("SampleObserver","onComplete")
            }

            override fun onNext(t: String) {
                Log.d("SampleObserver","onNext $t")
            }

        })
    }
}