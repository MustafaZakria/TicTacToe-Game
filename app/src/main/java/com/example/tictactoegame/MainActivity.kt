package com.example.tictactoegame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun btnClick(view: View) {
        val btn: Button = view as Button

        var BID: Int = when(btn.id) {
            R.id.btn1 -> 1
            R.id.btn2 -> 2
            R.id.btn3 -> 3
            R.id.btn4 -> 4
            R.id.btn5 -> 5
            R.id.btn6 -> 6
            R.id.btn7 -> 7
            R.id.btn8 -> 8
            R.id.btn9 -> 9

            else -> 0
        }

        Toast.makeText(this, "$BID", Toast.LENGTH_SHORT).show()
    }
}