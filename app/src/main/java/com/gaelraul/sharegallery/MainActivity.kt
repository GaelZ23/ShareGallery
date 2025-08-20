package com.gaelraul.sharegallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.gaelraul.sharegallery.ui.theme.ShareGalleryTheme
import com.gaelraul.sharegallery.ui.screens.MobileScreen
import com.gaelraul.sharegallery.ui.screens.TvScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShareGalleryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShareGalleryApp()
                }
            }
        }
    }
}

@Composable
fun ShareGalleryApp() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    
    // Detectar si es TV basándose en el ancho de pantalla
    // TV típicamente tiene pantallas más anchas
    val isTv = screenWidth >= 600.dp
    
    if (isTv) {
        TvScreen()
    } else {
        MobileScreen()
    }
}