package com.islam.muhammad.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.islam.muhammad.R
import kotlinx.android.synthetic.main.activity_video_post_category.*

class VideoPostCategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_post_category)

        chose_video_post_category_eman.setOnClickListener {
            val intent = Intent(this, AddPostVideoActivity::class.java)
            intent.putExtra("keyCategory","Eman")
            startActivity(intent)
            finish()
        }
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
        chose_video_post_category_kiyamot.setOnClickListener {
            val intent = Intent(this, AddPostVideoActivity::class.java)
            intent.putExtra("keyCategory","Kiyamot")
            startActivity(intent)
            finish()
        }
        chose_video_post_category_future.setOnClickListener {
            val intent = Intent(this, AddPostVideoActivity::class.java)
            intent.putExtra("keyCategory","Future")
            startActivity(intent)
            finish()
        }
    }



}