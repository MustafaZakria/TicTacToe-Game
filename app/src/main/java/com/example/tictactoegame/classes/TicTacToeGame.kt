package com.example.tictactoegame.classes

import android.util.Log
import com.example.tictactoegame.R
import java.util.*

abstract class TicTacToeGame(player1: Player) {

    var activePlayer: Player

    var player1: Player
    lateinit var player2: Player

    init {
        this.player1 = player1

        activePlayer = player1

        player1.role = "X"
        player1.color = R.color.blue
    }

    constructor(player1: Player, player2: Player) : this(player1) {
        this.player2 = player2
        player1.role = "O"
        player1.color = R.color.red
    }

    abstract fun playGame(tileNumber: Int): Player?

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
        //updateViewsScore()
        //showAlertDialog()
    }

    fun playAgain() {
        resetGame()
    }

    fun resetGame() {

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
}

class OnlineGame(player1: Player) : TicTacToeGame(player1) {

    init {
        val player2 = Player(name = "AI")
        player2.role = "O"
        player2.color = R.color.red
        this.player2 = player2
    }

    override fun playGame(tileNumber: Int): Player? {
        TODO("Not yet implemented")
    }

}