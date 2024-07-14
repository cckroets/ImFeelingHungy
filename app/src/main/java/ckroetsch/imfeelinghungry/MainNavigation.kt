package ckroetsch.imfeelinghungry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
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
import ckroetsch.imfeelinghungry.onboarding.AllFoods
import ckroetsch.imfeelinghungry.onboarding.AllPreferences
import ckroetsch.imfeelinghungry.onboarding.AllRestaurants
import ckroetsch.imfeelinghungry.onboarding.Diet
import ckroetsch.imfeelinghungry.onboarding.Food
import ckroetsch.imfeelinghungry.onboarding.Restaurant


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
            composable("restaurant") { RestaurantScreen(viewModel = viewModel, navController = navController) }
            composable("food") { FoodScreen(viewModel = viewModel, navController = navController) }
            composable("dietaryPreference") { DietaryPreferenceScreen(viewModel = viewModel, navController = navController) }
            composable("generateOrder") { GeneratedOrderScreen(viewModel = viewModel, navController = navController) }
            // Add other destinations here
        }
    }
}


@Composable
fun DietaryPreferenceScreen(
    modifier: Modifier = Modifier,
    viewModel: PreferencesViewModel,
    navController: NavController
) {
    val selectedFoods = viewModel.preferences.dietaryPreferences.toMap()
    val isSelected = { diet: Diet -> selectedFoods[diet.name] == Preference.LIKED }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
        // Add other composable content her
    ) {
        DietaryPreferenceSelection(
            onSelect = {
                viewModel.setDietaryPreference(it, if (isSelected(it)) Preference.DEFAULT else Preference.LIKED)
            },
            diets = AllPreferences,
            isSelected = isSelected,
        )
        OnboardingNavigator(
            navController = navController,
            nextPage = "dietaryPreference",
            prevPage = "food",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun FoodScreen(
    modifier: Modifier = Modifier,
    viewModel: PreferencesViewModel,
    navController: NavController,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        val selectedFoods = viewModel.preferences.foodPreferences.toMap()
        val isSelected = { food: Food -> selectedFoods[food.name] == Preference.LIKED }
        FoodSelection(
            modifier = Modifier.weight(1f),
            onSelect = { viewModel.setFoodPreference(it, if (isSelected(it)) Preference.DEFAULT else Preference.LIKED) },
            foods = AllFoods,
            isSelected = isSelected,
        )

        OnboardingNavigator(
            navController = navController,
            nextPage = "dietaryPreference",
            prevPage = "restaurant",
            modifier = Modifier.fillMaxWidth(),
        )

    }
}

@Composable
fun RestaurantScreen(
    modifier: Modifier = Modifier,
    viewModel: PreferencesViewModel,
    navController: NavController
) {
    val selectedRestaurants = viewModel.preferences.restaurantPreferences.toMap()
    val isSelected = { restaurant: Restaurant -> selectedRestaurants[restaurant.name] == Preference.LIKED }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
        // Add other composable content her
    ) {
        RestaurantSelection(
            modifier = Modifier.weight(1f),
            onSelect = { viewModel.setRestaurantPreference(it, if (isSelected(it)) Preference.DEFAULT else Preference.LIKED) },
            restaurants = AllRestaurants,
            isSelected = isSelected,
        )
        OnboardingNavigator(
            navController = navController,
            nextPage = "food",
            prevPage = "welcome",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun GeneratedOrderScreen(
    modifier: Modifier = Modifier,
    viewModel: PreferencesViewModel,
    navController: NavController
) {
    var text by remember { mutableStateOf("Generating...") }

    LaunchedEffect(Unit) {
        viewModel.generateMenuItem().collect { data ->
            text = data
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = text,
            fontSize = 36.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun OnboardingNavigator(
    modifier: Modifier = Modifier,
    navController: NavController,
    nextPage: String,
    prevPage: String = "welcome"
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .background(Color.Gray)
            .padding(14.dp)
    ) {
        IconButton(
            onClick = { navController.navigate(prevPage) },
        ) {
            Icon(
                modifier = Modifier.size(48.dp),
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = Color(0xFF039be5)
            )
        }

        IconButton(
            onClick = { navController.navigate(nextPage) },
        ) {
            Icon(
                modifier = Modifier.size(48.dp),
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = Color(0xFF039be5)
            )
        }


    }
}
