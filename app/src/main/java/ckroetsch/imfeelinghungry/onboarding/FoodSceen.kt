package ckroetsch.imfeelinghungry.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ckroetsch.imfeelinghungry.onboarding.OnboardingNavigator
import ckroetsch.imfeelinghungry.data.Preference
import ckroetsch.imfeelinghungry.data.PreferencesViewModel

@Composable
fun FoodScreen(
    modifier: Modifier = Modifier,
    viewModel: PreferencesViewModel,
    navController: NavController,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFFD32B)),// Matching background color
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        val selectedFoods = viewModel.preferences.foodPreferences.toMap()
        val isSelected = { food: Food -> selectedFoods[food.name] == Preference.LIKED }

        Text(
            text = "Select Your Favorite Food",
            style = MaterialTheme.typography.headlineSmall, // Adjust text style as needed
            color = Color.Black,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        FoodSelection(
            modifier = Modifier.weight(1f),
            onSelect = {
                viewModel.setFoodPreference(
                    it,
                    if (isSelected(it)) Preference.DEFAULT else Preference.LIKED
                )
            },
            foods = AllFoods,
            isSelected = isSelected,
        )
        OnboardingNavigator(
            navController = navController,
            nextPage = "dietaryPreference",
            prevPage = "preferences",
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .padding(horizontal = 16.dp, vertical = 2.dp),
        )

    }
}

@Composable
fun FoodCard(
    food: Food,
    isSelected: Boolean,
    select: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            painter = painterResource(food.icon),
            contentDescription = food.name,
            tint = Color.Unspecified,
            modifier = Modifier
                .clickable { select(!isSelected) }
                .border(
                    width = 4.dp,
                    color = if (isSelected) Color.Green else Color.Transparent,
                    shape = CircleShape,

                    )
                .size(120.dp)
                .clip(RoundedCornerShape(14.dp))

        )
        Text(text = food.name, modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

@Composable
fun FoodSelection(
    modifier: Modifier = Modifier,
    onSelect: (Food) -> Unit,
    foods: List<Food> = AllFoods,
    isSelected: (Food) -> Boolean
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(128.dp),
        contentPadding = PaddingValues(10.dp),
        verticalItemSpacing = 16.dp,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier.fillMaxWidth()
    ) {
        items(foods) { food ->
            FoodCard(food, isSelected(food), { onSelect(food) })
        }

    }
}
