package com.ourdream.muslimdream.post

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.ourdream.muslimdream.R
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_account_settings.*
import kotlinx.android.synthetic.main.activity_add_post_photo.*
import kotlinx.android.synthetic.main.activity_add_post_video.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap

class AddPostVideoActivity : AppCompatActivity(){

    companion object {
        const val notificationId = 1513
        const val CHANNEL_ID = "content_loading"
        const val CHANNEL_NAME = "Content Loading"
        const val ACTION_START = "com.akexorcist.service.start"
    }

    private var myUrl = ""
    private var myUrlPhoto = ""
    private var imageUri: Uri? = null
    private var imageUriframe: Uri? = null
    private var storagePostPicRef: StorageReference? = null
    private var postCategory:String? =null
    val userId = FirebaseAuth.getInstance().currentUser!!.uid


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post_video)

        val intent: Intent = intent
        postCategory = intent.getStringExtra("keyCategory")
        //Toast.makeText(this, "$postCategory", Toast.LENGTH_LONG).show()

        createNotificationChannel()


        storagePostPicRef = FirebaseStorage.getInstance().reference.child("postVideos")

        save_new_post_video_btn.setOnClickListener {
            uploadImage()
        }
        close_add_post_video_btn.setOnClickListener {
            super.onBackPressed()
            finish()
        }
        video_post_frame.setOnClickListener {
            CropImage.activity()
                .setAspectRatio(16, 9)
                .start(this@AddPostVideoActivity)

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
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode == Activity.RESULT_OK  &&  data != null)
        {
            val result = CropImage.getActivityResult(data)
            imageUriframe = result.uri
//            video_post_frame.setImageURI(imageUriframe)
        }
    }

    private fun uploadImage(){
        if(description_post_video.text.toString().isEmpty()){
            description_post_video.error="Please Write your description"
            description_post_video.requestFocus()
            return
        }else if (imageUriframe == null){
            Toast.makeText(this, "Please add your cover photo", Toast.LENGTH_LONG).show()
            return
        }else if(imageUri ==null){
            Toast.makeText(this, "Please add your video", Toast.LENGTH_LONG).show()
            return
        }else{

//            val progressDialog = ProgressDialog(this)
//            progressDialog.setTitle("Adding your Post")
//            progressDialog.setMessage("Please wait...")
//            progressDialog.show()

//            val notification = NotificationCompat
//                .Builder(this, "content_loading")
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setContentText("Please wait")
//                .setContentTitle("Uploading")
//                .setTicker("Uploading")
//                .setProgress(0, 100, true)
//                //.setAutoCancel(true)
//                .setDefaults(NotificationCompat.DEFAULT_ALL)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//
//            val no: NotificationManagerCompat = NotificationManagerCompat.from(this)
//            no.notify(1,notification.build())

            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                //.setSmallIcon(R.drawable.notification_icon)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Uploading")
                .setContentText("Please wait")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setProgress(0, 100, true)
                // Set the intent that will fire when the user taps the notification
                //.setContentIntent(pendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(notificationId, builder.build())
            }

            super.onBackPressed()


                //val ref = FirebaseDatabase.getInstance().reference.child("postVideoTemporary")
                val ref = FirebaseDatabase.getInstance().reference.child("postVideos")
                val postId = ref.push().key
                ///val fileRef = storagePostPicRef!!.child(System.currentTimeMillis().toString() + ".jpg")
                val fileRef = storagePostPicRef!!.child(userId).child(postId!!).child(postId.toString() + ".mp4")
                val fileRefPhoto = storagePostPicRef!!.child(userId).child(postId).child(postId.toString() + ".jpg")

                var uploadTask: StorageTask<*>
                var uploadTaskPhoto:StorageTask<*>
                uploadTask = fileRef.putFile(imageUri!!)
                uploadTaskPhoto = fileRefPhoto.putFile(imageUriframe!!)

                uploadTaskPhoto.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{ task ->
                    if (!task.isSuccessful){
                        task.exception?.let {
                            throw it
                        }
                    }
                    return@Continuation fileRefPhoto.downloadUrl
                }).addOnCompleteListener (OnCompleteListener<Uri> { task ->
                    if (task.isSuccessful)
                    {
                        val downloadUrl = task.result
                        myUrlPhoto = downloadUrl.toString()
                        //Toast.makeText(this,myUrlPhoto,Toast.LENGTH_SHORT).show()

                    }
                } )



                uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{ task ->
                    if (!task.isSuccessful)
                    {
                        task.exception?.let {
                            throw it
//                            progressDialog.dismiss()
                        }
                    }
                    return@Continuation fileRef.downloadUrl
                }).addOnCompleteListener (OnCompleteListener<Uri> { task ->
                    if (task.isSuccessful){
                        val downloadUrl = task.result
                        myUrl = downloadUrl.toString()

                        ///val ref = FirebaseDatabase.getInstance().reference.child("Post")

                        val localdate = LocalDate.now()
                        val localtime  = LocalTime.now()
                        val date = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(localdate).toString()
                        val time = DateTimeFormatter.ofPattern("HH:mm:ss").format(localtime).toString()



                        val postMap = HashMap<String, Any>()
                        postMap["postid"] = postId
                        //postMap["description"] = description_post.text.toString().toLowerCase()
                        postMap["description"] = description_post_video.text.toString()
                        postMap["publisher"] = userId
                        postMap["postimage"] = myUrl
                        postMap["postDate"] = date
                        postMap["postTime"] = time
                        postMap["coverPhoto"] = myUrlPhoto
                        postMap["postCategory"] = postCategory!!


                        //FirebaseDatabase.getInstance().reference.child("postVideoViews").child(userId).
                        //child(postId).child("videoViews").setValue(0)
                        //FirebaseDatabase.getInstance().getReference("VPView").child(postId).child("postView").setValue(0)

//                        ref.child(userId).child(postId).updateChildren(postMap)
//                        ref.child(postCategory!!).child(postId).updateChildren(postMap)
//                        FirebaseDatabase.getInstance().getReference("postVideoViews").child(postId).child("postView").setValue(0)
//                        ref.child(userId).child(postId).updateChildren(postMap)

                        //ref.child(postId).updateChildren(postMap)//final
                        FirebaseDatabase.getInstance().reference.child("postVideos").child("allUsers")
                            .child(userId).child("uploadVideoPost")
                            .child(postId).updateChildren(postMap)
                        FirebaseDatabase.getInstance().reference.child("postVideos").child(postCategory!!)
                            .child(postId).updateChildren(postMap)
                        FirebaseDatabase.getInstance().reference.child("postVideos").child("allPostVideos")
                            .child(postId).updateChildren(postMap)
                        FirebaseDatabase.getInstance().reference.child("postVideoViews").
                        child(postId).child("postView").setValue(0)






//                        progressDialog.dismiss()

                        builder.setContentTitle("Upload Complete")
                            .setContentText("Congratulation")
                            .setProgress(0,0,false)
                            .setOngoing(false)

                        with(NotificationManagerCompat.from(this)) {
                            // notificationId is a unique int for each notification that you must define
                            notify(notificationId, builder.build())
                        }

                        //Toast.makeText(this, "Post upload successfully.\nWait for verification", Toast.LENGTH_LONG).show()
//                        Toast.makeText(this, "Post upload successfully", Toast.LENGTH_LONG).show()

                        finish()
                    }else{
                        Toast.makeText(this,"Post upload Unsuccessfully.", Toast.LENGTH_LONG).show()
                        super.onBackPressed()

//                        notification.setContentTitle("Upload Unsuccessfull")
//                            .setContentText("Try Again")
//                            .setProgress(0,0,false)
//                            .setOngoing(false)
//                        no.notify(1,notification.build())

                        builder.setContentTitle("Upload Unsuccessfull")
                            .setContentText("Try Again")
                            .setProgress(0,0,false)
                            .setOngoing(false)
                        with(NotificationManagerCompat.from(this)) {
                            // notificationId is a unique int for each notification that you must define
                            notify(notificationId, builder.build())
                        }

//                        progressDialog.dismiss()
                    }
                } )
        }
    }


    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}