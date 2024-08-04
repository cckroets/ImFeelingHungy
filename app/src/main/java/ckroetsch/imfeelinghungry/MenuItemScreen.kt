package ckroetsch.imfeelinghungry

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ckroetsch.imfeelinghungry.data.MenuCustomization
import ckroetsch.imfeelinghungry.data.MenuItem
import ckroetsch.imfeelinghungry.data.ModificationType
import ckroetsch.imfeelinghungry.data.NutritionUnit
import ckroetsch.imfeelinghungry.data.NutritionalInformation
import ckroetsch.imfeelinghungry.data.Reason
import ckroetsch.imfeelinghungry.data.calculateFinalNutrition
import ckroetsch.imfeelinghungry.onboarding.AllRestaurants
import ckroetsch.imfeelinghungry.onboarding.Amount
import ckroetsch.imfeelinghungry.onboarding.NutritionGoal
import ckroetsch.imfeelinghungry.onboarding.NutritionalGain
import ckroetsch.imfeelinghungry.onboarding.toGains
import ckroetsch.imfeelinghungry.ui.theme.DarkOrange
import ckroetsch.imfeelinghungry.ui.theme.Green30
import ckroetsch.imfeelinghungry.ui.theme.MustardYellow
import ckroetsch.imfeelinghungry.ui.theme.Red30
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuItemScreen(
    menuItem: MenuItem,
    goals: List<NutritionGoal>,
    navController: NavController,
    onRegenerate: (String) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        containerColor = Color.White,
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
            //.background(
            //    brush = Brush.linearGradient(
            //        colors = listOf(Color.White, Color.White, Color(0xffffd32b))
            //    )
            //),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkOrange,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                title = {
                        Text(
                            menuItem.restaurantName,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                    },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
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
            item {
                Text(
                    text = menuItem.creationTitle ?: menuItem.menuItemTitle,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = paddedModifier
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = menuItem.creationDescription, modifier = paddedModifier)
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                MenuLabel(
                    text = "How to order",
                    modifier = paddedModifier
                )
                Spacer(modifier = Modifier.height(8.dp))
                MenuCreation(menuItem, goals)
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                MenuLabel(
                    text = "Highlights",
                    modifier = paddedModifier
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            itemsIndexed(menuItem.reasons) { index, reason ->
                ReasonItem(reason)
                if (index < menuItem.reasons.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                MenuLabel(
                    text = "Nutrition",
                    modifier = paddedModifier
                )
                Spacer(modifier = Modifier.height(8.dp))
                NutritionalLabel(menuItem.calculateFinalNutrition(), menuItem.originalNutrition)
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
           
                MenuLabel(
                    text = "Refine this creation",

                    modifier = paddedModifier
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scrollable(rememberScrollState(), orientation = Orientation.Horizontal),
                    contentPadding = PaddingValues(12.dp)
                ) {
                    items(menuItem.redoModifiers) { redoText ->
                        Button(
                            colors = ButtonDefaults.buttonColors(containerColor = DarkOrange),
                            onClick = { onRegenerate(redoText) },
                            modifier = Modifier
                                .padding(end = 8.dp)
                        ) {
                            Text(text = redoText)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "More Options",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = paddedModifier
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scrollable(rememberScrollState(), orientation = Orientation.Horizontal),
                    contentPadding = PaddingValues(12.dp)
                ) {
                    items(AllRestaurants) { restaurant ->
                        Button(
                            onClick = { onRegenerate(restaurant.name) },
                            modifier = Modifier
                                .padding(end = 8.dp)
                        ) {
                            Text(text = restaurant.name)
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun ReasonItem(reason: Reason) {
    MenuCard {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = reason.title, fontWeight = FontWeight.Bold)
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            Text(
                text = reason.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun NutritionalLabel(nutrition: NutritionalInformation, oldNutrition: NutritionalInformation) {
    MenuCard {
        Column(modifier = Modifier.padding(8.dp).padding(end = 16.dp)) {
            nutrition.servingSize?.let {
                Text(text = "Serving Size ($it)", style = MaterialTheme.typography.bodySmall)
            }
            NutritionRow("Calories", isTopLevel = true, nutrition.calories, oldNutrition.calories, NutritionUnit.CALORIES)
            NutritionRow("Total Fat", isTopLevel = true, nutrition.fatContent, oldNutrition.fatContent, NutritionUnit.GRAMS)
            NutritionRow("Saturated Fat", isTopLevel = false, nutrition.saturatedFatContent, oldNutrition.saturatedFatContent, NutritionUnit.GRAMS)
            NutritionRow("Trans Fat", isTopLevel = false, nutrition.transFatContent, oldNutrition.transFatContent, NutritionUnit.GRAMS)
            NutritionRow("Cholesterol", isTopLevel = true, nutrition.cholesterolContent, oldNutrition.cholesterolContent, NutritionUnit.MILLIGRAMS)
            NutritionRow("Sodium", isTopLevel = true, nutrition.sodiumContent, oldNutrition.sodiumContent, NutritionUnit.MILLIGRAMS)
            NutritionRow("Total Carbohydrates", isTopLevel = true, nutrition.carbohydrateContent, oldNutrition.carbohydrateContent, NutritionUnit.GRAMS)
            NutritionRow("Fiber", isTopLevel = false, nutrition.fiberContent, oldNutrition.fiberContent, NutritionUnit.GRAMS)
            NutritionRow("Sugars", isTopLevel = false, nutrition.sugarContent, oldNutrition.sugarContent, NutritionUnit.GRAMS)
            NutritionRow("Protein", isTopLevel = true, nutrition.proteinContent, oldNutrition.proteinContent, NutritionUnit.GRAMS)
        }
    }
}

enum class GainType {
    GOOD,
    BAD
}

@Composable
private fun NutritionalBenefits(
    nutrition: NutritionalInformation,
    goals: List<NutritionGoal>,
    modifier: Modifier = Modifier
) {
    val relevantMetrics = remember(nutrition, goals) {
        nutrition
            .toGains()
            .mapNotNull { gain ->
                val goal = goals.firstOrNull { it.metric == gain.metric } ?: return@mapNotNull null
                val gainType = when {
                    gain.value == 0.0 -> return@mapNotNull null
                    goal.amount == Amount.LOW && gain.value < 0 -> GainType.GOOD
                    goal.amount == Amount.HIGH && gain.value > 0 -> GainType.GOOD
                    else -> GainType.BAD
                }
                gain to gainType
            }.sortedBy { it.first.metric.ordinal }
    }
    relevantMetrics.forEach { (gain, gainType) ->
        NutritionalGain(
            label = gain.metric.displayName,
            value = gain.value,
            gainType = gainType,
            unit = gain.metric.unit,
            modifier = modifier
        )
    }
}

@Composable
fun NutritionalGain(
    label: String,
    value: Number,
    gainType: GainType,
    unit: NutritionUnit,
    modifier: Modifier = Modifier
) {
    val color = when (gainType) {
        GainType.GOOD -> Green30
        GainType.BAD -> Red30
    }
    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                if (value.toInt() > 0) append("+")
                append("${value.toInt()}${unit.suffix}")
            }
            append(" ")
            withStyle(SpanStyle(fontWeight = FontWeight.Normal)) {
                append(label)
            }
        },
        color = color,
        style = MaterialTheme.typography.labelSmall,
        modifier = modifier
            .padding(horizontal = 2.dp)
            .background(Color(0x6FCCCCCC), MaterialTheme.shapes.small)
            .padding(horizontal = 4.dp, vertical = 2.dp)
    )
}



@Composable
fun NutritionRow(
    label: String,
    isTopLevel: Boolean,
    value: Number?,
    oldNumber: Number? = null,
    unit: NutritionUnit = NutritionUnit.GRAMS
) {
    val style = if (isTopLevel) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodySmall
    val indent = if (isTopLevel) 0.dp else 16.dp
    // Output 3 texts as a row in a table: label, old number, and new number
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = indent, top = 4.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = style)
        Spacer(Modifier.weight(1f))
        oldNumber?.takeIf { it != value && value != null }?.let {
            Text(text = "${it.toInt()}${unit.suffix}",
                style = style.copy(textDecoration = TextDecoration.LineThrough, fontWeight = FontWeight.Light),
                textAlign = TextAlign.End,
                modifier = Modifier.requiredWidth(72.dp)
            )
        }
        Text(
            text = value?.let { "${it.toInt()}${unit.suffix}" } ?: "N/A", style = style,
            textAlign = TextAlign.End,
            modifier = Modifier.requiredWidth(72.dp)
        )
    }
}

@Composable
fun MenuCreation(item: MenuItem, goals: List<NutritionGoal>) {
    MenuCard {
        Column(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(item.menuItemTitle)
                }
            }
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge
            )
            HorizontalDivider()
            item.appliedCustomizations.forEachIndexed { index, customization ->
                CustomizationItem(customization, goals)
                Spacer(modifier = Modifier.height(1.dp))
            }
            //Text(
            //    text = "More options",
            //    style = MaterialTheme.typography.bodyMedium
            //)
            //item.availableCustomizations.forEachIndexed { index, customization ->
            //    CustomizationItem(customization, goals)
            //    if (index < item.availableCustomizations.size - 1) {
            //        Spacer(modifier = Modifier.height(1.dp))
            //    }
            //}
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomizationItem(
    customization: MenuCustomization,
    goals: List<NutritionGoal>,
    modifier: Modifier = Modifier
) {
    val icon = when (customization.type) {
        ModificationType.Choose -> "➕"
        ModificationType.Add -> "➕"
        ModificationType.Remove -> "➖"
        ModificationType.Substitute -> "🔄"
    }
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(end = 8.dp, top = 2.dp)
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            val text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(customization.ingredient)
                }
                if (customization.substituted != null) {
                    append(" ")
                    withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                        append(customization.substituted)
                    }
                }
                append(": ")
                append(customization.reason)
            }
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
            )
            FlowRow(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                customization.nutritionDifference?.let {
                    NutritionalBenefits(it, goals, Modifier)
                }
            }
        }
    }
}

@Composable
private fun MenuCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF00FFAB), // Bright green
            Color(0xFF6200EA), // Purple
            Color(0xFFFFC107), // Amber
            Color(0xFFFF4081)  // Pink
        )
    )
    GenerativeAICard(
        modifier.fillMaxWidth().padding(horizontal = 16.dp),
    ) {
        content()
    }
}

@Composable
fun GenerativeAICard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
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
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            content()
        }
    }
}

@Composable
private fun MenuLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        textAlign = TextAlign.Center,
        modifier = modifier.fillMaxWidth()
    )
}
