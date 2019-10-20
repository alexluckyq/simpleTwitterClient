package bell.assignment.simpletwitterclient.managers.location.model

data class LocationRequest (val locationRequestSource: LocationSource = LocationSource.ALL,
                            val refreshTimeMilliSec: Long = 60000L,
                            val refreshDistanceMeter: Float = 5000F)
