package com.example.didaktikarbol

import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SoundGameActivity : AppCompatActivity() {

    private lateinit var tvStars: TextView
    private lateinit var btnNext: Button
    private var stars = 0
    private var currentSoundId = -1
    private val totalSounds = 5

    // Mapping of sound buttons to "correct" category (0 for Beldurra, 1 for Babesa)
    private val soundCategories = mapOf(
        R.id.btnSound1 to 0, // Sirens -> Beldurra
        R.id.btnSound2 to 0, // Bomb -> Beldurra
        R.id.btnSound3 to 0, // Crying -> Beldurra
        R.id.btnSound4 to 1, // Breathing -> Babesa
        R.id.btnSound5 to 1  // Silence -> Babesa
    )

    private val toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sound_game)

        tvStars = findViewById(R.id.tvStars)
        btnNext = findViewById(R.id.btnNextActivity)

        setupSoundButtons()
        setupCategoryButtons()

        btnNext.setOnClickListener {
            startActivity(Intent(this, ReflectionActivity::class.java))
            finish()
        }
    }

    private fun setupSoundButtons() {
        val buttons = listOf(
            R.id.btnSound1, R.id.btnSound2, R.id.btnSound3, R.id.btnSound4, R.id.btnSound5
        )

        buttons.forEachIndexed { index, id ->
            findViewById<ImageButton>(id).setOnClickListener {
                currentSoundId = id
                playSound(index)
                // Highlight selected sound
                resetSoundButtonColors()
                it.setBackgroundColor(getColor(R.color.btnPrincipal))
            }
        }
    }

    private fun playSound(index: Int) {
        // Fallback tone generation because specific raw files are missing
        val toneType = when (index) {
            0 -> ToneGenerator.TONE_CDMA_ABBR_ALERT // Siren-like
            1 -> ToneGenerator.TONE_PROP_BEEP2      // Bomb-like beep
            2 -> ToneGenerator.TONE_PROP_PROMPT     // High pitch
            3 -> ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE // Soft
            else -> ToneGenerator.TONE_CDMA_SIGNAL_OFF   // Silence
        }
        toneGenerator.startTone(toneType, 500)
    }

    private fun resetSoundButtonColors() {
        listOf(R.id.btnSound1, R.id.btnSound2, R.id.btnSound3, R.id.btnSound4, R.id.btnSound5).forEach {
            findViewById<ImageButton>(it).setBackgroundResource(R.drawable.bg_boton_secundario)
        }
    }

    private fun setupCategoryButtons() {
        findViewById<Button>(R.id.btnBeldurra).setOnClickListener {
            checkAnswer(0)
        }
        findViewById<Button>(R.id.btnBabesa).setOnClickListener {
            checkAnswer(1)
        }
    }

    private fun checkAnswer(category: Int) {
        if (currentSoundId == -1) return

        val correctCategory = soundCategories[currentSoundId]
        if (category == correctCategory) {
            stars++
            tvStars.text = "⭐ $stars"
            currentSoundId = -1 // Reset after correct
            resetSoundButtonColors()
            if (stars >= totalSounds) {
                btnNext.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        toneGenerator.release()
    }
}
