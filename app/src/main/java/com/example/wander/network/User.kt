package com.example.wander.network

import com.squareup.moshi.Json

data class User(
    val id:Long?=null,
    val username: String,
    @Json(name = "first_name")
    val firstName: String,
    @Json(name = "last_name")
    val lastName: String,
    val email: String,
    val password: String?=null,
    val groups:List<String>? =null
)

data class DataResponse(val code: Long)
