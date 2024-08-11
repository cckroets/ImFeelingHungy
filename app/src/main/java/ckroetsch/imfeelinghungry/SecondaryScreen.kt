package ckroetsch.imfeelinghungry

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondaryScreen(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable (paddingValues: PaddingValues, scrollConnection: NestedScrollConnection) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = modifier,
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                ),
                title = { Text(text = title) },
                scrollBehavior = scrollBehavior,
            )
        }
    ) {
        content(it, scrollBehavior.nestedScrollConnection)
    }
}
