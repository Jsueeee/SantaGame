package com.example.santagame

import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class GameActivity : AppCompatActivity() {

    lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val point = Point()


        windowManager.defaultDisplay.getSize(point)

        gameView = GameView(this, point.x, point.y)
        Log.d("screen", "${point.x}         ${point.y}")

        setContentView(gameView)
    }

    override fun onPause() {
        super.onPause()
        gameView.pause()
    }

    override fun onResume() {
        super.onResume()
        gameView.resume()
    }

}