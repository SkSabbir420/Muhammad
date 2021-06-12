package com.islam.muhammad.main

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.MediaController
import com.islam.muhammad.R
import kotlinx.android.synthetic.main.activity_video_play.*

class VideoPlayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_play)

        val intent: Intent = intent
        val videoUrl = intent.getStringExtra("keyv")!!
        val controller = MediaController(this)
        controller.setMediaPlayer(video_play)
        video_play.setMediaController(controller)
        val videoUri = Uri.parse(videoUrl)
        video_play.setVideoURI(videoUri)
        controller.hide()
        video_play.seekTo(1)
        video_play.start()
    }
}