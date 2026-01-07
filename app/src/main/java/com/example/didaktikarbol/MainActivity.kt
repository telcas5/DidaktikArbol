package com.example.didaktikarbol

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnArbol = findViewById<Button>(R.id.btnArbol)
        val btnBunkers = findViewById<Button>(R.id.btnBunkers)

        btnArbol.setOnClickListener {
            val intent = Intent(this, ArbolActivity::class.java)
            startActivity(intent)
        }

        btnBunkers.setOnClickListener {
            val intent = Intent(this, BunkersActivity::class.java)
            startActivity(intent)
        }
    }
}
