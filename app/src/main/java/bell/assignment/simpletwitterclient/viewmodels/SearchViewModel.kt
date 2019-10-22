package bell.assignment.simpletwitterclient.viewmodels

import android.location.Location
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bell.assignment.simpletwitterclient.data.TweetData
import bell.assignment.simpletwitterclient.managers.cache.CacheManager
import bell.assignment.simpletwitterclient.utils.initizeTweetDataFromTweet
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.models.Search
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.services.params.Geocode
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SearchViewModel: ViewModel() {

    private val tweetsLiveData: MutableLiveData<List<TweetData>?> = MutableLiveData()

    fun getTweetsCacheKey(query:String, geocode: Geocode): String = "query=$query:$geocode"

    fun getTweetDataObservable(): LiveData<List<TweetData>?> = tweetsLiveData

    fun init() {
        retrieveTweetsByKeywords(
            ""
        )
    }

    fun retrieveTweetsByKeywords(
        query: String,
        location: Location = CacheManager.getCachedLocation(),
        radius: Int = CacheManager.getCachedRadius()
    ) {
        val geocode = Geocode(
            location.latitude, location.longitude,
            radius, Geocode.Distance.KILOMETERS)
        var cachedTweetList: Any? = null
        runBlocking {
            cachedTweetList =
                CacheManager.getLurCacheByKey(getTweetsCacheKey(query, geocode)).await()
        }
        cachedTweetList?.let {
            postUpdate(query, geocode, cachedTweetList as List<Tweet>)
        }?: queryTweets(query, geocode)
    }

    @WorkerThread
    private fun queryTweets(query: String, geocode: Geocode) {
        try {
            TwitterCore.getInstance().apiClient.searchService.tweets(
                query, geocode, null,
                null, null, 100, null, null, null, true
            ).enqueue(object : Callback<Search> {

                override fun onResponse(call: Call<Search>, response: Response<Search>) {
                    val tweets = response.body()?.tweets ?: Collections.emptyList()
                    postUpdate(query, geocode, tweets)
                }

                override fun onFailure(call: Call<Search>, throwable: Throwable) {
                    postUpdate(query, geocode, null)
                }
            })
        } catch (e: Exception) {
            postUpdate(query, geocode, null)
        }
    }

    private fun postUpdate(query: String, geocode: Geocode, tweetList: List<Tweet>?) {
        val tweetListItems = mutableListOf<TweetData>()
        tweetList?.let {
            it.forEach { tweet: Tweet -> tweetListItems.add(tweet.initizeTweetDataFromTweet()) }
        }
        updateTweetCache(query, geocode, tweetList)
        this.tweetsLiveData.postValue(tweetListItems.toMutableList())
    }

    private fun updateTweetCache(query: String, geocode: Geocode, tweetList: List<Tweet>?) {
        runBlocking {
            CacheManager.removeLurCacheByKey(getTweetsCacheKey(query, geocode)).await()
            CacheManager.setLurCache(getTweetsCacheKey(query, geocode), tweetList as Any).await()
        }
    }
}