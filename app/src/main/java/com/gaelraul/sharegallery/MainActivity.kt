package com.gaelraul.sharegallery

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.gaelraul.sharegallery.ui.theme.ShareGalleryTheme
import com.gaelraul.sharegallery.ui.screens.MobileScreen
import com.gaelraul.sharegallery.ui.screens.TvScreen

class MainActivity : ComponentActivity() {
    
    // Contract para solicitar permisos
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permiso concedido, continuar con la selección de imagen
            onPermissionGranted?.invoke()
        } else {
            // Permiso denegado, mostrar mensaje
            onPermissionDenied?.invoke()
        }
    }
    
    // Callbacks para manejar el resultado de permisos
    var onPermissionGranted: (() -> Unit)? = null
    var onPermissionDenied: (() -> Unit)? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShareGalleryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShareGalleryApp(
                        onRequestPermission = { permission ->
                            requestPermissionLauncher.launch(permission)
                        },
                        onPermissionGranted = { onPermissionGranted?.invoke() },
                        onPermissionDenied = { onPermissionDenied?.invoke() }
                    )
                }
            }
        }
    }
    
    /**
     * Solicita un permiso específico
     */
    fun requestPermission(permission: String) {
        requestPermissionLauncher.launch(permission)
    }
    
    /**
     * Verifica si tenemos un permiso específico
     */
    fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
}

@Composable
fun ShareGalleryApp(
    onRequestPermission: (String) -> Unit,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    
    // Detectar si es TV basándose en el ancho de pantalla
    // TV típicamente tiene pantallas más anchas
    val isTv = screenWidth >= 600.dp
    
    if (isTv) {
        TvScreen()
    } else {
        MobileScreen(
            onRequestPermission = onRequestPermission,
            onPermissionGranted = onPermissionGranted,
            onPermissionDenied = onPermissionDenied
        )
    }
}