package bell.assignment.simpletwitterclient.activities

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import bell.assignment.simpletwitterclient.R
import kotlinx.android.synthetic.main.activity_video.*

class VideoActivity : AppCompatActivity(){

    companion object {
        const val VIDEO_URL = "video"
        const val VIDEO_TYPE = "videoType"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        val mc = MediaController(this)
        mc.setAnchorView(videoView)
        mc.setMediaPlayer(videoView)

        videoView.setOnPreparedListener { loadingProgressBar.visibility = View.GONE }
        if ("animated_gif" == intent.getStringExtra(VIDEO_TYPE)) {
            videoView.setOnCompletionListener {
                videoView.start()
            }
        }
        videoView.setMediaController(mc)
        videoView.setVideoURI(Uri.parse(intent.getStringExtra(VIDEO_URL)))
        videoView.requestFocus()
        videoView.start()
    }


}