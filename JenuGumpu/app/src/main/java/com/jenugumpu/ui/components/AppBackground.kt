package com.jenugumpu.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AppBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFFFF7E8),
                        Color(0xFFFFE7C8),
                        Color(0xFFFFF3DF)
                    )
                )
            )
    ) {
        content()
    }
}

@Composable
fun AppHeader() {
    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Jenu-Gumpu", style = MaterialTheme.typography.headlineLarge)
        Text("Honey Producer Collective", style = MaterialTheme.typography.bodyMedium)
    }
}
