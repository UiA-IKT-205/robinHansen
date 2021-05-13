package no.ikt205.tictactoe

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.AlarmClock
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_game.*
import no.ikt205.tictactoe.API.data.Game
import no.ikt205.tictactoe.API.data.GameState
import no.ikt205.tictactoe.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    var playerTurn = true

    //sets Homeplayer and enemyplayer to /u0000, which is similar to "null", but it has a name.
    private var HomePlayer: Char = '\u0000'
    private var EnemyPlayer: Char = '\u0000'

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var game: Game? = intent.getParcelableExtra("game")

        // Starts and initializes the game, then loads potential players.
        game?.let {
            loadScreenState(it.state)
        }
        game?.let {
            loadPlayers(it)
        }
        buttonInitializer(game)

        binding.gameId.text = game?.gameId.toString()

        val mainHandler = Handler(Looper.getMainLooper())

        // Polls game and updates it
        mainHandler.post(object : Runnable {
            @SuppressLint("SetTextI18n")
            override fun run() {
                GameManager.pollGame(game?.gameId.toString()) { newGame: Game? ->
                    if (game?.players != newGame?.players && newGame != null)
                        with(binding) {
                            Player1Name.text = newGame.players[0] + " - X\n" + newGame.players[1] + " - O"
                        }

                    if (game?.state != newGame?.state && newGame != null) {
                        game = newGame
                        game?.let {
                            loadScreenState(it.state)
                        }
                        buttonInitializer(game)
                        println(getString(R.string.yourMove))
                        playerTurn = true
                    }

                }
                mainHandler.postDelayed(this, 100)
            }
        })
        buttonInitializer(game)

        backButton.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun loadScreenState(state: GameState) {
        // 1st horizontal row
        binding.button1.text = convertToChar(state[0][0])
        binding.button2.text = convertToChar(state[0][1])
        binding.button3.text = convertToChar(state[0][2])
        // 2nd horizontal row
        binding.button4.text = convertToChar(state[1][0])
        binding.button5.text = convertToChar(state[1][1])
        binding.button6.text = convertToChar(state[1][2])
        // 3rd horizontal row
        binding.button7.text = convertToChar(state[2][0])
        binding.button8.text = convertToChar(state[2][1])
        binding.button9.text = convertToChar(state[2][2])
    }


    //Gives player names and roles (X and O)
    @SuppressLint("SetTextI18n")
    fun loadPlayers(game: Game) {
        if (game.players.size == 1) {
            HomePlayer = 'X'
            EnemyPlayer = '0'
            binding.Player1Name.text = game.players[0] + " - X"
        } else {
            HomePlayer = 'O'
            EnemyPlayer = 'X'
            with(binding) {
                Player2Name.text = game.players[0] + " - X\n" + game.players[1] + " - O"
            }
            playerTurn = true
        }
    }

    //Clicking buttons go brrr.
    private fun buttonInitializer(game: Game?) {
        binding.button1.setOnClickListener {
            makeMove(game, 0, 0)
        }
        binding.button2.setOnClickListener {
            makeMove(game, 0, 1)
        }
        binding.button3.setOnClickListener {
            makeMove(game, 0, 2)
        }
        binding.button4.setOnClickListener {
            makeMove(game, 1, 0)
        }
        binding.button5.setOnClickListener {
            makeMove(game, 1, 1)
        }
        binding.button6.setOnClickListener {
            makeMove(game, 1, 2)
        }
        binding.button7.setOnClickListener {
            makeMove(game, 2, 0)
        }
        binding.button8.setOnClickListener {
            makeMove(game, 2, 1)
        }
        binding.button9.setOnClickListener {
            makeMove(game, 2, 2)
        }
    }

    //THe function that allows the player to make moves on the board.
    //There is currently a bug, that when there is no enemy, the board skips them, and gives you infinite turns.
    private fun makeMove(game: Game?, horizontal: Int, vertical: Int) {
        if (game != null && game.state[horizontal][vertical] == '0') {
            game.state[horizontal][vertical] = HomePlayer
            game.state.let {
                GameManager.updateGame(game.gameId, it)
            }
            playerTurn = false
        }
        game?.let {
            loadScreenState(it.state)
        }
        game?.let {
            checkWin(it.state)
        }
    }

    //Checks if the user has won or not.
    fun checkWin(state: GameState) {
        //Horizontally for O
        if((state[0][0] == 'O') && (state[0][1] == 'O') && (state[0][2] == 'O')){winner(binding.Player2Name.toString())}
        else if((state[1][0] == 'O') && (state[1][1] == 'O') && (state[1][2] == 'O')){winner(binding.Player2Name.toString())}
        else if((state[2][0] == 'O') && (state[2][1] == 'O') && (state[2][2] == 'O')){winner(binding.Player2Name.toString())}

        //Horizontally for X
        else if((state[0][0] == 'X') && (state[0][1] == 'X') && (state[0][2] == 'X')){winner(binding.Player1Name.toString())}
        else if((state[1][0] == 'X') && (state[1][1] == 'X') && (state[1][2] == 'X')){winner(binding.Player1Name.toString())}
        else if((state[2][0] == 'X') && (state[2][1] == 'X') && (state[2][2] == 'X')){winner(binding.Player1Name.toString())}

        // Vertically for O
        else if((state[0][0] == 'O') && (state[1][0] == 'O') && (state[2][0] == 'O')){winner(binding.Player1Name.toString())}
        else if((state[0][1] == 'O') && (state[1][1] == 'O') && (state[2][1] == 'O')){winner(binding.Player1Name.toString())}
        else if((state[0][2] == 'O') && (state[1][2] == 'O') && (state[2][2] == 'O')){winner(binding.Player1Name.toString())}

        //Vertically for X
        else if((state[0][0] == 'X') && (state[1][0] == 'X') && (state[2][0] == 'X')){winner(binding.Player1Name.toString())}
        else if((state[0][1] == 'X') && (state[1][1] == 'X') && (state[2][1] == 'X')){winner(binding.Player1Name.toString())}
        else if((state[0][2] == 'X') && (state[1][2] == 'X') && (state[2][2] == 'X')){winner(binding.Player1Name.toString())}

        // Diagonally for O
        else if((state[0][0] == 'O') && (state[1][1] == 'O') && (state[2][2] == 'O')){winner(binding.Player1Name.toString())}
        else if((state[0][2] == 'O') && (state[1][1] == 'O') && (state[2][0] == 'O')){winner(binding.Player1Name.toString())}

        // Diagonally for X
        else if((state[0][0] == 'X') && (state[1][1] == 'X') && (state[2][2] == 'X')){winner(binding.Player1Name.toString())}
        else if((state[0][2] == 'X') && (state[1][1] == 'X') && (state[2][0] == 'X')){winner(binding.Player1Name.toString())}
        
        // Draw
        else if((state[0][0] != '0') && (state[0][1] != '0') && (state[0][2] != '0') &&
            (state[1][0] != '0') && (state[1][1] != '0') && (state[1][2] != '0') &&
            (state[2][0] != '0') && (state[2][1] != '0') && (state[2][2] != '0'))
            winner("No one :(")
    }

    //Needed to convert to Char
    private fun convertToChar(isint: Char): String {
        if(isint == '0')
            return " "
        else
            return isint.toString()
    }

    //Winner function when a player wins, simply redirects to the winnerscreen.
    private fun winner(winCondition:String) {
        playerTurn=false
        val isWinner = Intent(this, WinnerScreen::class.java).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, winCondition)
        }
        startActivity(isWinner)
    }
}

    
