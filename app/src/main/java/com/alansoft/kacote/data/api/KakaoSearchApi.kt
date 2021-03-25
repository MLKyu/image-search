package com.alansoft.kacote.data.api

import com.alansoft.kacote.data.model.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 * Created by LEE MIN KYU on 2021/03/25
 * Copyright © 2021 Dreamus Company. All rights reserved.
 */
interface KakaoSearchApi {
    @Headers("Authorization:0e4079250a010fd99eb6e56583f42202")
    @GET("/v2/search/image")
    suspend fun getSearchImg(
        @Path("query") query: String,
        @Path("sort") sort: String,
        @Path("page") page: Int,
        @Path("size") size: Int
    ): Response<SearchResponse>

    @Headers("Authorization:0e4079250a010fd99eb6e56583f42202")
    @GET("/v2/search/video")
    suspend fun getSearchVideo(
        @Path("query") query: String,
        @Path("sort") sort: String,
        @Path("page") page: Int,
        @Path("size") size: Int
    ): Response<SearchResponse>
}