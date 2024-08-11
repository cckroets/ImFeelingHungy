import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ckroetsch.imfeelinghungry.data.MenuItem

@Composable
fun ViewDiscoverItemScreen(
    navController: NavController,
    navBackStackEntry: NavBackStackEntry
) {
    val menuItemJson = navBackStackEntry.arguments?.getString("menuItemJson")

    val menuItem = remember(menuItemJson) {
        menuItemJson?.let { Json.decodeFromString<MenuItem>(it) }
    }

    menuItem?.let {
        // Use the menuItem object to display the details
        Text(text = it.creationTitle ?: it.menuItemTitle)
        // Add more UI components here to display the rest of the details
    }
}
