package com.alansoft.kacote.ui.search

import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.alansoft.kacote.data.utils.Resource
import com.alansoft.kacote.repository.KakaoSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: KakaoSearchRepository
) : ViewModel() {

    private val query = MutableLiveData<String>()
    private val nextPageHandler = NextPageHandler(repository)

    val results = query.switchMap { search ->
        repository.searchMerge(search)
    }

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
//        if (results.value?.data?.meta?.is_end != false) {
//        } else {
        query.value?.let {
            if (it.isNotBlank()) {
                nextPageHandler.queryNextPage(it)
            }
        }
//        }
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

    class NextPageHandler(private val repository: KakaoSearchRepository) :
        Observer<Resource<Boolean>> {
        private var nextPageLiveData: LiveData<Resource<Boolean>>? = null
        val loadMoreState = MutableLiveData<LoadMoreState>()
        private var query: String? = null
        private var _hasMore: Boolean = false
        val hasMore
            get() = _hasMore

        init {
            reset()
        }

        fun queryNextPage(query: String) {
            if (this.query == query) {
                return
            }
            unregister()
            this.query = query

            nextPageLiveData = repository.searchQuery(query).switchMap { result ->
                liveData {
                    when (result.status) {
                        Resource.Status.SUCCESS -> {
                            emit(Resource.success(true))
                        }
                        Resource.Status.ERROR -> {
                            emit(Resource.success(false))
                        }
                        else -> {
                        }
                    }
                }
            }
            loadMoreState.value = LoadMoreState(
                isRunning = true,
                errorMessage = null
            )
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
        }
    }

}