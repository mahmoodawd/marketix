package com.example.shopify.data.remote

import com.example.shopify.BuildConfig
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor : Interceptor {


    val credentials = Credentials.basic(BuildConfig.API_KEY, BuildConfig.API_SECRET)

    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request()
            .url
            .newBuilder()
            .build()

        val request = chain.request()
            .newBuilder()
            .header("Authorization", credentials)
            .url(url)
            .build()

        return chain.proceed(request)
    }
}