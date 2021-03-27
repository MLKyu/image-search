package com.alansoft.kacote.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alansoft.kacote.data.model.Documents
import com.alansoft.kacote.data.utils.Resource
import javax.inject.Inject

/**
 * Created by LEE MIN KYU on 2021/03/27
 * Copyright © 2021 Dreamus Company. All rights reserved.
 */
class MyDataSource @Inject constructor() {

    private val _resource = MutableLiveData<Resource<List<Documents>>>(
        Resource.success(ArrayList())
    )

    val resource: LiveData<Resource<List<Documents>>> = _resource

    fun insertDocument(data: Documents) {
        val temp = resource.value
        temp?.data?.let {
            val tempList = ArrayList<Documents>()
            tempList.addAll(it)
            tempList.add(data)
            _resource.value = Resource.success(tempList)
        }
    }

    fun deleteDocument(data: Documents) {
        val temp = resource.value
        temp?.data?.let {
            val tempList = ArrayList<Documents>()
            tempList.addAll(it)
            tempList.remove(data)
            _resource.value = Resource.success(tempList)
        }
    }
}