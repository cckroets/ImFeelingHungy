package ckroetsch.imfeelinghungry

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ckroetsch.imfeelinghungry.data.PreferencesViewModel
import ckroetsch.imfeelinghungry.onboarding.DietaryPreferenceSelection
import ckroetsch.imfeelinghungry.onboarding.FoodSelection
import ckroetsch.imfeelinghungry.onboarding.RestaurantSelection
import kotlinx.coroutines.launch


@Composable
fun MainScreen(preferencesViewModel: PreferencesViewModel) {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()

    var startDestination by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            startDestination = if (preferencesViewModel.isDataStoreEmpty()) {
                "welcome"
            } else {
                "restaurant"
            }
        }
    }

    if (startDestination != null) {
        NavHost(navController = navController, startDestination = startDestination!!) {
            composable("welcome") { WelcomeScreen(navController = navController) }
            composable("restaurant") { RestaurantScreen(navController = navController) }
            composable("food") { FoodScreen(navController = navController) }
            composable("dietaryPreference") { DietaryPreferenceScreen(navController = navController)}
            // Add other destinations here
        }
    }
}


@Composable
fun DietaryPreferenceScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    DietaryPreferenceSelection()
}

@Composable
fun FoodScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    FoodSelection()
}

@Composable
fun RestaurantScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    RestaurantSelection()
}
