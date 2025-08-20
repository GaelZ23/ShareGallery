package com.gaelraul.sharegallery.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gaelraul.sharegallery.utils.PermissionManager
import com.gaelraul.sharegallery.utils.ImagePicker
import com.gaelraul.sharegallery.ui.viewmodel.ShareGalleryViewModel
import com.gaelraul.sharegallery.ui.theme.*
import com.gaelraul.sharegallery.ui.components.AnimatedLoadingButton
import com.gaelraul.sharegallery.ui.components.AnimatedFadeInContent
import kotlinx.coroutines.launch
import android.util.Base64

@Composable
fun MobileScreen(
    onRequestPermission: (String) -> Unit,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    viewModel: ShareGalleryViewModel = viewModel()
) {
    var username by remember { mutableStateOf("") }
    var isLoggedIn by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }
    var uploadMessage by remember { mutableStateOf("") }
    
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    // Observar estados del ViewModel
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    // Launcher para seleccionar imagen
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            selectedImageUri = selectedUri
            uploadMessage = "Imagen seleccionada: ${ImagePicker.getFileName(context, selectedUri)}"
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!isLoggedIn) {
                // Pantalla de login mejorada
                LoginScreen(
                    username = username,
                    onUsernameChange = { username = it },
                    onLogin = { 
                        if (username.isNotBlank()) {
                            isLoggedIn = true
                            viewModel.setCurrentUsername(username)
                        }
                    }
                )
            } else {
                // Pantalla principal después del login
                MainMobileScreen(
                    username = username,
                    selectedImageUri = selectedImageUri,
                    isUploading = isLoading,
                    uploadMessage = uploadMessage,
                    onSelectPhoto = {
                        if (PermissionManager.hasGalleryPermission(context)) {
                            imagePicker.launch("image/*")
                        } else {
                            onRequestPermission(PermissionManager.getRequiredPermission())
                        }
                    },
                    onUploadPhoto = {
                        selectedImageUri?.let { uri ->
                            // Subida real a Firebase
                            scope.launch {
                                try {
                                    isUploading = true
                                    uploadMessage = "Subiendo imagen a Firebase..."
                                    
                                    // Convertir URI a Base64
                                    val imageBytes = ImagePicker.uriToBytes(context, uri)
                                    
                                    if (imageBytes != null) {
                                        // Convertir bytes a Base64
                                        val imageBase64 = android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT)
                                        
                                        // Subir a Firebase usando el ViewModel
                                        viewModel.uploadPhoto(
                                            username = username,
                                            imageBase64 = imageBase64,
                                            fileName = ImagePicker.getFileName(context, uri)
                                        )
                                        
                                        // Limpiar selección
                                        selectedImageUri = null
                                        uploadMessage = "¡Imagen subida exitosamente!"
                                    } else {
                                        uploadMessage = "Error: No se pudo leer la imagen"
                                    }
                                } catch (e: Exception) {
                                    uploadMessage = "Error al subir: ${e.message}"
                                } finally {
                                    isUploading = false
                                }
                            }
                        }
                    },
                    onLogout = { 
                        isLoggedIn = false
                        username = ""
                        selectedImageUri = null
                        uploadMessage = ""
                        viewModel.setCurrentUsername("")
                    }
                )
            }
        }
    }
    
    // Manejar callbacks de permisos
    LaunchedEffect(Unit) {
        // Cuando se concede el permiso, abrir selector de imagen
        onPermissionGranted()
    }
    
    // Manejar cuando se deniega el permiso
    LaunchedEffect(Unit) {
        onPermissionDenied()
    }
    
    // Mostrar errores del ViewModel
    LaunchedEffect(error) {
        error?.let { errorMessage ->
            uploadMessage = "Error: $errorMessage"
            viewModel.clearError()
        }
    }
}

@Composable
fun LoginScreen(
    username: String,
    onUsernameChange: (String) -> Unit,
    onLogin: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Logo y título con gradiente
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(gradientStart, gradientEnd)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📱",
                    fontSize = 40.sp
                )
            }
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "ShareGallery",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Text(
                    text = "Comparte fotos en tiempo real",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Campo de entrada mejorado
            OutlinedTextField(
                value = username,
                onValueChange = onUsernameChange,
                label = { Text("Nombre de usuario") },
                placeholder = { Text("Ej: Gael, Ana, Carlos...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )
            
            // Botón de login con gradiente
            Button(
                onClick = onLogin,
                enabled = username.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    "Entrar a la galería", 
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Composable
fun MainMobileScreen(
    username: String,
    selectedImageUri: Uri?,
    isUploading: Boolean,
    uploadMessage: String,
    onSelectPhoto: () -> Unit,
    onUploadPhoto: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Header con información del usuario
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "¡Hola, $username! 👋",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                
                Text(
                    text = "Bienvenido a ShareGallery",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        // Botón para seleccionar foto con diseño mejorado
        AnimatedFadeInContent(visible = true) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Button(
                    onClick = onSelectPhoto,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("📸", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Seleccionar foto", 
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
        
        // Mostrar imagen seleccionada
        selectedImageUri?.let { uri ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Imagen seleccionada:",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Text(
                        text = uri.lastPathSegment ?: "Sin nombre",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Botón para subir foto
                    AnimatedLoadingButton(
                        text = "🚀 Subir a la galería",
                        isLoading = isUploading,
                        onClick = onUploadPhoto,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    )
                }
            }
        }
        
        // Mostrar mensaje de estado
        if (uploadMessage.isNotBlank()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (uploadMessage.contains("Error")) {
                        MaterialTheme.colorScheme.errorContainer
                    } else if (uploadMessage.contains("exitosamente")) {
                        MaterialTheme.colorScheme.tertiaryContainer
                    } else {
                        MaterialTheme.colorScheme.secondaryContainer
                    }
                )
            ) {
                Text(
                    text = uploadMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (uploadMessage.contains("Error")) {
                        MaterialTheme.colorScheme.onErrorContainer
                    } else if (uploadMessage.contains("exitosamente")) {
                        MaterialTheme.colorScheme.onTertiaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSecondaryContainer
                    },
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        
        // Botón de logout
        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                "Cerrar sesión",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
