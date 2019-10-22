package bell.assignment.simpletwitterclient.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import bell.assignment.simpletwitterclient.R
import bell.assignment.simpletwitterclient.data.TweetData
import bell.assignment.simpletwitterclient.databinding.ActivityDetailBinding
import bell.assignment.simpletwitterclient.managers.tweet.TweetActionManager
import bell.assignment.simpletwitterclient.managers.tweet.TweetActionManagerImpl
import bell.assignment.simpletwitterclient.utils.initizeTweetDataFromTweet
import bell.assignment.simpletwitterclient.utils.loadImageUrl
import bell.assignment.simpletwitterclient.viewmodels.DetailViewModel
import kotlinx.android.synthetic.main.layout_profile.*
import kotlinx.android.synthetic.main.layout_tweetaction.*
import kotlinx.android.synthetic.main.layout_tweetmedia.*

class DetailActivity: AppCompatActivity() {

    companion object {
        val TWEET_ID = "tweetId"

        fun openActivity(tweetId: Long, context: Context?) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(TWEET_ID, tweetId)
            context?.let {
                context.startActivity(intent)
            }
        }
    }

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var activityBinding: ActivityDetailBinding
    private val tweetActionManager: TweetActionManager = TweetActionManagerImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_detail)
        detailViewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        detailViewModel.getTweetLiveData().observe(this, Observer { 
            it?.let { 
                init(it)
            }
        })
        val tweetId = intent.getLongExtra(TWEET_ID, 0L)
        if (tweetId != 0L) {
            detailViewModel.retrueveTweetDetails(tweetId)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun init(tweetData: TweetData) {
        activityBinding.tweetData = tweetData
        profileImageView.loadImageUrl(tweetData.profileImageUrl, placeholder = R.drawable.default_user_thumbnail)

        when (tweetData.retweeted) {
            true -> {
                retweetImageButton.setImageResource(R.drawable.retweet_blue)
            }
            false -> {
                retweetImageButton.setImageResource(R.drawable.retweet)
            }
        }

        retweetImageButton.setOnClickListener { t ->
            if (tweetData.retweeted)
                tweetActionManager.unretweet(tweetId = tweetData.tweetId,
                    context = this@DetailActivity,
                    onActionSuccess = {
                        init(it.initizeTweetDataFromTweet())
                    })
            else
                tweetActionManager.retweet(tweetId = tweetData.tweetId,
                    context = this@DetailActivity,
                    onActionSuccess = {
                        init(it.initizeTweetDataFromTweet())
                    })
        }

        when (tweetData.favorited) {
            true -> {
                favouriteImageButton.setImageResource(R.drawable.heart_filled)
            }
            false -> {
                favouriteImageButton.setImageResource(R.drawable.heart_outline)
            }
        }

        favouriteImageButton.setOnClickListener { t ->
            if (tweetData.favorited)
                tweetActionManager.unfavorite(tweetId = tweetData.tweetId,
                    context = this@DetailActivity,
                    onActionSuccess = {
                        init(it.initizeTweetDataFromTweet())
                    })
            else
                tweetActionManager.favorite(tweetId = tweetData.tweetId,
                    context = this@DetailActivity,
                    onActionSuccess = {
                        init(it.initizeTweetDataFromTweet())
                    })
        }

        if (tweetData.tweetPhotoUrl.isNotEmpty()) {
            tweetPhoto.loadImageUrl(tweetData.tweetPhotoUrl)
            tweetPhoto.setOnClickListener { tweetActionManager.showImage(tweetData.tweetPhotoUrl,
                this@DetailActivity) }
        }

        if (tweetData.tweetVideoCoverUrl.isNotEmpty()) {
            tweetVideoImageView.loadImageUrl(tweetData.tweetVideoCoverUrl)
            playVideoImageButton.setOnClickListener { view ->
                val pair = tweetData.tweetVideoUrl
                tweetActionManager.showVideo(pair.first, pair.second, this@DetailActivity)
            }
        }
        activityBinding.executePendingBindings()
    }

}