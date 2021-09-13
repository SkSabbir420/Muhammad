package com.ourdream.muslimdream.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ourdream.muslimdream.R
import kotlinx.android.synthetic.main.activity_notification_for_conformation_mail.*

class NotificationForConformationMail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_for_conformation_mail)

        close_email_notification_btn.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}