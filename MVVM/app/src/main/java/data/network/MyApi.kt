package com.xyz.data.network

import com.xyz.BuildConfig
import com.xyz.data.db.AppDatabase
import com.xyz.data.network.responses.CommonResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface MyApi {

    @FormUrlEncoded
    @POST("v1/users")
    suspend fun userLogin(
        @Field("email") email: String,
        @Field("full_name") name: String,
        @Field("photo") photo: String
    ): Response<CommonResponse>

   

    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor,db: AppDatabase
        ): MyApi {

            val httpClient = OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(networkConnectionInterceptor)
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder().addHeader("authorization", "Bearer ${db.getUserDao().getToken()}").build()
                    chain.proceed(request)
                }


            return Retrofit.Builder()
                .client(httpClient.build())
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }

    }

}

