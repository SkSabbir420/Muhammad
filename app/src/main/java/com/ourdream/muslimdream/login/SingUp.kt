package com.ourdream.muslimdream.login

import android.app.DatePickerDialog
import android.app.ProgressDialog
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
import android.view.View
import android.widget.Toast
import com.ourdream.muslimdream.R
import kotlinx.android.synthetic.main.activity_sing_up.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


class SingUp : AppCompatActivity(){

    private var currentUser: FirebaseUser? = null
    private var selectedImage: Uri? = null
    private  var dateOfBirthu:String? = null
    private  var birthYear:Int? = null
    private var myAuth:FirebaseAuth = FirebaseAuth.getInstance()
    private var myRef = FirebaseDatabase.getInstance().reference
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_up)

        close_sing_up_btn.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        ivImagePerson.setOnClickListener {
            checkPermission()
        }

        buLogin.setOnClickListener {
            SingUpUser()
        }


    }//first curly.
    fun clickDataPicker(view: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in Toast
                dateOfBirthu ="""$dayOfMonth/${monthOfYear + 1}/$year"""
                birthYear = year
                dateOfBirth.setText(dateOfBirthu)
//              Toast.makeText(this, """$dayOfMonth - ${monthOfYear + 1} - $year""", Toast.LENGTH_LONG).show()

            }, year, month, day)
        dpd.show()
    }

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
        val localdate = LocalDate.now()
        val date = DateTimeFormatter.ofPattern("yyyy").format(localdate).toInt()
//        Toast.makeText(this, "${date}", Toast.LENGTH_LONG).show()
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
            dateOfBirth.error="Please Set Your Date of Birth"
            dateOfBirth.requestFocus()
            return
        }
        if(gender_editText.text.toString().isEmpty()){
            gender_editText.error="Please Enter Your Gender."
            gender_editText.requestFocus()
            return
        }
        if(gender_editText.text.toString()=="male"||
            gender_editText.text.toString()=="Male"||
            gender_editText.text.toString()=="Female"||
            gender_editText.text.toString()=="female"){
        }else{
            gender_editText.error="Give Your Current Gender."
            gender_editText.requestFocus()
            return
        }
        if(Religion_editText.text.toString().isEmpty()){
            Religion_editText.error="Please Enter Your Religion."
            Religion_editText.requestFocus()
            return
        }
        if(Religion_editText.text.toString()=="Muslim"||
            Religion_editText.text.toString()=="muslim"||
            Religion_editText.text.toString()=="Christian"||
            Religion_editText.text.toString()=="christian"||
            Religion_editText.text.toString()=="Hindu"||
            Religion_editText.text.toString()=="hindu"){
        }else{
            gender_editText.error="Give Your Current Religion."
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
        if(etPassword.text.toString().length < 6){
            etPassword.error="minimum use 6 character"
            etPassword.requestFocus()
            return
        }
        if(birthYear!!+12 > date){
            Toast.makeText(baseContext,"Sing-Up failed.\nYour age Under 12", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, Login::class.java))
            finish()
        }else{
            myAuth.createUserWithEmailAndPassword(
                etEmail.text.toString(),
                etPassword.text.toString()
            )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user: FirebaseUser? = myAuth.currentUser
                        user!!.sendEmailVerification()
                            .addOnCompleteListener {
                                if (task.isSuccessful) {
                                    SaveImageInFirebase()
                                    startActivity(Intent(this, Login::class.java))
                                    finish()
                                } else {
                                    Toast.makeText(
                                        baseContext,
                                        "Please Check your Internet connection and Try again",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                    } else {
                        Toast.makeText(
                            baseContext,
                            "Sing-Up failed.\nPlease Try again",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

    }

    fun SaveImageInFirebase(){
//        val progressDialog = ProgressDialog(this)
//        progressDialog.setTitle("Adding Account Information")
//        progressDialog.setMessage("Please wait...")
//        progressDialog.show()

        currentUser =myAuth.currentUser
        val storage= FirebaseStorage.getInstance()
        val storgaRef=storage.getReferenceFromUrl("gs://muhammad-be152.appspot.com")
        val imagePath= currentUser!!.uid + ".jpg"
        val ImageRef=storgaRef.child("profilePictures/" + imagePath )
        val localdate = LocalDate.now()
        val localtime  = LocalTime.now()
        val date = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(localdate).toString()
        val time = DateTimeFormatter.ofPattern("HH:mm:ss").format(localtime).toString()

        ImageRef.putFile(selectedImage!!).addOnSuccessListener {
            ImageRef.downloadUrl.addOnSuccessListener { uri ->

                val singupMap = HashMap<String, Any>()
                singupMap["uid"] = currentUser!!.uid
                singupMap["image"] = uri.toString()
                singupMap["email"] = currentUser!!.email!!
                //singupMap["username"] = personName.text.toString().lowercase()
                singupMap["username"] = personName.text.toString()
                singupMap["membership"] = "false"
                singupMap["verified"] = "false"
                singupMap["dateOfBirth"] = dateOfBirthu!!
                singupMap["gender"] = gender_editText.text.toString()
                singupMap["religion"] = Religion_editText.text.toString()
                singupMap["createDate"] = date
                singupMap["followers"] = 0
                singupMap["following"] = 0
                singupMap["createTime"] = time

                Toast.makeText(this,"Please check your Email Box and Follow link to Verify your Email address.",Toast.LENGTH_LONG).show()
                //singupMap["bio"] = "Edit Profile and Enter your Bio"

                myRef.child("users").child(currentUser!!.uid).updateChildren(singupMap)

                val UID="YDejNwWKD3RKreUktQWrcpWKpND3"
                currentUser!!.uid.let { it1 ->
                    FirebaseDatabase.getInstance().reference.child("follow").
                    child(it1.toString()).child("following").child(UID)
                        .setValue(true)
                }

                currentUser!!.uid.let { it1 ->
                    FirebaseDatabase.getInstance().reference
                        .child("follow").child(UID)
                        .child("followers").child(it1.toString())
                        .setValue(true)
                }

//                progressDialog.dismiss()
//                val intent = Intent(this, Login::class.java)
                val intent = Intent(this, NotificationForConformationMail::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}//last curly.