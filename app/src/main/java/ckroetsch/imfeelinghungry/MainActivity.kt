package ckroetsch.imfeelinghungry

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import ckroetsch.imfeelinghungry.data.PreferencesViewModel
import ckroetsch.imfeelinghungry.ui.theme.ImFeelingHungryTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImFeelingHungryTheme {
                MainNavigation()
            }
        }
    }
}
