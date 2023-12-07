package com.example.musicapp

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mSeekBar = findViewById<View>(R.id.seekBar) as SeekBar
        mSeekBar.progressDrawable = resources.getDrawable(R.drawable.seekbar_style)
    }
}