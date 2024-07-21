package ckroetsch.imfeelinghungry.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ckroetsch.imfeelinghungry.onboarding.OnboardingNavigator
import ckroetsch.imfeelinghungry.data.Preference
import ckroetsch.imfeelinghungry.data.PreferencesViewModel


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
        // Add other composable content her
    ) {
        Text(
            text = "Select Your Preferred diet",
            style = MaterialTheme.typography.headlineSmall, // Adjust text style as needed
            color = Color.Black,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        DietaryPreferenceSelection(
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
            nextPage = "dietaryPreference",
            prevPage = "food",
            modifier = Modifier
                .fillMaxWidth()
                .height(22.dp)
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
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        diets.forEach { preference ->
            key(preference) {
                DietaryPreferencePill(
                    preference,
                    isSelected(preference),
                    { onSelect(preference) },
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
    val color = if (isSelected) Color.Green else Color.Gray
    val icon = if (isSelected) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .clickable { select(!isSelected) }
            .background(color, RoundedCornerShape(50))
            .padding(16.dp)

    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = preference.name)
    }
}