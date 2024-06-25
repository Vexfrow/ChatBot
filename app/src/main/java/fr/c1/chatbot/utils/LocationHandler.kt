package fr.c1.chatbot.utils

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

/**
 * Location handler
 *
 * @constructor Create empty Location handler
 */
object LocationHandler {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    var currentLocation: Location? = null
        private set

    /**
     * Init location
     *
     * @param ctx
     */
    fun initLocation(ctx: Context) {
        Log.d(ContentValues.TAG, "Init Location")
        createLocationRequest()
        createLocationCallback()
        Log.d(ContentValues.TAG, "Check Permissions")
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx)
            //last location
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    currentLocation = location
                    Log.d(
                        ContentValues.TAG, "Longitude: " + currentLocation?.longitude +
                                ", Latitude: " + currentLocation?.latitude
                    )
                }
            }
        } else {
            Log.i(ContentValues.TAG, "Permissions not available")
        }
    }

    /**
     * Start location updates
     *
     * @param ctx
     */
    fun startLocationUpdates(ctx: Context) {
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            Log.i(ContentValues.TAG, "PERMISSIONS GRANTED")
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    /**
     * Stop location updates
     */
    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    /**
     * Create location request
     *
     */
    private fun createLocationRequest() {
        Log.d(ContentValues.TAG, "Building Location Request")
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setMinUpdateIntervalMillis(5000)
            .build()
    }

    /**
     * Create location callback
     *
     */
    private fun createLocationCallback() {
        Log.d(ContentValues.TAG, "Building Location Callback")
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                if (locationResult.lastLocation != null) {
                    // Normally, you want to save a new location to a database. We are simplifying
                    // things a bit and just saving it as a local variable, as we only need it again
                    // if a Notification is created (when user navigates away from app).
                    currentLocation = locationResult.lastLocation
                    Log.d(
                        ContentValues.TAG,
                        "Latitude: " + currentLocation?.latitude + ", Longitude: " + currentLocation?.longitude
                    )
                    stopLocationUpdates()
                } else {
                    Log.d(ContentValues.TAG, "Location information isn't available.")
                }
            }
        }
    }
}