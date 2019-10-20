package bell.assignment.simpletwitterclient.managers.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.ActivityCompat
import bell.assignment.simpletwitterclient.activities.MainActivity
import bell.assignment.simpletwitterclient.managers.location.model.LocationRequest
import bell.assignment.simpletwitterclient.managers.location.model.LocationSource
import bell.assignment.simpletwitterclient.managers.premission.PermissionManager
import bell.assignment.simpletwitterclient.managers.premission.PermissionmanagerImpl

class LocationManagerImpl: LocationManager {

    private val REQUEST_PERMISSION_LOCATRION = 1002
    private val REQUEST_SETTINGS = 1003

    private var context: Context? = null
    private var locationManager: android.location.LocationManager? = null
    private var onLocationUpdated: ((Location) -> Unit)? = null
    private var permissionManager: PermissionManager = PermissionmanagerImpl

    private val locationListener = object: LocationListener {
        override fun onLocationChanged(location: Location?) {
            location?.let {
                onLocationUpdated?.invoke(location)
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

        override fun onProviderEnabled(provider: String?) {}

        override fun onProviderDisabled(provider: String?) {
            context?.let {
                isLocationEnabled(it)
            }
        }
    }

    override fun startUpdateLocation(
        context: Context,
        locationRequest: LocationRequest,
        onLocationUpdated: (Location) -> Unit
    ) {
        this.context = context
        this.onLocationUpdated = onLocationUpdated

        if (locationManager != null) {
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
        }

        permissionManager.checkPermissions(
            activity = context as MainActivity,
            permissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            onPermissionResult = { result ->
                if (permissionManager.areAllPermissionGranted(result) && isLocationEnabled(context)) {
                    requestLocationUpdates(locationRequest)
                }
            },
            requestCode = REQUEST_PERMISSION_LOCATRION

        )
    }

    override fun stopUpdateLocation() {
        locationManager?.removeUpdates(locationListener)
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates(locationUpdateRequest: LocationRequest) {

        when (locationUpdateRequest.locationRequestSource) {
            LocationSource.ALL
            -> {
                locationManager?.requestLocationUpdates(
                    LocationSource.GPS_PROVIDER.sourceType,
                    locationUpdateRequest.refreshTimeMilliSec,
                    locationUpdateRequest.refreshDistanceMeter,
                    locationListener
                )
                locationManager?.requestLocationUpdates(
                    LocationSource.NETWORK_PROVIDER.sourceType,
                    locationUpdateRequest.refreshTimeMilliSec,
                    locationUpdateRequest.refreshDistanceMeter,
                    locationListener
                )
                locationManager?.requestLocationUpdates(
                    LocationSource.PASSIVE_PROVIDER.sourceType,
                    locationUpdateRequest.refreshTimeMilliSec,
                    locationUpdateRequest.refreshDistanceMeter,
                    locationListener
                )
            }
            LocationSource.GPS_PROVIDER
            -> {
                locationManager?.requestLocationUpdates(
                    LocationSource.GPS_PROVIDER.sourceType,
                    locationUpdateRequest.refreshTimeMilliSec,
                    locationUpdateRequest.refreshDistanceMeter,
                    locationListener
                )

            }
            LocationSource.NETWORK_PROVIDER
            -> {
                locationManager?.requestLocationUpdates(
                    LocationSource.NETWORK_PROVIDER.sourceType,
                    locationUpdateRequest.refreshTimeMilliSec,
                    locationUpdateRequest.refreshDistanceMeter,
                    locationListener
                )

            }
            LocationSource.PASSIVE_PROVIDER
            -> {
                locationManager?.requestLocationUpdates(
                    LocationSource.PASSIVE_PROVIDER.sourceType,
                    locationUpdateRequest.refreshTimeMilliSec,
                    locationUpdateRequest.refreshDistanceMeter,
                    locationListener
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean? {
        if (requestCode == REQUEST_SETTINGS) {
            requestLocationUpdates(LocationRequest())
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        results: IntArray
    ): Boolean? {
        return permissionManager.onRequestPermissionsResult(requestCode, permissions, results)
    }

    private fun isLocationEnabled(context: Context): Boolean {
        val isLocationEnabled = locationManager?.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
        if (isLocationEnabled == false) {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            ActivityCompat.startActivityForResult(
                context as MainActivity,
                intent,
                REQUEST_SETTINGS,
                null
            )
            return false
        }
        return true
    }
}