package com.example.wander.network

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.wander.GeofencingConstants.LANDMARK_DATA
import com.example.wander.LandmarkDataObject
import com.example.wander.preferences.PreferencesManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class Network {

    private var logger: HttpLoggingInterceptor = HttpLoggingInterceptor()
    private var client: OkHttpClient
    private var moshi: Moshi
    private var retrofit: Retrofit

    enum class NetworkState {
        SUCCESS,
        LOADING,
        ERROR
    }


    init {
        logger.level = HttpLoggingInterceptor.Level.BODY
        client = OkHttpClient.Builder().addInterceptor(logger).build()
        moshi = Moshi.Builder().add(KotlinJsonAdapterFactory())
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl("http://3.16.11.253:3000/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    val lasrkyNudgeService = retrofit.create(LasrkyNudgeService::class.java)


    suspend fun registerUser(
        user: User,
        onSuccess: (User) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {

        _networkCurrentState.postValue(NetworkState.LOADING)
        var responseUser: User?
        try {
            responseUser = lasrkyNudgeService.registerUser(user)
            onSuccess(responseUser)
            _networkCurrentState.postValue(NetworkState.SUCCESS)

        } catch (e: Throwable) {
            onError(e.message.toString())
            _networkCurrentState.postValue( NetworkState.ERROR)

        }
    }


    suspend fun loginUser(
        login: Login,
        onSuccess: (Token) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {

        _networkCurrentState.postValue(NetworkState.LOADING)
        var token: Token?
        try {
            token = lasrkyNudgeService.loginUser(login)
            onSuccess(token)
            _networkCurrentState.postValue(NetworkState.SUCCESS)

        } catch (e: Throwable) {
            onError(e.message.toString())
            _networkCurrentState.postValue( NetworkState.ERROR)
        }
    }

    suspend fun logoutUser(
        token:String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        _networkCurrentState.postValue(NetworkState.LOADING)
        try {
            lasrkyNudgeService.logoutUser(token)
            onSuccess()
            _networkCurrentState.postValue(NetworkState.SUCCESS)

        } catch (e: Throwable) {
            onError(e.message.toString())
            _networkCurrentState.postValue( NetworkState.ERROR)
        }
    }



    fun getPromotionsAround(
        onSuccess: (Array<LandmarkDataObject>) -> Unit,
        onError: (String) -> Unit
    ) {

        _networkCurrentState.value = NetworkState.LOADING
        Handler().postDelayed({
            onSuccess(LANDMARK_DATA)
            _networkCurrentState.value = NetworkState.SUCCESS

        }, 5000)

    }


    companion object {
        @Volatile
        private var INSTANCE: Network? = null
        private lateinit var _networkCurrentState: MutableLiveData<NetworkState>
        val networkCurrentState: LiveData<NetworkState> get() = _networkCurrentState

        fun getNetworkProvider(): Network {

            return INSTANCE ?: synchronized(this) {
                _networkCurrentState = MutableLiveData<NetworkState>()
                Network()
            }
        }

    }

}

