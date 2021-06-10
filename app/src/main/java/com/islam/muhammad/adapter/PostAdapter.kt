package com.islam.muhammad.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.islam.muhammad.model.Post
import com.islam.muhammad.model.User
import com.islam.muhammad.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.islam.muhammad.main.PostCommentActivity
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.posts_layout.view.*
import kotlinx.android.synthetic.main.posts_layout.*

class PostAdapter(private val mContext:Context,private val mPost: List<Post>):
    RecyclerView.Adapter<PostAdapter.ViewHolder>(){

    private  var firebaseUser:FirebaseUser? = null
    private  var userid:String? = null
    private  var postKey:String? = null
    private  var likeReference:DatabaseReference? = null
    private  var testClick:Boolean =false


    inner class  ViewHolder(@NonNull itemView:View):RecyclerView.ViewHolder(itemView){

        var userName:TextView
        var profileImage:CircleImageView
        var description:TextView
        var postImage:ImageView

        var likeButton:ImageView
        var likes:TextView

        var commentButton:ImageView
        var comments: TextView
        var date:TextView
        var post_progress_bar:ProgressBar
//        var adView: AdView
        //var publisher: TextView
        //var saveButton:ImageView

        init {
            userName = itemView.findViewById(R.id.user_name_post)
            profileImage = itemView.findViewById(R.id.user_profile_image_post)
            description = itemView.findViewById(R.id.post_text_home)
            postImage = itemView.findViewById(R.id.post_image_home)
            date = itemView.findViewById(R.id.txt_post_date)
            likeButton = itemView.findViewById(R.id.post_image_like_btn)
            likes = itemView.findViewById(R.id.post_image_like_text)
            post_progress_bar = itemView.findViewById(R.id.post_progressbar)

            commentButton = itemView.findViewById(R.id.post_image_comment_btn)
            comments = itemView.findViewById(R.id.post_image_comment_text)
//            adView = itemView.findViewById(R.id.adView)
            //publisher = itemView.findViewById(R.id.publisher)
            //saveButton = itemView.findViewById(R.id.post_save_comment_btn)

            itemView.post_image_like_btn.setOnClickListener {
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
                        }
                    }

                    /*override fun onDataChange(p0: DataSnapshot) {
                        if (testClick ==true){
                            if(p0.child(postKey!!).child("like").hasChild(userid!!)){
                                likeReference!!.child(postKey!!).child("like").child(userid!!).removeValue()
                                testClick =false
                            }else{
                                likeReference!!.child(postKey!!).child("like").child(userid!!).setValue(true)
                                testClick = false
                            }
                        }
                    }*///update

                    override fun onCancelled(p0: DatabaseError){

                    }
                })
            }
            itemView.post_image_comment_btn.setOnClickListener {
                val  position:Int = adapterPosition
                val post = mPost[position]
                postKey = post.getPostid()
                val intent = Intent(mContext, PostCommentActivity::class.java)
                intent.putExtra("key",postKey)
                mContext.startActivity(intent)
            }
            itemView.post_image_share_btn.setOnClickListener {
                val  position:Int = adapterPosition
                val post = mPost[position]
                val currentUrl = post.getPostimage()
                val title = post.getDescription()

                val i = Intent(Intent.ACTION_SEND)
                i.type = "text/plain"
                i.putExtra(Intent.EXTRA_TEXT, "Post Description:\n$title\n\nPhoto Link: $currentUrl")
                mContext.startActivity(Intent.createChooser(i, "Share this photo Uri with"))

            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.posts_layout,parent,false)
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
//        Toast.makeText(mContext,"$position",Toast.LENGTH_SHORT).show()
//        holder.post_progress_bar.visibility = View.VISIBLE
        publisherInfo(holder.profileImage,holder.userName,post.getPublisher())
        holder.description.setText(post.getDescription())
        holder.date.setText(post.getPostDate())

        //Picasso.get().load(post.getPostimage()).into(holder.postImage)
        Glide.with(mContext).load(post.getPostimage()).listener(object: RequestListener<Drawable>{
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                holder.post_progress_bar.visibility = View.GONE
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                holder.post_progress_bar.visibility = View.GONE
                return false
            }
        }).into(holder.postImage)

        getLikeButtonStatus(postKey!!, userid!!,holder)
        getcommentButtonStatus(postKey!!,holder)

//        MobileAds.initialize(mContext)
//        val adRequest = AdRequest.Builder().build()
//        //Picasso.get().load(adRequest!!).into(holder.adView)
//        holder.adView.loadAd(adRequest)

    }

    private fun publisherInfo(profileImage: CircleImageView, userName: TextView,publisherId: String) {
        val userRef = FirebaseDatabase.getInstance().reference.child("Users").child(publisherId)
        userRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    val user = p0.getValue<User>(User::class.java)
                    userName.text = user!!.getUsername()
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(profileImage)

                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    private  fun getLikeButtonStatus(postKey:String,userid:String,holder: ViewHolder){
        likeReference = FirebaseDatabase.getInstance().getReference("likes")
        //likeReference = FirebaseDatabase.getInstance().getReference("Post")//update
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

    private  fun getcommentButtonStatus(postkey:String,holder: ViewHolder){
//        val commentReference = FirebaseDatabase.getInstance().reference.child("Post").child(postkey).child("Comments")
        val commentReference = FirebaseDatabase.getInstance().reference.child("Comments").child(postkey)
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