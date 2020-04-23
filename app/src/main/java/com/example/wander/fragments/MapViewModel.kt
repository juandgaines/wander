package com.example.wander.fragments

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.wander.GeofencingConstants
import com.example.wander.LandmarkDataObject
import com.example.wander.LandmarkDataObjectResponse
import com.example.wander.network.LocationLinkerWithUser
import com.example.wander.network.Network
import com.example.wander.network.Result
import com.example.wander.network.UserWithLocation
import com.example.wander.preferences.PreferencesManager
import com.example.wander.setDecimals
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MapViewModel(application: Application) : AndroidViewModel(application) {

    private var viewModelJob: Job = Job()
    private var couroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    private var pro = Network.getNetworkProvider()
    private val _location = MutableLiveData<Location>()
    private val _geofenceRequest = MutableLiveData<GeofencingRequest>()
    private val _promoPlaces = MutableLiveData<LandmarkDataObject>()
    private val _errorMessage = MutableLiveData<String>()
    private val _responseLogOut = MutableLiveData<com.example.wander.network.Result<Void>>()
    private val _responseCreateLocation =
        MutableLiveData<com.example.wander.network.Result<LandmarkDataObjectResponse>>()
    private val _responsePromosNearBy =
        MutableLiveData<com.example.wander.network.Result<Array<UserWithLocation>>>()

    val createdLocation=MutableLiveData<Boolean>()

    val revealGeofences =
        MutableLiveData<Boolean>()

    init {
        revealGeofences.value=false
        createdLocation.value=false
    }

    val location: LiveData<Location> get() = _location
    val geofenceRequest: LiveData<GeofencingRequest> get() = _geofenceRequest
    val networkState: LiveData<Network.NetworkState> get() = Network.networkCurrentState
    val responseLogOut: LiveData<com.example.wander.network.Result<Void>> get() = _responseLogOut
    val responseCreateLocation: LiveData<com.example.wander.network.Result<LandmarkDataObjectResponse>> get() = _responseCreateLocation
    val errorMessage: LiveData<String> get() = _errorMessage
    val responsePromosNearBy: LiveData<com.example.wander.network.Result<Array<UserWithLocation>>> get() = _responsePromosNearBy


    fun setCoordinates(location: Location?) {
        location?.let {
            _location.value = location
        }
    }

    fun getPromotionsAround(location: LatLng) {

        val token = PreferencesManager.getPreferenceProvider(getApplication()).token ?: ""
        val userId = PreferencesManager.getPreferenceProvider(getApplication()).idUser


        couroutineScope.launch {
            pro.getPromotionsAround(
                token = "Token ${PreferencesManager.getPreferenceProvider(getApplication()).token ?: ""}",
                lat =  location.latitude.setDecimals(5),
                long = location.longitude.setDecimals(5),
                rad = 1,
                onSuccess = {
                    _responsePromosNearBy.postValue(Result.Success(it))
                },
                onError={
                    _responsePromosNearBy.postValue(Result.Error(it))
                })
        }
    }

    fun addGeofence(geofences: List<LandmarkDataObject>?) {

        if(geofences.isNullOrEmpty().not()) {
            val geofencingRequest = GeofencingRequest.Builder()
                // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
                // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
                // is already inside that geofence.
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)


            geofences?.forEach {

                val geofence = Geofence.Builder()

                    .setRequestId("${it.identificator}-${it.id}")
                    // Set the circular region of this geofence.
                    .setCircularRegion(
                        it.latitude,
                        it.longitude,
                        GeofencingConstants.GEOFENCE_RADIUS_IN_METERS
                    )
                    // Set the expiration duration of the geofence. This geofence gets
                    // automatically removed after this period of time.
                    .setExpirationDuration(GeofencingConstants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                    // Set the transition types of interest. Alerts are only generated for these
                    // transition. We track entry and exit transitions in this sample.
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                    .build()
                geofencingRequest.addGeofence(geofence)

            }
            _geofenceRequest.postValue(geofencingRequest.build())
        }
    }

    fun logout() {

        couroutineScope.launch {
            pro.logoutUser(
                "Token ${PreferencesManager.getPreferenceProvider(getApplication()).token ?: ""}",
                onSuccess = {
                    _responseLogOut.postValue(com.example.wander.network.Result.Success(null))
                },
                onError = {
                    _responseLogOut.postValue(com.example.wander.network.Result.Error(it))
                })
        }

    }

    fun createLocation(location: LandmarkDataObject) {

        couroutineScope.launch {
            pro.createLocation(
                token = "Token ${PreferencesManager.getPreferenceProvider(getApplication()).token ?: ""}",
                location = location,
                onSuccess = {
                    _responseCreateLocation.postValue(com.example.wander.network.Result.Success(it))
                },
                onError = {
                    _responseCreateLocation.postValue(com.example.wander.network.Result.Error(it))
                })
        }
    }

    fun relateLocationWithUser(linker: LocationLinkerWithUser) {

        couroutineScope.launch {
            pro.relateLocationWithUser(
                token = "Token ${PreferencesManager.getPreferenceProvider(getApplication()).token ?: ""}",
                link = linker,
                onSuccess = {
                    //getPromotionsAround(LatLng(0.toDouble(),0.toDouble()))
                },
                onError = {
                    _errorMessage.postValue("Error linking the user with location")
                })
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
