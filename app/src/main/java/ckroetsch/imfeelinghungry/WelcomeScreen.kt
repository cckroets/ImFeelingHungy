package ckroetsch.imfeelinghungry

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ckroetsch.imfeelinghungry.ui.theme.DarkOrange
import ckroetsch.imfeelinghungry.ui.theme.ImFeelingHungryTheme
import ckroetsch.imfeelinghungry.ui.theme.MustardYellow
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    sharedElementScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    navController: NavController,
) {
    Column(
        modifier.fillMaxSize().padding(16.dp)
            .scrollable(rememberScrollState(), orientation = Orientation.Vertical),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Title
        Text(
            text = "I'M FEELIN' HUNGRY",
            style = MaterialTheme.typography.displaySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
        //Spacer for title and image
        Spacer(modifier = Modifier.height(10.dp))

        Image(
            painter = painterResource(R.drawable.app_icon),
            contentDescription = "App Icon",
            modifier = Modifier
                .size(300.dp)
                .clip(CircleShape)
        )

        Text(
            text = "Discover healthier alternatives to enjoy fast food and eat smart",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(55.dp))

        ElevatedButton(
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DarkOrange), // Bright red color
            onClick = {
                navController.navigate("generateOrder")
            }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .requiredHeight(32.dp)
                    .clipToBounds()
            ) {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_white))
                with(sharedElementScope) {
                    val sharedContentState = rememberSharedContentState("pan")
                    LottieAnimation(
                        modifier = Modifier
                            .requiredHeight(64.dp)
                            .requiredWidth(64.dp)
                            .offset(x = (-8).dp, y = (-8).dp)
                            .then(Modifier
                                .sharedBounds(
                                    sharedContentState = sharedContentState,
                                    animatedVisibilityScope = animatedVisibilityScope))
                        ,
                        composition = composition,
                        isPlaying = false,
                    )
                }
                Spacer(Modifier.width(0.dp))
                Text(
                    text = "I'm feelin' hungry!",
                    style = MaterialTheme.typography.titleLarge,
                )
            }

        }

        Spacer(modifier = Modifier.height(25.dp))

        TextButton(
            onClick = {
                navController.navigate("restaurant")
            }) {
            Text(
                text = "Change Preferences",
            )
        }

        Spacer(modifier = Modifier.height(25.dp))
        TextButton(
            onClick = {
                navController.navigate("discover")
            }) {
            Text(text = "Discover")
            }

    }
}


@Preview
@Composable
fun WelcomeScreenPreview() {
    ImFeelingHungryTheme {
        val navController = rememberNavController()
        //WelcomeScreen(modifier = Modifier, navController = navController)
    }
}


