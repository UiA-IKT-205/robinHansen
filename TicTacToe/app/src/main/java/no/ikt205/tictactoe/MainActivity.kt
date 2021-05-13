package no.ikt205.tictactoe

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import no.ikt205.tictactoe.databinding.ActivityMainBinding
import no.ikt205.tictactoe.dialogs.CreateGameDialog
import no.ikt205.tictactoe.dialogs.GameDialogListener
import no.ikt205.tictactoe.dialogs.JoinGameDialog

class MainActivity : AppCompatActivity(), GameDialogListener {

    private val TAG:String = "MainActivity"
    lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.startbutton.setOnClickListener{
            createNewGame()
        }
        binding.joinbutton.setOnClickListener{
            joinGame()
        }
    }

    private fun createNewGame(){
        val cgdl = CreateGameDialog()
        cgdl.show(supportFragmentManager,"CreateGameDialogScreen")
    }
    private fun joinGame(){
        val jgdl = JoinGameDialog()
        jgdl.show(supportFragmentManager,"CreateGameDialogScreen")
    }

    override fun onDialogCreateGame(player: String) {
        //Info for the developer to see if it succeeded
        Log.d(TAG, player)
        GameManager.createGame(player)
        val prep = Intent(this, GameActivity::class.java).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, player)
        }
        startActivity(prep)
    }

    override fun onDialogJoinGame(player: String, gameId: String) {
        //Info for the developer to see if it succeeded
        Log.d(TAG, "$player $gameId")
        GameManager.joinGame(player,gameId)
        val prep = Intent(this, GameActivity::class.java).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, player)
        }
        startActivity(prep)
    }
}
