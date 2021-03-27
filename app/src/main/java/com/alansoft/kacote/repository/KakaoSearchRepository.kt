package com.alansoft.kacote.repository

import androidx.lifecycle.liveData
import com.alansoft.kacote.data.KakaoSearchDataSource
import com.alansoft.kacote.data.MyDataSource
import com.alansoft.kacote.data.model.*
import com.alansoft.kacote.data.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by LEE MIN KYU on 2021/03/25
 * Copyright © 2021 Dreamus Company. All rights reserved.
 */
class KakaoSearchRepository @Inject constructor(
    private val myDataSource: MyDataSource,
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

    fun searchMerge(query: String) =
        liveData<Resource<SearchMerge>>(Dispatchers.IO) {
            combine(
                searchQuery1(query),
                searchQuery2(query)
            ) { list1, list2 ->
                if (list1.data != null && list2.data != null) {
                    Resource.success(sort(list1.data, list2.data))
                } else {
                    Resource.error("", null)
                }
            }.collect {
                emit(it)
            }
        }

    private fun searchQuery1(query: String) =
        flow<Resource<SearchResponse<ImageDocuments>>> {
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

    private fun searchQuery2(query: String) =
        flow<Resource<SearchResponse<VClipDocuments>>> {
            emit(Resource.loading())
            val responseStatus = kakaoSearchDataSource.getSearchVclip(query, "recency", 1, 20)
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

    private fun sort(
        data1: SearchResponse<ImageDocuments>,
        data2: SearchResponse<VClipDocuments>
    ): SearchMerge {

        val mergeList = ArrayList<Documents>()

        mergeList.addAll(data1.documents)
        mergeList.addAll(data2.documents)

        mergeList.sortByDescending { it.datetime }

        val merge = SearchMerge(data1.meta, data2.meta, mergeList)
        return merge
    }

    fun getMy() = myDataSource.resource

    suspend fun insertItem(data: Documents) {
        coroutineScope {
            launch { myDataSource.insertDocument(data) }
        }
    }

    suspend fun deleteItem(data: Documents) {
        coroutineScope {
            launch { myDataSource.deleteDocument(data) }
        }
    }
}