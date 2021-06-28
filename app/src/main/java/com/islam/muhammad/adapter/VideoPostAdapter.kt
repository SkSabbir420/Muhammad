package com.islam.muhammad.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import com.islam.muhammad.model.User
import com.islam.muhammad.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.islam.muhammad.main.VideoPlayActivity
import com.islam.muhammad.model.VideoPost
import com.islam.muhammad.main.video_comment_activity
import com.islam.muhammad.model.Vpvcount
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_video_play.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.video_posts_layout.view.*

class VideoPostAdapter(private val mContext:Context, private val mPost: List<VideoPost>):RecyclerView.Adapter<VideoPostAdapter.ViewHolder>(){

    private  var firebaseUser:FirebaseUser? = null
    //private var mVideoView: VideoView? = null
    private  var userid:String? = null
    private  var postKey:String? = null
    private  var likeReference:DatabaseReference? = null
    //private  var viewReference:DatabaseReference? = null
    //val viewReference = FirebaseDatabase.getInstance().getReference("postVideoViews")
    val viewReference = FirebaseDatabase.getInstance().getReference("postVideoViews")
    //val viewReference = FirebaseDatabase.getInstance().getReference("VPView")
    private  var testClick:Boolean =false
//    private lateinit var mRewardedVideoAd: RewardedVideoAd


    inner class  ViewHolder(@NonNull itemView: View,):RecyclerView.ViewHolder(itemView){

        var profileImage:CircleImageView
        var description: TextView
        var postImage:ImageView
        var postVideo:VideoView
        var userName: TextView
        var likeButton:ImageView
        var likes:TextView
        var views:TextView
        var viewsText:TextView
        var commentButton:ImageView
        var comments: TextView
        var date:TextView



        //var publisher: TextView
        //var saveButton:ImageView

        init {
            profileImage = itemView.findViewById(R.id.user_profile_image_post)
            description = itemView.findViewById(R.id.post_text_home)
            postImage = itemView.findViewById(R.id.post_video_video)
            postVideo = itemView.findViewById(R.id.video_play_main)
            date = itemView.findViewById(R.id.txt_video_date)
           // mVideoView =postImage
            userName = itemView.findViewById(R.id.user_name_post)
            likeButton = itemView.findViewById(R.id.video_image_like_btn)
            likes = itemView.findViewById(R.id.video_image_like_text)
            views = itemView.findViewById(R.id.video_view)
            viewsText = itemView.findViewById(R.id.video_view_text)
            //mBufferingTextView = itemView.findViewById(R.id.buffering_textview)
            commentButton = itemView.findViewById(R.id.video_image_comment_btn)
            comments = itemView.findViewById(R.id.video_comments)

//            mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(mContext)
//            mRewardedVideoAd.rewardedVideoAdListener = this
//            mRewardedVideoAd.
//            loadAd("ca-app-pub-3940256099942544/5224354917",AdRequest.Builder().build())
//            mRewardedVideoAd.show()

            //publisher = itemView.findViewById(R.id.publisher)
            //saveButton = itemView.findViewById(R.id.post_save_comment_btn)



            itemView.video_image_like_btn.setOnClickListener {
                val  position:Int = adapterPosition
                val post = mPost[position]
                postKey = post.getPostid()

                testClick =true
                likeReference!!.addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(p0: DataSnapshot) {
                        if (testClick ==true){
                            if(p0.child(postKey!!).hasChild(userid!!)){
                                likeReference!!.child(postKey!!).child(userid!!).removeValue()
                                testClick =false
                            }else{
                                likeReference!!.child(postKey!!).child(userid!!).setValue(true)
                                testClick = false
                            }

                            /*if(p0.child(postKey!!).child("like").hasChild(userid!!)){
                                likeReference!!.child(postKey!!).child("like").child(userid!!).removeValue()
                                testClick =false
                            }else{
                                likeReference!!.child(postKey!!).child("like").child(userid!!).setValue(true)
                                testClick = false
                            }*/
                        }
                    }
                    override fun onCancelled(p0: DatabaseError){

                    }
                })
            }

            itemView.video_image_comment_btn.setOnClickListener {
                val  position:Int = adapterPosition
                val post = mPost[position]
                postKey = post.getPostid()
                val intent = Intent(mContext, video_comment_activity::class.java)
                intent.putExtra("key",postKey)
                intent.putExtra("keyUid",post.getPublisher())
                mContext.startActivity(intent)
            }
//            var count:Int
            itemView.post_video_video.setOnClickListener {
                val  position:Int = adapterPosition
                val post = mPost[position]
                val videoUri = Uri.parse(post.getPostimage())
                postImage.visibility = View.GONE
                postVideo.visibility = View.VISIBLE


//                val intent = Intent(mContext, VideoPlayActivity::class.java)
//                intent.putExtra("keyv",videoUri)
//                mContext.startActivity(intent)

                val controller = MediaController(mContext)
                controller.setMediaPlayer(postVideo)
                postVideo.setMediaController(controller)
                postVideo.setVideoURI(videoUri)
                controller.hide()
                postVideo.start()
                val view = itemView.video_view.text.toString().toInt()
                viewReference.child(post.getPostid()).child("postView").setValue(view + 1)

            }

