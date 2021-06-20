package com.islam.muhammad.fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.islam.muhammad.adapter.PostAdapter
import com.islam.muhammad.model.Post
import com.islam.muhammad.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.islam.muhammad.main.NotificationActivity
import com.islam.muhammad.main.SearchActivity
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_top__view.*
import java.util.*


//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {

    private  var postAdapter:PostAdapter? = null
    private  var postList:MutableList<Post>? = null
    private  var followingList:MutableList<Post>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

//        try{
//            MobileAds.initialize(context)
//            val adRequest = AdRequest.Builder().build()
//            view.adView.loadAd(adRequest)
//        }catch (ex:Exception){
//
//        }

        val searchButton= view.findViewById<ImageView>(R.id.people_search)
        searchButton.setOnClickListener {
            val intent = Intent(activity, SearchActivity::class.java)
            activity?.startActivity(intent)
            //activity?.finish()
        }

        val notificationButton= view.findViewById<ImageView>(R.id.notifications)
        notificationButton.setOnClickListener {
            val intent = Intent(activity, NotificationActivity::class.java)
            activity?.startActivity(intent)
            //activity?.finish()
        }



        var recyclerView:RecyclerView? = null
        recyclerView = view.findViewById(R.id.recycler_view_home)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd =true
        recyclerView.layoutManager = linearLayoutManager
        postList = ArrayList()

//        val options = FirebaseRecyclerOptions.Builder<Post>().setQuery(query,Post::class.java).build()
//        postAdapter = PostAdapter(options,context!!,postList!!)

        postAdapter = context?.let {
            PostAdapter(it, postList as ArrayList<Post>)
        }
        recyclerView.adapter =postAdapter


        checkFollowings()



        return view
    }
    override fun onResume() {
        super.onResume()
        shimmerFrameLayout_home.startShimmerAnimation()
    }

    override fun onPause() {
        shimmerFrameLayout_home.stopShimmerAnimation()
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
                    for (snapshot in p0.children) {
                        snapshot.key?.let { (followingList as ArrayList<String>).add(it) }
                    }
                    retrievePosts()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun retrievePosts() {
        val postsRef = FirebaseDatabase.getInstance().reference.child("Post")
        postsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot){

                //postList?.clear()
                for (snapshot in p0.children) {
                    val post = snapshot.getValue(Post::class.java)
                    for (id in (followingList as ArrayList<String>)) {
                        if (post!!.getPublisher() == id) {
                            try {
                                shimmerFrameLayout_home.stopShimmerAnimation()
                                shimmerFrameLayout_home.visibility = View.GONE
//                               adView.visibility = View.VISIBLE
                                recycler_view_home.visibility = View.VISIBLE
                            }catch (e:Exception){

                            }
                            postList!!.add(post)
                        }
                        postAdapter!!.notifyDataSetChanged()
                    }
                }


            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }


}
