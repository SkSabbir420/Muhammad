package com.islam.muslimdream.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.islam.muslimdream.main.MainActivity
import com.islam.muslimdream.R
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        sing_log_in.setOnClickListener {
            val intent = Intent(this, SingUp::class.java)
            startActivity(intent)
        }

        button.setOnClickListener {
            val intent = Intent(this, ForgetPassword::class.java)
            startActivity(intent)
        }

        save_log_in.setOnClickListener {
            doLogin()
        }

    }//first curly.

    private fun doLogin() {
        if (email_log_in.text.toString().isEmpty()) {
            email_log_in.error = "Please Enter Your Email"
            email_log_in.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email_log_in.text.toString()).matches()) {
            email_log_in.error = "Please Enter Valid Email Address"
            email_log_in.requestFocus()
            return
        }
        if (password_log_in.text.toString().isEmpty()) {
            password_log_in.error = "Please Enter Your Password"
            password_log_in.requestFocus()
            return
        }
        mAuth.signInWithEmailAndPassword(email_log_in.text.toString(), password_log_in.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(this,
                        "Please Check your Email Address or Password \n\t\t\tor Internet Connection",Toast.LENGTH_LONG).show()
                }
            }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            if (currentUser.isEmailVerified) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

}//last curly.