package com.example.tictactoegame.classes

import android.util.Log
import com.example.tictactoegame.R
import com.example.tictactoegame.ui.myEmail
import com.example.tictactoegame.ui.sessionID
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

abstract class TicTacToeGame(player1: Player) {

    var activePlayer: Player

    var player1: Player
    lateinit var player2: Player

    private val database = Firebase.database
    val myRef = database.reference

    init {
        this.player1 = player1

        activePlayer = player1

        player1.role = "X"
        player1.color = R.color.blue
    }

    constructor(player1: Player, player2: Player) : this(player1) {
        this.player2 = player2
        player2.role = "O"
        player2.color = R.color.red
    }

    fun setPlayerOne(player: Player) {
        player1 = player
        activePlayer = player1

        player1.role = "X"
        player1.color = R.color.blue
    }

    fun setPlayerTwo(player: Player) {
        player2 = player
        player2.role = "O"
        player2.color = R.color.red
    }

    abstract fun playGame(tileNumber: Int): Player?
    abstract fun playAgain()

    fun makeMove(tileNumber: Int): Boolean {

        activePlayer.plays(tileNumber)

        if (activePlayer.isWon(tileNumber)) {
            increaseWinnerScore()
            return true //winner found
        }

        changeRole()
        return false
    }

    fun changeRole() {
        activePlayer = if (activePlayer == player1)
            player2
        else
            player1
    }

    private fun increaseWinnerScore() {
        activePlayer.score++
    }


    open fun resetGame() {

        player1.tilesPlayed.clear()
        player2.tilesPlayed.clear()
    }
}

class OfflineOnePlayerGame(player1: Player) : TicTacToeGame(player1) {

    init {
        val player2 = Player(name = "AI")
        player2.role = "O"
        player2.color = R.color.red
        this.player2 = player2
    }

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

    override fun playAgain() {
        resetGame()
    }


    private fun autoMakeMove(): Boolean {
        val emptyTiles = mutableListOf<Int>()

        for (i in 1..9) {
            if (!(player2.tilesPlayed.contains(i) || player1.tilesPlayed.contains(i)))
                emptyTiles.add(i)
        }

        if (emptyTiles.isEmpty()) {
            changeRole()
            return false
        }

        val randomIndex = Random().nextInt(emptyTiles.size) //non-negative number less than size
        val tileNumber = emptyTiles[randomIndex]


        return makeMove(tileNumber)

    }
}

class OfflineTwoPlayersGame(player1: Player, player2: Player) : TicTacToeGame(player1, player2) {

    override fun playGame(tileNumber: Int): Player? {
        if (makeMove(tileNumber))
            return activePlayer
        return null
    }

    override fun playAgain() {
        resetGame()
    }
}

class OnlineGame(player1: Player, player2: Player) : TicTacToeGame(player1, player2) {



    override fun playGame(tileNumber: Int): Player? {
        myRef.child("PlayerOnline").child(sessionID).child(numToString(tileNumber)).setValue(myEmail)
        return null
    }

    override fun playAgain() {
        resetGame()
        myRef.child("PlayerOnline").removeValue()
    }

    fun numToString(num: Int): String{
        val str = when(num) {
            1 -> "one"
            2 -> "two"
            3 -> "three"
            4 -> "four"
            5 -> "five"
            6 -> "six"
            7 -> "seven"
            8 -> "eight"
            9 -> "nine"
            else -> ""
        }
        return str
    }

}