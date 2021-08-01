package com.islam.muslimdream.post

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.islam.muslimdream.R
import kotlinx.android.synthetic.main.activity_video_post_category.*

class VideoPostCategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_post_category)


        chose_video_post_category_Allah.setOnClickListener {
            val intent = Intent(this, AddPostVideoActivity::class.java)
            intent.putExtra("keyCategory","Allah")
            startActivity(intent)
            finish()
        }
        chose_video_post_category_Nabi.setOnClickListener {
            val intent = Intent(this, AddPostVideoActivity::class.java)
            intent.putExtra("keyCategory","Nabi")
            startActivity(intent)
            finish()
        }
        chose_video_post_category_others.setOnClickListener {
            val intent = Intent(this, AddPostVideoActivity::class.java)
            intent.putExtra("keyCategory","Others")
            startActivity(intent)
            finish()
        }
    }



}