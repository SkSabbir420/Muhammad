package com.islam.muhammad.fragments


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.islam.muhammad.R
import com.islam.muhammad.adapter.VideoPostAdapter
import com.islam.muhammad.adapter.VideoPostAdapterProfileTitle
import com.islam.muhammad.model.Top
import com.islam.muhammad.model.VideoPost
import com.islam.muhammad.model.VideoPostTitle
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.shimmerFrameLayout_home
import kotlinx.android.synthetic.main.fragment_top__view.*
import kotlinx.android.synthetic.main.fragment_video.*
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class NotificationsFragment : Fragment(){

    private  var postAdapter: VideoPostAdapter? = null
    private  var postAdapterTitle: VideoPostAdapterProfileTitle? = null
    private  var postList:MutableList<VideoPost>? = null
    private  var postListTitle:MutableList<VideoPostTitle>? = null
    private  var followingList:MutableList<VideoPost>? = null

//    val intent: Intent = intent
//    val postkey = intent.getStringExtra("key")!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {



        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_video, container, false)
        var recyclerView: RecyclerView? = null
        var recyclerViewtitle: RecyclerView? = null


        recyclerView = view.findViewById(R.id.recycler_view_video)
        recyclerViewtitle = view.findViewById(R.id.recycler_view_video_title)

        val linearLayoutManager = LinearLayoutManager(context)
        val linearLayoutManagerTitle = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        linearLayoutManager.reverseLayout = true
        linearLayoutManagerTitle.reverseLayout = true
        linearLayoutManager.stackFromEnd =true
        linearLayoutManagerTitle.stackFromEnd =true

        recyclerView.layoutManager = linearLayoutManager
        recyclerViewtitle.layoutManager = linearLayoutManagerTitle

        postList = ArrayList()
        postListTitle = ArrayList()
        postAdapter = context?.let { VideoPostAdapter(it,postList as ArrayList<VideoPost>) }
        postAdapterTitle = context?.let { VideoPostAdapterProfileTitle(it,postListTitle as ArrayList<VideoPostTitle>) }

        recyclerView.adapter =postAdapter
        recyclerViewtitle.adapter =postAdapterTitle

        checkFollowings()

        return view

    }
//    override fun onDestroy() {
//        super.onDestroy()
//        try {
//            val  sharedPreferences:SharedPreferences= context!!.getSharedPreferences("sharePrefs",0)
//            val editor = sharedPreferences.edit()
//            editor.apply {
//                editor.clear()
//            }.apply()
//        }catch (e:Exception){
//
//        }
//
//    }
    override fun onResume(){
        super.onResume()
        shimmerFrameLayout_video.startShimmerAnimation()
    }

    override fun onPause() {
        shimmerFrameLayout_video.stopShimmerAnimation()
        super.onPause()
    }

    private fun checkFollowings() {
        followingList = ArrayList()
        val followingRef = FirebaseDatabase.getInstance().reference
                .child("Follow").child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("Following")
        followingRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    (followingList as ArrayList<String>).clear()
                    for (snapshot in p0.children ){
                        snapshot.key?.let { (followingList as ArrayList<String>).add(it) }
                    }

                    retrievePosts()
                    retrieveProfileTitle()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private  fun retrieveProfileTitle(){
        val postsRef = FirebaseDatabase.getInstance().reference.child("Users")
        postsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
//                userList?.clear()
                for (snapshot in p0.children) {
                    val Topid = snapshot.getValue(VideoPostTitle::class.java)
//                    for (id in (followingList as ArrayList<String>)) {
//                    //if (post!!.getPublisher() == id) {
                    shimmerFrameLayout_video.stopShimmerAnimation()
                    shimmerFrameLayout_video.visibility = View.GONE
                    recycler_view_video_title.visibility = View.VISIBLE
                    postListTitle!!.add(Topid!!)
                    //profileId = Topid.getUID()
                    // }
                    postAdapterTitle!!.notifyDataSetChanged()
                    //}
                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun retrievePosts() {
        //val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val  sharedPreferences:SharedPreferences= context!!.getSharedPreferences("sharePrefs",0)
        val userId = sharedPreferences.getString("keyTitle",FirebaseAuth.getInstance().currentUser!!.uid)
        val postsRef = FirebaseDatabase.getInstance().reference.child("VideoPost").child(userId!!)
        val editor = sharedPreferences.edit()
                        editor.apply {
                            editor.clear()
                        }.apply()
        postsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                //postList?.clear()
                for (snapshot in p0.children ){
                    val post = snapshot.getValue(VideoPost::class.java)
//                    for (id in (followingList as ArrayList<String>)){
//                        if (post!!.getPublisher() == id){
                            shimmerFrameLayout_video.stopShimmerAnimation()
                            shimmerFrameLayout_video.visibility = View.GONE
                            recycler_view_video.visibility = View.VISIBLE
//                            Toast.makeText(context,"retrieve post",Toast.LENGTH_SHORT).show()
                            postList!!.add(post!!)
//                        }
                        postAdapter!!.notifyDataSetChanged()
//                    }
                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })


    }


}