package com.islam.muhammad.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.islam.muhammad.R
import com.islam.muhammad.model.VideoPostComment
import com.islam.muhammad.model.User
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView



class VideoPostCommentAdapter (val mContext: Context, private val mPost: List<VideoPostComment>):
    RecyclerView.Adapter<VideoPostCommentAdapter.ViewHolder>(){

    private  var firebaseUser: FirebaseUser? = null
    private  var userid:String? = null
    private  var likeReference: DatabaseReference? = null
    var postKey:String? = null

    inner class  ViewHolder(@NonNull itemView: View): RecyclerView.ViewHolder(itemView){

        var userName: TextView
        var profileImage: CircleImageView
        var description: TextView
        //var publisher: TextView


        init {
            userName = itemView.findViewById(R.id.user_name_post_comment)
            profileImage = itemView.findViewById(R.id.user_profile_image_post_comment)
            description = itemView.findViewById(R.id.post_comment_text)

            //publisher = itemView.findViewById(R.id.publisher)
//            itemView.setOnClickListener {
//                val  position:Int = adapterPosition
//                val post = mPost[position]
//                postKey = post.getPostid()
//            }






        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.post_comment_layout,parent,false)
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
//
        publisherInfo(holder.profileImage,holder.userName,post.getPublisher())
        holder.description.setText(post.getDescription())

//        Picasso.get().load(post.getPostimage()).into(holder.postImage)
    }



    private fun publisherInfo(profileImage: CircleImageView, userName: TextView, publisherId: String) {
        val userRef = FirebaseDatabase.getInstance().reference.child("users").child(publisherId)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    val user = p0.getValue<User>(User::class.java)
                    userName.text = user!!.getUsername()
                    Picasso.get().load(user.getImage()).placeholder(R.drawable.profile).into(profileImage)

                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }






}