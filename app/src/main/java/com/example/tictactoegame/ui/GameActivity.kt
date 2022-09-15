package com.example.tictactoegame.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.tictactoegame.R
import com.example.tictactoegame.classes.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GameActivity : AppCompatActivity() {

    private val database = Firebase.database
    private val myRef = database.reference

    lateinit var ticTacToeGame: TicTacToeGame
    lateinit var mode: String
    lateinit var myEmail: String
    lateinit var mainPlayer: Player

    lateinit var etEmail: EditText


    lateinit var tvScorePlayer1: TextView
    lateinit var tvScorePlayer2: TextView

    lateinit var tvNamePlayer1: TextView
    lateinit var tvNamePlayer2: TextView

    lateinit var btnPlayAgain: Button

    private val positiveButtonClick = { dialog: DialogInterface, which: Int ->
        playAgain()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        tvScorePlayer1 = findViewById(R.id.tv_score_x)
        tvScorePlayer2 = findViewById(R.id.tv_score_o)

        tvNamePlayer1 = findViewById(R.id.tv_player1_name)
        tvNamePlayer2 = findViewById(R.id.tv_player2_name)

        etEmail = findViewById(R.id.et_email)

        mode = getGameMode()
        myEmail = getEmail()
        mainPlayer = Player(splitString(myEmail), myEmail)

        if (mode.equals("onePlayerMode", true))
            ticTacToeGame = OfflineOnePlayerGame(mainPlayer)
        else if (mode.equals("twoPlayersMode", true))
            ticTacToeGame = OfflineTwoPlayersGame(mainPlayer, Player("player2"))
        else {
//            ticTacToeGame = OnlineGame(Player("player1"), Player("player2"))
            onlineGameUI()
            listenToIncomingCalls()
        }

        btnPlayAgain = findViewById(R.id.btn_play_again)
        btnPlayAgain.setOnClickListener {
            playAgain()
        }

        updateUI()
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

        //if(mode == "onlineMode")
        //fun
        //else


//        val winner: Player? = ticTacToeGame.playGame(tileNumber)
//
//        updateTiles()
//
//        if( winner!=null) {
//            celebrateWinner(winner)
//        }

        myRef.child("PlayerOnline").child(sessionID).child(numToString(tileNumber)).setValue(myEmail)

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

    fun stringToNum(str: String): Int{
        val num = when(str) {
            "one" -> 1
            "two" -> 2
            "three" -> 3
            "four" -> 4
            "five" -> 5
            "six" -> 6
            "seven" -> 7
            "eight" -> 8
            "nine" -> 9
            else -> -1
        }
        return num
    }

    private fun updateTiles() {
        var player: Player = ticTacToeGame.player1
        var tileNumber: Int = player.tilesPlayed.last()
        var btn = getButtonByNumber(tileNumber)

        for (i in 0..1) {
            btn?.isClickable = false
            btn?.text = player.role
            btn?.setBackgroundColor(ContextCompat.getColor(this, player.color))

            player = ticTacToeGame.player2
            if (player.tilesPlayed.isEmpty()) {
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


    private fun updateUI() {

        val player1 = ticTacToeGame.player1
        val player2 = ticTacToeGame.player2

        tvScorePlayer1.text = player1.score.toString()
        tvScorePlayer1.setTextColor(ContextCompat.getColor(this, player1.color))

        tvNamePlayer1.text = player1.name
        tvNamePlayer1.setTextColor(ContextCompat.getColor(this, player1.color))

        tvScorePlayer2.text = player2.score.toString()
        tvScorePlayer2.setTextColor(ContextCompat.getColor(this, player2.color))

        tvNamePlayer2.text = player2.name
        tvNamePlayer2.setTextColor(ContextCompat.getColor(this, player2.color))

    }

    private fun showWinnerMsg(winner: Player) {
        updateUI()
        winnerAlertDialog(winner)
    }

    private fun showLoserMsg(loser: Player) {
        updateUI()
        loserAlertDialog(loser)
    }

    private fun winnerAlertDialog(winner: Player) {

        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogCustom))

        with(builder)
        {
            setTitle("Congratulations!")
            setMessage("${winner.name} Won!")
            setPositiveButton("Play again", positiveButtonClick)
            setNegativeButton("Ok", null)
            show()
        }

    }

    private fun loserAlertDialog(loser: Player) {

        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogCustom))

        with(builder)
        {
            setTitle("Unfortunately!")
            setMessage("${loser.name} lost!")
            setPositiveButton("Play again", positiveButtonClick)
            setNegativeButton("Ok", null)
            show()
        }

    }

    fun getGameMode(): String {
        var mode = ""
        val extras = intent.extras

        if (extras != null) {
            mode = extras.getString("mode").toString()
        }
        return mode
    }

    fun getEmail(): String {
        var email = ""
        val extras = intent.extras

        if (extras != null) {
            email = extras.getString("email").toString()
        }
        return email
    }

    //ONLINE GAME
    fun btnRequestEvent(view: View) {
        val otherUserEmail = etEmail.text.toString()
        myRef.child("Users").child(splitString(otherUserEmail)).child("Request").push()
            .setValue(myEmail)

        //ticTacToeGame.player1 = mainPlayer
        ticTacToeGame = OnlineGame(mainPlayer, Player(splitString(otherUserEmail), otherUserEmail))
        playerType = "player1"

        playOnline(splitString(myEmail) + splitString(otherUserEmail))
    }

    fun btnAcceptEvent(view: View) {
        val otherUserEmail = etEmail.text.toString()
        myRef.child("Users").child(splitString(otherUserEmail)).child("Request").push()
            .setValue(myEmail)

        //ticTacToeGame.player2 = mainPlayer
        ticTacToeGame = OnlineGame(Player(splitString(otherUserEmail), otherUserEmail), mainPlayer)
        playerType = "player2"

        playOnline(splitString(otherUserEmail) + splitString(myEmail))
    }

    lateinit var sessionID: String
    lateinit var playerType: String

    fun playOnline(sessionID: String) {
        this.sessionID = sessionID
        myRef.child("PlayerOnline").removeValue()
        myRef.child("PlayerOnline").child(sessionID)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        ticTacToeGame.resetGame()
                        val td = snapshot.value as HashMap<String, Any>
                        var value: String
                        for (key in td.keys) {
                            value = td[key] as String

//                            if(value!=myEmail) {
//                                ticTacToeGame.activePlayer =
//                                    if(playerType === "player1") ticTacToeGame.player2 else ticTacToeGame.player1   //myEmail === ticTacToeGame.player1.email
//                            }
//                            else {
//                                ticTacToeGame.activePlayer =
//                                    if(playerType === "player1") ticTacToeGame.player1 else ticTacToeGame.player2
//                            }

                            if (ticTacToeGame.activePlayer.email == myEmail) {
                                if (value != myEmail)
                                    ticTacToeGame.changeRole()
                            } else {
                                if (value == myEmail)
                                    ticTacToeGame.changeRole()
                            }

                            val isWinnerExist = ticTacToeGame.makeMove(stringToNum(key))
                            updateTiles()
                            if (isWinnerExist && myEmail == ticTacToeGame.activePlayer.email) {
                                showWinnerMsg(ticTacToeGame.activePlayer)
                            } else {
                                if (ticTacToeGame.activePlayer == ticTacToeGame.player1)
                                    showLoserMsg(ticTacToeGame.player2)
                                else
                                    showLoserMsg(ticTacToeGame.player1)
                            }
                        }
                    } catch (ex: Exception) {
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("***", error.toString())
                }

            })
    }

    fun onlineGameUI() {
        val layoutOnline = findViewById<View>(R.id.layout_online)
        layoutOnline.visibility = View.VISIBLE
    }

    private fun listenToIncomingCalls() {
        myRef.child("Users").child(splitString(myEmail)).child("Request")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val td = snapshot.value as HashMap<String, Any>
                        if (td != null) {
                            val value: String
                            for (key in td.keys) {
                                value = td[key] as String
                                etEmail.setText(value)
                                myRef.child("Users").child(splitString(myEmail)).child("Request")
                                    .setValue(true)
                                break
                            }
                        }
                    } catch (ex: Exception) {
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("***", error.toString())
                }

            })
    }

    private fun splitString(string: String): String {
        var split = string.split("@")
        return split[0]
    }

}