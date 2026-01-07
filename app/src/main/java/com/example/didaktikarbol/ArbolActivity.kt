package com.example.didaktikarbol

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.SeekBar
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class ArbolActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var seekBar: SeekBar
    private lateinit var runnable: Runnable
    private var handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arbol)

        // Reproducir audio
        mediaPlayer = MediaPlayer.create(this, R.raw.genikako_arbola)
        mediaPlayer.start()
        mediaPlayer.isLooping = true // Opcional: si quieres que se repita

        // Setup SeekBar
        seekBar = findViewById(R.id.seekBarAudio)
        seekBar.max = mediaPlayer.duration

        // Update SeekBar
        runnable = Runnable {
            if (::mediaPlayer.isInitialized) {
                try {
                    seekBar.progress = mediaPlayer.currentPosition
                } catch (e: Exception) {
                   // handle potential exception
                }
            }
            handler.postDelayed(runnable, 500)
        }
        handler.postDelayed(runnable, 500)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser && ::mediaPlayer.isInitialized) {
                     mediaPlayer.seekTo(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Referencias a botones de control
        val btnPlay: ImageButton = findViewById(R.id.btnPlay)
        val btnPause: ImageButton = findViewById(R.id.btnPause)
        val btnStop: ImageButton = findViewById(R.id.btnStop)

        btnPlay.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
            }
        }

        btnPause.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            }
        }

        btnStop.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
                mediaPlayer.prepare() // Prepare for next start
                mediaPlayer.seekTo(0)
            } else {
                // If already stopped or paused, reset to beginning
                mediaPlayer.seekTo(0)
            }
        }

        val btnStart: Button = findViewById(R.id.btnStartPuzzle)
        btnStart.setOnClickListener {
            // Detener audio al cambiar de actividad
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            val intent = Intent(this, PuzzleActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
        handler.removeCallbacks(runnable)
    }
    
    override fun onPause() {
        super.onPause()
        if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
             mediaPlayer.pause()
        }
        handler.removeCallbacks(runnable)
    }

     override fun onResume() {
        super.onResume()
        if (::mediaPlayer.isInitialized && !mediaPlayer.isPlaying) {
             mediaPlayer.start()
        }
        handler.postDelayed(runnable, 500)
    }
}
