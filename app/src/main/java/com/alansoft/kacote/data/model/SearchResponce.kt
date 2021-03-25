package com.alansoft.kacote.data.model

/**
 * Created by LEE MIN KYU on 2021/03/25
 * Copyright © 2021 Dreamus Company. All rights reserved.
 */
data class SearchResponse(
    val meta: Meta,
    val documents: Documents
)

data class Meta(
    val total_count: Int,
    val pageable_count: Int,
    val is_end: Boolean
)

data class Documents(
    val title: String?,
    val contents: String?,
    val url: String?,
    val datetime: String?    // Datetime 문서 글 작성시간, ISO 8601
                            // [YYYY]-[MM]-[DD]T[hh]:[mm]:[ss].000+[tz]
)
