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
import android.provider.MediaStore
import android.util.Patterns
import android.widget.Toast
import com.islam.muhammad.R
import kotlinx.android.synthetic.main.activity_sing_up.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


class SingUp : AppCompatActivity(){

    private var currentUser: FirebaseUser? = null
    private var selectedImage: Uri? = null
    private var myAuth:FirebaseAuth = FirebaseAuth.getInstance()
    private var myRef = FirebaseDatabase.getInstance().reference


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
        if(ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf( android.Manifest.permission.READ_EXTERNAL_STORAGE),READIMAGE)
            return
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
        if(selectedImage == null){
            Toast.makeText(this, "Please Add Your Profile Picture", Toast.LENGTH_LONG).show()
            return
        }
        if(personName.text.toString().isEmpty()){
            personName.error="Please Enter Your Name"
            personName.requestFocus()
            return
        }
        if(dateOfBirth.text.toString().isEmpty()){
            dateOfBirth.error="Please Enter Your Date of Birth"
            dateOfBirth.requestFocus()
            return
        }
        if(gender_editText.text.toString().isEmpty()){
            gender_editText.error="Please Enter Your Gender."
            gender_editText.requestFocus()
            return
        }
        if(etEmail.text.toString().isEmpty()){
            etEmail.error="Please Enter Your Email Address"
            etEmail.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString()).matches()){
            etEmail.error="Please Enter Valid Email Address"
            etEmail.requestFocus()
            return
        }
        if(etPassword.text.toString().isEmpty()){
            etPassword.error="Please Enter Your Password"
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
                                    }else{
                                        Toast.makeText(baseContext,"Please Check your Internet connection and Try again", Toast.LENGTH_SHORT).show()
                                    }
                                }

                    } else {
                        Toast.makeText(baseContext,"Sing-Up failed.\nPlease Try again", Toast.LENGTH_SHORT).show()
                    }
                }

    }

    fun SaveImageInFirebase(){

        currentUser =myAuth.currentUser
        val storage= FirebaseStorage.getInstance()
        val storgaRef=storage.getReferenceFromUrl("gs://muhammad-be152.appspot.com")
        val imagePath= currentUser!!.uid + ".jpg"
        val ImageRef=storgaRef.child("profileImages/"+imagePath )
        val localdate = LocalDate.now()
        val localtime  = LocalTime.now()
        val date = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(localdate).toString()
        val time = DateTimeFormatter.ofPattern("HH:mm:ss").format(localtime).toString()

        ImageRef.putFile(selectedImage!!).addOnSuccessListener {
            ImageRef.downloadUrl.addOnSuccessListener { uri ->
                val singupMap = HashMap<String, Any>()
                singupMap["uid"] = currentUser!!.uid
                singupMap["email"] = currentUser!!.email!!
                singupMap["password"] = etPassword.text.toString()
                singupMap["image"] = uri.toString()
                singupMap["username"] = personName.text.toString().lowercase()
                singupMap["dateOfBirth"] = dateOfBirth.text.toString()
                singupMap["gender"] = gender_editText.text.toString()
                singupMap["createDate"] = date
                singupMap["createTime"] = time
                singupMap["bio"] = "Edit Profile and Enter your Bio"
                myRef.child("Users").child(currentUser!!.uid).updateChildren(singupMap)
                LoadTweets()
            }
        }
    }
    fun LoadTweets(){
        val currentUser =myAuth.currentUser
        if(currentUser!=null) {
            val intent = Intent(this, Login::class.java)
            intent.putExtra("email", currentUser.email)
            intent.putExtra("uid", currentUser.uid)
            startActivity(intent)
        }
    }
}//last curly.