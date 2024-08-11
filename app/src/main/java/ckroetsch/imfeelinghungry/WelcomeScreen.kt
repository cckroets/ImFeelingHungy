package ckroetsch.imfeelinghungry

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ckroetsch.imfeelinghungry.data.PreferencesViewModel
import ckroetsch.imfeelinghungry.data.Result
import kotlinx.coroutines.launch

@Composable
fun AnimatedBrushBorder(): Brush {
    // Colors for the gradient
    val gradientColors = listOf(
        MaterialTheme.colorScheme.inversePrimary,
        MaterialTheme.colorScheme.inverseSurface,
        MaterialTheme.colorScheme.tertiaryContainer,

    )

    // Animate the gradient's offset to create a moving effect
    val transition = rememberInfiniteTransition()
    val animatedOffset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Create the animated brush
    return Brush.linearGradient(
        colors = gradientColors,
        start = Offset(animatedOffset * 300f, animatedOffset * 300f),
        end = Offset(animatedOffset * 800f, animatedOffset * 800f),
    )
}

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    viewModel: PreferencesViewModel,
    navController: NavController,
) {
    val coroutineScope = rememberCoroutineScope()
    val menuItem by viewModel.generatedMenuItem.collectAsState(Result.None)
    var navigated by rememberSaveable { mutableStateOf(true) }
    val slotState = rememberSlotMachineController()
    LaunchedEffect(navigated, menuItem) {
        if (!navigated && menuItem is Result.Success) {
            slotState.stop()
            navController.navigate("generateOrder")
            navigated = true
        } else if (!navigated && menuItem is Result.Error) {
            slotState.stop()
            Toast.makeText(navController.context, "Failed to generate menu item", Toast.LENGTH_SHORT).show()
            navigated = true
        }
    }

    Column(
        modifier.fillMaxSize()
            .padding(16.dp)
            .scrollable(rememberScrollState(), orientation = Orientation.Vertical),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Spacer for title and image
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Discover healthier alternatives to enjoy fast food and eat smart",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        SlotMachine(controller = slotState)
        FilledTonalButton(
            shape = RoundedCornerShape(26.dp),
            //colors = ButtonDefaults.buttonColors(containerColor = DarkPurple), // Bright red color
            enabled = slotState.isIdle,
            border = BorderStroke(
                width = 2.dp,
                brush = AnimatedBrushBorder()
            ),
            onClick = {
                Log.i("WelcomeScreen", "Generating...")
                coroutineScope.launch {
                    slotState.spin()
                    viewModel.generateMenuItem()
                }
                navigated = false
            }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .requiredHeight(32.dp)
                    .clipToBounds()
            ) {
                Spacer(Modifier.width(0.dp))
                Text(
                    text = "I'm Feelin' Hungry",
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = { navController.navigate("preferences") }) {
            Text(text = "Set Dietary Goals", style = MaterialTheme.typography.titleMedium)
        }
    }
}

