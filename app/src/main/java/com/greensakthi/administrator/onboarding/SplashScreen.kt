package com.greensakthi.administrator.onboarding

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.greensakthi.administrator.home.MainActivity
import com.greensakthi.administrator.R
import com.greensakthi.administrator.utils.AppPreferences

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // initialising App Preferences
        AppPreferences.init(this)

        //window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        Handler().postDelayed({

            // checking whether user is logged in or not using the SharedPrefs
            if(AppPreferences.isLogin == true) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        },3000)

    }
}