package bell.assignment.simpletwitterclient.managers.location

import android.content.Context
import android.content.Intent
import android.location.Location
import bell.assignment.simpletwitterclient.managers.location.model.LocationRequest

interface LocationManager {

    /**
     * start location change tracker
     *
     */
    fun startUpdateLocation(context: Context, locationRequest: LocationRequest, onLocationChange: (Location) -> Unit)

    /**
     * stop location change tracker
     *
     */
    fun stopUpdateLocation()


    /**
     * handle onActivityResult for location settings resolver
     *
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean?


    /**
     * handle permission request result
     *
     */
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, results: IntArray): Boolean?

}