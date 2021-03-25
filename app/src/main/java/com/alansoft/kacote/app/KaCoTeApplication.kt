package com.alansoft.kacote.app

import android.app.Application
import com.alansoft.kacote.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Created by LEE MIN KYU on 2021/03/24
 * Copyright © 2021 Dreamus Company. All rights reserved.
 */
class KaCoTeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}