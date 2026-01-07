package com.example.didaktikarbol

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ReflectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reflection)

        val tvFeedback: TextView = findViewById(R.id.tvFeedback)
        val btnJarraitu: Button = findViewById(R.id.btnJarraitu)

        val emojiButtons = listOf(
            findViewById<Button>(R.id.btnSad),
            findViewById<Button>(R.id.btnCrying),
            findViewById<Button>(R.id.btnRelieved),
            findViewById<Button>(R.id.btnHope)
        )

        emojiButtons.forEach { button ->
            button.setOnClickListener {
                tvFeedback.visibility = View.VISIBLE
                btnJarraitu.visibility = View.VISIBLE
                // Disable other buttons to simulate single choice
                emojiButtons.forEach { it.isEnabled = false }
                button.isEnabled = true
                button.alpha = 1.0f
            }
        }

        btnJarraitu.setOnClickListener {
            startActivity(Intent(this, PeaceMuralActivity::class.java))
            finish()
        }
    }
}
