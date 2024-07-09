package ckroetsch.imfeelinghungry.onboarding

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import kotlin.math.absoluteValue


@Composable
fun RoadScreen() {
    var offsetY by remember { mutableFloatStateOf(0f) }
    val scrollableState = rememberScrollableState { delta ->
        offsetY += delta
        delta
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .scrollable(state = scrollableState, orientation = Orientation.Vertical)
        .pointerInput(Unit) {
            detectVerticalDragGestures { change, dragAmount ->
                change.consume()
                offsetY += dragAmount
            }
        }) {
        RoadLayout(offsetY = offsetY, totalSigns = 10) { i ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .background(Color.Blue)
                        .size(72.dp, 48.dp)
                )
                Box(
                    modifier = Modifier
                        .background(Color.DarkGray)
                        .size(16.dp, 64.dp)
                )
            }


        }
    }
}

@Composable
fun RoadLayout(
    offsetY: Float,
    totalSigns: Int,
    content: @Composable (Int) -> Unit
) {
    Layout(
        content = {
            for (i in 0 until totalSigns) {
                content(i)
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                val width = size.width.toInt()
                val height = size.height.toInt()
                val horizonY = height / 3
                val roadWidthTop = width / 8
                val roadWidthBottom = width * 8 / 10
                drawRoad(width, height, horizonY, roadWidthTop, roadWidthBottom, offsetY)
            }
    ) { measurables, constraints ->
        val width = constraints.maxWidth
        val height = constraints.maxHeight
        val horizonY = 0
        val roadWidthTop = width / 8
        val roadWidthBottom = width * 8 / 10
        val ratio = roadWidthTop / roadWidthBottom

        val placeables = measurables.map { measurable ->
            measurable.measure(Constraints.fixed(
                width = 100.dp.roundToPx(),
                height = 100.dp.roundToPx()
            ))
        }

        layout(width, height) {
            val signSpacing = 100.dp.roundToPx()

            (0 until totalSigns).forEach { index ->
                val placeable = placeables[index]

                val minScale = 0
                val maxScale = 1

                val y = index * signSpacing + offsetY.toInt()
                if (y + placeable.height > horizonY && y < height) {
                    val scale = (1f * y / height)
                    val signWidth = placeable.width * scale
                    val signHeight = placeable.height * scale

                    val roadWidthAtY = roadWidthBottom * -(1f - (index * signSpacing + (offsetY % signSpacing)) / height)
                    val signOffsetX = width / 2 - roadWidthAtY

                    placeables[index].placeWithLayer(
                        x = 0, //x = (signOffsetX - signWidth / 2).toInt(),
                        y = y,
                        zIndex = index.toFloat()
                        // Placing the sign in the middle of the road (perspective effect
                    ) {
                        scaleX = scale
                        scaleY = scale
                        translationX = lerp(0f, signOffsetX - signWidth / 2, 1 - scale)
                        //translationY = lerp(0f, -signHeight / 2, 1 - scale)
                    }
                }
            }
        }
    }
}

fun DrawScope.drawRoad(
    width: Int,
    height: Int,
    horizonY: Int,
    roadWidthTop: Int,
    roadWidthBottom: Int,
    offsetY: Float
) {
    // Drawing a curved road from bottom of screen to 70 degrees to the right
    val roadPath = Path().apply {
        moveTo(width / 2f, height.toFloat())
        arcTo(
            rect = Rect(
                left = width / 2f - roadWidthBottom / 2f,
                top = height.toFloat(),
                right = width / 2f + roadWidthBottom / 2f,
                bottom = height.toFloat() - roadWidthBottom / 2f,
            ),
            startAngleDegrees = 180f,
            sweepAngleDegrees = 70f,
            forceMoveTo = false
        )
        close()
    }
    drawPath(path = roadPath, color = Color.Gray, style = Fill)

    // Drawing road lines
    val lineSpacing = 60.dp.toPx()
    val lineLength = 20.dp.toPx()
    val lineWidthBottom = 4.dp.toPx()
    val lineWidthTop = 2.dp.toPx()
    val numberOfLines = (height / lineSpacing).toInt() + 1

    for (i in 0 until numberOfLines) {
        val y = i * lineSpacing + (offsetY % lineSpacing) - lineLength / 2
        if (y > horizonY && y < height) {
            val linePath = Path().apply {
                moveTo(width / 2f - lineWidthBottom / 2f, y + lineLength)
                lineTo(width / 2f + lineWidthBottom / 2f, y + lineLength)
                lineTo(width / 2f + lineWidthTop / 2f, y)
                lineTo(width / 2f - lineWidthTop / 2f, y)
                close()
            }
            drawPath(path = linePath, color = Color.Yellow, style = Fill)
        }
    }
}

@Composable
fun Sign(color: Color, modifier: Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Box(
            modifier = Modifier
                .background(color)
                .size(72.dp, 48.dp)
        )
        Box(
            modifier = Modifier
                .background(Color.DarkGray)
                .size(16.dp, 64.dp)
        )

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RoadPager() {
    val pagerState = rememberPagerState(pageCount = { colors.size })
    val offset = remember { Animatable(0f) }
    val currentItem = pagerState.currentPage

    LaunchedEffect(currentItem) {
        offset.animateTo(
            targetValue = currentItem.toFloat(),
            animationSpec = tween(durationMillis = 500, easing = LinearEasing)
        )
    }

    HorizontalPager(
        state = pagerState,
        pageSize = PageSize.Fixed(100.dp),
        pageSpacing = 10.dp,
        modifier = Modifier.fillMaxWidth()
    ) { page ->
        val signColor = colors[page]
        Sign(color = signColor, modifier = Modifier.graphicsLayer {
            val pageOffset = pagerState.getOffsetFractionForPage(page)
                val rotation = lerp(-45f, 0f, 1 - pageOffset.absoluteValue)
            val translationX = 10 * pageOffset * pageOffset
            val translationY = 10 * pageOffset * pageOffset
            val scale = lerp(0.5f, 1f, 1 - pageOffset.absoluteValue)

            //this.rotationY = rotation
            this.translationX = translationX
            this.translationY = translationY
            this.scaleX = scale.coerceAtLeast(0.1f)
            this.scaleY = scale.coerceAtLeast(0.1f)
        })
    }
}

// Dummy list of colors for signs
private val colors = listOf(
    Color.Red,
    Color.Blue,
    Color.Green,
    Color.Yellow
)

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RoadScreen()
}
