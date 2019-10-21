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

fun Tweet.initizeTweetDataFromTweet(): TweetData = TweetData(
    tweetId = this.id,
    profileImageUrl = this.user.profileImageUrl,
    timeStampString = this.createdAt,
    tweetText = this.text,
    reTweetCount = this.retweetCount,
    tweetFavoriteCount = this.favoriteCount,
    tweetFavorited = this.favorited,
    retweeted = this.retweeted,
    userName = this.user.name,
    userScreenName = this.user.screenName,
    tweetPhotoUrl = this.getImageUrl(),
    tweetVideoCoverUrl = this.getVideoCoverUrl(),
    tweetVideoUrl = this.getVideoUrlType()
)

fun Tweet.getVideoUrlType(): Pair<String, String> {
    return try {
        if (hasSingleVideo() || hasMultipleMedia()) {
            val variant = extendedEntities.media[0].videoInfo.variants
            Pair(variant[0].url, variant[0].contentType)
        } else
            Pair("", "")
    } catch (e: Exception) {
        Pair("", "")
    }
}

fun Tweet.getImageUrl(): String {
    return try {
        if (hasSingleImage() || hasMultipleMedia())
            entities.media[0]?.mediaUrl ?: ""
        else
            ""
    } catch (e: Exception) {
        ""
    }
}

fun Tweet.getVideoCoverUrl(): String {
    return try {
        if (hasSingleVideo() || hasMultipleMedia())
            entities.media[0]?.mediaUrlHttps ?: (entities.media[0]?.mediaUrl ?: "")
        else
            ""
    } catch (e: Exception) {
        ""
    }
}

fun Tweet.hasSingleVideo(): Boolean {
    extendedEntities?.media?.size?.let { return it == 1 && extendedEntities.media[0].type != "photo" }
    return false
}

fun Tweet.hasSingleImage(): Boolean {
    extendedEntities?.media?.size?.let { return it == 1 && extendedEntities.media[0].type == "photo" }
    return false
}

fun Tweet.hasMultipleMedia(): Boolean {
    extendedEntities?.media?.size?.let { return it > 1 }.run { return false }
}

//endregion Extensions for Tweet

// region map maker

fun Marker.getPinSnippetList(): List<String> = this.snippet.split("|")

//endregion map maker
