package com.islam.muhammad.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_sing_up.*
//image
import android.net.Uri
import android.widget.ImageView
//import com.bluapp.kotlinview.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.islam.muhammad.R

class sing_up : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    //for image
    private var imagePreview: ImageView? = null
    private val PICK_IMAGE_REQUEST = 1
    private var filePath: Uri? = null
    private var firebaseStorage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_up)

        auth = FirebaseAuth.getInstance()

        imagePreview = findViewById<ImageView>(R.id.img_sing_up) as ImageView
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        img_sing_up.setOnClickListener {
            ImagePicker()
        }

        save_sing_up.setOnClickListener {
            SingUpUser()
            uploadImage()
        }

        //first end.
    }


    private fun ImagePicker() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if(data == null || data.data == null){
                return
            }
            filePath = data.data
            imagePreview?.setImageURI(filePath)

            /*try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imagePreview?.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }*/
        }
    }

    private fun uploadImage(){
        if(filePath != null){
            val ref = storageReference?.child("uploads/" + UUID.randomUUID().toString())
            ref?.putFile(filePath!!)?.addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> {
                Toast.makeText(this@sing_up, "Image Uploaded", Toast.LENGTH_SHORT).show()
            })?.addOnFailureListener(OnFailureListener { e ->
                Toast.makeText(this@sing_up, "Image Uploading Failed " + e.message, Toast.LENGTH_SHORT).show()
            })
        }else{
            Toast.makeText(this, "Please Select an Image", Toast.LENGTH_SHORT).show()
        }
    }



    private fun SingUpUser(){

        if(name_sing_up.text.toString().isEmpty()){
            name_sing_up.error="Please Enter Your Name."
            name_sing_up.requestFocus()
            return
        }
        if(date_sing_up.text.toString().isEmpty()){
            date_sing_up.error="Please Enter Your Date of birth."
            date_sing_up.requestFocus()
            return
        }

        if(email_sing_up.text.toString().isEmpty()){
            email_sing_up.error="Please Enter Your Email."
            email_sing_up.requestFocus()
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email_sing_up.text.toString()).matches()){
            email_sing_up.error="Please Enter Valid Email."
            email_sing_up.requestFocus()
            return
        }

        if(password_sing_up.text.toString().isEmpty()){
            password_sing_up.error="Please Enter Your Email."
            password_sing_up.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(email_sing_up.text.toString(),password_sing_up.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user: FirebaseUser?=auth.currentUser
                        user!!.sendEmailVerification()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        startActivity(Intent(this, login::class.java))
                                        finish()
                                    }
                                }

                    } else {
                        Toast.makeText(baseContext,"singup faild.try after sumtime", Toast.LENGTH_SHORT).show()
                    }
                }

    }



}