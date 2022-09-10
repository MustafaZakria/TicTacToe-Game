package com.example.tictactoegame.classes

import java.util.*

abstract class TicTacToeGame {

    var activePlayer: Player

    var playerX: PlayerX = PlayerX()
    var playerO: PlayerO = PlayerO()

    init {
        activePlayer = playerX
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

        if(emptyTiles.isEmpty()) {
            changeRole()
            return false
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