package com.islam.muslimdream.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.islam.muslimdream.R
import com.islam.muslimdream.adapter.VideoPostAdapter
import com.islam.muslimdream.model.VideoPost
import kotlinx.android.synthetic.main.activity_search.*
import java.util.*

class VideoSearchActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var userAdapter: VideoPostAdapter? = null
    private var userArray: MutableList<VideoPost>? = null

    override fun onCreate(savedInstanceState: Bundle?){

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_search)

        recyclerView = findViewById(R.id.recyclerview_video_search)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(this)
        userArray = ArrayList()
        userAdapter = this.let{
            VideoPostAdapter(it, userArray as ArrayList<VideoPost>)
        }
        recyclerView?.adapter = userAdapter

        search_edit_text.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(name: CharSequence?, start: Int, before: Int, count: Int) {
                if (search_edit_text.text.toString() == ""){
                    userArray?.clear()
                    recyclerView?.visibility = View.GONE
                }else{
                    searchUser(name.toString())
                    recyclerView?.visibility = View.VISIBLE
//                    retrieveUsers()

                }
            }
            override fun afterTextChanged(s: Editable?){

            }
        })


    } //First curly.

    private fun searchUser(inputName: String){
        val query = FirebaseDatabase.getInstance().getReference()
                    .child("postVideos").child("allPostVideos").orderByChild("description")
                    .startAt(inputName).endAt(inputName + "\uf8ff")

        query.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot){
                userArray?.clear()
                for (snapshot in dataSnapshot.children){
                    val user = snapshot.getValue(VideoPost::class.java)
                    if (user != null){
                        userArray?.add(user)
                    }
                }
                userAdapter?.notifyDataSetChanged()
            }
            override fun onCancelled(p0: DatabaseError){ }
        })
    }

//    private fun retrieveUsers(){
//        val usersRef = FirebaseDatabase.getInstance().getReference()
//            .child("postVideos").child(FirebaseAuth.getInstance().currentUser!!.uid)
//        usersRef.addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(dataSnapshot: DataSnapshot){
//                if (search_edit_text?.text.toString() == ""){
//                    userArray?.clear()
//                    for (snapshot in dataSnapshot.children){
//                        val user = snapshot.getValue(VideoPost::class.java)
//                        if (user != null){
//                            userArray?.add(user)
//                        }
//                    }
//                    userAdapter?.notifyDataSetChanged()
//                }
//            }
//            override fun onCancelled(p0: DatabaseError) { }
//        })
//    }


}
