package ckroetsch.imfeelinghungry

import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import de.apuri.physicslayout.lib.BodyConfig
import de.apuri.physicslayout.lib.PhysicsLayout
import de.apuri.physicslayout.lib.drag.DragConfig
import de.apuri.physicslayout.lib.physicsBody
import de.apuri.physicslayout.lib.simulation.rememberSimulation
import kotlinx.coroutines.launch

@Composable
fun PhysicsBox(modifier: Modifier = Modifier, content: @Composable BoxScope.(boxSize: IntSize) -> Unit) {
    var boxSize by remember { mutableStateOf(IntSize.Zero) }

    Box(modifier = modifier
        .onGloballyPositioned { coordinates ->
            boxSize = coordinates.size
        }
        .background(Color.LightGray)
    ) {
        content(boxSize)
    }
}

@Composable
fun PhysicsItem(
    modifier: Modifier = Modifier,
    boxSize: IntSize,
    initialPosition: Offset = Offset.Zero,
    content: @Composable BoxScope.() -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var position by remember { mutableStateOf(initialPosition) }
    val offsetX = remember { Animatable(initialPosition.x) }
    val offsetY = remember { Animatable(initialPosition.y) }
    var isBeingDragged by remember { mutableStateOf(false) }
    var velocityY by remember { mutableStateOf(0f) }
    val gravity = 2000f // pixels per second squared
    val drag = 0.9f

    LaunchedEffect(boxSize.height) {
        while (true) {
            val delta = withFrameNanos { it / 1_000_000_000f }
            velocityY += gravity * delta
            position = Offset(offsetX.value, (position.y + velocityY * delta).coerceIn(0f, boxSize.height.toFloat()))
            offsetY.snapTo(position.y)

            if (isBeingDragged) continue

            if (position.y >= boxSize.height) {
                velocityY = 0f
                offsetY.snapTo(boxSize.height.toFloat())
            }

            if (position.y < boxSize.height && velocityY > 0) {
                velocityY *= drag
            }
        }
    }

    Box(
        modifier = modifier
            .offset { IntOffset(offsetX.value.toInt(), offsetY.value.toInt()) }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        coroutineScope.launch {
                            isBeingDragged = true
                            velocityY = 0f
                            offsetY.stop()
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        coroutineScope.launch {
                            offsetX.snapTo((offsetX.value + dragAmount.x).coerceIn(0f, boxSize.width.toFloat()))
                            offsetY.snapTo((offsetY.value + dragAmount.y).coerceIn(0f, boxSize.height.toFloat()))
                            position = Offset(offsetX.value, offsetY.value)
                        }
                    },
                    onDragEnd = {
                        isBeingDragged = false
                    }
                )
            }
    ) {
        content()
    }
}

@Composable
fun PhsyicsExample() {
    // All emoji veggies:
    val vegetables = remember {
        var id = 0
        listOf("🥕", "🍆", "🌽", "🍅", "🥦", "🥬")
            .flatMap { v -> buildList { repeat(5) { add(v to id++) } } }
            .shuffled()
    }

    val simulation = rememberSimulation()

    GravitySensor { (x, y) ->
        simulation.setGravity(Offset(-x, y).times(3f))
    }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))

    PhysicsLayout(simulation = simulation, modifier = Modifier.fillMaxSize()) {
        vegetables.forEach { (v, id) ->
            Text(
                text = v,
                fontSize = 64.sp,
                modifier = Modifier
                    //.offset(x = id.dp)
                    .align(Alignment.TopCenter)
                    .physicsBody(
                        id = id.toString(),
                        bodyConfig = BodyConfig(),
                        dragConfig = DragConfig()
                    )
            )
        }

        //LottieAnimation(
        //    composition = composition,
        //    iterations = 100,
        //    modifier = Modifier
        //        .size(256.dp)
        //        .align(Alignment.Center)
        //        .physicsBody(
        //            id = "pan",
        //            bodyConfig = BodyConfig(isStatic = true),
        //        )
        //)
    }
}

@Composable
fun GravitySensor(
    onGravityChanged: (List<Float>) -> Unit
) {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
        val gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
            ?: return@DisposableEffect onDispose { }

        val gravityListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val (x, y, z) = event.values
                onGravityChanged(listOf(x,y,z))
            }

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) { }
        }

        sensorManager.registerListener(
            gravityListener,
            gravitySensor,
            SensorManager.SENSOR_DELAY_NORMAL,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        onDispose {
            sensorManager.unregisterListener(gravityListener)
        }
    }
}
