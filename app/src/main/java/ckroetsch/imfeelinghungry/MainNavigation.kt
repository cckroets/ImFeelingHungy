package ckroetsch.imfeelinghungry

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ckroetsch.imfeelinghungry.data.PreferencesViewModel
import ckroetsch.imfeelinghungry.onboarding.DietaryPreferenceSelection
import ckroetsch.imfeelinghungry.onboarding.FoodSelection
import ckroetsch.imfeelinghungry.onboarding.RestaurantSelection
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import ckroetsch.imfeelinghungry.data.Preference
import ckroetsch.imfeelinghungry.data.Result
import ckroetsch.imfeelinghungry.onboarding.AllFoods
import ckroetsch.imfeelinghungry.onboarding.AllPreferences
import ckroetsch.imfeelinghungry.onboarding.AllRestaurants
import ckroetsch.imfeelinghungry.onboarding.Diet
import ckroetsch.imfeelinghungry.onboarding.DietaryPreferenceScreen
import ckroetsch.imfeelinghungry.onboarding.Food
import ckroetsch.imfeelinghungry.onboarding.FoodScreen
import ckroetsch.imfeelinghungry.onboarding.Restaurant
import ckroetsch.imfeelinghungry.onboarding.RestaurantScreen


@Composable
fun MainNavigation(preferencesViewModel: PreferencesViewModel) {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val viewModel: PreferencesViewModel = viewModel()
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
            composable("restaurant") {
                RestaurantScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }
            composable("food") { FoodScreen(viewModel = viewModel, navController = navController) }
            composable("dietaryPreference") {
                DietaryPreferenceScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }
            composable("generateOrder") {
                GeneratedOrderScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }
            // Add other destinations here
        }
    }
}



@Composable
fun GeneratedOrderScreen(
    modifier: Modifier = Modifier,
    viewModel: PreferencesViewModel,
    navController: NavController
) {
    val menuItem by viewModel.generateMenuItem().collectAsState(Result.Loading)

    when (val m = menuItem) {
        is Result.Error -> {
            Text(text = "Error: ${m.message}")
        }

        Result.Loading -> {
            Text(text = "Loading...")
            // Add other composable content her
        }

        is Result.Success -> {
            MenuItemScreen(m.data)
        }
    }
}

