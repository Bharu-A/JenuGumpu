package com.jenugumpu

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.jenugumpu.navigation.AppNavigation
import com.jenugumpu.ui.theme.JenuGumpuTheme
import com.jenugumpu.util.LocaleManager
import com.jenugumpu.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val settingsVm: SettingsViewModel by viewModels()

    override fun attachBaseContext(newBase: Context) {
        // Read saved locale preference and wrap the base context before UI inflates
        val prefs = newBase.getSharedPreferences("jenu_prefs", Context.MODE_PRIVATE)
        val isKannada = prefs.getBoolean("isKannada", false)
        super.attachBaseContext(LocaleManager.wrap(newBase, isKannada))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDark by settingsVm.isDarkMode.collectAsState()
            JenuGumpuTheme(darkTheme = isDark) {
                AppNavigation(
                    settingsVm = settingsVm,
                    onToggleLocale = { kannada -> LocaleManager.apply(this, kannada) }
                )
            }
        }
    }
}
