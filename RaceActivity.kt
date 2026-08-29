package com.Car_Racing

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import androidx.core.graphics.createBitmap
import androidx.core.graphics.get


@Composable
fun RaceActivity () {
    val context = LocalContext.current

    val background :Painter =painterResource(R.drawable.track_1)

    var carPosition by remember { mutableStateOf( Offset( 800f , 1680f)) }
    var carRotation by remember { mutableStateOf(50f) }


    var currentSpeed by remember { mutableStateOf(0f) }
    var acceleration by remember { mutableStateOf(1f) }
    var friction by remember { mutableStateOf(0.1f) }
    val maxSpeed =18f

    var throttle_isClicked by remember { mutableStateOf(false) }
    var radians = Math.toRadians(carRotation.toDouble() )

    val car_image = remember {
        ImageBitmap.imageResource(context.resources, R.drawable.car_red_1_m_model)
    }

    // ----------------------------------------
    val trackBitmap = remember(4096f) {
        val bitmap = createBitmap(4096, 4096)
        val canvas = android.graphics.Canvas(bitmap)
        val drawable = ContextCompat.getDrawable(context, R.drawable.ready_path_of_track_1)
        drawable?.setBounds(0, 0, canvas.width, canvas.height)
        drawable?.draw(canvas)
        bitmap
    }

    var pixel by remember { mutableStateOf( trackBitmap [
            carPosition.x.toInt() , carPosition.y.toInt()
    ]) }



    //------------------------------------
    LaunchedEffect(Unit) {
        while (true){
            if (throttle_isClicked && currentSpeed < maxSpeed ) {
                currentSpeed += acceleration

            }

            var radians = Math.toRadians(carRotation.toDouble())

            val half_Car_H =car_image.height * .45f /2
            val nextX = carPosition.x + Math.sin(radians )*half_Car_H
            val nextY = carPosition.y - Math.cos(radians) * half_Car_H

            pixel =trackBitmap.getPixel( nextX.toInt() , nextY.toInt() )
            val surfaceMultiplier =when {
                 isPurple(pixel)-> 1f
                isOrange(pixel)-> 0.5f
                else -> 0f
            }

            carPosition += Offset(
                currentSpeed * surfaceMultiplier * sin(radians).toFloat() ,
                currentSpeed * surfaceMultiplier * -cos(radians).toFloat() )


            if(currentSpeed > 0) {
                currentSpeed -= friction
                if (currentSpeed < 0) currentSpeed = 0f
            }

            delay(16)


        }
    }


    //--------------------------------- UI
    Box(modifier = Modifier.fillMaxSize()) {
        //------------------- draw car
        Canvas(modifier = Modifier.fillMaxSize()) {

            //-------------------------------------- the background
            val cameraX = (carPosition.x - size.width / 2).coerceIn(0f, 4096f - size.width)
            val cameraY = (carPosition.y - size.height / 2).coerceIn(0f, 4096f - size.height)

            withTransform({
                translate(-cameraX, -cameraY)
            }) {
                with(background) {
                    draw(size = Size(4096f, 4096f))
                }


                //----------------------------------- the car
                withTransform({
                    translate(carPosition.x, carPosition.y)
                    rotate(carRotation, Offset.Zero)
                    scale(0.45f, 0.45f, Offset.Zero)
                }) {
                    drawImage(
                        car_image,
                        Offset(
                            -car_image.width / 2f,
                            -car_image.height / 2f
                        )
                    )

                }


                // دائرة خارجية
                drawCircle(
                    color = androidx.compose.ui.graphics.Color.Cyan,
                    radius = 24f,
                    center =Offset( (( carPosition.x ) +(sin (radians)* car_image.height * 0.45/ 2f ) ).toFloat(),
                        ( (carPosition.y )- (cos (radians) *car_image.height * 0.45  / 2f ) ).toFloat() )  ,
                    style = Stroke(width = 4f)
                )
                // نقطة مركزية
                drawCircle(
                    color =Color(pixel) ,
                    radius = 20f,
                    center =      Offset(  (( carPosition.x  ) + (sin(radians) * (car_image.height * 0.45  / 2f)) ).toFloat(),
                        ( (carPosition.y  )  - (cos(radians) *(car_image.height * 0.45 / 2f)  )).toFloat() )

                )


                Row (modifier = Modifier.background(Color.White.copy(0.8F))
                    .padding(10.dp)
            
                    .align(Alignment.TopCenter)){
                    Text(" sensor : " , modifier = Modifier.padding(top = 10.dp))
                    Card (modifier = Modifier.background(Color(pixel) )
                        .padding(20.dp)){
            
                    }
                }
                
            }
            

        //------------------------ steering_wheel
        var wheelRotation by remember { mutableStateOf(0f) }
        var currentAngle by remember { mutableStateOf(0f) }
        var previousAngle by remember { mutableStateOf(0f) }

        val maxSteeringAngle = 90f
        Image(
            painter = painterResource(R.drawable.steering_wheel),
            contentDescription = "steering_wheel",
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.BottomEnd)
                .padding(bottom = 40.dp, end = 40.dp)
                .rotate(wheelRotation)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            wheelRotation = 0f
                        }
                    ) { change, _ ->
                        //  change.consume()

                        //1. define the center
                        val centerX = size.width / 2
                        val centerY = size.height / 2

                        //2.Determine the current angle relative to the center
                        currentAngle = Math.toDegrees(
                            atan2(
                                (change.position.y - centerY).toDouble(),
                                (change.position.x - centerX).toDouble()
                            )
                        ).toFloat()

                        //3.Determine the previous angle
                        previousAngle = Math.toDegrees(
                            atan2(
                                (change.previousPosition.y - centerY).toDouble(),
                                (change.previousPosition.x - centerX).toDouble()
                            )
                        ).toFloat()

                        //4. Calculating the difference in angle
                        val angleDifference = currentAngle - previousAngle

                        //5.Update the wheel rotation
                        wheelRotation = (wheelRotation + angleDifference)
                            .coerceIn(-maxSteeringAngle, maxSteeringAngle)

                        //6. chane the car rotation
                        carRotation += (wheelRotation / 20f)
                    }
                }
        )



        //------------------- draw throttle
        Image(
            painter = if (throttle_isClicked) painterResource(R.drawable.throttle_on)
            else painterResource(R.drawable.throttle_off),
            contentDescription = "throttle",
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 64.dp, bottom = 40.dp)
                .size(if (throttle_isClicked) 60.dp else 75.dp)
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            awaitFirstDown()
                            throttle_isClicked = true



                            //------------ when click Up
                            waitForUpOrCancellation()
                            throttle_isClicked = false
                        }
                    }
                })
        
    }



        fun isPurple(pixel: Int): Boolean {
        return ( android.graphics.Color.red(pixel) >=50 &&
                android.graphics.Color.blue(pixel) >=50 &&
                android.graphics.Color.red(pixel) == 0)
    }

    fun isOrange(pixel: Int): Boolean {
        return ( android.graphics.Color.red(pixel) >=50 &&
                android.graphics.Color.blue(pixel) ==0 &&
                android.graphics.Color.red(pixel) >= 50 )
    }
    
}







