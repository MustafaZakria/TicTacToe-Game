package com.example.tictactoegame.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.tictactoegame.*
import com.example.tictactoegame.classes.OfflineOnePlayerGame
import com.example.tictactoegame.classes.OfflineTwoPlayersGame
import com.example.tictactoegame.classes.Player
import com.example.tictactoegame.classes.TicTacToeGame

class MainActivity : AppCompatActivity() {

    lateinit var ticTacToeGame: TicTacToeGame
    lateinit var mode: String

    lateinit var tvScorePlayerX: TextView
    lateinit var tvScorePlayerO: TextView

    lateinit var btnPlayAgain: Button

    private val positiveButtonClick = { dialog: DialogInterface, which: Int ->
        playAgain()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvScorePlayerX = findViewById(R.id.tv_score_x)
        tvScorePlayerO = findViewById(R.id.tv_score_o)

        mode = getGameMode()
        ticTacToeGame = if (mode.equals("onePlayerMode", true))
            OfflineOnePlayerGame()
        else
            OfflineTwoPlayersGame()

        btnPlayAgain = findViewById(R.id.btn_play_again)
        btnPlayAgain.setOnClickListener {
            playAgain()
        }

        updateViewsScore()
    }

    private fun playAgain() {
        var btn: Button?
        for (i in 1..9) {
            btn = getButtonByNumber(i)
            btn?.isClickable = true
            btn?.text = ""
            btn?.setBackgroundColor(ContextCompat.getColor(this, R.color.gray))
        }

        ticTacToeGame.resetGame()
    }

    fun btnClick(view: View) {
        val btn: Button = view as Button

        val tileNumber = getTileNum(btn)

        val winner: Player? = ticTacToeGame.playGame(tileNumber)

        colorTiles()

        if( winner!=null) {
            celebrateWinner(winner)
        }

    }

    private fun colorTiles() {
        var player: Player = ticTacToeGame.playerX
        var tileNumber: Int = player.tilesPlayed.last()
        var btn = getButtonByNumber(tileNumber)



        for(i in 0..1) {
            btn?.isClickable = false
            btn?.text = player.role
            btn?.setBackgroundColor(ContextCompat.getColor(this, player.color))

            player = ticTacToeGame.playerO
            if(player.tilesPlayed.isEmpty()) {
                break
            }
            tileNumber = player.tilesPlayed.last()
            btn = getButtonByNumber(tileNumber)
        }
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

    private fun getTileNum(btn: Button): Int {
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


    private fun updateViewsScore() {

        val playerX = ticTacToeGame.playerX
        val playerO = ticTacToeGame.playerO

        tvScorePlayerO.text = playerO.score.toString()
        tvScorePlayerO.setTextColor(ContextCompat.getColor(this, playerO.color))

        tvScorePlayerX.text = playerX.score.toString()
        tvScorePlayerX.setTextColor(ContextCompat.getColor(this, playerX.color))

    }

    private fun celebrateWinner(winner: Player) {
        updateViewsScore()
        showAlertDialog(winner)
    }

    private fun showAlertDialog(winner: Player) {

        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogCustom))

        with(builder)
        {
            setTitle("Congratulations!")
            setMessage("Player ${winner.role} Won!")
            setPositiveButton("Play again", positiveButtonClick)
            setNegativeButton("Ok",null)
            show()
        }

    }

    fun getGameMode(): String {
        var mode: String = ""
        val extras = intent.extras

        if (extras != null) {
            mode = extras.getString("mode").toString()
        }
        return mode
    }

}