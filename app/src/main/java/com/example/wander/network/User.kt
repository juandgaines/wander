package com.example.wander.network

import com.example.wander.LandmarkDataObjectResponse
import com.squareup.moshi.Json

data class User(
    val id: Long? = null,
    val username: String,
    @Json(name = "first_name")
    val firstName: String,
    @Json(name = "last_name")
    val lastName: String,
    val email: String,
    val password: String? = null,
    val groups: List<String>? = null
)

data class DataResponse(val code: Long)

data class Login(
    val username: String,
    val password: String
)

data class Token(
    val token: String?,
    val user_id:Long?,
    val email:String?,
    val detail: String?
)

data class LocationLinkerWithUser(val location:Long,
    val user:Long, val id:Long?=null)


data class UserWithLocation(val id:Long?=null,
val location: LandmarkDataObjectResponse,
val user:User)
