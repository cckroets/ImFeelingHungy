package ckroetsch.imfeelinghungry.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ckroetsch.imfeelinghungry.data.Preference
import ckroetsch.imfeelinghungry.data.PreferencesViewModel
import ckroetsch.imfeelinghungry.ui.theme.DarkBlue
import ckroetsch.imfeelinghungry.ui.theme.DarkOrange

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
            .background(Color(0xFFFFD32B)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Select Your Preferred Diet",
            style = MaterialTheme.typography.headlineSmall, // Adjust text style as needed
            color = Color.Black,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        DietaryPreferenceSelection(
            modifier = Modifier.weight(1f),
            onSelect = {
                viewModel.setDietaryPreference(
                    it,
                    if (isSelected(it)) Preference.DEFAULT else Preference.LIKED
                )
            },
            diets = AllPreferences,
            isSelected = isSelected,
        )
        OnboardingNavigator(
            navController = navController,
            nextPage = "generateOrder",
            prevPage = "restaurant",
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp) // Adjusted height for better visibility
                .padding(16.dp)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DietaryPreferenceSelection(
    modifier: Modifier = Modifier,
    onSelect: (Diet) -> Unit,
    diets: List<Diet> = AllPreferences,
    isSelected: (Diet) -> Boolean
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2), // Increased the width of the columns
        contentPadding = PaddingValues(16.dp),
        verticalItemSpacing = 16.dp,
        horizontalArrangement = Arrangement.spacedBy(16.dp), // Added horizontal spacing
        modifier = modifier.fillMaxWidth()
    ) {
        items(diets) { preference ->
            DietaryPreferencePill(
                preference,
                isSelected(preference),
                { onSelect(preference) },
            )
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
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder(isSelected)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, // Ensured vertical alignment
            horizontalArrangement = Arrangement.Start, // Ensured horizontal arrangement
            modifier = modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { select(!isSelected) }
                .padding(horizontal = 16.dp, vertical = 8.dp) // Adjusted padding
        ) {
            Text(
                text = preference.name,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}
