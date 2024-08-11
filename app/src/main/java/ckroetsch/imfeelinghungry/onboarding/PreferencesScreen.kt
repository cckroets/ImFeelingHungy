package ckroetsch.imfeelinghungry.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ckroetsch.imfeelinghungry.SecondaryScreen
import ckroetsch.imfeelinghungry.data.AllPreferences
import ckroetsch.imfeelinghungry.data.AllRestaurants
import ckroetsch.imfeelinghungry.data.Diet
import ckroetsch.imfeelinghungry.data.Preference
import ckroetsch.imfeelinghungry.data.PreferencesViewModel
import ckroetsch.imfeelinghungry.data.Restaurant
import ckroetsch.imfeelinghungry.ui.theme.DarkPurple

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PreferencesScreen(
    modifier: Modifier = Modifier,
    viewModel: PreferencesViewModel,
) {
    val selectedRestaurants = viewModel.preferences.restaurantPreferences.toMap()
    val isRestaurantSelected = { restaurant: Restaurant -> selectedRestaurants[restaurant.name] == Preference.LIKED }

    val selectedFoods = viewModel.preferences.dietaryPreferences.toMap()
    val isDietSelected = { diet: Diet -> selectedFoods[diet.name] == Preference.LIKED }

    SecondaryScreen(modifier, "Preferences") { padding, scrollConnection ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.nestedScroll(scrollConnection),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding(),
                start = 16.dp,
                end = 16.dp
            ),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "Select your dietary goals:",
                    style = MaterialTheme.typography.labelLarge, // Adjust text style as needed
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AllPreferences.forEach { preference ->
                        DietaryPreferencePill(
                            preference,
                            isDietSelected(preference),
                            { viewModel.setDietaryPreference(preference, if (it) Preference.LIKED else Preference.DEFAULT) },
                        )
                    }
                }
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "Select your Favorite restaurants:",
                    style = MaterialTheme.typography.labelLarge, // Adjust text style as needed
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            items(AllRestaurants) { restaurant ->
                RestaurantCard(
                    restaurant = restaurant,
                    isSelected = isRestaurantSelected(restaurant),
                    select = { viewModel.setRestaurantPreference(restaurant, if (it) Preference.LIKED else Preference.DEFAULT) }
                )
            }
        }
    }
}

@Composable
fun RestaurantSelection(
    modifier: Modifier = Modifier,
    onSelect: (Restaurant) -> Unit,
    restaurants: List<Restaurant> = AllRestaurants,
    isSelected: (Restaurant) -> Boolean
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(40.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {

    }
}

@Composable
fun RestaurantCard(
    restaurant: Restaurant,
    isSelected: Boolean,
    select: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(8.dp),
        colors = CardDefaults.cardColors(if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerLow),
        onClick = { select(!isSelected) }
    ) {
        Box(Modifier.fillMaxWidth().padding(16.dp)) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Selection Icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.TopEnd)
                        .size(20.dp)
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


@Composable
fun DietaryPreferencePill(
    preference: Diet,
    isSelected: Boolean,
    select: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        modifier = modifier,
        selected = isSelected,
        onClick = { select(!isSelected) },
        label = { Text(text = preference.name) },
    )
}

