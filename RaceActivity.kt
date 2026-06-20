package com.Car_Racing

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlin.math.atan2

@Composable
fun RaceActivity () {
    val context = LocalContext.current

    var carRotation by remember { mutableStateOf(0f) }

    val car_image = remember {
        ImageBitmap.imageResource(context.resources, R.drawable.car_red_1_m_model)
    }


    Box(modifier = Modifier.fillMaxSize()) {
        //------------------- draw car
        Canvas(modifier = Modifier.fillMaxSize().background(Color.White)) {

            withTransform({
                translate(size.width / 2, size.height / 2)
                rotate(carRotation , Offset.Zero)
                scale(0.45f, 0.45f, Offset.Zero)
            }) {
                drawImage(car_image ,
                    Offset( - car_image.width/2f ,
                        - car_image.height/2f))

            }
        }

        //------------------------ steering_wheel
        var wheelRotation by remember { mutableStateOf(0f) }
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
                            wheelRotation= 0f
                        }
                    ) { change , _ ->
                      //  change.consume()

                        //1. define the center
                        val centerX = size.width / 2
                        val centerY = size.height / 2

                        //2.Determine the current angle relative to the center
                        val currentAngle = Math.toDegrees(
                            atan2((change.position.y - centerY).toDouble() ,
                                (change.position.x - centerX).toDouble() )
                        ).toFloat()

                        //3.Determine the previous angle
                        val previousAngle =Math.toDegrees(
                            atan2((change.previousPosition.y - centerY).toDouble() ,
                                (change.previousPosition.x - centerX).toDouble() )
                        ).toFloat()

                        //4. Calculating the difference in angle
                        val angleDifference = currentAngle - previousAngle

                        //5.Update the wheel rotation
                        wheelRotation = (wheelRotation + angleDifference )
                            .coerceIn(-maxSteeringAngle, maxSteeringAngle)

                        //6. chane the car rotation
                        carRotation += (wheelRotation / 40f)
                    }
                }
        )
    }

}








//
//var screensize
//        by remember { mutableStateOf(Size.Zero) }
//
//Text("size =${screensize.width}  , ${screensize.height}" )
//
//
//screensize =size
//withTransform({
//    translate (size.width/2 , size.height/2 )
//    scale( 0.45f , 0.45f , Offset.Zero)
//}){
//    drawImage(car_image )
//}