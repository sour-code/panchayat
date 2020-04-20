package com.example.coro1

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private var retrofit: Retrofit? = null
class Client{
    fun getClient(url: String): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}
