package com.islam.muhammad.fragments

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
import com.islam.muhammad.R
import com.islam.muhammad.adapter.PostAdapter
import com.islam.muhammad.adapter.TopIdAdapter
import com.islam.muhammad.model.Post
import com.islam.muhammad.model.Top
import kotlinx.android.synthetic.main.top_item_layout.view.*
import java.util.*

class TopViewFragment : Fragment() {
    //private  var postAdapter: ? = null
    private  var userAdapter:TopIdAdapter? = null
    private  var userList:MutableList<Top>? = null
   // private  var profileId:String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_top__view, container, false)

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

    private fun retrievePosts() {
        val postsRef = FirebaseDatabase.getInstance().reference.child("Users")
        postsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                //userList?.clear()
                for (snapshot in p0.children) {
                    val Topid = snapshot.getValue(Top::class.java)
                    //for (id in (followingList as ArrayList<String>)) {
                        //if (post!!.getPublisher() == id) {
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