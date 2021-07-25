package com.islam.muhammad.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.islam.muhammad.setting.AccountSettingsActivity
import com.islam.muhammad.model.User

import com.islam.muhammad.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.islam.muhammad.login.Login
import com.islam.muhammad.main.ShowMyAllPicturePost
import com.islam.muhammad.main.ShowMyAllVideoPost
import com.islam.muhammad.main.ShowSaveVideoPost
import com.islam.muhammad.main.VideoSearchActivity
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ProfileFragment : Fragment() {
    private lateinit var profileId: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        profileId = FirebaseAuth.getInstance().currentUser!!.uid

        view.show_save_all_post.setOnClickListener {
            val intent = Intent(activity, ShowSaveVideoPost::class.java)
            activity?.startActivity(intent)
            //activity?.finish()
        }
        view.show_my_all_video_post.setOnClickListener {
            val intent = Intent(activity, ShowMyAllVideoPost::class.java)
            activity?.startActivity(intent)
            //activity?.finish()
        }
        view.show_my_all_picture_post.setOnClickListener {
            val intent = Intent(activity, ShowMyAllPicturePost::class.java)
            activity?.startActivity(intent)
            //activity?.finish()
        }
        view.logout_btn.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View?) {
                try {
                    //Toast.makeText(context,"Logout", Toast.LENGTH_LONG).show()
                    FirebaseAuth.getInstance().signOut()
                    val intent= Intent(activity, Login::class.java)
                    activity?.startActivity(intent)
                    activity?.finish()
                }catch (e:Exception){}
            }
        })
        view.edit_account_settings_btn.setOnClickListener {
            startActivity(Intent(context, AccountSettingsActivity::class.java))
        }
        userInfo()
        getFollowers()
        getFollowings()

        return view
    }

//    private fun checkFollowAndFollowingButtonStatus() {
//        val followingRef = firebaseUser?.uid.let { it1 ->
//            FirebaseDatabase.getInstance().reference
//                .child("Follow").child(it1.toString())
//                .child("Following")
//        }
//
//        if (followingRef != null) {
//            followingRef.addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(p0: DataSnapshot) {
//                    if (p0.child(profileId).exists()) {
//                        view?.edit_account_settings_btn?.text = "Following"
//                    } else {
//                        view?.edit_account_settings_btn?.text = "Follow"
//                    }
//                }
//
//                override fun onCancelled(p0: DatabaseError) { }
//            })
//        }
//    }


    private fun getFollowers()
    {
        val followersRef = FirebaseDatabase.getInstance().reference
                .child("follow").child(profileId)
                .child("followers")

        followersRef.addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(p0: DataSnapshot)
            {
                if (p0.exists())
                {
                    view?.total_followers?.text = p0.childrenCount.toString()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }


    private fun getFollowings()
    {
        val followersRef = FirebaseDatabase.getInstance().reference
                .child("follow").child(profileId)
                .child("following")


        followersRef.addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(p0: DataSnapshot)
            {
                if (p0.exists())
                {
                    view?.total_following?.text = p0.childrenCount.toString()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }


    private fun userInfo() {
        val usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(profileId)

        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    val user = p0.getValue<User>(User::class.java)
                    try {
                        Picasso.get().load(user!!.getImage()).into(view!!.pro_image_profile_frag)
                    }catch(e:Exception){ }
                    view?.full_name_profile_frag?.text = user!!.getUsername()
                    //view?.bio_profile_frag?.text = user.getBio()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }


    override fun onStop() {
        super.onStop()

        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", profileId)
        pref?.apply()
    }

    override fun onPause() {
        super.onPause()

        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", profileId)
        pref?.apply()
    }

    override fun onDestroy() {
        super.onDestroy()

        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", profileId)
        pref?.apply()
    }
}

private fun RequestCreator.into(proImageProfileFrag: View?) {

}
