package ckroetsch.imfeelinghungry.favorite

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ckroetsch.imfeelinghungry.SecondaryScreen
import ckroetsch.imfeelinghungry.data.PreferencesViewModel
import ckroetsch.imfeelinghungry.discover.MenuItemCard

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    viewModel: PreferencesViewModel,
    navController: NavController
) {
    SecondaryScreen(
        modifier = modifier,
        title = "Favorites",
    ) { padding, scrollConnection ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollConnection),
            contentPadding = PaddingValues(
                start = 12.dp,
                end = 12.dp,
                top = padding.calculateTopPadding() + 16.dp,
                bottom = padding.calculateBottomPadding()
            )
        ) {
            val favorites = viewModel.favorites.value

            items(favorites) { menuItem ->
                MenuItemCard(
                    menuItem = menuItem,
                    modifier = Modifier.fillMaxSize(),
                    preferencesViewModel = viewModel,
                    navController = navController
                )
            }
        }
    }
}
