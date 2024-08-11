package ckroetsch.imfeelinghungry

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

val DefaultEmojis = listOf("🍟", "\uD83C\uDF45", "🍔", "\uD83D\uDE0B", "🥦", "🥕", "\uD83D\uDCAA", "\uD83C\uDF0E", "\uD83E\uDD51", "\uD83C\uDF2F")

enum class SlotMachineState {
    Idle, Spinning, Settling
}

@Stable
class SlotMachineController(
    private val scope: CoroutineScope,
    private val emojis: List<String>,
) {
    private val state = mutableStateOf(SlotMachineState.Idle)

    val emoji1 = Animatable(initialValue = 4f)
    val emoji2 = Animatable(initialValue = 1f)
    val emoji3 = Animatable(initialValue = 8f)

    fun spin() {
        state.value = SlotMachineState.Spinning
        scope.launch { delay(200); spinIndefinitely(emoji1, emojis.size) }
        scope.launch { delay(600); spinIndefinitely(emoji2, emojis.size) }
        scope.launch { delay(1000); spinIndefinitely(emoji3, emojis.size) }
    }

    val isIdle get() = state.value == SlotMachineState.Idle

    suspend fun stop() {
        state.value = SlotMachineState.Settling
        stopColumn(emoji1, emojis.size)
        stopColumn(emoji2, emojis.size)
        stopColumn(emoji3, emojis.size)
        state.value = SlotMachineState.Idle
    }
}

@Composable
fun rememberSlotMachineController(
    scope: CoroutineScope = rememberCoroutineScope(),
    emojis: List<String> = DefaultEmojis
) = remember { SlotMachineController(scope, emojis) }

@Composable
fun SlotMachine(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.background,
    controller: SlotMachineController = rememberSlotMachineController(),
    emojis: List<String> = DefaultEmojis,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(Modifier.wrapContentHeight()
            .fillMaxWidth()
            .requiredHeight(IntrinsicSize.Min)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color, RoundedCornerShape(16.dp))
                    .drawWithContent {
                        drawContent()
                        drawRoundRect(
                            brush = Brush.verticalGradient(listOf(color, color, Color.Transparent, Color.Transparent, Color.Transparent, Color.Transparent, Color.Transparent, color, color)),
                            topLeft = Offset(0f, 0f),
                            size = Size(size.width, size.height),
                        )
                    }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Display the emojis for each column
                SlotColumn(emojiIndex = controller.emoji1.value, emojis = emojis)
                SlotColumn(emojiIndex = controller.emoji2.value, emojis = emojis)
                SlotColumn(emojiIndex = controller.emoji3.value, emojis = emojis)
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun SlotColumn(emojiIndex: Float, emojis: List<String>) {
    // Calculate the three emojis to display based on the animated value
    val index = emojiIndex.toInt()
    val offset = emojiIndex - index
    Column(
        modifier = Modifier
            .sizeIn(maxHeight = 192.dp)
            .clipToBounds()
            .wrapContentHeight(unbounded = true, align = Alignment.CenterVertically)
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        repeat(5) { i ->
            Text(
                text = emojis[(index + i) % emojis.size],
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.requiredHeight(64.dp).offset(y = (-offset * 64).dp)
            )
        }
    }
}

suspend fun spinIndefinitely(animatable: Animatable<Float, AnimationVector1D>, itemCount: Int) {
    while (true) {
        animatable.animateTo(
            targetValue = animatable.value + itemCount,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 400,
                    easing = LinearEasing
                )
            )
        )
    }
}

suspend fun stopColumn(animatable: Animatable<Float, AnimationVector1D>, itemCount: Int) {
    if (!animatable.isRunning) return
    animatable.stop()
    val targetIndex = Random.nextInt(itemCount)
    val targetValue = animatable.value.toInt() + itemCount + targetIndex
    val individualDuration = 400f / itemCount
    val totalDuration = individualDuration * (targetIndex + itemCount)
    animatable.animateTo(
        targetValue = targetValue.toFloat(),
        animationSpec = tween(
            durationMillis = totalDuration.toInt(),
            easing = FastOutSlowInEasing
        )
    )
    animatable.snapTo(targetValue.toFloat())
}
