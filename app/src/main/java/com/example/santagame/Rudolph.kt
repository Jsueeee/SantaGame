package com.example.santagame

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import com.example.santagame.GameView.Companion.screenRatioX
import com.example.santagame.GameView.Companion.screenRatioY

class Rudolph internal constructor(res: Resources){
    var speed = 20
    var rudolph1: Bitmap
    var x = 0
    var y = 0
    var width: Float
    var height: Float
    var wasShot = true

    val rudolph: Bitmap
        get() {
            return rudolph1
        }

    init {
        rudolph1 = BitmapFactory.decodeResource(res, R.drawable.rudolph)
        width = rudolph1.width.toFloat()
        height = rudolph1.width.toFloat()
        width /= 10
        height /= 12
        width *= screenRatioX
        height *= screenRatioY

        rudolph1 = Bitmap.createScaledBitmap(rudolph1, width.toInt(), height.toInt(), false)
        y = -height.toInt()


    }

    val collisionShape: Rect
        get() = Rect(x, y, x + width.toInt(), y + height.toInt())

}