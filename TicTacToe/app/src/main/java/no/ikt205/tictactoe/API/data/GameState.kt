package no.ikt205.tictactoe.API.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//i had some troubles with ints and strings, but things worked more smoothly when i switched to Char
typealias GameState = List<MutableList<Char>>

@Parcelize
data class Game(val players:MutableList<String>, val gameId:String, val state:GameState ):Parcelable