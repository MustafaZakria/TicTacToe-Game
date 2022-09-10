package com.example.tictactoegame.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.tictactoegame.R

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    fun playOfflineOnePlayer(view: View) {
        val value = "onePlayerMode"

        intentToGameActivity("mode", value)

    }

    fun playOfflineTwoPlayers(view: View) {
        val value = "twoPlayersMode"

        intentToGameActivity("mode", value)
    }

    fun playOnline(view: View) {
        val value = "onlineMode"

        intentToGameActivity("mode", value)
    }

    private fun intentToGameActivity(key: String, value: String) {
        val i = Intent(this, MainActivity::class.java)
        i.putExtra(key, value)
        startActivity(i)
    }
}