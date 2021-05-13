package no.ikt205.tictactoe

import android.app.Application

class App:Application() {

    companion object{
        lateinit var context: App private set
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }
}