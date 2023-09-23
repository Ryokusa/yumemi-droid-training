package com.example.weatherapi.api

import okhttp3.Interceptor
import okhttp3.Response

class JapaneseCurrentWeatherDataInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        val newUrl = req.url.newBuilder()
            .addQueryParameter("lang", "ja")
            .addQueryParameter("units", "metric")
            .build()
        val newReq = req.newBuilder()
            .url(newUrl)
            .build()
        return chain.proceed(newReq)
    }
}
