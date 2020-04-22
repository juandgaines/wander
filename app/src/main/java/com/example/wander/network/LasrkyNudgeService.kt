package com.example.wander.network


import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface LasrkyNudgeService {

    @POST("users/register/")
    suspend fun registerUser(
        @Body user: User
    ): User

    @POST("rest-auth/login/")
    suspend fun loginUser(
        @Body login: Login
    ): Token


    @POST("rest-auth/logout/")
    suspend fun logoutUser(
        @Header("Authorization") contentRange: String
    ): Token?


}
