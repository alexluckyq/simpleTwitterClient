package bell.assignment.simpletwitterclient.managers.tweet

import android.content.Context
import android.content.Intent
import android.widget.Toast
import bell.assignment.simpletwitterclient.activities.ImageActivity
import bell.assignment.simpletwitterclient.activities.LoginActivity
import bell.assignment.simpletwitterclient.activities.VideoActivity
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Tweet
import retrofit2.Call

object TweetActionManagerImpl: TweetActionManager {

    override fun favorite(
        tweetId: Long,
        context: Context,
        onActionSuccess: (tweet: Tweet) -> Unit
    ) {
        if (TwitterCore.getInstance().sessionManager.activeSession == null)
            context.startActivity(Intent(context, LoginActivity::class.java))
        else {
            val call = TwitterCore.getInstance().apiClient.favoriteService.create(tweetId, null)
            enqueue(call, context, onActionSuccess)
        }
    }

    override fun unfavorite(
        tweetId: Long,
        context: Context,
        onActionSuccess: (tweet: Tweet) -> Unit
    ) {
        if (TwitterCore.getInstance().sessionManager.activeSession == null)
            context.startActivity(Intent(context, LoginActivity::class.java))
        else {
            val call = TwitterCore.getInstance().apiClient.favoriteService.destroy(tweetId, null)
            enqueue(call, context, onActionSuccess)
        }
    }

    override fun retweet(
        tweetId: Long,
        context: Context,
        onActionSuccess: (tweet: Tweet) -> Unit
    ) {
        if (TwitterCore.getInstance().sessionManager.activeSession == null)
            context.startActivity(Intent(context, LoginActivity::class.java))
        else {
            val call = TwitterCore.getInstance().apiClient.statusesService.retweet(tweetId, null)
            enqueue(call, context, onActionSuccess)
        }
    }

    override fun unretweet(
        tweetId: Long,
        context: Context,
        onActionSuccess: (tweet: Tweet) -> Unit
    ) {
        if (TwitterCore.getInstance().sessionManager.activeSession == null)
            context.startActivity(Intent(context, LoginActivity::class.java))
        else {
            val call = TwitterCore.getInstance().apiClient.statusesService.unretweet(tweetId, null)
            enqueue(call, context, onActionSuccess)
        }
    }

    override fun showImage(imageUrl: String, context: Context) {
        val intent = Intent(context, ImageActivity::class.java)
        intent.putExtra(ImageActivity.IMAGE_URL, imageUrl)
        context.startActivity(intent)
    }

    override fun showVideo(videoUrl: String, videoType: String, context: Context) {
        val intent =  Intent(context, VideoActivity::class.java)
        intent.putExtra(VideoActivity.VIDEO_URL, videoUrl)
        intent.putExtra(VideoActivity.VIDEO_TYPE, videoType)
        context.startActivity(intent)
    }

    private fun enqueue(call: Call<Tweet>, context: Context, onActionSuccess: (tweet: Tweet) -> Unit) {
        call.enqueue(object : Callback<Tweet>() {
            override fun success(result: Result<Tweet>?) {
                result?.let {
                    onActionSuccess?.invoke(result.data)
                }
            }

            override fun failure(exception: TwitterException?) {
                Toast.makeText(context,
                    "Something went wrong, please try again later.",
                    Toast.LENGTH_SHORT).show();
            }
        })
    }
}