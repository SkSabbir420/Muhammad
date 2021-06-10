package com.islam.muhammad.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.islam.muhammad.R
import com.islam.muhammad.model.Top
import com.islam.muhammad.model.User
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.top_item_layout.view.*

class TopIdAdapter(private val mContext:Context,private val mTop:List<Top>):RecyclerView.
                    Adapter<TopIdAdapter.ViewHolder>() {

    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser


    inner class  ViewHolder(@NonNull itemView: View):RecyclerView.ViewHolder(itemView){
        var userName: TextView = itemView.findViewById(R.id.profile_username_top_id)
        var profileImage: CircleImageView= itemView.findViewById(R.id.profile_image_top_id)
        var totalFollowersForTop:TextView= itemView.findViewById(R.id.total_followers_top)
        var totalFollowingForTop: TextView= itemView.findViewById(R.id.total_following_top)
        var followButton:Button =itemView.findViewById(R.id.follow_btn_top_id)
        init {
            itemView.follow_btn_top_id.setOnClickListener {
                val  position:Int = adapterPosition
                val post = mTop[position]
                val UID = post.getUID()

                if(itemView.follow_btn_top_id.text.toString() == "Follow"){

                    firebaseUser?.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("Follow").
                        child(it1.toString()).child("Following").child(UID)
                            .setValue(true)
                    }

                    firebaseUser?.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Follow").child(UID)
                            .child("Followers").child(it1.toString())
                            .setValue(true)
                    }
                }else{

                    firebaseUser?.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Follow").child(it1.toString())
                            .child("Following").child(UID)
                            .removeValue()
                    }

                    firebaseUser?.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Follow").child(UID)
                            .child("Followers").child(it1.toString())
                            .removeValue()
                    }
                }
            }
        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.top_item_layout,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return mTop.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val top = mTop[position]
        val profileId = top.getUID()
        holder.userName.setText(top.getUsername())
        Picasso.get().load(top.getImage()).into(holder.profileImage)
        checkFollowingStatus(top.getUID(), holder.followButton)


        val followingRef = FirebaseDatabase.getInstance().reference
            .child("Follow").child(profileId)
            .child("Following")
        followingRef.addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(p0: DataSnapshot)
            {
                if (p0.exists())
                {
                    holder.totalFollowingForTop.setText( p0.childrenCount.toString())
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })

        val followersRef = FirebaseDatabase.getInstance().reference
            .child("Follow").child(profileId)
            .child("Followers")

        followersRef.addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(p0: DataSnapshot)
            {
                if (p0.exists())
                {
                    holder.totalFollowersForTop.setText( p0.childrenCount.toString())
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })



    }


    private fun checkFollowingStatus(uid: String, followButton: Button){



        val followingRef = firebaseUser?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Follow").child(it1.toString())
                .child("Following")
        }

        followingRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(datasnapshot: DataSnapshot){
                if (datasnapshot.child(uid).exists()){
                    followButton.text = "Following"
                }else{
                    followButton.text = "Follow"
                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }


}