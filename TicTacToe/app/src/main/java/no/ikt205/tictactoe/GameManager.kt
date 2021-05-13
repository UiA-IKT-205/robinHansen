package no.ikt205.tictactoe

import android.content.Intent
import android.util.Log
import no.ikt205.tictactoe.API.data.GameState
import no.ikt205.tictactoe.API.GameService
import no.ikt205.tictactoe.API.data.Game
import no.ikt205.tictactoe.App.Companion.context

typealias GameCallback = (game:Game?) -> Unit

//most of the code in here is pretty self explanatory through names.
object GameManager {
    private const val TAG:String = "GameManager"
    //Here i make a list (3x3) to be used in multiplayer.
    private val StartingGameState:GameState = listOf(mutableListOf('0','0','0'),mutableListOf('0','0','0'),mutableListOf('0','0','0'))

    fun createGame(player:String){
        GameService.createGame(player,StartingGameState) { state: Game?, err: Int? ->
            if(err != null){
                Log.d(TAG, "Could not start the game, code : $err")
            } else {
                val prep = Intent(context, GameActivity::class.java)
                prep.putExtra("game", state)
                prep.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
                context.startActivity(prep)
            }
        }
    }

    fun joinGame(player:String, gameId:String){
        GameService.joinGame(player, gameId) { game: Game?, err: Int? ->
            if(err != null){
                Log.d(TAG, "Could not join gam, code : $err")
            } else {
                val prep = Intent(context, GameActivity::class.java)
                prep.putExtra("game", game)
                prep.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
                context.startActivity(prep)
            }
        }
    }

    fun pollGame(gameId:String, callback:GameCallback){
        GameService.pollGame(gameId) { game: Game?, err: Int? ->
            if(err != null){
                Log.d(TAG, "Could not refresh game, code : $err")
            } else {
                callback(game)
            }
        }

    }

    fun updateGame(gameId:String, state:GameState){
        GameService.updateGame(gameId, state) { game: Game?, err: Int? ->
            if(err != null){
                Log.d(TAG, "Could not update game, code: $err")
            }
        }
    }

}