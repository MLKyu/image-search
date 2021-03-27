package com.alansoft.kacote.ui.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alansoft.kacote.data.model.Documents
import com.alansoft.kacote.repository.KakaoSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by LEE MIN KYU on 2021/03/27
 * Copyright © 2021 Dreamus Company. All rights reserved.
 */
@HiltViewModel
class MyViewModel @Inject constructor(
    private val repository: KakaoSearchRepository
) : ViewModel() {

    val results = repository.getMy()

    fun deleteItem(data: Documents) {
        viewModelScope.launch {
            repository.deleteItem(data)
        }
    }
}