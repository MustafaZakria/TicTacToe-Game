package com.example.tictactoegame

import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor

abstract class Player {

    var tilesPlayed: MutableList<Int> = mutableListOf()

    abstract var color: Int
    abstract var role: String

    fun isWon(btnId: Int): Boolean {
        //row
        val quotient: Int = (btnId - 1) / 3
        var rowVictory: Boolean = true
        for (i in 1..3) {
            if (!tilesPlayed.contains((quotient * 3) + i))
                rowVictory = false
        }

        //column
        var reminder: Int = btnId % 3
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

    fun plays(btn: Button, btnId: Int) {

        btn.isClickable = false
        //btn.isEnabled = false
        btn.text = this.role
        //btn.setBackgroundResource(this.color)
        btn.setBackgroundColor(getColor(btn.context, this.color))
        tilesPlayed.add(btnId)

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