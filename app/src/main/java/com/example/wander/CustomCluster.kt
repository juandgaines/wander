package com.example.wander

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class CustomCluster ( lat:Double, lng:Double,  title:String="",snippet:String=""): ClusterItem {

    private var position=LatLng(lat,lng)
    private var title=title
    private var snippet=snippet

    init {
        position
    }


    override fun getSnippet(): String {
        return snippet
    }

    override fun getTitle(): String {
        return title
    }

    override fun getPosition(): LatLng {
        return position
    }
}