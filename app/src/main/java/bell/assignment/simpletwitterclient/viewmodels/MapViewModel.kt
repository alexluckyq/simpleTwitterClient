package bell.assignment.simpletwitterclient.viewmodels

import android.location.Location
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bell.assignment.simpletwitterclient.managers.cache.CacheManager
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.models.Search
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.services.params.Geocode
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MapViewModel: ViewModel() {

    companion object {
        val refreshMapInterval: Long = 30000L
    }

    private val tweetsLiveData: MutableLiveData<MutableList<Tweet>> = MutableLiveData()

    fun getTweetsObservable(): LiveData<MutableList<Tweet>> = tweetsLiveData

    fun getTweetCacheKey(radius: Int): String = "tweetradius=$radius"

    fun fetchTweetsByLocation(location: Location = CacheManager.getCachedLocation(),
                              radius: Int = CacheManager.getCachedRadius()) {
        var tweetList: Any? = null
        runBlocking {
            tweetList =
                CacheManager.getLurCacheByKey(getTweetCacheKey(radius)).await()
        }
        tweetList?.let {
            postUpdate(radius, tweetList = tweetList as List<Tweet>)
        } ?: startRefreshTimer(0, refreshMapInterval, {
            loadTweets(location, radius)
        })
    }

    @WorkerThread
    private fun loadTweets(location: Location, radius: Int) {
        Log.d("tweets", "loadTweets")
        val geocode =
            Geocode(
                location.latitude, location.longitude,
                radius, Geocode.Distance.KILOMETERS
            )
        try {
            TwitterCore.getInstance().apiClient.searchService.tweets(
                "", geocode, null,
                null, null, 100, null, null, null, true
            ).enqueue(object : Callback<Search> {
                override fun onResponse(call: Call<Search>, response: Response<Search>) {
                    val tweets = response.body()?.tweets ?: Collections.emptyList()
                    postUpdate(radius, tweets)
                }

                override fun onFailure(call: Call<Search>, throwable: Throwable) {
                    postUpdate(radius, null)
                }
            })
        } catch (e: Exception) {
            postUpdate(radius, null)
        }
    }

    private fun postUpdate(radius: Int, tweetList: List<Tweet>?) {
        updateTweetCache(radius, tweetList)
        this.tweetsLiveData.postValue(tweetList?.toMutableList())
    }

    private fun updateTweetCache(radius: Int, tweetList: List<Tweet>?) {
        runBlocking {
            CacheManager.removeLurCacheByKey(getTweetCacheKey(radius)).await()
            CacheManager.setLurCache(getTweetCacheKey(radius), tweetList as Any).await()
        }
    }

    private fun startRefreshTimer(delayMillis: Long = 0, repeatMillis: Long = 0, action: () -> Unit) =
        GlobalScope.launch {
            delay(delayMillis)
            if (repeatMillis > 0) {
                while (true) {
                    action()
                    delay(repeatMillis)
                }
            } else {
                action()
            }
        }
}