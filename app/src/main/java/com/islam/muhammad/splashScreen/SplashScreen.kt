package com.islam.muhammad.splashScreen

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle

import com.islam.muhammad.login.Login
import com.islam.muhammad.R

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()
        splashScreen()
    }

    private fun splashScreen(){
        val splashScreen = object:Thread(){
            override fun run(){
                try {
                    sleep(2500)
                    val intent = Intent(baseContext, Login::class.java)
                    startActivity(intent)
                    finish()
                }catch(e:Exception){}
            }
        }
        splashScreen.start()
    }


}