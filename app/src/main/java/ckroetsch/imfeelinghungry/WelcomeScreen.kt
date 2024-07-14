package ckroetsch.imfeelinghungry

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
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


    Column(
        modifier
            .background(Color(0xffffd32b))
            .fillMaxSize()
            .padding(16.dp),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Spacer for title
        Spacer(modifier = Modifier.height(35.dp))
        //Title
        Text(
            text = "I'M FEELIN' HUNGRY",
            style = TextStyle(fontSize = 55.sp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
        //Spacer for title and image
        Spacer(modifier = Modifier.height(10.dp))


        //Placeholder
        Image(
            painter = painterResource(R.drawable.app_icon),
            contentDescription = "App Icon",
            modifier = Modifier
                .size(250.dp)
                .clip(CircleShape)
        )


        Text(
            text = "Discover healthier alternatives to enjoy fast food and eat smart",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(55.dp))

        Button(
            shape = RoundedCornerShape(26.dp),
            modifier = Modifier.size(width = 200.dp, height = 65.dp),
            onClick = {
                navController.navigate("restaurant")
            }) {
            Text(
                text = "Get Started",
                style = MaterialTheme.typography.titleLarge,
            )
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
