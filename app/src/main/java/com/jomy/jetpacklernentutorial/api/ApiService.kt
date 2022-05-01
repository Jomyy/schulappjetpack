package com.jomy.jetpacklernentutorial.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface APIService {
    @GET("foodplan")
    suspend fun getFood(): List<List<String>>

    @GET("substitutions")
    suspend fun getSubs(): List<List<String>>

    @GET("substitutionsnext")
    suspend fun getSubsNext(): List<List<String>>

    companion object {
        var apiService: APIService? = null
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
