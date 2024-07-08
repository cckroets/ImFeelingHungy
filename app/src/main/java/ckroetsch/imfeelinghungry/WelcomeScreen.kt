package ckroetsch.imfeelinghungry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun WelcomeScreen(
    modifier:Modifier = Modifier,
    navController: NavController,
    //onContinueClicked: () -> Unit
) {
    Column(
        modifier.fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ){
        Button(onClick = {
            navController.navigate("restaurant")
        }) {
            Text(text = "Restaurant")
        }

        Button(onClick = {
            navController.navigate("food")
        }) {
            Text(text = "food")
        }

        Button(onClick = {
            navController.navigate("dietaryPreference")
        }) {
            Text(text = "Dietary Preference")
        }


    }
}


@Preview
@Composable
fun WelcomeScreenPreview() {
    val navController = rememberNavController()
    WelcomeScreen(modifier = Modifier, navController = navController)
}