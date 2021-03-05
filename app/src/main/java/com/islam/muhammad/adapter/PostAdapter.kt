package com.islam.muhammad.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.islam.muhammad.model.Post
import com.islam.muhammad.model.User
import com.islam.muhammad.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class PostAdapter(private val mContext:Context,
                  private val mPost: List<Post>): RecyclerView.
                  Adapter<PostAdapter.ViewHolder>(){

    private  var firebaseUser:FirebaseUser? = null
//    private  var likeReference:DatabaseReference? = null
//    private  var testClick:Boolean =false
    private  var userid:String? = null
    private  var postKey:String? = null

    inner class  ViewHolder(@NonNull itemView: View):RecyclerView.ViewHolder(itemView){
        var profileImage:CircleImageView
        var description: TextView
        var postImage:ImageView
//        var likes: TextView
//        var likeButton:ImageView

//        var commentButton:ImageView
        var userName: TextView
//        var comments: TextView
        //var publisher: TextView
        //var saveButton:ImageView

        init {
            profileImage = itemView.findViewById(R.id.user_profile_image_post)
            description = itemView.findViewById(R.id.post_text_home)
            postImage = itemView.findViewById(R.id.post_image_home)
//            likeButton = itemView.findViewById(R.id.post_image_like_btn)
//            commentButton = itemView.findViewById(R.id.post_image_comment_btn)
//            comments = itemView.findViewById(R.id.comments)
            userName = itemView.findViewById(R.id.user_name_post)
//            likes = itemView.findViewById(R.id.post_image_like_text)
            //publisher = itemView.findViewById(R.id.publisher)
            //saveButton = itemView.findViewById(R.id.post_save_comment_btn)
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
        holder.description.setText(post.getDescription())
        Picasso.get().load(post.getPostimage()).into(holder.postImage)
        publisherInfo(holder.profileImage,holder.userName,post.getPublisher())
//        getLikeButtonStatus(postKey!!, userid!!,holder)

//        holder.likeButton.setOnClickListener{
//            testClick =true
//            likeReference!!.addValueEventListener(object :ValueEventListener{
//                override fun onDataChange(p0: DataSnapshot) {
//                    if (testClick ==true){
//                        if(p0.child(postKey!!).hasChild(userid!!)){
//                            likeReference!!.child(postKey!!).removeValue()
//                            testClick =false
//                        }else{
//                            likeReference!!.child(postKey!!).child(userid!!).setValue(true)
//                            testClick = false
//                        }
//                    }
//                }
//
//                override fun onCancelled(p0: DatabaseError){
//
//                }
//            })
//
//        }


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

//    private  fun getLikeButtonStatus(postKey:String,userid:String,holder: ViewHolder){
//        likeReference = FirebaseDatabase.getInstance().getReference("likes")
//        likeReference!!.addValueEventListener(object :ValueEventListener{
//            override fun onDataChange(p0: DataSnapshot) {
//                if (p0.child(postKey).hasChild(userid)){
//                    var likeCount:Int = p0.child(postKey).childrenCount.toInt()
//                    holder.likes.setText("$likeCount likes")
//                    holder.likeButton.setImageResource(R.drawable.ic_baseline_favorite_24)
//                }else{
//                    var likeCount:Int = p0.child(postKey).childrenCount.toInt()
//                    holder.likes.setText("$likeCount likes")
//                    holder.likeButton.setImageResource(R.drawable.ic_baseline_favorite_border_24)
//                }
//            }
//
//            override fun onCancelled(p0: DatabaseError){
//
//            }
//        })
//    }


}