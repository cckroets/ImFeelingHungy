package ckroetsch.imfeelinghungry.data

import androidx.annotation.DrawableRes
import ckroetsch.imfeelinghungry.R

data class Restaurant(
    val name: String,
    val imageUrl: Int,
)

data class Food(
    val name: String,
    @DrawableRes val icon: Int,
)

data class Diet(
    val name: String
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
    val emojis: List<String>,
    val goals: List<NutritionGoal>
) {
    HIGH_PROTEIN("High Protein", listOf("\uD83D\uDCAA"), listOf(
        NutritionGoal(Amount.HIGH, NutritionMetric.PROTEIN)
    )),
    HIGH_FIBER("High Fiber", listOf("\uD83E\uDD66"), listOf(
        NutritionGoal(Amount.HIGH, NutritionMetric.FIBER)
    )),
    LOW_FOOTPRINT("Low Carbon Footprint", listOf("\uD83C\uDF0E"), listOf()),
    HIGH_IRON("High Iron", listOf("\uD83C\uDFCB\uFE0F\u200D♀\uFE0F"), listOf(
        NutritionGoal(Amount.HIGH, NutritionMetric.IRON)
    )),
    VEGAN("Vegan", listOf("\uD83C\uDF31", "\uD83E\uDD51", "\uD83E\uDD55"), listOf()),
    VEGETARIAN("Vegetarian", listOf("\uD83E\uDD66", "\uD83C\uDF45"), listOf()),
    KETO("Keto", listOf("\uD83E\uDD51", "\uD83E\uDD53", "\uD83E\uDDC0"), listOf(
        NutritionGoal(Amount.HIGH, NutritionMetric.PROTEIN),
        NutritionGoal(Amount.LOW, NutritionMetric.CALORIES),
        NutritionGoal(Amount.LOW, NutritionMetric.CARBOHYDRATES)
    )),
    GLUTEN_FREE("Gluten Free", emptyList(), listOf()),
    DAIRY_FREE("Dairy Free", emptyList(), listOf()),
    LOW_CARB("Low Carb", emptyList(), listOf(
        NutritionGoal(Amount.LOW, NutritionMetric.CARBOHYDRATES)
    )),
    LOW_FAT("Low Fat", emptyList(), listOf(
        NutritionGoal(Amount.LOW, NutritionMetric.FAT)
    )),
    LOW_CALORIE("Low Calorie", listOf("⚖\uFE0F"), listOf(
        NutritionGoal(Amount.LOW, NutritionMetric.CALORIES)
    )),
    LOW_SUGAR("Low Sugar", emptyList(), listOf(
        NutritionGoal(Amount.LOW, NutritionMetric.TOTAL_SUGAR)
    )),
    LOW_SODIUM("Low Sodium", emptyList(), listOf(
        NutritionGoal(Amount.LOW, NutritionMetric.SODIUM)
    )),
    LOW_CHOLESTEROL("Low Cholesterol", emptyList(), listOf(
        NutritionGoal(Amount.LOW, NutritionMetric.CHOLESTEROL)
    ))
}

val AllPreferences = DietType.entries.map { dietType ->
    Diet(dietType.idName)
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
    Restaurant("McDonald's", R.drawable.icons_mcdonalds),
    Restaurant("Wendy's", R.drawable.icons_wendys),
    Restaurant("Starbucks", R.drawable.icons_starbucks),
    Restaurant("Chick-fil-A", R.drawable.icons_chikfile),
    Restaurant("Subway", R.drawable.icons_subway),
    Restaurant("Taco Bell", R.drawable.icons_taco_bell),
    Restaurant("Burger King", R.drawable.icons_burgerking),
    Restaurant("KFC", R.drawable.icons_kfc),
    Restaurant("Chipotle", R.drawable.icons_chipotle),
    Restaurant("Five Guys", R.drawable.icons_fiveguys),
    Restaurant("In n Out", R.drawable.icons_innout),
)
