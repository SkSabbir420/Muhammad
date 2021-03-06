package com.ourdream.muslimdream.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.ourdream.muslimdream.R
import com.ourdream.muslimdream.adapter.VideoPostAdapter
import com.ourdream.muslimdream.model.VideoPost
import com.ourdream.muslimdream.model.VideoPostTitle
import kotlinx.android.synthetic.main.activity_show_my_all_video_post.*
import java.util.ArrayList

class ShowMyAllVideoPost : AppCompatActivity() {
    private  var postAdapter: VideoPostAdapter? = null
    private  var postList:MutableList<VideoPost>? = null
    private  var postListTitle:MutableList<VideoPostTitle>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_my_all_video_post)

        all_video_back.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        var recyclerView: RecyclerView? = null
        recyclerView = findViewById(R.id.recycler_view_video_all_post)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd =true


        recyclerView.layoutManager = linearLayoutManager

        postList = ArrayList()
        postListTitle = ArrayList()
        postAdapter = this.let { VideoPostAdapter(it,postList as ArrayList<VideoPost>) }

        recyclerView.adapter =postAdapter

        retrievePosts()

    }

    override fun onResume(){
        super.onResume()
        shimmerFrameLayout_video_all_post.startShimmerAnimation()
    }

    override fun onPause() {
        shimmerFrameLayout_video_all_post.stopShimmerAnimation()
        super.onPause()
    }






    private fun retrievePosts() {
        //val userId = FirebaseAuth.getInstance().currentUser!!.uid
        var postsRef: DatabaseReference? = null
        postsRef = FirebaseDatabase.getInstance().reference.child("postVideos").child("allUsers")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("uploadVideoPost")

        postsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                //postList?.clear()
                for (snapshot in p0.children ){
                    val post = snapshot.getValue(VideoPost::class.java)
//                    for (id in (followingList as ArrayList<String>)){
//                        if (post!!.getPublisher() == id){
                    try {
                        shimmerFrameLayout_video_all_post.stopShimmerAnimation()
                        shimmerFrameLayout_video_all_post.visibility = View.GONE
                        recycler_view_video_all_post.visibility = View.VISIBLE
                    }catch (e:Exception){ }
//                            Toast.makeText(context,"retrieve post",Toast.LENGTH_SHORT).show()
                    postList!!.add(post!!)
//                        }
                    postAdapter!!.notifyDataSetChanged()
//                    }
                }
//                if(p0.children == null){
//                shimmerFrameLayout_video_all_post.stopShimmerAnimation()
//                shimmerFrameLayout_video_all_post.visibility = View.GONE
//                my_video_notice.visibility = View.VISIBLE
//                }
                shimmerFrameLayout_video_all_post.stopShimmerAnimation()
                shimmerFrameLayout_video_all_post.visibility = View.GONE

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })


    }

}