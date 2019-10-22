package bell.assignment.simpletwitterclient.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import bell.assignment.simpletwitterclient.databinding.ActivityDetailBinding
import bell.assignment.simpletwitterclient.viewmodels.DetailViewModel

class DetailActivity: AppCompatActivity() {

    companion object {
        val TWEET_ID = "tweetId"

        fun openActivity(tweetId: Long) {

        }
    }

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var activityBinding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = DataBindingUtil.setContentView(this,
            bell.assignment.simpletwitterclient.R.layout.activity_detail)
        detailViewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)


    }

}