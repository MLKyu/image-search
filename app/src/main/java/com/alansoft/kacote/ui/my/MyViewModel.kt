package com.alansoft.kacote.ui.my

import androidx.lifecycle.ViewModel
import com.alansoft.kacote.repository.KakaoSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by LEE MIN KYU on 2021/03/27
 * Copyright © 2021 Dreamus Company. All rights reserved.
 */
@HiltViewModel
class MyViewModel @Inject constructor(
    private val repository: KakaoSearchRepository
) : ViewModel() {


}