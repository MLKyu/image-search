package com.alansoft.kacote.data.api

import com.alansoft.kacote.data.model.ImageDocuments
import com.alansoft.kacote.data.model.SearchResponse
import com.alansoft.kacote.data.model.VClipDocuments
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by LEE MIN KYU on 2021/03/25
 * Copyright © 2021 Dreamus Company. All rights reserved.
 */
interface KakaoSearchApi {
    @GET("/v2/search/image")
    suspend fun getSearchImg(
        @Query("query") query: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<SearchResponse<ImageDocuments>>

    @GET("/v2/search/vclip")
    suspend fun getSearchVideo(
        @Query("query") query: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<SearchResponse<VClipDocuments>>
}