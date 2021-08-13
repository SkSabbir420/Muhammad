package com.islam.muhammad.fragments

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
import com.islam.muhammad.adapter.PostAdapter
import com.islam.muhammad.adapter.TopIdAdapter
import com.islam.muhammad.model.Post
import com.islam.muhammad.model.Top
import com.islam.muhammad.model.User
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_top__view.*
import kotlinx.android.synthetic.main.fragment_top__view.view.*
import kotlinx.android.synthetic.main.top_item_layout.view.*
import java.util.*

class TopViewFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_top__view, container, false)

        view.memberShipSubmision.setOnClickListener {
            val email = textMemberShipEmail.text.toString().trim(){it <= ' '}
            if (email.isEmpty()){
                Toast.makeText(context,"Please Enter your Email Address", Toast.LENGTH_SHORT).show()
            }else{
                setMemberShip(email)
            }
        }
        view.verifiedSubmision.setOnClickListener {
            val email = textMemberShipEmail.text.toString().trim(){it <= ' '}
            if (email.isEmpty()){
                Toast.makeText(context,"Please Enter your Email Address", Toast.LENGTH_SHORT).show()
            }else{
                setVerified(email)
            }
        }
//        view.memberShipSubmisionRemove.setOnClickListener {
//            val email = textMemberShipEmail.text.toString().trim(){it <= ' '}
//            if (email.isEmpty()){
//                Toast.makeText(context,"Please Enter your Email Address", Toast.LENGTH_SHORT).show()
//            }else{
//            removeMemberShip(email)
//            }
//        }
//        view.verifiedSubmisionRemove.setOnClickListener {
//            val email = textMemberShipEmail.text.toString().trim(){it <= ' '}
//            if (email.isEmpty()){
//                Toast.makeText(context,"Please Enter your Email Address", Toast.LENGTH_SHORT).show()
//            }else{
//            removeVerified(email)
//            }
//        }

        return view
    }

    private fun  setMemberShip(email:String) {
        val postsRef = FirebaseDatabase.getInstance().reference.child("users")
        postsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot){

                for (snapshot in p0.children) {
                    val user = snapshot.getValue(User::class.java)
                        if (user!!.getEmail() == email) {
                            if(user.getMembership() == "false" ){
                                FirebaseDatabase.getInstance().reference.child("users")
                                    .child(user.getUID()).child("membership").setValue("true")
                                Toast.makeText(context,"Successfully add premium member",Toast.LENGTH_SHORT).show()

                            }else{
                                Toast.makeText(context,"This Email already premium member",Toast.LENGTH_SHORT).show()
                            }
                        }

                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun  setVerified(email:String) {
        val postsRef = FirebaseDatabase.getInstance().reference.child("users")
        postsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot){

                for (snapshot in p0.children) {
                    val user = snapshot.getValue(User::class.java)
                    if (user!!.getEmail() == email) {
                        if(user.getVerified() == "false" ){
                            FirebaseDatabase.getInstance().reference.child("users")
                                .child(user.getUID()).child("verified").setValue("true")
                            Toast.makeText(context,"Successfully add verified",Toast.LENGTH_SHORT).show()

                        }else{
                            Toast.makeText(context,"This Email already verified",Toast.LENGTH_SHORT).show()
                        }
                    }

                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun  removeMemberShip(email:String) {
        val postsRef = FirebaseDatabase.getInstance().reference.child("users")
        postsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot){

                for (snapshot in p0.children) {
                    val user = snapshot.getValue(User::class.java)
                    if (user!!.getEmail() == email) {
                        if(user.getMembership() == "true"){
//                            FirebaseDatabase.getInstance().reference.child("users")
//                                .child(user.getUID()).child("membership").removeValue()
                            FirebaseDatabase.getInstance().reference.child("users")
                                .child(user.getUID()).child("membership").setValue("false" )
                            Toast.makeText(context,"Successfully remove premium member",Toast.LENGTH_SHORT).show()

                        }else{
                            Toast.makeText(context,"This Email already not premium member",Toast.LENGTH_SHORT).show()
                        }
                    }

                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun  removeVerified(email:String) {
        val postsRef = FirebaseDatabase.getInstance().reference.child("users")
        postsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot){

                for (snapshot in p0.children) {
                    val user = snapshot.getValue(User::class.java)
                    if (user!!.getEmail() == email) {
                        if(user.getVerified() ==  "true"){
//                            FirebaseDatabase.getInstance().reference.child("users")
//                                .child(user.getUID()).child("verified").removeValue()
                            FirebaseDatabase.getInstance().reference.child("users")
                                .child(user.getUID()).child("verified").setValue("false")
                            Toast.makeText(context,"Successfully remove from verified",Toast.LENGTH_SHORT).show()

                        }else{
                            Toast.makeText(context,"This Email already not verified",Toast.LENGTH_SHORT).show()
                        }
                    }

                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }


}