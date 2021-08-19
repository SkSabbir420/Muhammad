package com.ourdream.muslimdream.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ourdream.muslimdream.R
import com.ourdream.muslimdream.main.MainActivity
import com.ourdream.muslimdream.model.User
import com.ourdream.muslimdream.model.VideoPostTitle
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.video_post_title.view.*


class VideoPostAdapterProfileTitle(private val mContext:Context, private val mPost: List<VideoPostTitle>):RecyclerView.Adapter<VideoPostAdapterProfileTitle.ViewHolder>(){

    private  var userUid:String? = null

    inner class  ViewHolder(@NonNull itemView: View):RecyclerView.ViewHolder(itemView){

        var profileImageTitle:CircleImageView
        var profileNameTile:TextView

        init {
            profileImageTitle = itemView.findViewById(R.id.user_profile_image_post_title)
            profileNameTile = itemView.findViewById(R.id.profile_username_title)

            itemView.user_profile_image_post_title.setOnClickListener {
                val  position:Int = adapterPosition
                val post = mPost[position]
                userUid = post.getUID()

                val sharedPreferences:SharedPreferences = mContext.getSharedPreferences("sharePrefs",0)
                val editor = sharedPreferences.edit()
                editor.apply {
                    putString("keyTitle", userUid)
                }.apply()
                try {
                    val intent = Intent(mContext, MainActivity::class.java)
                    mContext.startActivity(intent)
                }catch (e:Exception){}

//                Toast.makeText(mContext,"$userUid",Toast.LENGTH_SHORT).show()

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.video_post_title,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mPost.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val post = mPost[position]
        holder.profileNameTile.setText(post.getUsername())
        Picasso.get().load(post.getImage()).into(holder.profileImageTitle)
//        publisherInfo(holder.profileImageTitle,post.getPublisher())

    }

    private fun publisherInfo(profileImage: CircleImageView, publisherId: String) {
        val userRef = FirebaseDatabase.getInstance().reference.child("users").child(publisherId)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    val user = p0.getValue<User>(User::class.java)
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(profileImage)
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

}
