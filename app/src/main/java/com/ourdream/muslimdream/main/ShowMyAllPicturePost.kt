package com.ourdream.muslimdream.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ourdream.muslimdream.R
import com.ourdream.muslimdream.adapter.PostAdapter
import com.ourdream.muslimdream.model.Post
import kotlinx.android.synthetic.main.activity_show_my_all_picture_post.*
import java.util.ArrayList

class ShowMyAllPicturePost : AppCompatActivity() {
    private  var postAdapter: PostAdapter? = null
    private  var postList:MutableList<Post>? = null
    private  var followingList:MutableList<Post>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_my_all_picture_post)

        all_picture_back.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        var recyclerView: RecyclerView? = null
        recyclerView = findViewById(R.id.recycler_view_home_allPost)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd =true
        recyclerView.layoutManager = linearLayoutManager
        postList = ArrayList()

//        val options = FirebaseRecyclerOptions.Builder<Post>().setQuery(query,Post::class.java).build()
//        postAdapter = PostAdapter(options,context!!,postList!!)

        postAdapter = this.let {
            PostAdapter(it, postList as ArrayList<Post>)
        }
        recyclerView.adapter =postAdapter


        retrievePosts()

    }
    override fun onResume() {
        super.onResume()
        shimmerFrameLayout_home_allPost.startShimmerAnimation()
    }

    override fun onPause() {
        shimmerFrameLayout_home_allPost.stopShimmerAnimation()
        super.onPause()
    }

    private fun retrievePosts() {
        val postsRef = FirebaseDatabase.getInstance().reference.child("postPictures")
            .child("allUsers").child(FirebaseAuth.getInstance().currentUser!!.uid)
        postsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot){

                //postList?.clear()
                for (snapshot in p0.children) {
                    val post = snapshot.getValue(Post::class.java)
                    //for (id in (followingList as ArrayList<String>)) {
                        //if (post!!.getPublisher() == id) {
                            try {
                                shimmerFrameLayout_home_allPost.stopShimmerAnimation()
                                shimmerFrameLayout_home_allPost.visibility = View.GONE
//                               adView.visibility = View.VISIBLE
                                recycler_view_home_allPost.visibility = View.VISIBLE
                            }catch (e:Exception){

                            }
                            postList!!.add(post!!)
                       // }
                        postAdapter!!.notifyDataSetChanged()
                   // }
                }
//                if(p0.children == null){
//                shimmerFrameLayout_home_allPost.stopShimmerAnimation()
//                shimmerFrameLayout_home_allPost.visibility = View.GONE
////                               adView.visibility = View.VISIBLE
//                my_picture_notice.visibility = View.VISIBLE
//                }
                shimmerFrameLayout_home_allPost.stopShimmerAnimation()
                shimmerFrameLayout_home_allPost.visibility = View.GONE
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }


}
