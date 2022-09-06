package com.example.tictactoegame

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.*

class MainActivity : AppCompatActivity() {

    var mode: String = "onePlayerMode"

    lateinit var activePlayer: Player
    lateinit var playerX: PlayerX
    lateinit var playerO: PlayerO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playerX = PlayerX()
        playerO = PlayerO()
        activePlayer = playerX

    }

    fun btnClick(view: View) {
        val btn: Button = view as Button

        if (mode.equals("onePlayerMode", true))
            onePlayerMode(btn)
        else
            twoPlayersMode(btn)
    }

    fun playAgain(view: View) {
        resetGame()
    }

    private fun twoPlayersMode(btn: Button) {
        playGame(btn)
    }

    private fun onePlayerMode(btn: Button) {
        playGame(btn)
        autoPlay()
    }

    private fun playGame(btn: Button) {
        val btnId = getBtnNum(btn)

        activePlayer.plays(btn, btnId)

        if (activePlayer.isWon(btnId)) {
            Toast.makeText(this, "Player ${activePlayer.role} Won!", Toast.LENGTH_SHORT).show()
        }

        changeRole()
    }


    private fun autoPlay() {
        val emptyTiles = mutableListOf<Int>()

        for (i in 1..9) {
            if (!(playerO.tilesPlayed.contains(i) || playerX.tilesPlayed.contains(i)))
                emptyTiles.add(i)
        }

        val randomIndex = Random().nextInt(emptyTiles.size) //non-negative number less than size
        val btnId = emptyTiles[randomIndex]

        val btn = getButtonByNumber(btnId)

        if (btn != null) {
            playGame(btn)
        }
    }

    private fun resetGame() {
        var btn: Button?
        for (i in 1..9) {
            btn = getButtonByNumber(i)
            btn?.isClickable = true
            btn?.text = ""
            btn?.setBackgroundColor(ContextCompat.getColor(this, R.color.gray))
        }

        playerX.tilesPlayed.clear()
        playerO.tilesPlayed.clear()
    }

    private fun getButtonByNumber(number: Int): Button? {
        return when (number) {
            1 -> findViewById(R.id.btn1)
            2 -> findViewById(R.id.btn2)
            3 -> findViewById(R.id.btn3)
            4 -> findViewById(R.id.btn4)
            5 -> findViewById(R.id.btn5)
            6 -> findViewById(R.id.btn6)
            7 -> findViewById(R.id.btn7)
            8 -> findViewById(R.id.btn8)
            9 -> findViewById(R.id.btn9)
            else -> null
        }
    }

    private fun getBtnNum(btn: Button): Int {
        var btnId: Int = when (btn.id) {
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
        return btnId
    }

    private fun changeRole() {
        activePlayer = if (activePlayer is PlayerX)
            playerO
        else
            playerX
    }


}