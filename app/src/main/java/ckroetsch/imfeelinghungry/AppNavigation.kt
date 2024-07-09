package ckroetsch.imfeelinghungry

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ckroetsch.imfeelinghungry.onboarding.FoodSelection
import ckroetsch.imfeelinghungry.onboarding.RestaurantSelection
import ckroetsch.imfeelinghungry.onboarding.DietaryPreferenceSelection

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") { WelcomeScreen(navController = navController) }
        composable("restaurant") { RestaurantScreen(navController = navController) }
        composable("food") { FoodScreen(navController = navController) }
        composable("dietaryPreference") { DietaryPreferenceScreen(navController = navController) }
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
