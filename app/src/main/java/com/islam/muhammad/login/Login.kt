package com.islam.muhammad.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.islam.muhammad.main.MainActivity
import com.islam.muhammad.R
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        sing_log_in.setOnClickListener {
            val intent= Intent(this, SingUp::class.java)
            startActivity(intent)
        }

        save_log_in.setOnClickListener{
            doLogin()
        }

    }//first curly.

    private fun doLogin(){
        if(email_log_in.text.toString().isEmpty()){
            email_log_in.error="Please Enter Your Email."
            email_log_in.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email_log_in.text.toString()).matches()){
            email_log_in.error="Please Enter Valid Email."
            email_log_in.requestFocus()
            return
        }
        if(password_log_in.text.toString().isEmpty()){
            password_log_in.error="Please Enter Your Email."
            password_log_in.requestFocus()
            return
        }
        auth.signInWithEmailAndPassword(email_log_in.text.toString(), password_log_in.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {

                    updateUI(null)
                }
            }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private  fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser != null){
            if(currentUser.isEmailVerified) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                Toast.makeText(baseContext, "Please verify your email address.",
                    Toast.LENGTH_SHORT).show()
            }
        }else{
            //Toast.makeText(baseContext, "Login failed.", Toast.LENGTH_SHORT).show()
        }
    }

}//last curly.