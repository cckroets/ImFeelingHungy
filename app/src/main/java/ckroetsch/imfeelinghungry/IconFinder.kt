package ckroetsch.imfeelinghungry

import kotlin.random.Random

fun levenshteinDistance(s1: String, s2: String): Int {
    val lenS1 = s1.length
    val lenS2 = s2.length

    val dp = Array(lenS1 + 1) { IntArray(lenS2 + 1) }

    for (i in 0..lenS1) {
        for (j in 0..lenS2) {
            if (i == 0) {
                dp[i][j] = j
            } else if (j == 0) {
                dp[i][j] = i
            } else {
                val cost = if (s1[i - 1] == s2[j - 1]) 0 else 1
                dp[i][j] = minOf(dp[i - 1][j] + 1, dp[i][j - 1] + 1, dp[i - 1][j - 1] + cost)
            }
        }
    }

    return dp[lenS1][lenS2]
}

fun findMostSimilarString(target: String, options: List<String>): String? {
    if (options.isEmpty()) return null

    return options.minByOrNull { levenshteinDistance(target, it) }
}

fun findIconForMenuItem(name: String): Int {
    return drawableMap.filter { (keyword, id) ->
        name.contains(keyword, ignoreCase = true)
    }.shuffled(Random(name.hashCode()))
        .firstOrNull()?.second ?:
        if (name.contains("protein", ignoreCase = true)) {
            R.drawable._216_steak
        } else {
            null
        }
    ?: R.drawable._098_dish

    //return findMostSimilarString(name, drawableMap.keys.toList())?.let {
    //    drawableMap[it]
    //}
}

val drawableMap = listOf(
    "sausage" to R.drawable._005_sausage,
    "wheat" to R.drawable._008_wheat,
    "bowl" to R.drawable._133_bowl,
    "burger" to R.drawable._009_burger,
    "burger" to R.drawable._139_burger,
    "burger" to R.drawable._138_burger,
    "burger" to R.drawable._073_sandwich,
    "beef" to R.drawable._009_burger,
    "bread" to R.drawable._031_bread,
    "chicken" to R.drawable._208_chicken_leg,
    "chicken" to R.drawable._081_chicken_leg,
    "ketchup" to R.drawable._044_ketchup,
    "barbecue" to R.drawable._065_barbecue,
    "sandwich" to R.drawable._069_sandwich,
    "coffee" to R.drawable._070_coffee_cup,
    "broccoli" to R.drawable._092_broccoli,
    "avocado" to R.drawable._122_avocado,
    "vegetable" to R.drawable._126_vegetables,
    "veggie" to R.drawable._126_vegetables,
    "steak" to R.drawable._127_steak,
    "beans" to R.drawable._147_beans,
    "grain" to R.drawable._187_grain,
    "honey" to R.drawable._192_honey,
    "taco" to R.drawable._259_taco,
    "wrap" to R.drawable._288_wrap,
)

class IconFinder {
}
