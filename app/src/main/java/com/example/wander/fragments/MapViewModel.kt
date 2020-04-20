package com.example.wander.fragments

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wander.GeofencingConstants
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest

class MapViewModel : ViewModel() {


    private val _location = MutableLiveData<Location>()
    private val _geofenceRequest = MutableLiveData<GeofencingRequest>()

    val location: LiveData<Location> get() = _location
    val geofenceRequest: LiveData<GeofencingRequest> get() = _geofenceRequest


    fun setCoordinates(location: Location?) {
        location?.apply {
            _location.value = location
        }
    }


    fun addGeofence() {
        val currentGeofenceData = GeofencingConstants.LANDMARK_DATA[0]
        val geofence = Geofence.Builder()

            .setRequestId(currentGeofenceData.id)
            // Set the circular region of this geofence.
            .setCircularRegion(
                currentGeofenceData.latLong.latitude,
                currentGeofenceData.latLong.longitude,
                GeofencingConstants.GEOFENCE_RADIUS_IN_METERS
            )
            // Set the expiration duration of the geofence. This geofence gets
            // automatically removed after this period of time.
            .setExpirationDuration(GeofencingConstants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
            // Set the transition types of interest. Alerts are only generated for these
            // transition. We track entry and exit transitions in this sample.
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .build()

        val geofencingRequest = GeofencingRequest.Builder()
            // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
            // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
            // is already inside that geofence.
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)

            // Add the geofences to be monitored by geofencing service.
            .addGeofence(geofence)
            .build()

        _geofenceRequest.value=geofencingRequest
    }

}
