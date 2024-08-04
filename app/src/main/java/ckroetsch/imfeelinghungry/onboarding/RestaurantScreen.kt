package ckroetsch.imfeelinghungry.onboarding

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ckroetsch.imfeelinghungry.data.Preference
import ckroetsch.imfeelinghungry.data.PreferencesViewModel
import ckroetsch.imfeelinghungry.ui.theme.DarkOrange
import ckroetsch.imfeelinghungry.ui.theme.MustardYellow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    viewModel: PreferencesViewModel,
    navController: NavController
) {
    val selectedRestaurants = viewModel.preferences.restaurantPreferences.toMap()
    val isRestaurantSelected = { restaurant: Restaurant -> selectedRestaurants[restaurant.name] == Preference.LIKED }

    val selectedFoods = viewModel.preferences.dietaryPreferences.toMap()
    val isDietSelected = { diet: Diet -> selectedFoods[diet.name] == Preference.LIKED }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkOrange,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                title = {
                    Text(
                        "Preferences",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
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
        modifier = modifier.padding(8.dp),
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
