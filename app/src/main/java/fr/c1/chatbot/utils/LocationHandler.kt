package fr.c1.chatbot.utils

import android.Manifest
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import fr.c1.chatbot.MainActivity
import fr.c1.chatbot.R
object LocationHandler {

    private var configurationChange = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var currentLocation: Location? = null
    fun initLocation(cxt : Context) {
        Log.i(ContentValues.TAG, "Init Location")
        createLocationRequest()
        createLocationCallback()
        Log.i(ContentValues.TAG, "Check Permissions")
        if (ActivityCompat.checkSelfPermission(cxt, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED){
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(cxt)
            //last location
            fusedLocationClient.lastLocation.addOnSuccessListener {
                location: Location? ->
                // Got last known location. In some rare situations this can be null.
                if(location != null){
                    currentLocation = location
                    Log.d(ContentValues.TAG,"Latitude: "+currentLocation?.getLatitude() +
                            ", Longitude: "+ currentLocation?.getLongitude())
                }
            }
        }else{
            Log.i(ContentValues.TAG, "Permissions not available")
        }

    }
    fun startLocationUpdates(cxt: Context) {
        if (ActivityCompat.checkSelfPermission(cxt, Manifest.permission.ACCESS_FINE_LOCATION)
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
    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun createLocationRequest() {
        Log.i(ContentValues.TAG, "Building Location Request")
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setMinUpdateIntervalMillis(5000)
            .build()
    }

    private fun createLocationCallback() {
        Log.i(ContentValues.TAG, "Building Location Callback")
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
                        "Latitude: " + currentLocation?.getLatitude() + ", Longitude: " + currentLocation?.getLongitude()
                    )
                } else {
                    Log.d(ContentValues.TAG, "Location information isn't available.")
                }
            }
        }
    }
}