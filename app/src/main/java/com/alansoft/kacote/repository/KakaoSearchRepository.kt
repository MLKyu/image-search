package com.alansoft.kacote.repository

import androidx.lifecycle.liveData
import com.alansoft.kacote.data.KakaoSearchDataSource
import com.alansoft.kacote.data.model.ImageDocuments
import com.alansoft.kacote.data.model.SearchResponse
import com.alansoft.kacote.data.utils.Resource
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * Created by LEE MIN KYU on 2021/03/25
 * Copyright © 2021 Dreamus Company. All rights reserved.
 */
class KakaoSearchRepository @Inject constructor(
    private val kakaoSearchDataSource: KakaoSearchDataSource
) {


    /**
     * query	String	검색을 원하는 질의어	O
    sort	String	결과 문서 정렬 방식, accuracy(정확도순) 또는 recency(최신순), 기본 값 accuracy	X
    page	Integer	결과 페이지 번호, 1~50 사이의 값, 기본 값 1	X
    size	Integer	한 페이지에 보여질 문서 수, 1~50 사이의 값, 기본 값 10	X
     */
    fun searchQuery(query: String) =
        liveData<Resource<SearchResponse<ImageDocuments>>>(Dispatchers.IO) {
            emit(Resource.loading())
            val responseStatus = kakaoSearchDataSource.getSearchImg(query, "recency", 1, 20)
            when (responseStatus.status) {
                Resource.Status.SUCCESS -> {
                    responseStatus.data?.let {
                        emit(Resource.success(it))
                    }
                }
                Resource.Status.ERROR -> {
                    responseStatus.message?.let {
                        emit(Resource.error(it))
                    }
                }
                else -> {
                    // loading
                }
            }
        }
}