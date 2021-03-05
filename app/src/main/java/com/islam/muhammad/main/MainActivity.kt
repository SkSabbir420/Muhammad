package com.islam.muhammad.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.islam.muhammad.R
import com.islam.muhammad.fragments.*
import kotlinx.android.synthetic.main.fragment_home.*

class MainActivity : AppCompatActivity(){

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
                    moveToFragment(AdvertiseFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_add_post -> {
                    item.isChecked =false
                    startActivity(Intent(this@MainActivity, AddPostActivity::class.java))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_notifications -> {
                    moveToFragment(TopViewFragment())
                    return@OnNavigationItemSelectedListener true
                }
//                R.id.Search_Fragment -> {
//                    moveToFragment(TopViewFragment())
//                    return@OnNavigationItemSelectedListener true
//                }


            }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        moveToFragment(HomeFragment())

//        people_search.setOnClickListener {
//            moveToFragment(SearchFragment())
//        }
    }


    private fun moveToFragment(fragment: Fragment){
        val fragmentTrans = supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.fragment_container, fragment)
        fragmentTrans.commit()
    }



}