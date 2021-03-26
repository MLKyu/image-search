package com.alansoft.kacote.ui.search

import androidx.lifecycle.ViewModel
import com.alansoft.kacote.repository.KakaoSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: KakaoSearchRepository
) : ViewModel() {

//    val data = repository.searchQuery()

}