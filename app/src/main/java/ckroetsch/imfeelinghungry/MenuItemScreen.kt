package ckroetsch.imfeelinghungry

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ckroetsch.imfeelinghungry.data.MenuCustomization
import ckroetsch.imfeelinghungry.data.MenuItem
import ckroetsch.imfeelinghungry.data.NutritionalInformation
import ckroetsch.imfeelinghungry.data.Reason

// ... (Your data classes: MenuItem, Reason, NutritionalInformation, MenuCustomization)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuItemScreen(menuItem: MenuItem) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(menuItem.restaurantName) })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Text(
                    text = menuItem.menuItemTitle,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = menuItem.menuItemLongDescription)
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Text(text = "Reasons", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(menuItem.reasons) { reason ->
                ReasonItem(reason)
            }

            item {
                Text(text = "Nutrition", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                NutritionInfo(menuItem.nutrition)
            }

            item {
                Text(text = "Applied Customizations", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(menuItem.appliedCustomizations) { customization ->
                CustomizationItem(customization)
            }

            item {
                Text(text = "Available Customizations", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(menuItem.availableCustomizations) { customization ->
                CustomizationItem(customization)
            }

            item {
                Text(text = "Redo Modifiers", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scrollable(rememberScrollState(), orientation = Orientation.Horizontal),
                    contentPadding = PaddingValues(12.dp)
                ) {
                    items(menuItem.redoModifiers) { redoText ->
                        Button(
                            onClick = { },
                            modifier = Modifier
                                .padding(end = 8.dp)
                        ) {
                            Text(text = redoText)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReasonItem(reason: Reason) {
    Card(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = reason.title, fontWeight = FontWeight.Bold)
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            Text(text = reason.description)
        }
    }
}

@Composable
fun NutritionInfo(nutrition: NutritionalInformation) {
    Card(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
        Column(modifier = Modifier.padding(8.dp)) {
            NutritionRow("Calories", nutrition.calories)
            NutritionRow("Carbohydrates", nutrition.carbohydrateContent)
            NutritionRow("Fat", nutrition.fatContent)
            NutritionRow("Protein", nutrition.proteinContent)
            // ... Add rows for other nutritional values
        }
    }
}

@Composable
fun NutritionRow(label: String, value: Any?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label)
        Text(text = value?.toString() ?: "N/A")
    }
}

@Composable
fun CustomizationItem(customization: MenuCustomization) {
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            Text(text = customization.type, fontWeight = FontWeight.Bold)
            Text(text = customization.description)
            //customization.nutritionDifference?.let {
            //    Spacer(modifier = Modifier.height(4.dp))
            //    Text(text = "Nutrition Difference", fontWeight = FontWeight.Bold)
            //    NutritionInfo(it)
            //}
        }
}

