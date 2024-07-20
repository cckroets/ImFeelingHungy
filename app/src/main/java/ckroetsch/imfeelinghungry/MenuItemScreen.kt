package ckroetsch.imfeelinghungry

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import ckroetsch.imfeelinghungry.data.MenuCustomization
import ckroetsch.imfeelinghungry.data.MenuItem
import ckroetsch.imfeelinghungry.data.NutritionUnit
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
                Text(text = "Highlights", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(menuItem.reasons) { reason ->
                ReasonItem(reason)
            }
            item {
                Text(text = "Customizations", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(menuItem.appliedCustomizations) { customization ->
                CustomizationItem(customization)
            }

            item {
                Text(text = "Nutrition", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                NutritionalLabel(menuItem.nutrition)
            }

            item {
                Text(text = "Optional Modifications", style = MaterialTheme.typography.bodyMedium)
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
fun NutritionalLabel(nutrition: NutritionalInformation) {
    Card(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
        Column(modifier = Modifier.padding(8.dp)) {
            NutritionRow("Calories", isTopLevel = true, nutrition.calories, NutritionUnit.CALORIES)
            NutritionRow("Total Fat", isTopLevel = true, nutrition.fatContent, NutritionUnit.GRAMS)
            NutritionRow("Sat. Fat", isTopLevel = false, nutrition.saturatedFatContent, NutritionUnit.GRAMS)
            NutritionRow("Trans. Fat", isTopLevel = false, nutrition.transFatContent, NutritionUnit.GRAMS)
            NutritionRow("Cholesterol", isTopLevel = true, nutrition.cholesterolContent, NutritionUnit.MILLIGRAMS)
            NutritionRow("Sodium", isTopLevel = true, nutrition.sodiumContent, NutritionUnit.MILLIGRAMS)
            NutritionRow("Total Carbs", isTopLevel = true, nutrition.carbohydrateContent, NutritionUnit.GRAMS)
            NutritionRow("Fiber", isTopLevel = false, nutrition.fiberContent, NutritionUnit.GRAMS)
            NutritionRow("Sugars", isTopLevel = false, nutrition.sugarContent, NutritionUnit.GRAMS)
            NutritionRow("Protein", isTopLevel = true, nutrition.proteinContent, NutritionUnit.GRAMS)
        }
    }
}



@Composable
fun NutritionRow(
    label: String,
    isTopLevel: Boolean,
    value: Number?,
    unit: NutritionUnit = NutritionUnit.GRAMS
) {
    val style = if (isTopLevel) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodySmall
    val indent = if (isTopLevel) 0.dp else 16.dp
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = indent, top = 4.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = style)
        Text(text = value?.let { "${it.toInt()}${unit.suffix}" } ?: "N/A", style = style)
    }
}

@Composable
fun CustomizationItem(customization: MenuCustomization) {
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            Row(horizontalArrangement = Arrangement.Absolute.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text(text = customization.type + " " + customization.ingredient, fontWeight = FontWeight.Bold)
                if (customization.substituted != null) {
                    // Strike through the original ingredient
                    Text(text = customization.substituted, style = MaterialTheme.typography.bodySmall, textDecoration = TextDecoration.LineThrough)
                }
            }
            if (customization.reason != null) {
                Text(text = customization.reason)
            }
            //customization.nutritionDifference?.let {
            //    Spacer(modifier = Modifier.height(4.dp))
            //    Text(text = "Nutrition Difference", fontWeight = FontWeight.Bold)
            //    NutritionInfo(it)
            //}
        }
}

