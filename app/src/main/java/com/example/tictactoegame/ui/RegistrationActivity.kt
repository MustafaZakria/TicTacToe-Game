package com.example.tictactoegame.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.tictactoegame.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegistrationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private val database = Firebase.database
    private val myRef = database.reference

    lateinit var tvEmail: TextView
    lateinit var tvPassword: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        tvEmail = findViewById(R.id.et_email)
        tvPassword = findViewById(R.id.et_password)

        auth = Firebase.auth
    }

    fun btnRegisterEvent(view: View) {
        registerToFirebase(tvEmail.text.toString(), tvPassword.text.toString())
    }

    private fun registerToFirebase(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){  task ->
                if(task.isSuccessful) {
                    Toast.makeText(this, "registration successful", Toast.LENGTH_SHORT).show()

                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        saveToDatabase(currentUser)
                    }

                    loadMain()
                }
                else {
                    Toast.makeText(this, "registration Failed", Toast.LENGTH_SHORT).show()

                }
            }
    }

    override fun onStart() {
        super.onStart()

        loadMain()
    }

    private fun loadMain() {
        val currentUser = auth.currentUser
        if(currentUser!=null) {
            val i = Intent(this, HomeActivity::class.java)
            i.putExtra("email", currentUser.email)
            i.putExtra("id", currentUser.uid)
            startActivity(i)
        }
    }

    private fun splitString(string: String): String {
        var split = string.split("@")
        return split[0]
    }

    private fun saveToDatabase(currentUser: FirebaseUser) {
        myRef.child("Users").child(splitString(currentUser.email.toString())).child("Request").setValue(currentUser.uid)
    }

}