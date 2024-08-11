package ckroetsch.imfeelinghungry

import ViewDiscoverItemScreen
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ckroetsch.imfeelinghungry.data.PreferencesViewModel
import ckroetsch.imfeelinghungry.data.Result
import ckroetsch.imfeelinghungry.discover.DiscoverScreen
import ckroetsch.imfeelinghungry.favorite.FavoriteScreen
import ckroetsch.imfeelinghungry.onboarding.PreferencesScreen
import ckroetsch.imfeelinghungry.ui.theme.DarkOrange
import ckroetsch.imfeelinghungry.ui.theme.ImFeelingHungryTheme
import ckroetsch.imfeelinghungry.ui.theme.MustardYellow

enum class Routes(val routes: String) {
    WELCOME("welcome"),
    RESTAURANT("preferences"),
    FOOD("food"),
    DIETARY_PREFERENCE("dietaryPreference"),
    GENERATE_ORDER("generateOrder")
}

@Composable
fun MainNavigation() {
    val viewModel: PreferencesViewModel = viewModel()
    val startDestination by remember { mutableStateOf<String>("welcome") }
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { HungryNavBar(navController) }
    ) { padding ->
        NavHost(
            navController = navController,
            modifier = Modifier.background(MustardYellow).padding(padding),
            startDestination = startDestination
        ) {
            composable("welcome") { WelcomeScreen(Modifier, navController) }
            composable("preferences") { PreferencesScreen(viewModel = viewModel, navController = navController) }
            composable("generateOrder") {
                GeneratedOrderScreen(
                    viewModel = viewModel,
                    navController = navController,
                )
            }
            composable(
                route = "favorites",
                enterTransition = {
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End)
                },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None },
                exitTransition = {
                    if (targetState.destination.route == "welcome") {
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start)
                    } else {
                        ExitTransition.KeepUntilTransitionsFinished
                    }
                },
            ) {
                FavoriteScreen(viewModel = viewModel, navController = navController)
            }
            composable(
                route = "discover",
                enterTransition = {
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)
                },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.KeepUntilTransitionsFinished },
                exitTransition = {
                    if (targetState.destination.route == "welcome") {
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End)
                    } else {
                        ExitTransition.KeepUntilTransitionsFinished
                    }
                },
            ) {
                DiscoverScreen(viewModel = viewModel, navController = navController)
            }
            // Add other destinations here
            composable(
                route = "viewDiscoverItem/{menuItemJson}",
                arguments = listOf(navArgument("menuItemJson") { type = NavType.StringType }),
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(300)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(300)
                    )
                }
            ) { backStackEntry ->
                ViewDiscoverItemScreen(navController, backStackEntry, viewModel)
            }
        }
    }
}

@Composable
fun GeneratedOrderScreen(
    modifier: Modifier = Modifier,
    viewModel: PreferencesViewModel,
    navController: NavController
) {
    val menuItem by viewModel.generatedMenuItem.collectAsState(Result.Loading)
    LaunchedEffect(Unit) {
        viewModel.generateMenuItem()
    }

    Crossfade(menuItem) {
        when (val m = it) {
            is Result.Error -> {
                Text(text = "Error: ${m.message}")
            }
            Result.Loading -> {
                PhsyicsExample()
                //LoadingAnimation(sharedElementScope, animatedVisibilityScope)
            }
            is Result.Success -> {
                val goals = remember(viewModel) { viewModel.dietGoals }
                MenuItemScreen(
                    m.data,
                    goals,
                    navController,
                    viewModel
                ) { viewModel.regenerateWithInstructions(it) }
            }
        }
    }
}


@Composable
fun HungryNavBar(navController: NavController) {
    NavigationBar(
        containerColor = MustardYellow,
        tonalElevation = 24.dp,
    ) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        val isFavoritesSelected = currentRoute == "favorites"
        val isDiscoverSelected = currentRoute == "discover" || currentRoute?.contains("viewDiscoverItem") == true
        val isHomeSelected = currentRoute == "welcome" || currentRoute == "preferences" || currentRoute == "generateOrder"

        HungryNavItem(
            selectedIcon = Icons.Filled.Favorite,
            unselectedIcon = Icons.Outlined.FavoriteBorder,
            label = "Favorites",
            selected = isFavoritesSelected,
            // Navigate to favorites and clear back stack
            onClick = {
                navController.navigate("favorites")
            }
        )
        HungryNavItem(
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            label = "Home",
            selected = isHomeSelected,
            // Navigate to welcome and clear back stack
            onClick = {
                navController.navigate("welcome")
            }
        )
        HungryNavItem(
            selectedIcon = Icons.Filled.Search,
            unselectedIcon = Icons.Outlined.Search,
            label = "Discover",
            selected = isDiscoverSelected,
            onClick = {
                navController.navigate("discover")
            }
        )
    }
}

@Composable
private fun RowScope.HungryNavItem(
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector = selectedIcon,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    NavigationBarItem(
        icon = {
            Icon(if (selected) selectedIcon else unselectedIcon, contentDescription = "Home")
        },
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = DarkOrange,
            selectedIconColor = Color.White
        ),
        label = { Text(label) },
        selected = selected,
        onClick = onClick
    )
}


@Composable
fun BottomNavigationBar() {
    Scaffold(
        containerColor = Color.White,
        bottomBar = {

        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            // Content
        }
    }
}

@Preview
@Composable
fun BottomNavigationBarPreview() {
    ImFeelingHungryTheme {
        BottomNavigationBar()
    }
}


// Custom fab that allows for displaying extended content
@Composable
fun CustomFloatingActionButton(
    isVisible: Boolean = true,
    fabIcon: ImageVector,
    content: @Composable () -> Unit,
) {
    var isExpanded = remember { mutableStateOf(false) }
    val offset = LocalDensity.current.run { IntOffset(0.dp.roundToPx(), -64.dp.roundToPx()) }

    // Get current screen width
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp - 64.dp
    val elevation = FloatingActionButtonDefaults.elevation()
    val shape = RoundedCornerShape(18.dp)
    Box(Modifier.fillMaxSize()) {
        AnimatedPopup(
            expanded = isExpanded.value,
            offset = offset,
            alignment = Alignment.BottomEnd,
            onDismissRequest = { isExpanded.value = false },
        ) {
            Surface(
                shape = shape,
                shadowElevation = 6.dp,
                color = DarkOrange,
                contentColor = Color.White,
                modifier = Modifier
                    .widthIn(max = screenWidth)
                    .heightIn(min = 200.dp)
                    .defaultMinSize(minWidth = 200.dp)
            ) {
                content()
            }
        }

        ExtendedFloatingActionButton(
            onClick = {
                isExpanded.value = !isExpanded.value
            },
            containerColor = DarkOrange,
            contentColor = Color.White,
            elevation = elevation,
            modifier = Modifier.align(Alignment.BottomEnd),
            shape = shape
        ) {
            Icon(
                imageVector = fabIcon,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp),
                tint = Color.White
            )
            AnimatedVisibility(
                visible = isExpanded.value,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally()
            ) {
                Text(
                    text = "Refine",
                    softWrap = false,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }
    }
}

