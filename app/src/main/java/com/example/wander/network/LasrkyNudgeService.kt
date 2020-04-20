package com.example.wander.network


import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface LasrkyNudgeService {

    @POST("users/register/")
    suspend fun registerUser(
        @Body user: User
    ): User

}
