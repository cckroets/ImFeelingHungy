import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import ckroetsch.imfeelinghungry.MenuItemScreen
import ckroetsch.imfeelinghungry.data.HungryJson
import ckroetsch.imfeelinghungry.data.MenuItem
import ckroetsch.imfeelinghungry.data.PreferencesViewModel

@Composable
fun ViewDiscoverItemScreen(
    navController: NavController,
    navBackStackEntry: NavBackStackEntry,
    viewModel: PreferencesViewModel
) {
    val menuItemJson = navBackStackEntry.arguments?.getString("menuItemJson")

    val menuItem = remember(menuItemJson) {
        menuItemJson?.let { HungryJson.decodeFromString<MenuItem>(it) }
    }
    val goals = remember(viewModel) { viewModel.dietGoals }

    menuItem?.let {
        MenuItemScreen(
            menuItem = it,
            goals = goals,
            navController = navController,
            viewModel = viewModel
        )
    } ?: run {
        // If menuItem is null, show an error message
        Text("Error: Invalid menu item data.")
    }
}
