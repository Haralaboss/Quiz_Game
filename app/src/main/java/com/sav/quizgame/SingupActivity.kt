package com.sav.quizgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.sav.quizgame.databinding.ActivitySingupBinding

class SingupActivity : AppCompatActivity() {

    lateinit var signupBinding : ActivitySingupBinding
    val auth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        signupBinding = ActivitySingupBinding.inflate(layoutInflater)
        val view = signupBinding.root
        setContentView(view)

        signupBinding.buttonSignUp.setOnClickListener {

            val email = signupBinding.editTextSignUpEmail.text.toString()
            val password = signupBinding.editTextSignUpPassword.text.toString()

            signupWithFirebase(email, password)
        }

        signupBinding.buttonCancel.setOnClickListener {
            finish()
        }
    }

    fun signupWithFirebase(email : String, password : String){

        signupBinding.buttonSignUp.isClickable = false

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {task ->

            if(task.isSuccessful){
                Toast.makeText(applicationContext, "Your account has been created", Toast.LENGTH_SHORT).show()
                finish()
                signupBinding.buttonSignUp.isClickable = true
            }
            else{
                Toast.makeText(applicationContext, task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()
            }
        }

    }

}