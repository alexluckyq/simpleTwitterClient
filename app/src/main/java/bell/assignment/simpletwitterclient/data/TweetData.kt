package bell.assignment.simpletwitterclient.data

/**
 * TweetData model mapping from Tweet of Twitter framework
 */
class TweetData(
        val tweetId: Long,
        val profileImageUrl: String? = null,
        val userName: String = "",
        val timeStampString: String,
        val tweetText: String,
        val tweetFavoriteCount: Int = 0,
        val tweetFavorited: Boolean = false,
        val reTweetCount: Int = 0,
        val retweeted: Boolean = false,
        val userScreenName: String = "",
        val tweetPhotoUrl: String = "",
        val tweetVideoCoverUrl: String = "",
        val tweetVideoUrl: Pair<String, String> = Pair("", "")
) {

        companion object {
                const val INDEX_TWEET_ID = 0
                const val INDEX_PROFILE_PHOTO_URL = 1
                const val INDEX_TIME_STAMP = 2
                const val INDEX_TWEET_TEXT = 3
        }

    val mapMarkerSnippetString: String
        get() = "$tweetId|$profileImageUrl|$timeStampString|$tweetText"


}