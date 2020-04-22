package com.example.wander.network


import com.example.wander.LandmarkDataObject
import com.example.wander.LandmarkDataObjectResponse
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



    @POST("locations/")
    suspend fun createLocation(
        @Header("Authorization") contentRange: String,
        @Body location:LandmarkDataObject
    ): LandmarkDataObjectResponse?


    @POST("user-locations/create/")
    suspend fun relateLocationWithUser(
        @Header("Authorization") contentRange: String, @Body link:LocationLinkerWithUser
    ): LocationLinkerWithUser?

}
