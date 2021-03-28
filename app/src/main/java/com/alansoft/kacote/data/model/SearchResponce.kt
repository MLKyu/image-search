package com.alansoft.kacote.data.model

import java.util.*

/**
 * Created by LEE MIN KYU on 2021/03/25
 * Copyright © 2021 Dreamus Company. All rights reserved.
 */
data class SearchResponse<T>(
    val meta: Meta,
    val documents: List<T>
)

data class Meta(
    val total_count: Int,
    val pageable_count: Int,
    val is_end: Boolean
)

data class VClipDocuments(
    val title: String?,
    val url: String?,
    val play_time: Int,
    val thumbnail: String?,
    val author: String?
) : Documents()

data class ImageDocuments(
    val collection: String?,
    val thumbnail_url: String?,
    val image_url: String?,
    val width: String?,
    val height: String?,
    val display_sitename: String?,
    val doc_url: String?
) : Documents()

open class Documents {
    val datetime: Date? = null
}

data class SearchMerge(
    val imageMeta: Meta?,
    val vClipMeta: Meta?,
    val documents: List<Documents>
)