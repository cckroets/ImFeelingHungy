package ckroetsch.imfeelinghungry.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid

import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text


import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ckroetsch.imfeelinghungry.onboarding.OnboardingNavigator
import ckroetsch.imfeelinghungry.data.Preference
import ckroetsch.imfeelinghungry.data.PreferencesViewModel
import coil.compose.AsyncImage


@Composable
fun RestaurantScreen(
    modifier: Modifier = Modifier,
    viewModel: PreferencesViewModel,
    navController: NavController
) {
    val selectedRestaurants = viewModel.preferences.restaurantPreferences.toMap()
    val isSelected =
        { restaurant: Restaurant -> selectedRestaurants[restaurant.name] == Preference.LIKED }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xffffd32b)) // Matching background color
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Select Your Favorite Restaurants",
            style = MaterialTheme.typography.headlineSmall, // Adjust text style as needed
            color = Color.Black,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        RestaurantSelection(
            modifier = Modifier.weight(1f),
            onSelect = {
                viewModel.setRestaurantPreference(
                    it,
                    if (isSelected(it)) Preference.DEFAULT else Preference.LIKED
                )
            },
            restaurants = AllRestaurants,
            isSelected = isSelected,
        )


        OnboardingNavigator(
            navController = navController,
            nextPage = "food",
            prevPage = "welcome",
            modifier = Modifier
                .fillMaxWidth()
                .height(22.dp)
        )
    }
}

@Composable
fun RestaurantSelection(
    modifier: Modifier = Modifier,
    onSelect: (Restaurant) -> Unit,
    restaurants: List<Restaurant> = AllRestaurants,
    isSelected: (Restaurant) -> Boolean
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(128.dp),
        contentPadding = PaddingValues(16.dp),
        verticalItemSpacing = 16.dp,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier.fillMaxWidth()
    ) {
        items(restaurants) { restaurant ->
            RestaurantCard(restaurant, isSelected(restaurant), { onSelect(restaurant) })
        }
    }
}

@Composable
fun RestaurantCard(
    restaurant: Restaurant,
    isSelected: Boolean,
    select: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    // Add outline to Column when selected
    Column(
        modifier = modifier.border(
            width = 2.dp,
            color = if (isSelected) Color.Green else Color.Transparent,
            shape = RoundedCornerShape(12.dp) // Using rounded corners for the border

        ).clickable { select(!isSelected) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = restaurant.imageUrl,
            contentDescription = restaurant.name,
            contentScale = ContentScale.Fit,
            modifier = Modifier.clip(RoundedCornerShape(12.dp))
                .size(120.dp)
                .background(Color.White)
        )
        Text(text = restaurant.name, modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}
