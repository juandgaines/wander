package com.example.wander

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.wander.fragments.MapFragment

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class MapsActivity : AppCompatActivity() {

    private val TAG = MapsActivity::class.java.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        val navHost = supportFragmentManager.findFragmentById(R.id.mapFragment)
        navHost?.let { navFragment ->
            navFragment.childFragmentManager.primaryNavigationFragment?.let { fragment ->
                (navFragment as MapFragment).enableMyLocation()
            }
        }

    }
}

