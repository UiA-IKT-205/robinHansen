package no.ikt205.tictactoe

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_winner_screen.*
import no.ikt205.tictactoe.databinding.ActivityWinnerScreenBinding

class WinnerScreen : AppCompatActivity() {
    private lateinit var binding: ActivityWinnerScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWinnerScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val winner = intent.getStringExtra(AlarmClock.EXTRA_MESSAGE).toString()

        // Shows who wins on the screen
        findViewById<TextView>(R.id.winner_name).apply {
            text = winner
        }

        // A back button to take you back to the main menu
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}