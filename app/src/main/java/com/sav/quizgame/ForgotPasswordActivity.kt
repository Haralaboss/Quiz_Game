package com.sav.quizgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.sav.quizgame.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var forgotBinding : ActivityForgotPasswordBinding

    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        forgotBinding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view = forgotBinding.root
        setContentView(view)

        forgotBinding.buttonForgotPassword.setOnClickListener {

            val userEmail = forgotBinding.editTextEmailForgot.text.toString()

            auth.sendPasswordResetEmail(userEmail).addOnCompleteListener { task->

                if(task.isSuccessful){
                    Toast.makeText(applicationContext, "We sent you a mail to reset your password",Toast.LENGTH_SHORT).show()
                    finish()
                }
                else{
                    Toast.makeText(applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }

        forgotBinding.buttonCancelForgotPass.setOnClickListener {
            finish()
        }

    }
}