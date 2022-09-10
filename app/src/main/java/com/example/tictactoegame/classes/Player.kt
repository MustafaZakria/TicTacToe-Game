package com.example.tictactoegame.classes

import com.example.tictactoegame.R

abstract class Player {

    var tilesPlayed: MutableList<Int> = mutableListOf()

    abstract var color: Int
    abstract var role: String

    var score: Int = 0

    fun isWon(tileNumber: Int): Boolean {
        //row
        val quotient: Int = (tileNumber - 1) / 3
        var rowVictory: Boolean = true
        for (i in 1..3) {
            if (!tilesPlayed.contains((quotient * 3) + i))
                rowVictory = false
        }

        //column
        var reminder: Int = tileNumber % 3
        if (reminder == 0)
            reminder = 3
        var columnVictory: Boolean = true
        for (i in 0..2) {
            if (!tilesPlayed.contains((i * 3) + reminder))
                columnVictory = false
        }

        //diagonal
        var diagonalVictory: Boolean = false
        if (tilesPlayed.contains(1) && tilesPlayed.contains(5) && tilesPlayed.contains(9))
            diagonalVictory = true
        if (tilesPlayed.contains(3) && tilesPlayed.contains(5) && tilesPlayed.contains(7))
            diagonalVictory = true


        return (rowVictory || columnVictory || diagonalVictory)
    }

    fun plays(tileNumber: Int) {   //btn: Button,

        //btn.isClickable = false
        //btn.text = this.role
        //btn.setBackgroundColor(getColor(btn.context, this.color))
        tilesPlayed.add(tileNumber)

    }


}

class PlayerX : Player() {

    override var color: Int = R.color.blue
    override var role: String = "X"
}

class PlayerO : Player() {

    override var color: Int = R.color.red
    override var role: String = "O"
}