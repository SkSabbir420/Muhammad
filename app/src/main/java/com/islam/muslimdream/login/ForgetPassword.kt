package com.islam.muslimdream.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.islam.muslimdream.R
import kotlinx.android.synthetic.main.activity_forget_password.*

class ForgetPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        fogetSubmitbtn.setOnClickListener {
            val email = textForgetEmail.text.toString().trim(){it <= ' '}
            if (email.isEmpty()){
                Toast.makeText(this,"Please Enter your Email Address",Toast.LENGTH_SHORT).show()
            }else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task->
                        if(task.isSuccessful){
                            Toast.makeText(this,"Email sent is successfully\nfor Reset your password",Toast.LENGTH_SHORT).show()
                            finish()
                        }else{
                            Toast.makeText(this,task.exception!!.message.toString(),Toast.LENGTH_SHORT).show()
                        }

                    }
            }
        }
    }
}