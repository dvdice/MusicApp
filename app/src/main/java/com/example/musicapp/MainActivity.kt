package com.example.musicapp

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    var startTime = 0.0
    private var finalTime = 0.0
    var forwardTime = 10000
    var backwardTime = 10000
    var oneTimeOnly = 0
    var handler: Handler = Handler()

    var mediaPlayer = MediaPlayer()
    lateinit var seekBar: SeekBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var isPlaying: Boolean = false
        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        val play_btn: ImageButton = findViewById(R.id.play_btn)
        val forward_btn: ImageButton = findViewById(R.id.forward_btn)
        val back_btn: ImageButton = findViewById(R.id.back_btn)
        val title_txt : TextView = findViewById(R.id.songTitle)
        val timeLeft_txt: TextView = findViewById(R.id.timeLeft_txt)
        val time_txt: TextView = findViewById(R.id.time_txt)
        seekBar.progressDrawable = resources.getDrawable(R.drawable.seekbar_style)

        mediaPlayer = MediaPlayer.create(
            this, R.raw.cantstop
        )
        time_txt.text = "" +
            String.format(
            "%d: %d",
            TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.duration.toLong()),
            TimeUnit.MILLISECONDS.toSeconds(
                startTime.toLong()
                        - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(
                        mediaPlayer.duration.toLong()
                    )
                ))
        )

        title_txt.text = "" + resources.getResourceEntryName(R.raw.cantstop)

        seekBar.isClickable = false

        val updateSongTime: Runnable = object : Runnable {
            override fun run() {
                startTime = mediaPlayer.currentPosition.toDouble()
                timeLeft_txt.text = "" +
                        String.format(
                            "%d:  %d",
                            TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                            if (TimeUnit.MILLISECONDS.toSeconds(
                                    startTime.toLong()
                                            - TimeUnit.MINUTES.toSeconds(
                                        TimeUnit.MILLISECONDS.toMinutes(
                                            startTime.toLong()
                                        )
                                    )) > 59.toLong()
                            ) {0} else {
                                TimeUnit.MILLISECONDS.toSeconds(
                                    startTime.toLong()
                                            - TimeUnit.MINUTES.toSeconds(
                                        TimeUnit.MILLISECONDS.toMinutes(
                                            startTime.toLong()
                                        )
                                    ))
                            }

                        )
                seekBar.progress = startTime.toInt()
                handler.postDelayed(this, 100)
            }
        }

        forward_btn.setOnClickListener {
            val temp = startTime
            if (temp + forwardTime <= finalTime){
                startTime += forwardTime
                mediaPlayer.seekTo(startTime.toInt())
            }else{
                Toast.makeText(this, "Can't jump forward", Toast.LENGTH_SHORT).show()
            }
        }

        back_btn.setOnClickListener {
            val temp = startTime
            if (temp - backwardTime > 0){
                startTime -= backwardTime
                mediaPlayer.seekTo(startTime.toInt())
            }else{
                Toast.makeText(this, "Can't jump backward", Toast.LENGTH_SHORT).show()
            }
        }

        play_btn.setOnClickListener {
            if (isPlaying){
                mediaPlayer.pause()
                play_btn.setBackgroundResource(R.drawable.play_icon)
                isPlaying = false
            }else{
                isPlaying = true
                play_btn.setBackgroundResource(R.drawable.pause_icon)
                mediaPlayer.start()
            }
            finalTime = mediaPlayer.duration.toDouble()
            startTime = mediaPlayer.currentPosition.toDouble()

            if (oneTimeOnly == 0){
                seekBar.max = finalTime.toInt()
                oneTimeOnly = 1
            }
            timeLeft_txt.text = startTime.toString()
            seekBar.progress = startTime.toInt()

            handler.postDelayed(updateSongTime, 100)
        }



    }
}