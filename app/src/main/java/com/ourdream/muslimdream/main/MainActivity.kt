package com.ourdream.muslimdream.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.ourdream.muslimdream.R
import com.ourdream.muslimdream.fragments.*
import com.ourdream.muslimdream.post.VerifiedAccountNotification
import com.ourdream.muslimdream.post.post_main_activity
import kotlinx.android.synthetic.main.fragment_home.*

class MainActivity : AppCompatActivity(){

    var verification:String? = "null"

    private val onNavigationItemSelectedListener = BottomNavigationView.
    OnNavigationItemSelectedListener{
            item -> when (item.itemId) {
                R.id.nav_profile -> {
                    moveToFragment(ProfileFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_home -> {
                    moveToFragment(HomeFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_search -> {
//                    moveToFragment(AdvertiseFragment())
                    moveToFragment(NotificationsFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_add_post -> {

                    if(verification == "true"){
                        item.isChecked =false
                        //startActivity(Intent(this@MainActivity, AddPostActivity::class.java))
                        startActivity(Intent(this@MainActivity, post_main_activity::class.java))
                        return@OnNavigationItemSelectedListener true
                    }else if(verification == "false"){
                        startActivity(Intent(this@MainActivity, VerifiedAccountNotification::class.java))
                        return@OnNavigationItemSelectedListener true
                        //Toast.makeText(this, "Your Account Does not Verified", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this, "Something Wrong!", Toast.LENGTH_SHORT).show()
                    }

                }
                R.id.nav_notifications -> {
                    //Toast.makeText(this, "Working this feature!!\n Coming very soon.", Toast.LENGTH_SHORT).show()
                    moveToFragment(TopViewFragment())
                    return@OnNavigationItemSelectedListener true
                }

            }

        false
    }
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //fanUpdate()//Need cloud function

        val getData = object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                verification = snapshot.child("verified").getValue().toString()
            }
        }

        val reference = FirebaseDatabase.getInstance().reference.child("users").
                            child(FirebaseAuth.getInstance().currentUser!!.uid)
        reference.addValueEventListener(getData)


        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        moveToFragment(NotificationsFragment())
    }


    private fun moveToFragment(fragment: Fragment){
        val fragmentTrans = supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.fragment_container, fragment)
        fragmentTrans.commit()
    }
    private fun fanUpdate(){
        val profileId = FirebaseAuth.getInstance().currentUser!!.uid
        val followingRef = FirebaseDatabase.getInstance().reference
            .child("follow").child(profileId)
            .child("following")
        followingRef.addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(p0: DataSnapshot)
            {
                if (p0.exists())
                {
                    FirebaseDatabase.getInstance().reference.child("users")
                        .child(profileId).child("followers").setValue(p0.childrenCount)

                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })

        val followersRef = FirebaseDatabase.getInstance().reference
            .child("follow").child(profileId)
            .child("followers")

        followersRef.addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(p0: DataSnapshot)
            {
                if (p0.exists())
                {
                    FirebaseDatabase.getInstance().reference.child("users")
                        .child(profileId).child("following").setValue(p0.childrenCount)


                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }



}