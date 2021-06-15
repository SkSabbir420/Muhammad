package com.islam.muhammad.main

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import com.islam.muhammad.R
import kotlinx.android.synthetic.main.activity_video_play.*

class VideoPlayActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_play)

//        lateinit var mRewardedVideoAd: RewardedVideoAd


//        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this)
//        mRewardedVideoAd.rewardedVideoAdListener = this
//        mRewardedVideoAd.
//        loadAd("ca-app-pub-3940256099942544/5224354917", AdRequest.Builder().build())
//        mRewardedVideoAd.show()

        val intent: Intent = intent
        val videoUrl = intent.getStringExtra("keyv")!!
        val controller = MediaController(this)
        controller.setMediaPlayer(video_play)
        video_play.setMediaController(controller)
        val videoUri = Uri.parse(videoUrl)
        video_play.setVideoURI(videoUri)
        controller.hide()
        video_play.start()

        //video_progressbar.visibility = View.GONE
    }

//    override fun onRewardedVideoAdLoaded() {
//
//    }
//
//    override fun onRewardedVideoAdOpened() {
//
//    }
//
//    override fun onRewardedVideoStarted() {
//
//    }
//
//    override fun onRewardedVideoAdClosed() {
//
//    }
//
//    override fun onRewarded(p0: RewardItem?) {
//
//    }
//
//    override fun onRewardedVideoAdLeftApplication() {
//
//    }
//
//    override fun onRewardedVideoAdFailedToLoad(p0: Int) {
//
//    }
//
//    override fun onRewardedVideoCompleted() {
//
//    }
}