package com.alansoft.kacote.ui.search

import android.view.KeyEvent
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.pressKey
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.alansoft.kacote.MainActivity
import com.alansoft.kacote.R
import com.alansoft.kacote.data.model.Documents
import com.alansoft.kacote.data.utils.Resource
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by LEE MIN KYU on 2021/03/28
 * Copyright © 2021 Dreamus Company. All rights reserved.
 */
@RunWith(AndroidJUnit4::class)
class SearchFragmentTest {

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> =
        ActivityTestRule(MainActivity::class.java)


    private val results = MutableLiveData<Resource<List<Documents>>>()
    private val loadMoreStatus = MutableLiveData<SearchViewModel.LoadMoreState>()

    @Before
    fun init() {
    }

    @Test
    fun search() {
        activityRule.runOnUiThread {
            onView(withId(R.id.progress_bar)).check(matches(CoreMatchers.not(isDisplayed())))
            onView(withId(R.id.input_et)).perform(
                typeText("foo"),
                pressKey(KeyEvent.KEYCODE_ENTER)
            )
            results.postValue(Resource.loading(null))
            onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun error() {
        activityRule.runOnUiThread {
            results.postValue(Resource.error("failed to load", null))
            onView(withId(R.id.error_msg)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun loadMoreProgress() {
        activityRule.runOnUiThread {
            loadMoreStatus.postValue(SearchViewModel.LoadMoreState(true, null))
            onView(withId(R.id.load_more_bar)).check(matches(isDisplayed()))
            loadMoreStatus.postValue(SearchViewModel.LoadMoreState(false, null))
            onView(withId(R.id.load_more_bar)).check(matches(CoreMatchers.not(isDisplayed())))
        }
    }

    @Test
    fun loadMoreProgressError() {
        activityRule.runOnUiThread {
            loadMoreStatus.postValue(SearchViewModel.LoadMoreState(true, "QQ"))
            onView(withText("QQ")).check(
                matches(
                    withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
                )
            )
        }
    }
}