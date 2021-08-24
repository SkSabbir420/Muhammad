package com.ourdream.muslimdream.main

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.MediaController
import android.widget.Toast
//import com.google.android.gms.ads.AdRequest
//import com.google.android.gms.ads.MobileAds
//import com.google.android.gms.ads.reward.RewardItem
//import com.google.android.gms.ads.reward.RewardedVideoAd
//import com.google.android.gms.ads.reward.RewardedVideoAdListener
import com.ourdream.muslimdream.R
import kotlinx.android.synthetic.main.activity_video_play.*

class VideoPlayActivity : AppCompatActivity()
//    , RewardedVideoAdListener
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_play)

//        lateinit var mRewardedVideoAd: RewardedVideoAd


//        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this)
//        mRewardedVideoAd.rewardedVideoAdListener = this
//        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
//            AdRequest.Builder().build())
//
//        if(mRewardedVideoAd.isLoaded) {
//            Toast.makeText(this,"Load success",Toast.LENGTH_SHORT).show()
//            mRewardedVideoAd.show()
//        }else{
//            Toast.makeText(this,"Load Unsuccess",Toast.LENGTH_SHORT).show()
//        }

        val intent: Intent = intent
        val videoUrl = intent.getStringExtra("keyv")!!
//        val mCurrentPosition = 0
        val controller = MediaController(this)
        controller.setMediaPlayer(video_play)
        video_play.setMediaController(controller)
        val videoUri = Uri.parse(videoUrl)
//        if(mRewardedVideoAd.isLoaded) {
//            Toast.makeText(this,"Load success",Toast.LENGTH_SHORT).show()
//            mRewardedVideoAd.show()
//        }else{
//            Toast.makeText(this,"Load Unsuccess",Toast.LENGTH_SHORT).show()
//        }
        video_play.setVideoURI(videoUri)
        controller.hide()
        video_play.start()
//        video_play.setOnPreparedListener {
//            if (mCurrentPosition > 0) {
//                video_play.seekTo(mCurrentPosition)
//            } else {
//                video_play.seekTo(1)
//            }
//            video_play.start()
//        }

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