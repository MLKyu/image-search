package com.alansoft.kacote.ui.search

import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.alansoft.kacote.data.model.Documents
import com.alansoft.kacote.data.model.SearchMerge
import com.alansoft.kacote.data.utils.Resource
import com.alansoft.kacote.repository.KakaoSearchRepository
import com.alansoft.kacote.utils.FIRST_PAGE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * Created by LEE MIN KYU on 2021/03/24
 * Copyright © 2021 Dreamus Company. All rights reserved.
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: KakaoSearchRepository
) : ViewModel() {

    private val query by lazy { MutableLiveData<String>() }
    private val nextPageHandler by lazy { NextPageHandler(repository) }
    val _results by lazy { query.switchMap { search -> repository.searchMerge(search) } as MutableLiveData }
    val results: LiveData<Resource<SearchMerge>> by lazy { _results }

    val loadMoreStatus: LiveData<LoadMoreState>
        get() = nextPageHandler.loadMoreState

    fun setQuery(originalInput: String) {
        val input = originalInput.toLowerCase(Locale.getDefault()).trim()
        if (input == query.value) {
            return
        }
        nextPageHandler.reset()
        if (input.isNotBlank()) {
            query.value = input
        }
    }

    fun loadNextPage() {
        results.value?.data?.run {
            if (imageMeta?.is_end == false || vClipMeta?.is_end == false) {
                query.value?.let {
                    if (it.isNotBlank()) {
                        nextPageHandler.queryNextPage(it)
                    }
                }
            }
        }
    }


    class LoadMoreState(val isRunning: Boolean, val errorMessage: String?) {
        private var handledError = false

        val errorMessageIfNotHandled: String?
            get() {
                if (handledError) {
                    return null
                }
                handledError = true
                return errorMessage
            }
    }

    inner class NextPageHandler(private val repository: KakaoSearchRepository) :
        Observer<Resource<Boolean>> {
        private var nextPageLiveData: LiveData<Resource<Boolean>>? = null
        val loadMoreState = MutableLiveData<LoadMoreState>()
        private var query: String? = null
        private var _hasMore: Boolean = false
        val hasMore
            get() = _hasMore
        private var nextPage = FIRST_PAGE

        init {
            reset()
        }

        fun queryNextPage(query: String) {
            if (this.query == query) {
                return
            }
            unregister()
            this.query = query
            nextPage += 1
            loadMoreState.value = LoadMoreState(
                isRunning = true,
                errorMessage = null
            )
            nextPageLiveData = repository.searchMerge(query, nextPage).switchMap { result ->
                liveData {
                    when (result.status) {
                        Resource.Status.SUCCESS -> {
                            emit(Resource.success(true))

                            val newResult = arrayListOf<Documents>()
                            val tempResult = results.value
                            tempResult?.data?.documents?.let {
                                newResult.addAll(it)
                            }
                            result.data?.documents?.let {
                                newResult.addAll(it)

                                val successResult = Resource.success(
                                    SearchMerge(
                                        result.data.imageMeta,
                                        result.data.vClipMeta,
                                        newResult
                                    )
                                )
                                _results.value = successResult
                            }
                        }
                        Resource.Status.ERROR -> {
                            emit(Resource.error<Boolean>(result.message ?: "로드에 실패 하였습니다."))
                        }
                        else -> {
                        }
                    }
                }
            }
            nextPageLiveData?.observeForever(this)
        }

        override fun onChanged(result: Resource<Boolean>?) {
            if (result == null) {
                reset()
            } else {
                when (result.status) {
                    Resource.Status.SUCCESS -> {
                        _hasMore = result.data == true
                        unregister()
                        loadMoreState.setValue(
                            LoadMoreState(
                                isRunning = false,
                                errorMessage = null
                            )
                        )
                    }
                    Resource.Status.ERROR -> {
                        _hasMore = true
                        unregister()
                        loadMoreState.setValue(
                            LoadMoreState(
                                isRunning = false,
                                errorMessage = result.message
                            )
                        )
                    }
                    Resource.Status.LOADING -> {
                        // ignore
                    }
                }
            }
        }

        private fun unregister() {
            nextPageLiveData?.removeObserver(this)
            nextPageLiveData = null
            if (_hasMore) {
                query = null
            }
        }

        fun reset() {
            unregister()
            _hasMore = true
            loadMoreState.value = LoadMoreState(
                isRunning = false,
                errorMessage = null
            )
            nextPage = FIRST_PAGE
        }
    }

    fun insertMyItem(data: Documents) {
        viewModelScope.launch {
            repository.insertItem(data)
        }
    }

    fun refresh() {
        query.value?.let {
            query.value = it
        }
    }
}