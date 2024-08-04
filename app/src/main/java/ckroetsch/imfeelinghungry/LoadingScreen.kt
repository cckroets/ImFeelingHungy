package ckroetsch.imfeelinghungry

import android.graphics.Paint
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ckroetsch.imfeelinghungry.ui.theme.ImFeelingHungryTheme
import ckroetsch.imfeelinghungry.ui.theme.MustardYellow
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun VegetableTossAnimation() {
    val vegetables = listOf("🥕", "🍆", "🌽", "🍅", "🥦")
    val drawOrder = remember { vegetables.indices.shuffled() }
    val heightAnimators = vegetables.map { remember { Animatable(initialValue = 0f) } }
    val rotationAnimators = vegetables.map { remember { Animatable(initialValue = 0f) } }
    val horizontalAnimators = vegetables.map { remember { Animatable(initialValue = 0f) } }
    val rng = remember { Random(23L) }
    val customEasing = CubicBezierEasing(0.33f, 0.0f, 0.67f, 1.0f)

    LaunchedEffect(Unit) {
        while (true) {
            vegetables.forEachIndexed { index, _ ->
                launch {
                    horizontalAnimators[index].animateTo(
                        targetValue = index * 1f,
                        animationSpec = infiniteRepeatable(
                            animation = keyframes {
                                durationMillis = 1100
                                0f at 0 with customEasing // Start
                                index * 1f at 500 with customEasing // End
                                index * 0.9f at 900 with customEasing // End
                                0f * 1f at 1100 with customEasing // End
                            },
                            repeatMode = RepeatMode.Restart
                        )
                    )
                }
                launch {
                    val direction = rng.nextBoolean().let { if (it) 1 else -1 }
                    rotationAnimators[index].animateTo(
                        targetValue = -10f,
                        animationSpec = infiniteRepeatable(
                            animation = keyframes {
                                durationMillis = 1100
                                -10f at 0 with customEasing // Start
                                rng.nextInt(20, 70).toFloat() * direction at 500 with customEasing // End
                                -10f at 1100 with customEasing // End
                            },
                            repeatMode = RepeatMode.Restart
                        )
                    )
                }
                launch {
                    heightAnimators[index].animateTo(
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = keyframes {
                                durationMillis = 1100
                                0f at 0 with customEasing // Start
                                rng.nextInt(65, 100) / 100f at rng.nextInt(450, 550) with customEasing // Middle (top of the parabola)
                                0f at 1000 with customEasing // End
                                0f at 1100 with customEasing // End
                            },
                            repeatMode = RepeatMode.Restart
                        )
                    )
                }
            }
            delay(1100) // Wait before restarting the animation
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(300.dp)) {
            val panY = size.height / 2 + 100f

            // Draw the frying pan
            drawPan(topLeft = Offset(x = size.width / 2 - 60f, y = panY))

            // Draw vegetables as text
            vegetables.forEachIndexed { i, vegetable ->
                val index = drawOrder.indexOf(i)
                val xOffset = (index - 2) * 120f
                val yOffset = heightAnimators[index].value * -550f // Adjust the scale for parabolic effect
                val pivot = Offset(x = size.width / 2 + xOffset, y = panY + yOffset)
                    drawIntoCanvas {
                        //translate(left = pivot.x, top = pivot.y) {
                            rotate(rotationAnimators[index].value, pivot) {
                                it.nativeCanvas.drawText(
                                    vegetable,
                                    pivot.x,
                                    pivot.y,
                                    Paint().apply {
                                        color = androidx.compose.ui.graphics.Color.Black.toArgb()
                                        textSize = 180f
                                        textAlign = android.graphics.Paint.Align.CENTER
                                        isAntiAlias = true
                                    }
                                )
                            }

                        //}
                    }
                }
            }
        }
}

fun DrawScope.drawPan(topLeft: Offset) {
    val panWidth = 120f
    val panHeight = 40f
    val handleWidth = 60f
    val handleHeight = 10f

    // Draw pan body
    drawRoundRect(
        color = Color.DarkGray,
        topLeft = topLeft,
        size = Size(panWidth, panHeight),
        cornerRadius = CornerRadius(10f, 10f)
    )

    // Draw handle
    drawRoundRect(
        color = Color.DarkGray,
        topLeft = Offset(topLeft.x + panWidth, topLeft.y + panHeight / 4),
        size = Size(handleWidth, handleHeight),
        cornerRadius = CornerRadius(5f, 5f)
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun PreviewVegetableTossAnimation() {
    ImFeelingHungryTheme {
        VegetableTossAnimation()
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun LoadingAnimation(
    sharedElementScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    with(sharedElementScope) {
        Box(Modifier.fillMaxSize()) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
            val sharedState = rememberSharedContentState("pan")
            LottieAnimation(
                composition = composition,
                iterations = 100,
                modifier = Modifier
                    .sharedBounds(
                        sharedContentState = sharedState,
                        //placeHolderSize = SharedTransitionScope.PlaceHolderSize(),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
            )
        }
    }
}
