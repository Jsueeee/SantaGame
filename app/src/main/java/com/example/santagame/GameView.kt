package com.example.santagame

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceView
import java.util.*
import kotlin.collections.ArrayList

class GameView(private val activity: GameActivity, screenX: Int, screenY: Int) : SurfaceView(activity), Runnable {

    var thread: Thread? = null
    var isPlaying = false
    var isGameOver = false
    val screenX: Int
    val screenY: Int
    val paint: Paint
    val background1: Background
    val background2: Background
    val santa: Santa
    val snowBalls: MutableList<SnowBall>
    val rudolphs: Array<Rudolph?>
    val random: Random

    private var b1: Float
    private var b2: Float

    companion object{
        var screenRatioX: Float = 0.0f
        var screenRatioY: Float = 0.0f
    }

    init {
        this.screenX = screenX
        this.screenY = screenY
        screenRatioX = 1920f / screenX
        screenRatioY = 1080f / screenY
        background1 = Background(screenX, screenY, resources)
        background2 = Background(screenX, screenY, resources)
        background2.x = screenX

        paint = Paint()

        b1 = background1.x.toFloat()
        b2 = background2.x.toFloat()

        santa = Santa(this, screenY, resources)
        Log.d("screen", screenY.toString())

        snowBalls = ArrayList()

        rudolphs = arrayOfNulls<Rudolph>(4)
        for(i in 0..3){
            val rudolph = Rudolph(resources)
            rudolphs[i] = rudolph
        }

        random = Random()


    }

    override fun run() {
        while(isPlaying){
            update()
            draw()
            sleep()
        }

    }

    private fun update(){

        b1 -= 10 * screenRatioX!!
        b2 -= 10 * screenRatioX!!

        if (b1 + background1.background.width < 0) {
            b1 = screenX!!.toFloat()
            Log.d("LOG UPDATE", "UPDATE 1: $b1")
        }
        if (b2 + background2.background.width < 0) {
            b2 = screenX!!.toFloat()
            Log.d("LOG UPDATE", "UPDATE 2: $b2")
        }
        if(santa.isGoingUp){
            santa.y -= 30 * screenRatioY
        }else{
            santa.y += 30 * screenRatioY
        }
        if(santa.y < 0)
            santa.y = 0F
        if(santa.y >= screenY - santa.height)
            santa.y = (screenY - santa.height).toFloat()

        val trash: MutableList<SnowBall> = ArrayList()
        for(snowball in snowBalls){
            if(snowball.x > screenX)    trash.add(snowball)
            snowball.x += (50 * screenRatioX).toInt()
            Log.d("snowball", "${snowball.x}")

            for(rudolph in rudolphs){
                if(Rect.intersects(
                        rudolph!!.collisionShape,
                        snowball.collisionShape
                    )){

                    rudolph.x = -100000
                    snowball.x = screenX + 500
                    rudolph.wasShot = true
                }
            }
        }
        for (snowball in trash) snowBalls.remove(snowball)

        for(rudolph in rudolphs){
            rudolph!!.x -= rudolph!!.speed

            if(rudolph.x + rudolph.width < 0){

//                if(!rudolph.wasShot){
//                    isGameOver = true
//                    return
//                }

                val bound = (30* screenRatioX).toInt()
                rudolph.speed = random.nextInt(bound)

                if(rudolph.speed < 10 * screenRatioX)
                    rudolph.speed = (10 * screenRatioX).toInt()
                rudolph.x = screenX
                rudolph.y = random.nextInt((screenY - rudolph.height).toInt())

                rudolph.wasShot = false
            }

//            if(Rect.intersects(rudolph.collisionShape, santa.collisionShape)){
//                isGameOver = true
//                return
//            }
        }
    }

    private fun draw(){
        if (holder.surface.isValid) {
            val canvas: Canvas = holder.lockCanvas()
            canvas.drawBitmap(background1.background, b1, background1.y.toFloat(), paint)
            canvas.drawBitmap(background2.background, b2, background2.y.toFloat(), paint)

            if(isGameOver){
                isPlaying = false
                canvas.drawBitmap(santa.dead, santa.x, santa.y, paint)
                holder.unlockCanvasAndPost(canvas)
                return
            }

            for(rudolph in rudolphs)
                canvas.drawBitmap(rudolph!!.rudolph, rudolph.x.toFloat(), rudolph.y.toFloat(), paint)

            canvas.drawBitmap(santa.santa, santa.x, santa.y, paint)

            for(snowball in snowBalls)
                canvas.drawBitmap(snowball.snowball, snowball.x.toFloat(), snowball.y.toFloat(), paint)

            holder.unlockCanvasAndPost(canvas)
        }
    }

    private fun sleep(){
        Thread.sleep(17)
    }

    fun resume(){
        isPlaying = true
        thread = Thread(this)
        thread!!.start()
    }

    fun pause(){
        isPlaying = false
        thread!!.join()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event!!.action){
            MotionEvent.ACTION_DOWN -> if(event.x < screenX / 2){
                santa.isGoingUp = true
            }
            MotionEvent.ACTION_UP -> {
                santa.isGoingUp = false
                if (event.x > screenX / 2) santa.toShoot++
            }
        }

        return true
    }

    fun newSnowBall(){
        val snowball = SnowBall(resources)
        snowball.x = (santa.x + santa.width).toInt()
        snowball.y = (santa.y + santa.height / 2).toInt()
        snowBalls.add(snowball)
    }
}