package com.islam.muhammad.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.*
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.islam.muhammad.model.User
import com.islam.muhammad.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.islam.muhammad.model.VideoPost
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class VideoPostAdapter(private val mContext:Context, private val mPost: List<VideoPost>):RecyclerView.Adapter<VideoPostAdapter.ViewHolder>(){

    private  var firebaseUser:FirebaseUser? = null
    private var mVideoView: VideoView? = null
    //private var mBufferingTextView: TextView? = null

    inner class  ViewHolder(@NonNull itemView: View):RecyclerView.ViewHolder(itemView){
        var profileImage:CircleImageView
        var description: TextView
        var postImage:VideoView
        var likeButton:ImageView
        var commentButton:ImageView
        var userName: TextView
        var likes: TextView
        var comments: TextView
        //var publisher: TextView
        //var saveButton:ImageView

        init {
            profileImage = itemView.findViewById(R.id.user_profile_image_post)
            description = itemView.findViewById(R.id.post_text_home)
            postImage = itemView.findViewById(R.id.post_video_video)
            mVideoView =postImage
            //mBufferingTextView = itemView.findViewById(R.id.buffering_textview)
            likeButton = itemView.findViewById(R.id.post_image_like_btn)
            commentButton = itemView.findViewById(R.id.post_image_comment_btn)
            comments = itemView.findViewById(R.id.comments)
            userName = itemView.findViewById(R.id.user_name_post)
            likes = itemView.findViewById(R.id.likes)
            //publisher = itemView.findViewById(R.id.publisher)
            //saveButton = itemView.findViewById(R.id.post_save_comment_btn)
        }
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
        val post = mPost[position]

        holder.description.setText(post.getDescription())

//            val mediaController = MediaController(mContext)
//            mediaController.setAnchorView(holder.postImage)
//            val onlineUri:Uri = Uri.parse(post.getPostimage())
//            //val offlineURL:Uri = Uri.parse("android.resource://com.islam.muhammad/${R.raw.funny_video}")
//            holder.postImage.setMediaController(mediaController)
//            holder.postImage.setVideoURI(onlineUri)
//            //holder.postImage.setVideoURI(offlineURL)
//            holder.postImage.requestFocus()
//            //holder.postImage.start()

            val controller = MediaController(mContext)
            controller.setMediaPlayer(mVideoView)
            mVideoView!!.setMediaController(controller)
            val mCurrentPosition = 0
            val videoUri = Uri.parse(post.getPostimage())
            mVideoView!!.setVideoURI(videoUri)
            controller.hide()
            mVideoView!!.setOnPreparedListener {
                if (mCurrentPosition > 0) {
                    mVideoView!!.seekTo(mCurrentPosition)
                } else {
                    mVideoView!!.seekTo(1)
                }
                    //mVideoView!!.start()
            }

        publisherInfo(holder.profileImage,holder.userName,post.getPublisher())


    }

    private fun publisherInfo(profileImage: CircleImageView, userName: TextView,publisherId: String) {
        val userRef = FirebaseDatabase.getInstance().reference.child("Users").child(publisherId)
        userRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    val user = p0.getValue<User>(User::class.java)
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(profileImage)
                    userName.text = user!!.getUsername()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })


    }


}