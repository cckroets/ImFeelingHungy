package ckroetsch.imfeelinghungry.onboarding

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ckroetsch.imfeelinghungry.R
import ckroetsch.imfeelinghungry.data.Preference
import ckroetsch.imfeelinghungry.data.PreferencesViewModel
import ckroetsch.imfeelinghungry.ui.theme.DarkOrange
import coil.compose.AsyncImage
import okhttp3.Cookie

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
            .background(Color.White), // Matching background color
        verticalArrangement = Arrangement.SpaceBetween, // Ensure even spacing
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Select Your Favorite Restaurants",
                style = MaterialTheme.typography.headlineSmall, // Adjust text style as needed
                color = Color.Black,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            RestaurantSelection(
                modifier = Modifier.fillMaxWidth(),
                onSelect = {
                    viewModel.setRestaurantPreference(
                        it,
                        if (isSelected(it)) Preference.DEFAULT else Preference.LIKED
                    )
                },
                restaurants = AllRestaurants,
                isSelected = isSelected,
            )
        }

        OnboardingNavigator(
            navController = navController,
            nextPage = "dietaryPreference",
            prevPage = "welcome",
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(16.dp)
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
    //LazyColumn(
    //    contentPadding = PaddingValues(16.dp),
    //    verticalArrangement = Arrangement.spacedBy(4.dp),
    //    modifier = modifier.fillMaxWidth()
    //) {
    //    items(restaurants) { restaurant ->
    //        RestaurantCard(
    //            restaurant = restaurant,
    //            isSelected = isSelected(restaurant),
    //            select = { onSelect(restaurant) }
    //        )
    //    }
    //}
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(40.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        items(restaurants) { restaurant ->
            RestaurantCard(
                restaurant = restaurant,
                isSelected = isSelected(restaurant),
                select = { onSelect(restaurant) }
            )
        }
    }
}

@Composable
fun RestaurantCard2(
    restaurant: Restaurant,
    isSelected: Boolean,
    select: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder(isSelected),
        onClick = { select(!isSelected) }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .padding(start = 8.dp, end = 16.dp)
        ) {
            Image(
                painter = painterResource(restaurant.imageUrl),
                modifier = Modifier.size(48.dp),
                contentDescription = restaurant.name,
            )
            Text(
                text = restaurant.name,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(16.dp).weight(1f)
            )
            Crossfade(isSelected, label = "selected${restaurant.name}") {
                if (it) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Selection Icon",
                        tint = DarkOrange,
                        modifier = Modifier.size(24.dp).border(2.dp, DarkOrange, CircleShape)
                    )
                } else {
                    Box(
                        modifier = Modifier.size(24.dp).border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)
                    )
                }
            }
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
    OutlinedCard(
        modifier = modifier,
        colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder(isSelected),
        onClick = { select(!isSelected) }
    ) {
        Box(Modifier.fillMaxWidth().padding(16.dp)) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Selection Icon",
                    tint = DarkOrange,
                    modifier = Modifier.align(Alignment.TopEnd)
                        .size(16.dp)
                        .border(2.dp, DarkOrange, CircleShape)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .padding(start = 8.dp, end = 8.dp)
            ) {
                Box {
                    Image(
                        painter = painterResource(restaurant.imageUrl),
                        modifier = Modifier.size(48.dp),
                        contentDescription = restaurant.name,
                    )
                }
                Text(
                    text = restaurant.name,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

        }
    }
}
