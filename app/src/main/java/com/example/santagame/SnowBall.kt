package com.example.santagame

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import com.example.santagame.GameView.Companion.screenRatioX
import com.example.santagame.GameView.Companion.screenRatioY

class SnowBall internal constructor(res: Resources) {
    var x = 0
    var y = 0
    var width: Float
    var height: Float
    var snowball: Bitmap

    init {
        snowball = BitmapFactory.decodeResource(res, R.drawable.snowball)
        width = snowball.width.toFloat()/8
        height = snowball.height.toFloat()/8

        width /= 4
        height /= 4

        width *= screenRatioX
        height *= screenRatioY

        snowball = Bitmap.createScaledBitmap(snowball, width.toInt(), height.toInt(), false)

    }

    val collisionShape: Rect
        get() = Rect(x, y, x + width.toInt(), y + height.toInt())
}