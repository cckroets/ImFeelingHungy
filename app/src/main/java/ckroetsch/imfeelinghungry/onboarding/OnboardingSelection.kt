package ckroetsch.imfeelinghungry.onboarding

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward

import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ckroetsch.imfeelinghungry.R
import ckroetsch.imfeelinghungry.data.NutritionMetric
import ckroetsch.imfeelinghungry.data.NutritionalInformation

data class Restaurant(
    val name: String,
    val imageUrl: String,
)

data class Food(
    val name: String,
    @DrawableRes val icon: Int,
)

data class Diet(
    val name: String,
    val imageUrl: String,
)

enum class Amount {
    LOW,
    HIGH
}

data class NutritionGoal(
    val amount: Amount,
    val metric: NutritionMetric
)

data class NutritionalGain(
    val metric: NutritionMetric,
    val value: Double,
)

fun NutritionalInformation.toGains(): List<NutritionalGain> = buildList {
    calories?.let {
        add(NutritionalGain(NutritionMetric.CALORIES, it.toDouble()))
    }
    carbohydrateContent?.let {
        add(NutritionalGain(NutritionMetric.CARBOHYDRATES, it))
    }
    cholesterolContent?.let {
        add(NutritionalGain(NutritionMetric.CHOLESTEROL, it))
    }
    fatContent?.let {
        add(NutritionalGain(NutritionMetric.FAT, it))
    }
    fiberContent?.let {
        add(NutritionalGain(NutritionMetric.FIBER, it))
    }
    proteinContent?.let {
        add(NutritionalGain(NutritionMetric.PROTEIN, it))
    }
    saturatedFatContent?.let {
        add(NutritionalGain(NutritionMetric.SATURATED_FAT, it))
    }
    ironContent?.let {
        add(NutritionalGain(NutritionMetric.IRON, it))
    }
    sodiumContent?.let {
        add(NutritionalGain(NutritionMetric.SODIUM, it))
    }
    sugarContent?.let {
        add(NutritionalGain(NutritionMetric.TOTAL_SUGAR, it))
    }
    transFatContent?.let {
        add(NutritionalGain(NutritionMetric.TRANS_FAT, it))
    }
}.filter { it.value != 0.0 }.distinctBy { it.metric }

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

enum class DietType(
    val idName: String,
    val imageUrl: String,
    val goals: List<NutritionGoal>
) {
    HIGH_PROTEIN("High Protein", "", listOf(
        NutritionGoal(Amount.HIGH, NutritionMetric.PROTEIN)
    )),
    HIGH_FIBER("High Fiber", "", listOf(
        NutritionGoal(Amount.HIGH, NutritionMetric.FIBER)
    )),
    HIGH_IRON("High Iron", "", listOf(
        NutritionGoal(Amount.HIGH, NutritionMetric.IRON)
    )),
    VEGAN("Vegan", "", listOf()),
    VEGETARIAN("Vegetarian", "", listOf()),
    KETO("Keto", "", listOf(
        NutritionGoal(Amount.HIGH, NutritionMetric.PROTEIN),
        NutritionGoal(Amount.LOW, NutritionMetric.CALORIES),
        NutritionGoal(Amount.LOW, NutritionMetric.CARBOHYDRATES)
    )),
    GLUTEN_FREE("Gluten Free", "", listOf()),
    DAIRY_FREE("Dairy Free", "", listOf()),
    LOW_CARB("Low Carb", "", listOf(
        NutritionGoal(Amount.LOW, NutritionMetric.CARBOHYDRATES)
    )),
    LOW_FAT("Low Fat", "", listOf(
        NutritionGoal(Amount.LOW, NutritionMetric.FAT)
    )),
    LOW_CALORIE("Low Calorie", "", listOf(
        NutritionGoal(Amount.LOW, NutritionMetric.CALORIES)
    )),
    LOW_SUGAR("Low Sugar", "", listOf(
        NutritionGoal(Amount.LOW, NutritionMetric.TOTAL_SUGAR)
    )),
    LOW_SODIUM("Low Sodium", "", listOf(
        NutritionGoal(Amount.LOW, NutritionMetric.SODIUM)
    )),
    LOW_CHOLESTEROL("Low Cholesterol", "", listOf(
        NutritionGoal(Amount.LOW, NutritionMetric.CHOLESTEROL)
    ))
}

val AllPreferences = DietType.entries.map { dietType ->
    Diet(dietType.idName, dietType.imageUrl)
}

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
    Restaurant("Chick-fil-A", "https://seeklogo.com/images/C/chick-fil-a-logo-3528751D14-seeklogo.com.png"),
    Restaurant("Subway", "https://1000logos.net/wp-content/uploads/2017/06/Subway-logo-500x278.png"),
    Restaurant("Pizza Hut", "https://1000logos.net/wp-content/uploads/2017/05/Pizza-Hut-logo-500x345.png"),
    Restaurant("Wendy's", "https://1000logos.net/wp-content/uploads/2017/08/Wendys-Logo-500x166.png"),
    Restaurant("Taco Bell", "https://brandslogos.com/wp-content/uploads/images/large/taco-bell-logo-1.png"),
    Restaurant("Domino's Pizza", "https://brandslogos.com/wp-content/uploads/images/large/dominos-logo.png"),
    Restaurant("Burger King", "https://brandslogos.com/wp-content/uploads/images/large/burger-king-logo.png"),
    Restaurant("Dunkin", "https://seeklogo.com/images/D/dunkin-logo-9395D53B67-seeklogo.com.png"),
    Restaurant("KFC", "https://seeklogo.com/images/K/kfc-new-logo-72E6348046-seeklogo.com.png"),
    Restaurant("Olive Garden", "https://seeklogo.com/images/O/olive-garden-logo-E35EBA47ED-seeklogo.com.png"),
    Restaurant("iHOP", "https://seeklogo.com/images/I/ihop-logo-773205CEC6-seeklogo.com.png"),
    Restaurant("Chipotle", "https://seeklogo.com/images/C/chipotle-mexican-grill-logo-0B3A1AD1EE-seeklogo.com.png"),
    Restaurant("Panera Bread", "https://seeklogo.com/images/P/panera-bread-logo-1A9D50E5CA-seeklogo.com.png"),
    Restaurant("Popeyes", "https://seeklogo.com/images/P/popeyes-logo-A358FB175D-seeklogo.com.png"),
    Restaurant("Jack in the box", "https://seeklogo.com/images/J/jack-in-the-box-logo-1E1F3133BF-seeklogo.com.png"),
    Restaurant("Five Guys", "https://seeklogo.com/images/F/five-guys-logo-EE87090C5B-seeklogo.com.png"),
    Restaurant("In n Out", "https://seeklogo.com/images/I/In-N-Out_Burger-logo-55D778023E-seeklogo.com.png"),
    Restaurant("Jollibee", "https://seeklogo.com/images/J/Jollibee-logo-CBD5940475-seeklogo.com.png"),
)


@Composable
fun OnboardingNavigator(
    modifier: Modifier = Modifier,
    navController: NavController,
    nextPage: String,
    prevPage: String = "welcome"
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(Color.Transparent)


    ) {
        IconButton(
            onClick = { navController.navigate(prevPage) },
        ) {
            Icon(
                modifier = Modifier
                    .size(22.dp)
                    .scale(2f), // Scale up the icon,
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = Color(0xFF039be5)
            )
        }

        IconButton(
            onClick = { navController.navigate(nextPage) },
        ) {
            Icon(
                modifier = Modifier
                    .size(22.dp)
                    .scale(2f), // Scale up the icon,
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = Color(0xFF039be5)
            )
        }
    }
}







