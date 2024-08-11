package ckroetsch.imfeelinghungry

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ckroetsch.imfeelinghungry.ui.theme.DarkOrange

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    Column(
        modifier.fillMaxSize()
            .padding(16.dp)
            .scrollable(rememberScrollState(), orientation = Orientation.Vertical),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                Spacer(Modifier.width(0.dp))
                Text(
                    text = "I'm feelin' hungry!",
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }
        Spacer(modifier = Modifier.height(25.dp))
        TextButton(onClick = { navController.navigate("preferences") }) {
            Text(text = "Set Dietary Goals")
        }
    }
}

