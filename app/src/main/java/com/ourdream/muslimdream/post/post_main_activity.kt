package com.ourdream.muslimdream.post

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ourdream.muslimdream.R
import kotlinx.android.synthetic.main.activity_post_main.*

class post_main_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_main)

        chose_photo_post_id.setOnClickListener {
            val intent = Intent(this, AddPostPhotoActivity::class.java)
            startActivity(intent)
//            finish()
        }
        chose_video_post_id.setOnClickListener {
            //val intent = Intent(this, AddPostVideoActivity::class.java)
            val intent = Intent(this, VideoPostCategoryActivity::class.java)
            startActivity(intent)
//            finish()
        }
    }
}