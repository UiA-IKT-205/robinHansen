package no.ikt205.tictactoe.API

import android.util.Log
import no.ikt205.tictactoe.API.data.Game
import no.ikt205.tictactoe.API.data.GameState
import no.ikt205.tictactoe.App
import no.ikt205.tictactoe.R
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject


typealias GameServiceCallback = (state: Game?, errorCode:Int? ) -> Unit

object GameService {

    const val TAG:String = "GameService"
    private val context = App.context
    private val requestQue:RequestQueue = Volley.newRequestQueue(context)

    //i found it easier to just use "CREATE_GAME.url" and the simply add on whats needed afterwards. it cleard up some headaches
    //Which is why i only need "CREATE_GAME"
    private enum class APIEndpoints(val url:String) {
        CREATE_GAME("%1s%2s%3s".format(context.getString(R.string.protocol), context.getString(R.string.domain),context.getString(R.string.base_path)))
    }

    //Creates the game and sets up (in conjuction with other code) to be joined
    fun createGame(playerId:String, state:GameState, callback:GameServiceCallback) {

        val url = APIEndpoints.CREATE_GAME.url
        val requestData = JSONObject()
        requestData.put("player", playerId)
        requestData.put("state", JSONArray(state))

        val request = object : JsonObjectRequest(Method.POST,url, requestData,
            {
                // Success game created.
                val game = Gson().fromJson(it.toString(0), Game::class.java)
                Log.d(TAG, "Starting : $game")
                callback(game,null)
            }, {
                callback(null, it.networkResponse.statusCode)
            } ) {
            // Error creating new game.
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Game-Service-Key"] = context.getString(R.string.game_service_key)
                return headers
            }
        }

        requestQue.add(request)
    }

    //Joins a game
    fun joinGame(playerId:String, gameId:String, callback: GameServiceCallback){
        val url = APIEndpoints.CREATE_GAME.url+"/"+gameId+"/join"
        val requestData = JSONObject()
        requestData.put("player", playerId)
        requestData.put("gameId", gameId)

        val request = object : JsonObjectRequest(Request.Method.POST,url, requestData,
            {
                // Success game created.
                val game = Gson().fromJson(it.toString(0), Game::class.java)
                Log.d(TAG, "Joining : $game")
                callback(game,null)
            }, {
                // Error creating new game.
                callback(null, it.networkResponse.statusCode)
            } ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Game-Service-Key"] = context.getString(R.string.game_service_key)
                return headers
            }
        }

        requestQue.add(request)
    }

    //Poll
    fun pollGame(gameId: String,callback:GameServiceCallback){
        val url = APIEndpoints.CREATE_GAME.url+"/"+gameId+"/poll"
        val requestData = JSONObject()
        requestData.put("gameId", gameId)

        val request = object : JsonObjectRequest(Method.GET,url, requestData,
            {
                // Success game created.
                val game = Gson().fromJson(it.toString(0), Game::class.java)
                Log.d(TAG, "Polling : $game")
                callback(game,null)
            }, {
                // Error creating new game.
                callback(null, it.networkResponse.statusCode)
            } ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Game-Service-Key"] = context.getString(R.string.game_service_key)
                return headers
            }
        }

        requestQue.add(request)
    }

    //Updates the game
    fun updateGame(gameId: String, state:GameState, callback: GameServiceCallback){
        val url = APIEndpoints.CREATE_GAME.url+"/"+gameId+"/update"
        val requestData = JSONObject()
        requestData.put("gameId", gameId)
        requestData.put("state", JSONArray(state))

        val request = object : JsonObjectRequest(Request.Method.POST,url, requestData,
            {
                // Success game created.
                val game = Gson().fromJson(it.toString(0), Game::class.java)
                callback(game,null)
            }, {
                // Error creating new game.
                callback(null, it.networkResponse.statusCode)
            } ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Game-Service-Key"] = context.getString(R.string.game_service_key)
                return headers
            }
        }

        requestQue.add(request)
    }



}