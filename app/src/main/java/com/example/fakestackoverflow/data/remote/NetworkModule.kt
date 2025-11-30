package com.example.fakestackoverflow.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.util.concurrent.TimeUnit

 object NetworkModule {


     private const val BASE_URL = "https://api.stackexchange.com/2.3/"

     private val moshi: Moshi = Moshi.Builder()
         .add(KotlinJsonAdapterFactory())
         .build()

     fun provideOkHttpClient(): OkHttpClient {
         val logging = HttpLoggingInterceptor().apply {
             level = HttpLoggingInterceptor.Level.BODY
         }
         return OkHttpClient.Builder()
             .addInterceptor(logging)
             .connectTimeout(20, TimeUnit.SECONDS)
             .readTimeout(30, TimeUnit.SECONDS)
             .build()
     }

     fun provideRetrofit(okHttpClient: OkHttpClient = provideOkHttpClient()): Retrofit {
         return Retrofit.Builder()
             .baseUrl(BASE_URL)
             .client(okHttpClient)
             .addConverterFactory(MoshiConverterFactory.create(moshi))
             .build()
     }

     fun provideStackApi(retrofit: Retrofit = provideRetrofit()): StackApi {
         return retrofit.create(StackApi::class.java)
     }
 }