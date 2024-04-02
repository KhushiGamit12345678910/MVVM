package com.example.mvvmarchitecture.apifiles

import android.annotation.SuppressLint
import com.example.mvvmarchitecture.const.BASE_URL
import okhttp3.OkHttpClient

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
   /* @SuppressLint("SuspiciousIndentation")
    fun getRetrofit(): ApiInterface {
        val logging = HttpLoggingInterceptor()

        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        var httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)*/

        val retrofit: ApiInterface = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
//        return retrofit
//    }
}