package com.example.tictactoegame.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.tictactoegame.R
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {
    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        analytics = Firebase.analytics
    }

    fun playOfflineOnePlayer(view: View) {
        val mode = "onePlayerMode"

        intentToGameActivity(mode)

    }

    fun playOfflineTwoPlayers(view: View) {
        val mode = "twoPlayersMode"

        intentToGameActivity(mode)
    }

    fun playOnline(view: View) {
        val mode = "onlineMode"

        intentToGameActivity(mode)
    }

    private fun intentToGameActivity(value: String) {
        val i = Intent(this, GameActivity::class.java)
        i.putExtra("mode", value)
        i.putExtra("email", getEmail())
        startActivity(i)
    }

    fun getEmail(): String {
        var email = ""
        val extras = intent.extras

        if (extras != null) {
            email = extras.getString("email").toString()
        }
        return email
    }
}