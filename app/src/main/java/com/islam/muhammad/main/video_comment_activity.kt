package com.islam.muhammad.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.islam.muhammad.R
import com.islam.muhammad.adapter.VideoPostCommentAdapter
import com.islam.muhammad.model.VideoPostComment
import kotlinx.android.synthetic.main.activity_video_comment.*
import java.util.ArrayList

class video_comment_activity : AppCompatActivity() {

    private  var postAdapter: VideoPostCommentAdapter? = null
    private  var postList:MutableList<VideoPostComment>? = null
    private var postkey:String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_comment)
        val intent: Intent = intent
        postkey = intent.getStringExtra("key")!!


        var recyclerView: RecyclerView? = null
        recyclerView = findViewById(R.id.recycler_view_comment_video)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd =true
        recyclerView.layoutManager = linearLayoutManager
        postList = ArrayList()

        postAdapter = this.let { VideoPostCommentAdapter(it, postList as ArrayList<VideoPostComment>) }
        recyclerView.adapter =postAdapter

        retrievePosts()


        close_add_video_commet_btn.setOnClickListener {
            super.onBackPressed()
            finish()
        }
        save_comment_video.setOnClickListener {
            uploadComment()
            //Toast.makeText(this,"successfully add your comment ",Toast.LENGTH_SHORT).show()
            description_comment_video.text.clear()
        }

    }

    private fun uploadComment() {
        val ref = FirebaseDatabase.getInstance().reference.child("VideoPost").child("$postkey").child("Comments")
        val postId = ref.push().key

        val postMap = HashMap<String, Any>()
        postMap["postid"] = postId!!
        postMap["description"] = description_comment_video.text.toString().toLowerCase()
        postMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid
        ref.child(postId).updateChildren(postMap)
    }


    private fun retrievePosts() {
        val postsRef = FirebaseDatabase.getInstance().reference.child("VideoPost").child("$postkey").child("Comments")
        postsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                postList?.clear()
                for (snapshot in p0.children) {
                    val post = snapshot.getValue(VideoPostComment::class.java)
                    postList!!.add(post!!)
                    postAdapter!!.notifyDataSetChanged()
                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}