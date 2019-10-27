package com.androdocs.mylocation

import com.androdocs.mylocation.Notification
import retrofit2.http.Body
import retrofit2.http.POST

interface RestClient {
    @POST("incident/1")
    fun post1(@Body notification: Notification): retrofit2.Call<NotificationResponse>


}