package com.alansoft.kacote.retofit

import com.alansoft.kacote.model.ImageSearchResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    @Headers("Authorization:0e4079250a010fd99eb6e56583f42202" )
    @GET("/v2/search/image")
    suspend fun getImageSearch(
        @Query("query") query: String,
        @Query("sort") sort: String,
        @Query("page") page: String,
        @Query("size") size: String
    ) : ImageSearchResponse
}