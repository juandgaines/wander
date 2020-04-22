package com.example.wander.fragments

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.wander.GeofencingConstants
import com.example.wander.LandmarkDataObject
import com.example.wander.network.Network
import com.example.wander.preferences.PreferencesManager
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


    val location: LiveData<Location> get() = _location
    val geofenceRequest: LiveData<GeofencingRequest> get() = _geofenceRequest

    val networkState: LiveData<Network.NetworkState> get() = Network.networkCurrentState
    val responseLogOut: LiveData<com.example.wander.network.Result<Void>> get() = _responseLogOut
    val errorMessage: LiveData<String> get() = _errorMessage


    fun setCoordinates(location: Location?) {
        location?.let {
            _location.value = location
        }
    }

    fun getPromotionsAround(location: LatLng?) {

        pro.getPromotionsAround({
            addGeofence(it)
        }, {
            _errorMessage.value = it
        })


    }


    fun addGeofence(geofences: Array<LandmarkDataObject>) {

        val geofencingRequest = GeofencingRequest.Builder()
            // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
            // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
            // is already inside that geofence.
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)


        geofences.forEach {

            val geofence = Geofence.Builder()

                .setRequestId(it.id)
                // Set the circular region of this geofence.
                .setCircularRegion(
                    it.latLong.latitude,
                    it.latLong.longitude,
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
        _geofenceRequest.value = geofencingRequest.build()
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

}
