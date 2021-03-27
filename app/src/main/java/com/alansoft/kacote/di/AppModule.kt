package com.alansoft.kacote.di

import com.alansoft.kacote.data.KakaoSearchDataSource
import com.alansoft.kacote.data.MyDataSource
import com.alansoft.kacote.data.api.KakaoSearchApi
import com.alansoft.kacote.repository.KakaoSearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by LEE MIN KYU on 2021/03/24
 * Copyright © 2021 Dreamus Company. All rights reserved.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideKakaoSearchDataSource(kakaoSearchApi: KakaoSearchApi) =
        KakaoSearchDataSource(kakaoSearchApi)

    @Singleton
    @Provides
    fun provideMyDataSource() =
        MyDataSource()

    @Singleton
    @Provides
    fun provideKakaoSearchRepository(
        myDataSource: MyDataSource,
        kakaoSearchDataSource: KakaoSearchDataSource
    ) =
        KakaoSearchRepository(myDataSource, kakaoSearchDataSource)
}