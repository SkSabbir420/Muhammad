package com.ourdream.muslimdream.fragments


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

import com.ourdream.muslimdream.R
import com.ourdream.muslimdream.adapter.VideoPostAdapter
import com.ourdream.muslimdream.adapter.VideoPostAdapterProfileTitle
import com.ourdream.muslimdream.main.VideoSearchActivity
import com.ourdream.muslimdream.model.VideoPost
import com.ourdream.muslimdream.model.VideoPostTitle
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
    private  var sharedPreferences:SharedPreferences? = null
    private  var userId:String? = null

//    val intent: Intent = intent
//    val postkey = intent.getStringExtra("key")!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {



        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_video, container, false)

        val videoSearchButton= view.findViewById<ImageView>(R.id.video_search)
        videoSearchButton.setOnClickListener {
            val intent = Intent(activity, VideoSearchActivity::class.java)
            activity?.startActivity(intent)
            //activity?.finish()
        }


        var recyclerView: RecyclerView? = null
        var recyclerViewtitle: RecyclerView? = null
        val arr:Array<String> = arrayOf("Allah","Nabi","Others")
        val ran = (0..2).random()
        val defValue = arr[ran]
//        Toast.makeText(context,defValue,Toast.LENGTH_SHORT).show()

        //val defValue = "Allah"
        sharedPreferences = requireContext().getSharedPreferences("sharePrefs",0)
        userId = sharedPreferences!!.getString("keyTitle",defValue)


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
                .child("follow").child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("following")
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
        val postsRef = FirebaseDatabase.getInstance().reference.child("users")
        postsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
//                userList?.clear()
                for (snapshot in p0.children) {
                    val Topid = snapshot.getValue(VideoPostTitle::class.java)
                    for (id in (followingList as ArrayList<String>)) {
                    if (Topid!!.getUID() == id) {
                        try {
                            shimmerFrameLayout_video.stopShimmerAnimation()
                            shimmerFrameLayout_video.visibility = View.GONE
                            recycler_view_video_title.visibility = View.VISIBLE
                        }catch (e:Exception){ }

                    postListTitle!!.add(Topid!!)
                    //profileId = Topid.getUID()
                     }
                    postAdapterTitle!!.notifyDataSetChanged()
                    }
                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun retrievePosts() {
        //val userId = FirebaseAuth.getInstance().currentUser!!.uid
        var postsRef:DatabaseReference? = null
        if(userId == "Allah"||userId =="Nabi"||userId =="Others"){
            postsRef = FirebaseDatabase.getInstance().reference.child("postVideos").child(userId!!)
        }else{
            postsRef = FirebaseDatabase.getInstance().reference.child("postVideos")
                .child("allUsers").child(userId!!).child("uploadVideoPost")
        }
        val editor = sharedPreferences!!.edit()
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
                            try {
                                shimmerFrameLayout_video.stopShimmerAnimation()
                                shimmerFrameLayout_video.visibility = View.GONE
                                recycler_view_video.visibility = View.VISIBLE
                            }catch (e:Exception){ }
//                            Toast.makeText(context,"retrieve post",Toast.LENGTH_SHORT).show()
                            postList!!.add(post!!)
//                        }
                        postAdapter!!.notifyDataSetChanged()
//                    }
                }
//                if(p0.children == null) {
//                    shimmerFrameLayout_video.stopShimmerAnimation()
//                    shimmerFrameLayout_video.visibility = View.GONE
//                    video_notice.visibility = View.VISIBLE
//                }
                shimmerFrameLayout_video.stopShimmerAnimation()
                shimmerFrameLayout_video.visibility = View.GONE



            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })


    }


}