package ckroetsch.imfeelinghungry.discover

import kotlinx.serialization.encodeToString
import androidx.compose.material3.Icon
import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.IOException
import androidx.navigation.NavController
import ckroetsch.imfeelinghungry.MenuItemScreen
import ckroetsch.imfeelinghungry.R
import ckroetsch.imfeelinghungry.data.PreferencesViewModel
import ckroetsch.imfeelinghungry.ui.theme.DarkOrange
import kotlinx.serialization.json.Json
import ckroetsch.imfeelinghungry.data.MenuItem
import ckroetsch.imfeelinghungry.ui.theme.MustardYellow
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    modifier: Modifier = Modifier,
    viewModel: PreferencesViewModel,
    navController: NavController,
) {
    val menuItems = LoadMenuItemsFromJson()
    //val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
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
                    Text(text = "Discover")
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
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 0.dp,
                end = 0.dp,
                top = padding.calculateTopPadding() + 16.dp,
                bottom = padding.calculateBottomPadding()
            )
        ) {
            val paddedModifier = Modifier.padding(horizontal = 12.dp)
            items(menuItems) { menuItem ->
                MenuItemCard(
                    menuItem = menuItem,
                    modifier = paddedModifier,
                    preferencesViewModel = viewModel,
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
            .padding(vertical = 8.dp)
            .border(
                border = BorderStroke(
                    width = 3.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(MustardYellow, DarkOrange, MustardYellow)
                    )
                ),
                shape = RoundedCornerShape(8.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = Color.White), // Explicitly set Card's color to white
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
                    text = menuItem.restaurantName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
                Text(
                    text = menuItem.creationTitle ?: menuItem.menuItemTitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${menuItem.originalNutrition.calories} calories",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.ic_star),
//                        contentDescription = null,
//                        modifier = Modifier.size(12.dp),
//                        tint = Color.Red
//                    )
                    Spacer(modifier = Modifier.width(4.dp))

                }
            }
            Icon(
                imageVector = (if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder) as ImageVector,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        if (preferencesViewModel.isFavorite(menuItem)) {
                            preferencesViewModel.removeFavorite(menuItem)
                            isFavorite = false
                        } else {
                            preferencesViewModel.addFavorite(menuItem)
                            isFavorite = true
                        }
                    },
                tint = if (isFavorite) Color.Red else Color.Gray
            )
        }
    }
}


@Composable
fun LoadMenuItemsFromJson(): List<MenuItem> {
    val context = LocalContext.current
    var menuItems by remember { mutableStateOf<List<MenuItem>>(emptyList()) }

    val seed = rememberSaveable() {
        System.currentTimeMillis()
    }

    LaunchedEffect(Unit) {
        menuItems = loadJsonFromAssets(context, "discover_items.json")
            .shuffled(Random(seed)) // Shuffle the list to randomize
            .take(10) // Take only 5 items
    }

    Log.e("menuItems:", "$menuItems")
    return menuItems
}

private val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    explicitNulls = false
}

private fun loadJsonFromAssets(context: Context, fileName: String): List<MenuItem> {
    return try {
        val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        json.decodeFromString(jsonString)
    } catch (e: IOException) {
        e.printStackTrace()
        emptyList()
    }
}


fun serializeMenuItem(menuItem: MenuItem): String {
    return Json.encodeToString(menuItem)
}
