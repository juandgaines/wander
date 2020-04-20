package com.example.wander.network


import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface LasrkyNudgeService {

    @POST("2.0/?method=geo.gettopartists&format=json")
    suspend fun registerUser(
        @Body user: User
    ): DataResponse

}
