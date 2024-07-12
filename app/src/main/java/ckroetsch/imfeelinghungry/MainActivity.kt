package ckroetsch.imfeelinghungry

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import ckroetsch.imfeelinghungry.data.PreferencesViewModel


class MainActivity : ComponentActivity() {

    private val preferencesViewModel: PreferencesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainNavigation(preferencesViewModel)
        }
    }
}
