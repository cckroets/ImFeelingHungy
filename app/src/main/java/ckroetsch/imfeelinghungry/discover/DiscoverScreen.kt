package ckroetsch.imfeelinghungry.discover

import kotlinx.serialization.encodeToString
import androidx.compose.material3.Icon
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.datastore.core.IOException
import androidx.navigation.NavController
import ckroetsch.imfeelinghungry.NutritionalBenefits
import ckroetsch.imfeelinghungry.data.DietType
import ckroetsch.imfeelinghungry.data.HungryJson
import ckroetsch.imfeelinghungry.data.PreferencesViewModel
import ckroetsch.imfeelinghungry.data.MenuItem
import ckroetsch.imfeelinghungry.data.NutritionGoal
import ckroetsch.imfeelinghungry.data.calculateFinalNutrition
import ckroetsch.imfeelinghungry.findIconForMenuItem
import kotlin.math.min
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    modifier: Modifier = Modifier,
    viewModel: PreferencesViewModel,
    navController: NavController,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(
            start = 0.dp,
            end = 0.dp,
            top = 16.dp,
            bottom = 16.dp
        )
        ) {
        item {
            CenterAlignedTopAppBar(title = { Text(text = "Discover") },)
        }
        items(DiscoverSection.entries) {
            DiscoverRow(
                section = it,
                preferencesViewModel = viewModel,
                navController = navController
            )
        }
    }
}

@Composable
fun DiscoverRow(
    section: DiscoverSection,
    modifier: Modifier = Modifier,
    preferencesViewModel: PreferencesViewModel,
    navController: NavController,
) {
    val menuItems = LoadMenuItemsFromJson(section)
    val goals = section.goals
    Column(modifier) {
        Text(
            text = section.title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(menuItems) { menuItem ->
                MenuItemCard2(
                    menuItem = menuItem,
                    preferencesViewModel = preferencesViewModel,
                    goals = goals,
                    navController = navController
                )
            }
        }
    }

}

@Composable
fun MenuItemCard(
    menuItem: MenuItem,
    modifier: Modifier = Modifier,
    preferencesViewModel: PreferencesViewModel,
    navController: NavController,
) {
    var isFavorite by remember { mutableStateOf(preferencesViewModel.isFavorite(menuItem)) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        elevation = CardDefaults.cardElevation(8.dp),
        onClick = {
            val serializedMenuItem = serializeMenuItem(menuItem)
            navController.navigate("viewDiscoverItem/$serializedMenuItem")
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)

        ) {
            val resturantImage = preferencesViewModel.lookupRestuarantImage(menuItem.restaurantName)
            Image(
                painter = painterResource(id = resturantImage),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .padding(end = 12.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(10.dp))
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = menuItem.creationTitle ?: menuItem.menuItemTitle,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = menuItem.restaurantName,
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${menuItem.originalNutrition.calories} calories",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
            }
            IconButton(
                onClick = {
                    if (preferencesViewModel.isFavorite(menuItem)) {
                        preferencesViewModel.removeFavorite(menuItem)
                        isFavorite = false
                    } else {
                        preferencesViewModel.addFavorite(menuItem)
                        isFavorite = true
                    }
                },
            ) {
                Icon(
                    painter = rememberVectorPainter(if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder),
                    contentDescription = "Favorite",
                )
            }
        }
    }
}

enum class DiscoverSection(
    val title: String,
    val fileName: String,
    val goals: List<NutritionGoal> = emptyList()
) {
    HIGH_PROTEIN("\uD83C\uDF56 High Protein", "discover/high_protein.json", DietType.HIGH_PROTEIN.goals),
    LOW_CALORIE("\uD83C\uDF73 Low Calorie", "discover/low_calorie.json", DietType.LOW_CALORIE.goals),
    SUSTAINABLE("\uD83C\uDF0D Sustainable Choices", "discover/sustainable.json", DietType.LOW_FOOTPRINT.goals)
}

@Composable
fun LoadMenuItemsFromJson(section: DiscoverSection): List<MenuItem> {
    val context = LocalContext.current
    var menuItems by remember { mutableStateOf<List<MenuItem>>(emptyList()) }

    LaunchedEffect(Unit) {
        menuItems = loadJsonFromAssets(context, section.fileName)
    }
    return menuItems
}

private fun loadJsonFromAssets(context: Context, fileName: String): List<MenuItem> {
    return try {
        val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        HungryJson.decodeFromString(jsonString)
    } catch (e: IOException) {
        e.printStackTrace()
        emptyList()
    }
}


fun serializeMenuItem(menuItem: MenuItem): String {
    return HungryJson.encodeToString(menuItem)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MenuItemCard2(
    menuItem: MenuItem,
    modifier: Modifier = Modifier,
    goals: List<NutritionGoal>,
    preferencesViewModel: PreferencesViewModel,
    navController: NavController,
) {
    val resturantImage = remember(menuItem.restaurantName) {
        preferencesViewModel.lookupRestuarantImage(menuItem.restaurantName)
    }

    Card(
        modifier = modifier.width(220.dp).padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        elevation = CardDefaults.cardElevation(8.dp),
        onClick = {
            val serializedMenuItem = serializeMenuItem(menuItem)
            navController.navigate("viewDiscoverItem/$serializedMenuItem")
        }
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 8.dp)
        ) {
            Box(Modifier.align(Alignment.CenterHorizontally)) {
                Image(
                    painter = painterResource(id = findIconForMenuItem(menuItem.menuItemTitle + " " + menuItem.creationDescription)),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .size(100.dp)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(10.dp))
                )
            }

                Text(
                    text = menuItem.creationTitle ?: menuItem.menuItemTitle,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = resturantImage),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(24.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = menuItem.restaurantName,
                    style = MaterialTheme.typography.labelMedium
                )
            }
                Spacer(modifier = Modifier.height(4.dp))

            FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                menuItem.reasons.subList(0, min(2, menuItem.reasons.size)).forEach {
                    Text(
                        text = it.title,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.onSecondary, shape = MaterialTheme.shapes.small)
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
                menuItem.calculateFinalNutrition().let {
                    FlowRow(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        NutritionalBenefits(it, goals, Modifier, false)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
        }
    }
}
