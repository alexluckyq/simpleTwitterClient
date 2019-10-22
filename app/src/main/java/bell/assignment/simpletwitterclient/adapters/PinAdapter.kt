package bell.assignment.simpletwitterclient.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import bell.assignment.simpletwitterclient.R
import bell.assignment.simpletwitterclient.data.TweetData
import bell.assignment.simpletwitterclient.utils.formatTime
import bell.assignment.simpletwitterclient.utils.getPinSnippetList
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class PinAdapter (private val context: Context) : GoogleMap.InfoWindowAdapter,
    GoogleMap.OnInfoWindowClickListener {

    override fun getInfoContents(marker: Marker): View {
        val twitterStringList = marker.getPinSnippetList()
        val profileImageUrl = twitterStringList[TweetData.INDEX_PROFILE_PHOTO_URL]
        val timestamp = twitterStringList[TweetData.INDEX_TIME_STAMP]
        val tweet = twitterStringList[TweetData.INDEX_TWEET_TEXT]

        val view = LayoutInflater.from(context).inflate(R.layout.map_marker_view, null)
        Glide.with(context).load(profileImageUrl).placeholder(R.drawable.default_user_thumbnail).into(view.findViewById(R.id.profileImage))

        view.findViewById<TextView>(R.id.timestamp).text = timestamp.formatTime()
        view.findViewById<TextView>(R.id.name).text = marker.title
        view.findViewById<TextView>(R.id.tweetText).text = tweet

        return view
    }

    override fun getInfoWindow(marker: Marker?): View? = null

    override fun onInfoWindowClick(marker: Marker?) {
        marker?.let {
            val text = marker.getPinSnippetList()
            val id = text[TweetData.INDEX_TWEET_ID].toLong()
//        startTwitterDetailActivity(id)
        }
    }
}