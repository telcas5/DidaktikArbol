package com.example.tuproyecto

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.example.didaktikarbol.R

class MainActivity : AppCompatActivity() {

    private lateinit var gridPuzzle: GridLayout
    private val size = 3
    private lateinit var tiles: Array<Bitmap?>
    private lateinit var imageViews: Array<ImageView>
    private var emptyIndex = 8

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridPuzzle = findViewById(R.id.gridPuzzle)

        prepararPuzzle()
        dibujarPuzzle()
    }

    private fun prepararPuzzle() {
        val fullBitmap = BitmapFactory.decodeResource(resources, R.drawable.gernikako_arbola)

        val width = fullBitmap.width / size
        val height = fullBitmap.height / size

        tiles = Array(size * size) { null }
        for (row in 0 until size) {
            for (col in 0 until size) {
                val piece = Bitmap.createBitmap(
                    fullBitmap,
                    col * width,
                    row * height,
                    width,
                    height
                )
                tiles[row * size + col] = piece
            }
        }

        tiles[8] = null // Hueco vacío
        emptyIndex = 8

        // Mezclar
        tiles.shuffle()
        emptyIndex = tiles.indexOfFirst { it == null }
    }

    private fun dibujarPuzzle() {
        imageViews = Array(size * size) { ImageView(this) }
        gridPuzzle.removeAllViews()

        for (i in 0 until size * size) {
            val imageView = imageViews[i]
            val params = GridLayout.LayoutParams().apply {
                width = MATCH_PARENT
                height = MATCH_PARENT
                setMargins(4, 4, 4, 4)
                rowSpec = GridLayout.spec(i / size)
                columnSpec = GridLayout.spec(i % size)
            }
            imageView.layoutParams = params
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.adjustViewBounds = true

            actualizarPieza(i)

            imageView.setOnClickListener { moverPieza(i) }
            gridPuzzle.addView(imageView)
        }
    }

    private fun actualizarPieza(index: Int) {
        val bmp = tiles[index]
        val iv = imageViews[index]
        if (bmp != null) {
            iv.setImageBitmap(bmp)
            iv.setBackgroundColor(0xFFDDDDDD.toInt())
        } else {
            iv.setImageBitmap(null)
            iv.setBackgroundColor(0xFF000000.toInt()) // Hueco negro
        }
    }

    private fun moverPieza(index: Int) {
        if (!esAdyacente(index, emptyIndex)) return

        // Intercambiar
        val temp = tiles[index]
        tiles[index] = tiles[emptyIndex]
        tiles[emptyIndex] = temp
        emptyIndex = index

        // Actualizar vistas
        actualizarPieza(index)
        actualizarPieza(emptyIndex)

        if (puzzleResuelto()) {
            Toast.makeText(this, "¡🎉 GANASTE! 🎉", Toast.LENGTH_LONG).show()
        }
    }

    private fun esAdyacente(i: Int, j: Int): Boolean {
        val rowI = i / size
        val colI = i % size
        val rowJ = j / size
        val colJ = j % size
        return (Math.abs(rowI - rowJ) + Math.abs(colI - colJ)) == 1
    }

    private fun puzzleResuelto(): Boolean {
        for (i in 0 until tiles.size - 1) {
            if (tiles[i] == null) return false
        }
        return tiles.last() == null
    }
}
