package bell.assignment.simpletwitterclient.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import bell.assignment.simpletwitterclient.R
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import com.twitter.sdk.android.core.identity.TwitterLoginButton

class LoginActivity: AppCompatActivity() {

    private lateinit var loginButton: TwitterLoginButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        loginButton = findViewById(R.id.loginButton)

        loginButton.callback = object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>?) {
                finish()
            }

            override fun failure(e: TwitterException) {
                Toast.makeText(this@LoginActivity, R.string.login_failed, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loginButton.onActivityResult(requestCode, resultCode, data)
    }

}