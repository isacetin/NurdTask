package com.example.nurd_task.services

import com.example.nurd_task.models.DevicesModel
import retrofit2.Call
import retrofit2.http.GET

interface DevicesAPI {

    @GET("test_android/items.test")
    fun getDadata(): Call<DevicesModel>
}