package ckroetsch.imfeelinghungry

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ckroetsch.imfeelinghungry.ui.theme.ImFeelingHungryTheme

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    Box(
        modifier = modifier
            .background(Color(0xffffd32b))
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text(

                    text = "I'M FEELIN' HUNGRY",
                    style = TextStyle(fontSize = 55.sp),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
            }

            //Spacer for between title and app icon
            Spacer(modifier = Modifier.height(25.dp))

            // Placeholder
            Image(
                painter = painterResource(R.drawable.app_icon),
                contentDescription = "App Icon",
                modifier = Modifier.size(250.dp)
            )

            //Spacer for between app icon and text
            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "Discover healthier alternatives to enjoy fast food and eat smart",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            //Spacer for between smaller text and button
            Spacer(modifier = Modifier.height(45.dp))

            Button(
                shape = RoundedCornerShape(26.dp),
                modifier = Modifier.size(width = 200.dp, height = 65.dp),
                onClick = {
                    navController.navigate("restaurant")
                }
            ) {
                Text(
                    text = "Get Started",
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }
    }
}

@Preview
@Composable
fun WelcomeScreenPreview() {
    ImFeelingHungryTheme {
        val navController = rememberNavController()
        WelcomeScreen(modifier = Modifier, navController = navController)
    }
}
