package com.example.tictactoegame

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.*

class MainActivity : AppCompatActivity() {

    var mode: String = "nePlayerMode"

    lateinit var activePlayer: Player

    lateinit var playerX: PlayerX
    lateinit var playerO: PlayerO

    //lateinit var ticTacToeGame: TicTacToeGame

    lateinit var tvScorePlayerX: TextView
    lateinit var tvScorePlayerO: TextView

    private val positiveButtonClick = { dialog: DialogInterface, which: Int ->
        resetGame()
    }
    private val negativeButtonClick = { dialog: DialogInterface, which: Int ->

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvScorePlayerX = findViewById(R.id.tv_score_x)
        tvScorePlayerO = findViewById(R.id.tv_score_o)

        playerX = PlayerX()
        playerO = PlayerO()
        activePlayer = playerX

        updateViewsScore()

    }

    fun btnClick(view: View) {
        val btn: Button = view as Button

        if (mode.equals("onePlayerMode", true))
            playOnePlayer(btn)
        else
            playTwoPlayers(btn)
    }

    fun playAgain(view: View) {
        resetGame()
    }

    private fun playTwoPlayers(btn: Button) {
        makeMove(btn)
    }

    private fun playOnePlayer(btn: Button) {
        if (makeMove(btn))  //found winner
            return
        if (autoMakeMove())
            changeRole()
    }

    private fun makeMove(btn: Button): Boolean {
        val btnId = getBtnNum(btn)

        activePlayer.plays(btn, btnId)

        if (activePlayer.isWon(btnId)) {
            celebrateWinner()
            return true //play finished
        }

        changeRole()
        return false
    }


    private fun autoMakeMove(): Boolean {
        val emptyTiles = mutableListOf<Int>()

        for (i in 1..9) {
            if (!(playerO.tilesPlayed.contains(i) || playerX.tilesPlayed.contains(i)))
                emptyTiles.add(i)
        }

        val randomIndex = Random().nextInt(emptyTiles.size) //non-negative number less than size
        val btnId = emptyTiles[randomIndex]

        val btn = getButtonByNumber(btnId)

        return makeMove(btn!!)

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

    private fun updateViewsScore() {

        tvScorePlayerO.text = playerO.score.toString()
        tvScorePlayerO.setTextColor(ContextCompat.getColor(this, playerO.color))

        tvScorePlayerX.text = playerX.score.toString()
        tvScorePlayerX.setTextColor(ContextCompat.getColor(this, playerX.color))

    }

    private fun celebrateWinner() {
        activePlayer.score++
        updateViewsScore()
        showAlertDialog()
    }

    private fun showAlertDialog() {

        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogCustom))

        with(builder)
        {
            setTitle("Congratulations!")
            setMessage("Player ${activePlayer.role} Won!")
            setPositiveButton("Play again", positiveButtonClick)
            setNegativeButton("Ok", negativeButtonClick)
            show()
        }

    }

}