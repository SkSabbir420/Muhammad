package com.islam.muhammad.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.islam.muhammad.R
import com.islam.muhammad.adapter.UserAdapter
import com.islam.muhammad.model.User
import kotlinx.android.synthetic.main.activity_search.*
import java.util.*

class SearchActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var userAdapter: UserAdapter? = null
    private var userArray: MutableList<User>? = null

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        recyclerView = findViewById(R.id.recycler_view_search)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(this)
        userArray = ArrayList()

        userAdapter = this.let{
            UserAdapter(it, userArray as ArrayList<User>, true)
        }
        recyclerView?.adapter = userAdapter

        search_edit_text.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(name: CharSequence?, start: Int, before: Int, count: Int) {
                if (search_edit_text.text.toString() == ""){

                }else{
                    recyclerView?.visibility = View.VISIBLE
                    retrieveUsers()
                    searchUser(name.toString().toLowerCase())
                }
            }
            override fun afterTextChanged(s: Editable?){

            }
        })

    } //First curly.

    private fun searchUser(inputName: String){
        val query = FirebaseDatabase.getInstance().getReference()
                    .child("Users").orderByChild("username")
                    .startAt(inputName).endAt(inputName + "\uf8ff")

        query.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot){
                userArray?.clear()
                for (snapshot in dataSnapshot.children){
                    val user = snapshot.getValue(User::class.java)
                    if (user != null){
                        userArray?.add(user)
                    }
                }
                userAdapter?.notifyDataSetChanged()
            }
            override fun onCancelled(p0: DatabaseError){ }
        })
    }

    private fun retrieveUsers(){
        val usersRef = FirebaseDatabase.getInstance().getReference().child("Users")
        usersRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot){
                if (search_edit_text?.text.toString() == ""){
                    userArray?.clear()
                    for (snapshot in dataSnapshot.children){
                        val user = snapshot.getValue(User::class.java)
                        if (user != null){
                            userArray?.add(user)
                        }
                    }
                    userAdapter?.notifyDataSetChanged()
                }
            }
            override fun onCancelled(p0: DatabaseError) { }
        })
    }


}
