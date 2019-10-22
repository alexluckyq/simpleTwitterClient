package bell.assignment.simpletwitterclient.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bell.assignment.simpletwitterclient.R
import bell.assignment.simpletwitterclient.utils.loadImageUrl
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        val imageUrl = intent.getStringExtra(IMAGE_URL)
        imageView.loadImageUrl(imageUrl)
    }

    companion object {
        const val IMAGE_URL = "imageUrl"
    }
}