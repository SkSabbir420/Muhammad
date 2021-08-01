package com.islam.muslimdream.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.islam.muslimdream.R
import com.islam.muslimdream.adapter.TopIdAdapter
import com.islam.muslimdream.model.Top
import kotlinx.android.synthetic.main.fragment_top_view.*
import java.util.*

class TopViewFragment : Fragment() {
    //private  var postAdapter: ? = null
    private  var userAdapter:TopIdAdapter? = null
    private  var userList:MutableList<Top>? = null
   // private  var profileId:String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_top_view, container, false)

        var recyclerView: RecyclerView? = null
        recyclerView = view.findViewById(R.id.recycler_view_top_user)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd =true
        recyclerView.layoutManager = linearLayoutManager
        userList = ArrayList()

        userAdapter = context?.let{
            TopIdAdapter(it, userList as ArrayList<Top>)
        }
        recyclerView.adapter =userAdapter
        retrievePosts()
        //getFollowers()
        //getFollowings()

        return view
    }

    override fun onResume() {
        super.onResume()
        shimmerFrameLayout.startShimmerAnimation()
    }

    override fun onPause() {
        shimmerFrameLayout.stopShimmerAnimation()
        super.onPause()
    }

    private fun retrievePosts() {
        val postsRef = FirebaseDatabase.getInstance().reference.child("users")
        postsRef.orderByChild("followers").addValueEventListener(object : ValueEventListener {//Sort use
        //postsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                userList?.clear()
                for (snapshot in p0.children) {
                    val Topid = snapshot.getValue(Top::class.java)
                    //for (id in (followingList as ArrayList<String>)) {
                        //if (post!!.getPublisher() == id) {
                            try {
                                shimmerFrameLayout.stopShimmerAnimation()
                                shimmerFrameLayout.visibility = View.GONE
                                recycler_view_top_user.visibility = View.VISIBLE
                            }catch (e:Exception){ }
                            userList!!.add(Topid!!)
                    //profileId = Topid.getUID()
                       // }
                    userAdapter!!.notifyDataSetChanged()
                    //}
                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

//    private fun getFollowers()
//    {
//        val followersRef = FirebaseDatabase.getInstance().reference
//            .child("Follow").child(profileId!!)
//            .child("Followers")
//
//        followersRef.addValueEventListener(object : ValueEventListener
//        {
//            override fun onDataChange(p0: DataSnapshot)
//            {
//                if (p0.exists())
//                {
//                    //view?.total_followers_top_id.text = p0.childrenCount.toString()
//                    view?.total_followers_top?.text = p0.childrenCount.toString()
//                }
//            }
//
//            override fun onCancelled(p0: DatabaseError) {
//
//            }
//        })
//    }
//
//
//    private fun getFollowings()
//    {
//        val followersRef = FirebaseDatabase.getInstance().reference
//            .child("Follow").child(profileId!!)
//            .child("Following")
//
//
//        followersRef.addValueEventListener(object : ValueEventListener
//        {
//            override fun onDataChange(p0: DataSnapshot)
//            {
//                if (p0.exists())
//                {
//                    //view?.total_following_top_id.text = p0.childrenCount.toString()
//                    view?.total_following_top?.text = p0.childrenCount.toString()
//                }
//            }
//
//            override fun onCancelled(p0: DatabaseError) {
//
//            }
//        })
//    }
}