package com.islam.muhammad.main

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.islam.muhammad.R
import kotlinx.android.synthetic.main.activity_add_post_video.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap

class AddPostVideoActivity : AppCompatActivity() {

    private var myUrl = ""
    private var imageUri: Uri? = null
    private var storagePostPicRef: StorageReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post_video)

        storagePostPicRef = FirebaseStorage.getInstance().reference.child("Posts Videos")

        save_new_post_video_btn.setOnClickListener {
            uploadImage()
        }
        close_add_post_video_btn.setOnClickListener {
            super.onBackPressed()
            finish()
        }


        //CropImage.activity().setAspectRatio(2, 1).start(this@AddPostVideoActivity)

        video_post.setOnClickListener {
            loadImage()
        }
//        image_post.setOnClickListener {
//            loadImage()
//        }


            //uploadImage()

    }


//}//extra

    //Load video
    val PICK_IMAGE_CODE=123
    fun loadImage(){
        var intent= Intent(Intent.ACTION_PICK,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==PICK_IMAGE_CODE  && data!=null && resultCode == RESULT_OK){
            imageUri =data.data
        }
    }

    private fun uploadImage(){
        when{
            imageUri ==null -> Toast.makeText(this, "Please select image first.", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(description_post_video.text.toString()) -> Toast.makeText(this, "Please write description first.", Toast.LENGTH_LONG).show()
            else ->{
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Adding new Post")
                progressDialog.setMessage("Please wait, we are adding your post...")
                progressDialog.show()
                val ref = FirebaseDatabase.getInstance().reference.child("VideoPostTemp")
                val postId = ref.push().key
                ///val fileRef = storagePostPicRef!!.child(System.currentTimeMillis().toString() + ".jpg")
                val fileRef = storagePostPicRef!!.child(postId.toString() + ".mp4")

                var uploadTask: StorageTask<*>
                uploadTask = fileRef.putFile(imageUri!!)
                uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{ task ->
                    if (!task.isSuccessful)
                    {
                        task.exception?.let {
                            throw it
                            progressDialog.dismiss()
                        }
                    }
                    return@Continuation fileRef.downloadUrl
                }).addOnCompleteListener (OnCompleteListener<Uri> { task ->
                    if (task.isSuccessful)
                    {
                        val downloadUrl = task.result
                        myUrl = downloadUrl.toString()

                        ///val ref = FirebaseDatabase.getInstance().reference.child("Post")

                        val localdate = LocalDate.now()
                        val localtime  = LocalTime.now()
                        val date = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(localdate).toString()
                        val time = DateTimeFormatter.ofPattern("HH:mm:ss").format(localtime).toString()



                        val postMap = HashMap<String, Any>()
                        postMap["postid"] = postId!!
                        //postMap["description"] = description_post.text.toString().toLowerCase()
                        postMap["description"] = description_post_video.text.toString()
                        postMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid
                        postMap["postimage"] = myUrl
                        postMap["postDate"] = date
                        postMap["postTime"] = time

                        ref.child(postId).updateChildren(postMap)

                        Toast.makeText(this, "Post updated successfully.", Toast.LENGTH_LONG).show()

                        val intent = Intent(this@AddPostVideoActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        progressDialog.dismiss()
                    }
                    else
                    {
                        progressDialog.dismiss()
                    }
                } )
            }
        }
    }
}