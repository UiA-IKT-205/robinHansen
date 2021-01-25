package no.uia.ikt205.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import no.uia.ikt205.pomodoro.util.millisecondsToDescriptiveTime

class MainActivity : AppCompatActivity() {

    lateinit var timer:CountDownTimer
    lateinit var startButton:Button
    lateinit var ThirtyMinutes:Button
    lateinit var SixtyMinutes:Button
    lateinit var NintyMinutes:Button
    lateinit var HundredTwentyMinutes:Button


    lateinit var coutdownDisplay:TextView

    var timerGoing = 0
    var timeToCountDownInMs = 5000L
    val timeTicks = 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ThirtyMinutes = findViewById<Button>(R.id.button_30)
        ThirtyMinutes.setOnClickListener(){
            timeToCountDownInMs = 1800000L
        }
        SixtyMinutes = findViewById<Button>(R.id.button_60)
        SixtyMinutes.setOnClickListener(){
            timeToCountDownInMs = 3600000L
        }

        NintyMinutes = findViewById<Button>(R.id.button_90)
        NintyMinutes.setOnClickListener(){
            timeToCountDownInMs = 5400000L
        }
        HundredTwentyMinutes = findViewById<Button>(R.id.button_120)
        HundredTwentyMinutes.setOnClickListener(){
            timeToCountDownInMs = 7200000L
        }

        startButton = findViewById<Button>(R.id.startCountdownButton)
        startButton.setOnClickListener(){
            startCountDown(it)
        }
        coutdownDisplay = findViewById<TextView>(R.id.countDownView)

    }

    fun startCountDown(v: View){

        timer = object : CountDownTimer(timeToCountDownInMs,timeTicks) {
            override fun onFinish() {
                Toast.makeText(this@MainActivity,"Arbeidsøkt er ferdig", Toast.LENGTH_SHORT).show()
                timerGoing = 0
            }

            override fun onTick(millisUntilFinished: Long) {
               updateCountDownDisplay(millisUntilFinished)
            }
        }

        if(timerGoing == 0){
            timer.start()
            timerGoing = 1
        }
    }

    fun updateCountDownDisplay(timeInMs:Long){
        coutdownDisplay.text = millisecondsToDescriptiveTime(timeInMs)
    }

}