package com.islam.muhammad.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import androidx.core.app.ActivityCompat

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Patterns
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import java.text.SimpleDateFormat
import com.islam.muhammad.R
import kotlinx.android.synthetic.main.activity_account_settings.*
import kotlinx.android.synthetic.main.activity_sing_up.*
import java.util.*


class SingUp : AppCompatActivity(){

    private var currentUser: FirebaseUser? = null
    private var myAuth:FirebaseAuth = FirebaseAuth.getInstance()
    private var myRef = FirebaseDatabase.getInstance().reference
    private var selectedImage: Uri? = null
    //private var gender:String? = "custom"


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_up)

        close_sing_up_btn.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        add_profile_pic_btn.setOnClickListener{
            checkPermission()
        }
        buLogin.setOnClickListener {
            //gender = onRadioButtonClicked(it)
            //Toast.makeText(this,gender,Toast.LENGTH_SHORT).show()
            SingUpUser()


        }


    }//first curly.

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            READIMAGE->{
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    loadImage()
                }else{
                    Toast.makeText(applicationContext,"Cannot access your images", Toast.LENGTH_LONG).show()
                }
            }
            else-> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
    val READIMAGE:Int=253
    fun checkPermission(){
        if(Build.VERSION.SDK_INT>=23){
            if(ActivityCompat.checkSelfPermission(this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf( android.Manifest.permission.READ_EXTERNAL_STORAGE),READIMAGE)
                return
            }
        }
        loadImage()
    }
    val PICK_IMAGE_CODE=123
    fun loadImage(){
        val intent= Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==PICK_IMAGE_CODE  && data!=null && resultCode == RESULT_OK){
            selectedImage=data.data
            val filePathColum= arrayOf(MediaStore.Images.Media.DATA)
            val cursor= contentResolver.query(selectedImage!!,filePathColum,null,null,null)
            cursor!!.moveToFirst()
            val coulomIndex=cursor.getColumnIndex(filePathColum[0])
            val picturePath=cursor.getString(coulomIndex)
            cursor.close()
            ivImagePerson.setImageBitmap(BitmapFactory.decodeFile(picturePath))
        }
    }


    private fun SingUpUser(){
        if(personName.text.toString().isEmpty()){
            personName.error="Please Enter Your Name."
            personName.requestFocus()
            return
        }
        if(dateOfBirth.text.toString().isEmpty()){
            dateOfBirth.error="Please Enter Your Date of birth."
            dateOfBirth.requestFocus()
            return
        }
        if(gender_editText.text.toString().isEmpty()){
            gender_editText.error="Please Enter Your Gender."
            gender_editText.requestFocus()
            return
        }
        if(etEmail.text.toString().isEmpty()){
            etEmail.error="Please Enter Your Email."
            etEmail.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString()).matches()){
            etEmail.error="Please Enter Valid Email."
            etEmail.requestFocus()
            return
        }
        if(etPassword.text.toString().isEmpty()){
            etPassword.error="Please Enter Your Email."
            etPassword.requestFocus()
            return
        }
        myAuth.createUserWithEmailAndPassword(etEmail.text.toString(),etPassword.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user: FirebaseUser?=myAuth.currentUser
                        user!!.sendEmailVerification()
                                .addOnCompleteListener {
                                    if (task.isSuccessful) {
                                        SaveImageInFirebase()
                                        startActivity(Intent(this, Login::class.java))
                                        finish()
                                    }
                                }

                    } else {
                        Toast.makeText(baseContext,"singup faild.try after sumtime", Toast.LENGTH_SHORT).show()
                    }
                }

    }
    fun SaveImageInFirebase(){
        currentUser =myAuth.currentUser
        val email:String=currentUser!!.email.toString()
        val storage= FirebaseStorage.getInstance()
        val storgaRef=storage.getReferenceFromUrl("gs://muhammad-be152.appspot.com")
        val df= SimpleDateFormat("ddMMyyHHmmss")
        val dataobj= Date()
        val imagePath= currentUser!!.uid + ".jpg"
        val ImageRef=storgaRef.child("profileImages/"+imagePath )
        ImageRef.putFile(selectedImage!!).addOnSuccessListener {
            ImageRef.downloadUrl.addOnSuccessListener { uri ->
                val imagestore = FirebaseDatabase.getInstance().reference.child("Users").child(currentUser!!.uid)
                val hashMap = HashMap<String, String>()
                hashMap["image"] = uri.toString()
                imagestore.setValue(hashMap).addOnSuccessListener {
                }
                myRef.child("Users").child(currentUser!!.uid).child("email").setValue(currentUser!!.email)
                myRef.child("Users").child(currentUser!!.uid).child("uid").setValue(currentUser!!.uid)
                myRef.child("Users").child(currentUser!!.uid).child("bio").setValue("Edit Profile and Enter your Bio.")
                myRef.child("Users").child(currentUser!!.uid).child("username").setValue(personName.text.toString().toLowerCase())
                myRef.child("Users").child(currentUser!!.uid).child("fullname").setValue(personName.text.toString().toLowerCase())
                myRef.child("Users").child(currentUser!!.uid).child("dateOfBirth").setValue(dateOfBirth.text.toString())
                myRef.child("Users").child(currentUser!!.uid).child("password").setValue(etPassword.text.toString())
                myRef.child("Users").child(currentUser!!.uid).child("gender").setValue(gender_editText.text.toString())
                LoadTweets()
            }
        }
    }
    fun SplitString(email:String):String{
        val split= email.split("@")
        return split[0]
    }
//    override fun onStart() {
//        super.onStart()
//        LoadTweets()
//    }
    fun LoadTweets(){
        var currentUser =myAuth.currentUser
        if(currentUser!=null) {
            var intent = Intent(this, Login::class.java)
            intent.putExtra("email", currentUser.email)
            intent.putExtra("uid", currentUser.uid)
            startActivity(intent)
        }
    }

//    fun onRadioButtonClicked(view: View):String? {
//        var result:String? = null
//        if (view is RadioButton) {
//            // Is the button now checked?
//            val checked = view.isChecked
//
//            // Check which radio button was clicked
//            when (view.getId()) {
//                R.id.radio_male ->
//                    if (checked) {
//                        result ="male"
//                        // Pirates are the best
//
//                    }
//                R.id.radio_female ->
//                    if (checked) {
//                        result = "female"
//                        //myRef.child("Users").child(currentUser!!.uid).child("gender").setValue("female")
//                        // Ninjas rule
//                    }
//            }
//        }
//        return result
//    }

}//last curly.