package com.example.wander

import com.google.android.gms.maps.model.LatLng
import java.util.concurrent.TimeUnit

data class LandmarkDataObject(
    val id: String,
    val bank: String,
    val promo: String,
    val latLong: LatLng
)

internal object GeofencingConstants {

    val GEOFENCE_EXPIRATION_IN_MILLISECONDS: Long = TimeUnit.HOURS.toMillis(1)

    val LANDMARK_DATA = arrayOf(
        LandmarkDataObject(
            "promo_fence_1",
            "Scotiabank",
            "Hey! 20% Percent discount on the Starbucks Coffee",
            LatLng(4.710216, -74.062312)
        ),

        LandmarkDataObject(
            "promo_fence_2",
            "Bank of America",
            "Hey! This is a temporal coupon for your next buy in Walmart that is near by you",
            LatLng(4.710361, -74.059439)
        ),

        LandmarkDataObject(
            "promo_fence_3",
            "Capital One",
            "Hey! This is a temporal coupon for your next buy in Walmart that is near by you",
            LatLng(4.708163, -74.059336)
        ),

        LandmarkDataObject(
            "promo_fence_4",
            "U.S. Bancorp",
            "Hey! This is a temporal coupon for your next buy in Walmart that is near by you",
            LatLng(4.707489, -74.062161)
        ),
        LandmarkDataObject(
            "promo_fence_5",
            "U.S. Bancorp",
            "Hey! This is a temporal coupon for your next buy in Walmart that is near by you",
            LatLng(4.711357, -74.064588)
        )
    )

    val NUM_LANDMARKS = LANDMARK_DATA.size
    const val GEOFENCE_RADIUS_IN_METERS = 50f
    const val EXTRA_GEOFENCE_INDEX = "GEOFENCE_INDEX"
}