package bell.assignment.simpletwitterclient.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bell.assignment.simpletwitterclient.data.TweetData
import bell.assignment.simpletwitterclient.utils.initizeTweetDataFromTweet
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.models.Tweet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel: ViewModel() {

    private val tweetLiveData: MutableLiveData<TweetData> = MutableLiveData()

    fun retrueveTweetDetails(tweetId: Long) {
        try {
            TwitterCore.getInstance().apiClient.statusesService.show(
                tweetId, null, null, null
            ).enqueue(object : Callback<Tweet> {
                override fun onResponse(call: Call<Tweet>?, response: Response<Tweet>?) {
                    if (response?.isSuccessful == true) {
                        postData(response.body()?.initizeTweetDataFromTweet())
                    }
                }

                override fun onFailure(call: Call<Tweet>?, t: Throwable?) {
                    postData(null)
                }
            })
        } catch (e: Exception) {
            postData(null)
        }
    }

    fun postData(tweet: TweetData?) {
        this.tweetLiveData.value = tweet
    }

    fun getTweetLiveData() = tweetLiveData

}