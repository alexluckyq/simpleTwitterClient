package bell.assignment.simpletwitterclient.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.text.format.DateUtils
import androidx.core.content.ContextCompat
import bell.assignment.simpletwitterclient.data.TweetData
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.twitter.sdk.android.core.models.Coordinates
import com.twitter.sdk.android.core.models.Tweet
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

// region Stirng

private val DATE_TIME: SimpleDateFormat =
    SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH)

fun String.formatTime(): CharSequence? {
    val createdAt = this.toLongValue()
    if (createdAt != -1L) {
        return DateUtils.getRelativeTimeSpanString(createdAt)
    }
    return null
}

fun String.toLongValue(): Long {
    if (this == null) return -1L
    return try {
        DATE_TIME.parse(this).time
    } catch (e: ParseException) {
        -1
    }
}

//endregion Stirng

// region Location

fun Location.getLatLng(): LatLng {
    return LatLng(this.latitude, this.longitude) ?: LatLng(45.4943571, -73.5802513)
}

//endregion Location

fun isLocationPermissionGranted(context: Context): Boolean =
    ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

// region Extensions for Tweet

fun Tweet.getLatLngFromTweet(): LatLng? {
    return (this.coordinates?.let { it -> LatLng(it.latitude, it.longitude) }
        ?: run {
            this.place?.boundingBox?.coordinates?.let { it2 ->
                if (it2.isNotEmpty() && it2[0].isNotEmpty()) LatLng(
                    it2[0][0][Coordinates.INDEX_LATITUDE],
                    it2[0][0][Coordinates.INDEX_LONGITUDE]
                )
                else null
            } ?: run { null }
        })
}

fun Tweet.getTweetMarkerString(): String = TweetData(
    profileImageUrl = this.user.profileImageUrlHttps,
    tweetId = this.id, tweetText = this.text, timeStampString = this.createdAt
).mapMarkerSnippetString

//endregion Extensions for Tweet

// region map maker

fun Marker.getPinSnippetList(): List<String> = this.snippet.split("|")

//endregion map maker
