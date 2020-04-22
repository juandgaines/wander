package com.example.wander

import com.google.android.gms.maps.model.LatLng
import com.squareup.moshi.Json
import java.util.concurrent.TimeUnit

data class LandmarkDataObject(

    @Json(name = "id")
    val id: Long?=null,
    val identificator: String,
    @Json(name = "entity")
    val bank: String,
    @Json(name = "message")
    val promo: String,
    val latitude:Double,
    val longitude:Double
)

data class LandmarkDataObjectResponse(

    @Json(name = "id")
    val id: Long?=null,
    val identificator: String,
    @Json(name = "entity")
    val bank: String,
    @Json(name = "message")
    val promo: String,
    val latitude:String,
    val longitude:String
)

data class LocationForPromotion(
    val id: String,
    val bank: String,
    val promo: String,
    val latLong: LatLng
)

internal object GeofencingConstants {

    val GEOFENCE_EXPIRATION_IN_MILLISECONDS: Long = TimeUnit.HOURS.toMillis(1)

    val LANDMARK_DATA = arrayOf(
        LandmarkDataObject(
            identificator = "promo_fence_1",
            bank =
            "Scotiabank",
            promo =
            "Hey! 20% Percent discount on the Starbucks Coffee",
            latitude =  4.710216, longitude =  -74.062312
        ),

        LandmarkDataObject(
            identificator = "promo_fence_2",
            bank = "Bank of America",
            promo = "Hey! This is a temporal coupon for your next buy in Walmart that is near by you",
            latitude = 4.710361,longitude =  -74.059439
        ),

        LandmarkDataObject(
            identificator = "promo_fence_3",
            bank = "Capital One",
            promo = "Hey! This is a temporal coupon for your next buy in Walmart that is near by you",
            latitude =  4.708163, longitude =  -74.059336
        ),

        LandmarkDataObject(
            identificator =  "promo_fence_4",
            bank = "U.S. Bancorp",
            promo = "Hey! This is a temporal coupon for your next buy in Walmart that is near by you",
            latitude= 4.707489,longitude =  -74.062161
        ),
        LandmarkDataObject(
            identificator = "promo_fence_5",
            bank = "U.S. Bancorp",
            promo = "Hey! This is a temporal coupon for your next buy in Walmart that is near by you",
            latitude = 4.711357, longitude =  -74.064588
        )
    )

    val NUM_LANDMARKS = LANDMARK_DATA.size
    const val GEOFENCE_RADIUS_IN_METERS = 50f
    const val EXTRA_GEOFENCE_INDEX = "GEOFENCE_INDEX"
}