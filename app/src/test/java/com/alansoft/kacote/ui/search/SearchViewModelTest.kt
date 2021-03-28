package com.alansoft.kacote.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alansoft.kacote.data.model.SearchMerge
import com.alansoft.kacote.data.utils.Resource
import com.alansoft.kacote.mock
import com.alansoft.kacote.repository.KakaoSearchRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.mock

/**
 * Created by LEE MIN KYU on 2021/03/28
 * Copyright © 2021 Dreamus Company. All rights reserved.
 */
@RunWith(JUnit4::class)
class SearchViewModelTest {
    @Rule
    @JvmField
    val instantExecutor = InstantTaskExecutorRule()
    private val repository = mock(KakaoSearchRepository::class.java)
    private lateinit var viewModel: SearchViewModel

    @Before
    fun init() {
        viewModel = SearchViewModel(repository)
    }

    @Test
    fun empty() {
        val result = mock<Observer<Resource<SearchMerge>>>()
        viewModel.results.observeForever(result)
        viewModel.loadNextPage()
        Mockito.verifyNoMoreInteractions(repository)
    }

    @Test
    fun refresh() {
        viewModel.refresh()
        Mockito.verifyNoMoreInteractions(repository)
        viewModel.setQuery("이민규")
        viewModel.refresh()
        Mockito.verifyNoMoreInteractions(repository)
        viewModel.results.observeForever(mock())
        Mockito.verify(repository).searchMerge("이민규")
        Mockito.reset(repository)
        viewModel.refresh()
        Mockito.verify(repository).searchMerge("이민규")
    }
}