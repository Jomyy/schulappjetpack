package com.jomy.schulapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

const val CACHE_CONTROL_HEADER = "Cache-Control"
const val CACHE_CONTROL_NO_CACHE = "no-cache"

interface APIService {

    @GET("foodplan")
    @Headers("$CACHE_CONTROL_HEADER: $CACHE_CONTROL_NO_CACHE")
    suspend fun getFood(): List<List<String>>

    @GET("substitutions")
    @Headers("$CACHE_CONTROL_HEADER: $CACHE_CONTROL_NO_CACHE")
    suspend fun getSubs(): List<List<String>>

    @GET("substitutionsnext")
    @Headers("$CACHE_CONTROL_HEADER: $CACHE_CONTROL_NO_CACHE")
    suspend fun getSubsNext(): List<List<String>>

    @GET("allclasses")
    @Headers("$CACHE_CONTROL_HEADER: $CACHE_CONTROL_NO_CACHE")
    suspend fun getAllClasses(): List<String>

    companion object {
        private var apiService: APIService? = null
        fun getInstance(): APIService {
            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl("https://schulapi.ddns.net/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(APIService::class.java)
            }
            return apiService!!
        }
    }
}
