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

/**
 * documents
Name	Type	Description
title	String	동영상 제목
url	String	동영상 링크
datetime	Datetime	동영상 등록일, ISO 8601
[YYYY]-[MM]-[DD]T[hh]:[mm]:[ss].000+[tz]
play_time	Integer	동영상 재생시간, 초 단위
thumbnail	String	동영상 미리보기 URL
author	String	동영상 업로더
 */
data class ImageDocuments(
    val title: String?,
    val url: String?,
    val datetime: Date?,      // Datetime 문서 글 작성시간, ISO 8601
    // [YYYY]-[MM]-[DD]T[hh]:[mm]:[ss].000+[tz]
    val play_time: Int,
    val thumbnail: String?,
    val author: String?
)

/**
 * documents
Name	Type	Description
collection	String	컬렉션
thumbnail_url	String	미리보기 이미지 URL
image_url	String	이미지 URL
width	Integer	이미지의 가로 길이
height	Integer	이미지의 세로 길이
display_sitename	String	출처
doc_url	String	문서 URL
datetime	Datetime	문서 작성시간, ISO 8601
[YYYY]-[MM]-[DD]T[hh]:[mm]:[ss].000+[tz]
 */
data class VClipDocuments(
    val collection: String?,
    val thumbnail_url: String?,
    val image_url: String?,
    val width: String?,
    val height: String?,
    val display_sitename: String?,
    val doc_url: String?,
    val datetime: Date?
)



