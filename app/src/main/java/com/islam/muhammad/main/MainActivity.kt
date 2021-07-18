package com.islam.muhammad.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.islam.muhammad.R
import com.islam.muhammad.fragments.*
import com.islam.muhammad.post.post_main_activity
import kotlinx.android.synthetic.main.fragment_home.*

class MainActivity : AppCompatActivity(){
    var checkMembership:String? = "null"



    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener{
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
                    //Toast.makeText(this, "Working this feature!!\n Coming very soon.", Toast.LENGTH_SHORT).show()
                }
//                R.id.nav_add_post -> {
//
//                    if(checkMembership == "true"){
//                        item.isChecked =false
//                        //startActivity(Intent(this@MainActivity, AddPostActivity::class.java))
//                        startActivity(Intent(this@MainActivity, post_main_activity::class.java))
//                        return@OnNavigationItemSelectedListener true
//                    }else{
//                        //Toast.makeText(this, "You don't have premium membership.", Toast.LENGTH_SHORT).show()
//                        Toast.makeText(this, "Your Don't Have Premium Membership", Toast.LENGTH_SHORT).show()
//                    }
//
//                }
                R.id.nav_membership -> {
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

        val getData = object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                checkMembership = snapshot.child("membership").getValue().toString()
            }
        }

        val reference = FirebaseDatabase.getInstance().reference.child("Users").
                            child(FirebaseAuth.getInstance().currentUser!!.uid)
        reference.addValueEventListener(getData)


        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        moveToFragment(HomeFragment())
    }


    private fun moveToFragment(fragment: Fragment){
        val fragmentTrans = supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.fragment_container, fragment)
        fragmentTrans.commit()
    }



}