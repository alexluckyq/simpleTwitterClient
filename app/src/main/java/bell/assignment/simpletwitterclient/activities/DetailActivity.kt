package bell.assignment.simpletwitterclient.activities

import androidx.appcompat.app.AppCompatActivity
import bell.assignment.simpletwitterclient.viewmodels.DetailViewModel

class DetailActivity: AppCompatActivity() {

    companion object {
        val TWEET_ID = "tweetId"

        fun openActivity() {

        }
    }

    private lateinit var detailViewModel: DetailViewModel
}