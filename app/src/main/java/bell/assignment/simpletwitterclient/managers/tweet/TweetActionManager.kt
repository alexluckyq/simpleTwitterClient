package bell.assignment.simpletwitterclient.managers.tweet

import android.app.Activity
import android.content.Context
import com.twitter.sdk.android.core.models.Tweet

interface TweetActionManager {

    fun favorite(tweetId: Long,
                 context: Context,
                 onActionSuccess: ((tweet: Tweet) -> Unit)
    )

    fun unfavorite(tweetId: Long,
                   context: Context,
                   onActionSuccess: ((tweet: Tweet) -> Unit)
    )

    fun retweet(tweetId: Long,
                context: Context,
                onActionSuccess: ((tweet: Tweet) -> Unit))

    fun unretweet(tweetId: Long,
                  context: Context,
                  onActionSuccess: ((tweet: Tweet) -> Unit))

    fun showImage(imageUrl: String,
                  context: Context)

    fun showVideo(videoUrl: String,
                  videoType: String,
                  context: Context)
}