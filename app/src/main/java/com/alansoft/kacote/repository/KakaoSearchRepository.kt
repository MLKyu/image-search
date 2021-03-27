package com.alansoft.kacote.repository

import androidx.lifecycle.liveData
import com.alansoft.kacote.data.KakaoSearchDataSource
import com.alansoft.kacote.data.MyDataSource
import com.alansoft.kacote.data.model.*
import com.alansoft.kacote.data.utils.Resource
import com.alansoft.kacote.utils.FIRST_PAGE
import com.alansoft.kacote.utils.PAGE_SIZE
import com.alansoft.kacote.utils.SearchSortType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by LEE MIN KYU on 2021/03/25
 * Copyright © 2021 Dreamus Company. All rights reserved.
 */
@Singleton
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
            val responseStatus =
                kakaoSearchDataSource.getSearchImg(
                    query,
                    SearchSortType.RECENCY,
                    FIRST_PAGE,
                    PAGE_SIZE
                )
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
        liveData(Dispatchers.IO) {
            combine(
                searchImgQuery(query),
                searchVClipQuery(query)
            ) { list1, list2 ->
                if (list1.status == Resource.Status.SUCCESS || list2.status == Resource.Status.SUCCESS) {
                    if (list1.data != null || list2.data != null) {
                        Resource.success(sort(list1.data, list2.data))
                    } else {
                        Resource.error(list1.message ?: list2.message ?: "검색 결과가 없습니다.", null)
                    }
                } else if (list1.status == Resource.Status.ERROR && list2.status == Resource.Status.ERROR) {
                    Resource.error(list1.message ?: list2.message ?: "네트워크 연결 실패", null)
                } else {
                    Resource.loading()
                }

            }.collect {
                emit(it)
            }
        }

    private fun searchImgQuery(query: String) =
        flow<Resource<SearchResponse<ImageDocuments>>> {
            emit(Resource.loading())
            val responseStatus = kakaoSearchDataSource.getSearchImg(
                query, SearchSortType.RECENCY, FIRST_PAGE, PAGE_SIZE
            )
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
                    // ignore
                }
            }
        }

    private fun searchVClipQuery(query: String) =
        flow<Resource<SearchResponse<VClipDocuments>>> {
            emit(Resource.loading())
            val responseStatus =
                kakaoSearchDataSource.getSearchVclip(
                    query,
                    SearchSortType.RECENCY,
                    FIRST_PAGE,
                    PAGE_SIZE
                )
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
                    // ignore
                }
            }
        }

    private fun sort(
        data1: SearchResponse<ImageDocuments>?,
        data2: SearchResponse<VClipDocuments>?
    ): SearchMerge {
        val mergeList = ArrayList<Documents>()
        data1?.documents?.let {
            mergeList.addAll(it)
        }
        data2?.documents?.let {
            mergeList.addAll(it)
        }
        if (data1?.documents != null && data2?.documents != null) {
            mergeList.sortByDescending { it.datetime }
        }

        val merge = SearchMerge(data1?.meta, data2?.meta, mergeList)
        return merge
    }

    fun getMy() = liveData {
        emitSource(myDataSource.resource)
    }

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