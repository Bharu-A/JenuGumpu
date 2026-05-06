package com.jenugumpu.util

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

object LocaleManager {

    /**
     * Applies the selected locale using the modern AppCompatDelegate API.
     * This updates the entire app UI language — including Compose text — immediately.
     */
    fun apply(context: Context, kannada: Boolean) {
        val tag = if (kannada) "kn" else "en"
        // Modern approach: AppCompatDelegate handles recreation automatically
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(tag))
    }

    /**
     * Wraps a context with the desired locale configuration.
     * Used by attachBaseContext in MainActivity for pre-API-33 compat.
     */
    fun wrap(context: Context, kannada: Boolean): Context {
        val locale = if (kannada) Locale("kn") else Locale.ENGLISH
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}
