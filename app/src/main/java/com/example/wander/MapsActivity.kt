package com.example.wander

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.wander.databinding.ActivityMapsBinding


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class MapsActivity : AppCompatActivity() {

    private val TAG = MapsActivity::class.java.simpleName

    private lateinit var binding: ActivityMapsBinding
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_maps)
        navController = findNavController(R.id.myNavHostFragment)
    }


    fun showLoading() {
        binding.loadingView.visibility = VISIBLE
    }

    fun hideLoading() {
        binding.loadingView.visibility = GONE
    }


}



