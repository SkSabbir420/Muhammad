package com.islam.muhammad.post

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.islam.muhammad.R
import com.islam.muhammad.main.AddPostPhotoActivity
import com.islam.muhammad.main.AddPostVideoActivity
import kotlinx.android.synthetic.main.activity_post_main.*

class post_main_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_main)
        chose_photo_post_id.setOnClickListener {
            val intent = Intent(this, AddPostPhotoActivity::class.java)
            startActivity(intent)
            finish()
        }
        chose_video_post_id.setOnClickListener {
            val intent = Intent(this, AddPostVideoActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}