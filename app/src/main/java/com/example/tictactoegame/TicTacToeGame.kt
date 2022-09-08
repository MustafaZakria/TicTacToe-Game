package com.example.tictactoegame

import android.util.Log
import java.util.*

abstract class TicTacToeGame {

    var activePlayer: Player

    var playerX: PlayerX = PlayerX()
    var playerO: PlayerO = PlayerO()

    init {

        activePlayer = playerX
        Log.d("###", activePlayer.role)
        //updateViewsScore()
    }

    abstract fun playGame(tileNumber: Int): Player?

    fun makeMove(tileNumber: Int): Boolean {
        //val btnId = getBtnNum(btn)

        activePlayer.plays(tileNumber)
        Log.d("###", playerX.tilesPlayed.toString())
        if (activePlayer.isWon(tileNumber)) {
            increaseWinnerScore()
            return true //play finished
        }

        changeRole()
        return false
    }

//    private fun getButtonByNumber(number: Int): Button? {
//        return when (number) {
//            1 -> findViewById(R.id.btn1)
//            2 -> findViewById(R.id.btn2)
//            3 -> findViewById(R.id.btn3)
//            4 -> findViewById(R.id.btn4)
//            5 -> findViewById(R.id.btn5)
//            6 -> findViewById(R.id.btn6)
//            7 -> findViewById(R.id.btn7)
//            8 -> findViewById(R.id.btn8)
//            9 -> findViewById(R.id.btn9)
//            else -> null
//        }
//    }
//
//    private fun getBtnNum(btn: Button): Int {
//        var btnId: Int = when (btn.id) {
//            R.id.btn1 -> 1
//            R.id.btn2 -> 2
//            R.id.btn3 -> 3
//            R.id.btn4 -> 4
//            R.id.btn5 -> 5
//            R.id.btn6 -> 6
//            R.id.btn7 -> 7
//            R.id.btn8 -> 8
//            R.id.btn9 -> 9
//
//            else -> 0
//        }
//        return btnId
//    }

    fun changeRole() {
        activePlayer = if (activePlayer is PlayerX)
            playerO
        else
            playerX
    }

    private fun increaseWinnerScore() {
        activePlayer.score++
        //updateViewsScore()
        //showAlertDialog()
    }

    fun playAgain() {
        resetGame()
    }

    fun resetGame() {

//        var btn: Button?
//        for (i in 1..9) {
//            btn = getButtonByNumber(i)
//            btn?.isClickable = true
//            btn?.text = ""
//            btn?.setBackgroundColor(ContextCompat.getColor(this, R.color.gray))
//        }

        playerX.tilesPlayed.clear()
        playerO.tilesPlayed.clear()
    }
}

class OfflineOnePlayerGame : TicTacToeGame() {

    override fun playGame(tileNumber: Int): Player? {

        val winner: Player

        if (makeMove(tileNumber))  //found winner
            return activePlayer

        if (autoMakeMove()) {
            winner = activePlayer
            changeRole()
            return winner
        }
        return null
    }


    private fun autoMakeMove(): Boolean {
        val emptyTiles = mutableListOf<Int>()

        for (i in 1..9) {
            if (!(playerO.tilesPlayed.contains(i) || playerX.tilesPlayed.contains(i)))
                emptyTiles.add(i)
        }

        val randomIndex = Random().nextInt(emptyTiles.size) //non-negative number less than size
        val tileNumber = emptyTiles[randomIndex]


        return makeMove(tileNumber)

    }


}

class OfflineTwoPlayersGame : TicTacToeGame() {

    override fun playGame(tileNumber: Int): Player? {
        if (makeMove(tileNumber))
            return activePlayer
        return null
    }
}