package com.example.wander.fragments

import android.app.Activity.RESULT_OK
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.wander.*
import com.example.wander.Utils.Companion.BACKGROUND_LOCATION_PERMISSION_INDEX
import com.example.wander.Utils.Companion.LOCATION_PERMISSION_INDEX
import com.example.wander.Utils.Companion.REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE
import com.example.wander.Utils.Companion.REQUEST_TURN_DEVICE_LOCATION_ON
import com.example.wander.Utils.Companion.checkDeviceLocationSettingsAndStartGeofence
import com.example.wander.Utils.Companion.foregroundAndBackgroundLocationPermissionApproved
import com.example.wander.Utils.Companion.isPermissionGranted
import com.example.wander.Utils.Companion.requestForegroundAndBackgroundLocationPermissions
import com.example.wander.databinding.MapFragmentBinding
import com.example.wander.network.Network
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.heatmaps.HeatmapTileProvider

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class MapFragment : Fragment(), OnMapReadyCallback {

    private val TAG = MapFragment::class.java.simpleName
    private lateinit var viewModel: MapViewModel
    private lateinit var binding: MapFragmentBinding
    private lateinit var map: GoogleMap
    private lateinit var overlay: TileOverlay
    private lateinit var clusterManager: ClusterManager<CustomCluster>

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: LatLng? = null

    private lateinit var mapsActivity: MapsActivity


    private val runningQOrLater =
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q


    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(activity, GeofenceBroadcastReceiver::class.java)
        intent.action = ACTION_GEOFENCE_EVENT
        // Use FLAG_UPDATE_CURRENT so that you get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        PendingIntent.getBroadcast(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    lateinit var geofencingClient: GeofencingClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.map_fragment, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.map.onCreate(savedInstanceState)
        binding.map.onResume()
        binding.map.getMapAsync(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MapViewModel::class.java)
        mapsActivity = requireActivity() as MapsActivity
        activity?.let {
            geofencingClient = LocationServices.getGeofencingClient(it)
            createChannel(it.applicationContext)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        viewModel.location.observe(viewLifecycleOwner, Observer { loc ->
            currentLocation = LatLng(loc.latitude, loc.longitude)
            focusMapOnCoordinates()
        })

        viewModel.geofenceRequest.observe(viewLifecycleOwner, Observer { geofencingRequest ->
            geofencingRequest?.let { gr ->
                geofencingClient.addGeofences(gr, geofencePendingIntent)?.run {
                    addOnSuccessListener {
                        Log.d("Add Geofence", gr.geofences.first().requestId)
                    }
                }
            }
        })

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            when (it) {
                Network.NetworkState.SUCCESS,
                Network.NetworkState.ERROR -> {
                    binding.loadingView.visibility=GONE
                }
                else -> {
                    binding.loadingView.visibility= VISIBLE
                }
            }
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onStart() {
        super.onStart()
        checkPermissionsAndStartGeofencing()
    }

    private fun checkPermissionsAndStartGeofencing() {
        if (foregroundAndBackgroundLocationPermissionApproved(requireContext(), runningQOrLater)) {
            checkLocationSolver()
        } else {
            requestForegroundAndBackgroundLocationPermissions(
                requireContext(),
                runningQOrLater,
                this
            )
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
        setUpCluster()
        setMapStyle(map)
        enableMyLocation()
    }

    private fun focusMapOnCoordinates() {
        val zoomLevel = 18f
        Handler().postDelayed({
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, zoomLevel))
            map.isMyLocationEnabled = true
            binding.map.onResume()
        }, 300)
    }

    fun enableMyLocation() {
        if (isPermissionGranted(requireContext())) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    // Got last known location. In some rare situations this can be null.
                    viewModel.setCoordinates(location)
                }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.d(TAG, "onRequestPermissionResult")

        if (grantResults.isEmpty() || grantResults[LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_DENIED || (requestCode == REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE && grantResults[BACKGROUND_LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_DENIED)) {
            // Permission denied.
            Log.d(TAG, "Permission denied")
            mapsActivity.navController.navigateUp()
        } else {
            checkLocationSolver()
        }
    }

    private fun checkLocationSolver() {
        checkDeviceLocationSettingsAndStartGeofence(true, this, onSuccess = {
            viewModel.getPromotionsAround(currentLocation)
        }, onError = {
            startIntentSenderForResult(
                it.resolution.intentSender,
                REQUEST_TURN_DEVICE_LOCATION_ON,
                null,
                0,
                0,
                0,
                null
            )
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_TURN_DEVICE_LOCATION_ON -> {
                    enableMyLocation()
                }
                else -> Unit
            }
        }


    }

    private fun setMapStyle(map: GoogleMap) {
        try {

            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireActivity().applicationContext,
                    R.raw.map_style
                )
            )

            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
    }

    private fun setUpCluster() {

        clusterManager = ClusterManager<CustomCluster>(activity, map)
        map.setOnCameraIdleListener(clusterManager)
        map.setOnMarkerClickListener(clusterManager)
    }

    private fun drawGeoFencesArea() {
        GeofencingConstants.LANDMARK_DATA.forEach {
            val geoFenceLatLong = LatLng(it.latLong.latitude, it.latLong.longitude)
            val circleOptions = CircleOptions()
                .center(
                    geoFenceLatLong
                )
                .radius(GeofencingConstants.GEOFENCE_RADIUS_IN_METERS.toDouble())
                .fillColor(0x4000ff00)
                .strokeColor(Color.GREEN)
                .strokeWidth(2f)
            addItems(it)
            map.addCircle(circleOptions)
        }

        val provider = HeatmapTileProvider.Builder()
            .data(GeofencingConstants.LANDMARK_DATA.map {
                it.latLong
            })
            .build()
        overlay = map.addTileOverlay(TileOverlayOptions().tileProvider(provider))

    }

    private fun addItems(landmark: LandmarkDataObject) {
        val item = CustomCluster(landmark.latLong.latitude, landmark.latLong.longitude)
        clusterManager.addItem(item)
        //map.addMarker(MarkerOptions().position(item.position))
    }

    override fun onDestroy() {
        super.onDestroy()

        geofencingClient.removeGeofences(geofencePendingIntent)?.run {
            // Regardless of success/failure of the removal, add the new geofence
            addOnCompleteListener {
                // Add the new geofence request with the new geofence
                geofencingClient.addGeofences(
                    viewModel.geofenceRequest.value,
                    geofencePendingIntent
                )?.run {
                    addOnSuccessListener {
                        // Geofences added.
                        Toast.makeText(
                            requireContext(), R.string.geofences_added,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        Log.e(
                            "Add Geofence",
                            viewModel.geofenceRequest.value?.geofences?.first()?.requestId ?: ""
                        )
                        // Tell the viewmodel that we've reached the end of the game and
                        // activated the last "geofence" --- by removing the Geofence.
                    }
                    addOnFailureListener {
                        // Failed to add geofences.
                        Toast.makeText(
                            mapsActivity, R.string.geofences_not_added,
                            Toast.LENGTH_SHORT
                        ).show()
                        if ((it.message != null)) {
                            Log.w(TAG, it.message)
                        }
                    }
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.map_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.draw_geofences -> {
                drawGeoFencesArea()
                true
            }
            R.id.clear_map -> {
                map.clear()
                true
            }
            else -> NavigationUI.onNavDestinationSelected(
                item!!,
                requireActivity().findNavController(R.id.myNavHostFragment)
            )
                    || super.onOptionsItemSelected(item)
        }

    companion object {
        internal const val ACTION_GEOFENCE_EVENT =
            "MapsActivity.wander.action.ACTION_GEOFENCE_EVENT"
    }

}