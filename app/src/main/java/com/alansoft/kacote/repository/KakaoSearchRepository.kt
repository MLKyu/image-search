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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.withContext
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

    fun searchMerge(query: String, page: Int = FIRST_PAGE) =
        liveData(Dispatchers.IO) {
            searchImgQuery(query, page).zip(searchVClipQuery(query, page)) { list1, list2 ->
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

     fun searchImgQuery(query: String, page: Int = FIRST_PAGE) =
        flow<Resource<SearchResponse<ImageDocuments>>> {
            emit(Resource.loading())
            val responseStatus = kakaoSearchDataSource.getSearchImg(
                query, SearchSortType.RECENCY, page, PAGE_SIZE
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

    private fun searchVClipQuery(query: String, page: Int = FIRST_PAGE) =
        flow<Resource<SearchResponse<VClipDocuments>>> {
            emit(Resource.loading())
            val responseStatus =
                kakaoSearchDataSource.getSearchVclip(
                    query,
                    SearchSortType.RECENCY,
                    page,
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

        return SearchMerge(data1?.meta, data2?.meta, mergeList)
    }

    fun getMy() = liveData {
        emitSource(myDataSource.resource)
    }

    suspend fun insertItem(data: Documents) {
        withContext(Dispatchers.Main) {
            myDataSource.insertDocument(data)
        }
    }

    suspend fun deleteItem(data: Documents) {
        withContext(Dispatchers.Main) {
            myDataSource.deleteDocument(data)
        }
    }
}