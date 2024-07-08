package ckroetsch.imfeelinghungry.onboarding

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ckroetsch.imfeelinghungry.R
import coil.compose.AsyncImage

data class Restaurant(
    val name: String,
    val imageUrl: String,
)

data class Food(
    val name: String,
    @DrawableRes val icon: Int,
)

data class FoodPreference(
    val name: String,
    val imageUrl: String,
)

val AllFoods = listOf(
    Food("Pizza", R.drawable._243_pizza),
    Food("Burgers", R.drawable._139_burger),
    Food("Coffee", R.drawable._070_coffee_cup),
    Food("Tacos", R.drawable._259_taco),
    Food("Pasta", R.drawable._038_paella),
    Food("Salad", R.drawable._133_bowl),
    Food("Sandwiches", R.drawable._256_sandwich),
    Food("Sushi", R.drawable._272_sushi),
    Food("Chicken", R.drawable._081_chicken_leg),
    Food("Steak", R.drawable._216_steak),
    Food("Seafood", R.drawable._004_fish),
    Food("Soup", R.drawable._058_soup),
    Food("Rice", R.drawable._155_chinese_food),
    Food("Noodles", R.drawable._003_noodles),
    Food("Bread", R.drawable._031_bread),
    Food("Cheese", R.drawable._148_cheese),
    Food("Eggs", R.drawable._176_eggs),
    Food("Fruit", R.drawable._120_apple),
    Food("Vegetables", R.drawable._282_vegetables),
    Food("Dessert", R.drawable._039_cake),
    Food("Snacks", R.drawable._152_chips),
    Food("Beverages", R.drawable._010_soft_drink),
)

val AllPreferences = listOf(
    FoodPreference("High Protein", "https://www.logodesign.net/images/nature-logo.png"),
    FoodPreference("High Fiber", "https://www.logodesign.net/images/nature-logo.png"),
    FoodPreference("High Iron", "https://www.logodesign.net/images/nature-logo.png"),
    FoodPreference("Vegan", "https://www.logodesign.net/images/nature-logo.png"),
    FoodPreference("Vegetarian", "https://www.logodesign.net/images/nature-logo.png"),
    FoodPreference("Keto", "https://www.logodesign.net/images/nature-logo.png"),
    FoodPreference("Gluten Free", "https://www.logodesign.net/images/nature-logo.png"),
    FoodPreference("Dairy Free", "https://www.logodesign.net/images/nature-logo.png"),
    FoodPreference("Paleo", "https://www.logodesign.net/images/nature-logo.png"),
    FoodPreference("Pescatarian", "https://www.logodesign.net/images/nature-logo.png"),
    FoodPreference("Low Carb", "https://www.logodesign.net/images/nature-logo.png"),
    FoodPreference("Low Fat", "https://www.logodesign.net/images/nature-logo.png"),
    FoodPreference("Low Calorie", "https://www.logodesign.net/images/nature-logo.png"),
    FoodPreference("Low Sugar", "https://www.logodesign.net/images/nature-logo.png"),
    FoodPreference("Low Sodium", "https://www.logodesign.net/images/nature-logo.png"),
)


/**
 * McDonald’s- McDonalds-logo-500x281.png
 * Starbucks - Starbucks-logo-500x281.png
 * Chick-fil-A - Chick-fil-A-logo-500x281.png
 * Subway- Subway-logo-500x278.png
 * Pizza Hut - Pizza-Hut-logo-500x345.png
 * Wendy’s- Wendys-Logo-500x166.png
 * Taco Bell - Taco-Bell-Logo-500x313.png
 * Domino’s Pizza - Dominos-logo-500x281.png
 * Burger King - Burger-King_Logo-500x300.png
 * Dunkin - Dunkin-Donuts-logo-500x281.png
 * KFC - Kfc_logo-500x281.png
 * Olive Garden- Olive-Garden-Logo-500x281.png
 * iHOP - IHOP-Logo-500x281.png
 * Chipotle Mexican Grill - Chipotle-logo-500x281.png
 * Panera Bread - Panera-Bread-logo-500x281.png
 * Popeyes Louisiana Kitchen - Popeyes-logo-500x329.png
 * Jack in the box - Jack-in-the-Box-Logo-500x281.png
 * Five Guy Burgers and Fries - Five-Guys-Logo-500x281.png
 * In n Out - In-N-Out-Burger-Logo-500x281.png
 * Jollibee - Jollibee-logo-500x281.png
 */

val AllRestaurants = listOf(
    Restaurant("McDonald's", "https://1000logos.net/wp-content/uploads/2017/03/McDonalds-logo-500x281.png"),
    Restaurant("Starbucks", "https://1000logos.net/wp-content/uploads/2023/04/Starbucks-logo-500x281.png"),
    Restaurant("Chick-fil-A", "https://1000logos.net/wp-content/uploads/2021/04/Chick-fil-A-logo-500x281.png"),
    Restaurant("Subway", "https://1000logos.net/wp-content/uploads/2017/06/Subway-logo-500x278.png"),
    Restaurant("Pizza Hut", "https://1000logos.net/wp-content/uploads/2017/05/Pizza-Hut-logo-500x345.png"),
    Restaurant("Wendy's", "https://1000logos.net/wp-content/uploads/2017/08/Wendys-Logo-500x166.png"),
    Restaurant("Taco Bell", "https://www.logodesign.net/images/nature-logo.png"),
    Restaurant("Domino's Pizza", "https://www.logodesign.net/images/nature-logo.png"),
    Restaurant("Burger King", "https://www.logodesign.net/images/nature-logo.png"),
    Restaurant("Dunkin", "https://www.logodesign.net/images/nature-logo.png"),
    Restaurant("KFC", "https://www.logodesign.net/images/nature-logo.png"),
    Restaurant("Olive Garden", "https://www.logodesign.net/images/nature-logo.png"),
    Restaurant("iHOP", "https://www.logodesign.net/images/nature-logo.png"),
    Restaurant("Chipotle Mexican Grill", "https://www.logodesign.net/images/nature-logo.png"),
    Restaurant("Panera Bread", "https://www.logodesign.net/images/nature-logo.png"),
    Restaurant("Popeyes Louisiana Kitchen", "https://www.logodesign.net/images/nature-logo.png"),
    Restaurant("Jack in the box", "https://www.logodesign.net/images/nature-logo.png"),
    Restaurant("Five Guy Burgers and Fries", "https://www.logodesign.net/images/nature-logo.png"),
    Restaurant("In n Out", "https://www.logodesign.net/images/nature-logo.png"),
    Restaurant("Jollibee", "https://www.logodesign.net/images/nature-logo.png"),
)

@Composable
fun RestaurantSelection() {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(128.dp),
        contentPadding = PaddingValues(16.dp),
        verticalItemSpacing = 16.dp,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxSize()
    ) {
        items(AllRestaurants) { restaurant ->
            RestaurantCard(restaurant)
        }
    }
}

@Composable
fun FoodSelection() {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(128.dp),
        contentPadding = PaddingValues(16.dp),
        verticalItemSpacing = 16.dp,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxSize()
    ) {
        items(AllFoods) { food ->
            FoodCard(food)
        }
    }
}

@Composable
fun FoodCard(food: Food) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(food.icon),
            contentDescription = food.name,
            tint = Color.Unspecified,
            modifier = Modifier.clip(CircleShape)
        )
        Text(text = food.name, modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

@Composable
fun RestaurantCard(restaurant: Restaurant) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            model = restaurant.imageUrl,
            contentDescription = restaurant.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.clip(CircleShape)
        )
        Text(text = restaurant.name, modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}