            itemView.video_image_share_btn.setOnClickListener {
                val  position:Int = adapterPosition
                val post = mPost[position]
                val currentUrl = post.getPostimage()
                val title = post.getDescription()
                val i = Intent(Intent.ACTION_SEND)
                i.type = "text/plain"
                i.putExtra(Intent.EXTRA_TEXT, "Post Description:\n$title\n\nVideo Link: $currentUrl")
                mContext.startActivity(Intent.createChooser(i, "Share this video Uri with"))

            }


        }

//        override fun onRewardedVideoAdLoaded() {
//
//        }
//
//        override fun onRewardedVideoAdOpened() {
//
//        }
//
//        override fun onRewardedVideoStarted() {
//
//        }
//
//        override fun onRewardedVideoAdClosed() {
//
//        }
//
//        override fun onRewarded(p0: RewardItem?) {
//
//        }
//
//        override fun onRewardedVideoAdLeftApplication() {
//
//        }
//
//        override fun onRewardedVideoAdFailedToLoad(p0: Int) {
//
//        }
//
//        override fun onRewardedVideoCompleted() {
//
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.video_posts_layout,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mPost.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        userid = firebaseUser!!.uid
        val post = mPost[position]
        postKey = post.getPostid()

//            val mediaController = MediaController(mContext)
//            mediaController.setAnchorView(holder.postImage)
//            //val onlineUri:Uri = Uri.parse(post.getPostimage())
//            val offlineURL:Uri = Uri.parse("android.resource://com.islam.muhammad/${R.raw.funny_video}")
//            holder.postImage.setMediaController(mediaController)
//            //holder.postImage.setVideoURI(onlineUri)
//            holder.postImage.setVideoURI(offlineURL)
//            holder.postImage.requestFocus()
//            //holder.postImage.start()

            /*val controller = MediaController(mContext)
            controller.setMediaPlayer(mVideoView)
            mVideoView!!.setMediaController(controller)
            val mCurrentPosition = 0
            val videoUri = Uri.parse(post.getPostimage())
            //Toast.makeText(mContext,"$videoUri",Toast.LENGTH_SHORT).show()
            mVideoView!!.setVideoURI(videoUri)
            controller.hide()
            mVideoView!!.setOnPreparedListener {
                if (mCurrentPosition > 0){
                    mVideoView!!.seekTo(mCurrentPosition)
                } else {
                    mVideoView!!.seekTo(1)
                }
//                mVideoView!!.start()
            }*/
        val publisher = post.getPublisher()

        publisherInfo(holder.profileImage,holder.userName,post.getPublisher())
        Picasso.get().load(post.getCoverPhoto()).into(holder.postImage)
        holder.description.text = post.getDescription()
        holder.date.setText(post.getPostDate())
        getLikeButtonStatus(postKey!!, userid!!,holder)
        getViewButtonStatus(postKey!!,holder)
        getcommentButtonStatus(postKey!!,publisher,holder)





    }


    private fun publisherInfo(profileImage: CircleImageView, userName: TextView,publisherId: String) {
        val userRef = FirebaseDatabase.getInstance().reference.child("users").child(publisherId)
        userRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    val user = p0.getValue<User>(User::class.java)
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(profileImage)
                    userName.text = user.getUsername()
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }


    private  fun getLikeButtonStatus(postKey:String,userid:String,holder:ViewHolder){
//        likeReference = FirebaseDatabase.getInstance().getReference("VideoPost")
        likeReference = FirebaseDatabase.getInstance().getReference("postVideoLikes")
        likeReference!!.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.child(postKey).hasChild(userid)){
                    var likeCount:Int = p0.child(postKey).childrenCount.toInt()
                    holder.likes.setText("$likeCount likes")
                    holder.likeButton.setImageResource(R.drawable.ic_baseline_favorite_24)
                }else{
                    var likeCount:Int = p0.child(postKey).childrenCount.toInt()
                    holder.likes.setText("$likeCount likes")
                    holder.likeButton.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                }

                /*if (p0.child(postKey).child("like").hasChild(userid)){
                    var likeCount:Int = p0.child(postKey).child("like").childrenCount.toInt()
                    holder.likes.setText("$likeCount likes")
                    holder.likeButton.setImageResource(R.drawable.ic_baseline_favorite_24)
                }else{
                    var likeCount:Int = p0.child(postKey).child("like").childrenCount.toInt()
                    holder.likes.setText("$likeCount likes")
                    holder.likeButton.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                }*/

            }
            override fun onCancelled(p0: DatabaseError){
            }
        })

    }
    private  fun getViewButtonStatus(postkey:String,holder:ViewHolder){
        var count:Int
        viewReference.child(postkey).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val post = p0.getValue<Vpvcount>(Vpvcount::class.java)
                    count = post!!.getPostView()
                    holder.views.setText("$count")
                }
            }
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(mContext,"Not work",Toast.LENGTH_SHORT).show()
            }
        })

    }

    private  fun getcommentButtonStatus(postkey:String,publisher:String,holder:ViewHolder){
        //val commentReference = FirebaseDatabase.getInstance().reference.child("VideoPost").child(postkey).child("Comments")
        val commentReference = FirebaseDatabase.getInstance().reference.child("postVideoComments").child(publisher).child(postkey)
        commentReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val likeCount:Int = p0.childrenCount.toInt()
                holder.comments.setText("$likeCount comments")
            }
            override fun onCancelled(p0: DatabaseError){
            }
        })

    }



}

private fun RequestCreator.into(postImage: VideoView) {

}
