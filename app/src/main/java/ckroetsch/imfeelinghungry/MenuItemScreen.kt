package ckroetsch.imfeelinghungry

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
                Column {
                    menuItem.redoModifiers.forEach { modifier ->
                        Text(text = modifier)
                    }
                }
            }
        }
    }
}

@Composable
fun ReasonItem(reason: Reason) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = reason.title, fontWeight = FontWeight.Bold)
        Text(text = reason.description)
    }
}

@Composable
fun NutritionInfo(nutrition: NutritionalInformation) {
    Column {
        NutritionRow("Calories", nutrition.calories)
        NutritionRow("Carbohydrates", nutrition.carbohydrateContent)
        NutritionRow("Cholesterol", nutrition.cholesterolContent)
        // ... Add rows for other nutritional values
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
        customization.nutritionDifference?.let {
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Nutrition Difference", fontWeight = FontWeight.Bold)
            NutritionInfo(it)
        }
    }
}
