package com.alansoft.kacote.utils

import com.alansoft.kacote.R

/**
 * Created by LEE MIN KYU on 2021/03/28
 * Copyright © 2021 Dreamus Company. All rights reserved.
 */
enum class SearchSortType {
    ACCURACY,
    RECENCY
}

const val FIRST_PAGE = 1
const val PAGE_SIZE = 20
const val REQUEST_KEY = "requestKey"
const val BUNDLE_QUERY = "query"

enum class TabType(val resourceId: Int) {
    SEARCH_RESULT(R.string.tab_text_1),
    MY(R.string.tab_text_2)
}

enum class ViewType {
    IMAGE,
    VCLIP
}