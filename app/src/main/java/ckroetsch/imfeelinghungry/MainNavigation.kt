package ckroetsch.imfeelinghungry

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ckroetsch.imfeelinghungry.data.PreferencesViewModel
import ckroetsch.imfeelinghungry.data.Result
import ckroetsch.imfeelinghungry.onboarding.DietaryPreferenceScreen
import ckroetsch.imfeelinghungry.onboarding.FoodScreen
import ckroetsch.imfeelinghungry.onboarding.RestaurantScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainNavigation(preferencesViewModel: PreferencesViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val viewModel: PreferencesViewModel = viewModel()
    var startDestination by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            startDestination = "welcome"
        }
    }

    if (startDestination != null) {
        SharedTransitionLayout {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = startDestination!!) {
                composable("welcome") { WelcomeScreen(navController = navController) }
                composable("restaurant") { RestaurantScreen(viewModel = viewModel, navController = navController) }
                composable("food") { FoodScreen(viewModel = viewModel, navController = navController) }
                composable("dietaryPreference") { DietaryPreferenceScreen(viewModel = viewModel, navController = navController) }
                composable("generateOrder") { GeneratedOrderScreen(viewModel = viewModel, navController = navController) }
                // Add other destinations here
            }
        }
    }
}

@Composable
fun GeneratedOrderScreen(
    modifier: Modifier = Modifier,
    viewModel: PreferencesViewModel,
    navController: NavController
) {
    val menuItem by viewModel.generatedMenuItem.collectAsState(Result.Loading)
    LaunchedEffect(Unit) {
        viewModel.generateMenuItem()
    }

    Crossfade(menuItem) {
        when (val m = it) {
            is Result.Error -> {
                Text(text = "Error: ${m.message}")
            }

            Result.Loading -> {
                LoadingAnimation()
                // Add other composable content her
            }

            is Result.Success -> {
                val goals = remember(viewModel) { viewModel.dietGoals }
                MenuItemScreen(
                    m.data,
                    goals,
                    navController
                ) { viewModel.regenerateWithInstructions(it) }
            }
        }
    }
}
