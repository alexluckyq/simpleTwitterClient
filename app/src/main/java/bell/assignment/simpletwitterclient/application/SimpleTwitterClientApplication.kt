package bell.assignment.simpletwitterclient.application

import android.app.Application
import android.util.Log
import bell.assignment.simpletwitterclient.BuildConfig
import com.twitter.sdk.android.core.DefaultLogger
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig

class SimpleTwitterClientApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initTwitter()
    }

    private fun initTwitter() {
        val twitterConfig = TwitterConfig.Builder(this)
            .logger(DefaultLogger(Log.DEBUG))//enable logging when app is in debug mode
            .twitterAuthConfig(
                TwitterAuthConfig(
                    BuildConfig.TWITTER_API_KEY,
                    BuildConfig.TWITTER_CONSUMER_SECRET
                )
            )
            .debug(true)//enable debug mode
            .build()

        Twitter.initialize(twitterConfig)
    }


}