package com.gaelraul.sharegallery.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gaelraul.sharegallery.utils.PermissionManager
import com.gaelraul.sharegallery.utils.ImagePicker
import com.gaelraul.sharegallery.ui.viewmodel.ShareGalleryViewModel
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
    val uiState by viewModel.uiState.collectAsState()
    val photos by viewModel.photos.collectAsState()
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
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (!isLoggedIn) {
            // Pantalla de login
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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "📱 ShareGallery",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Text(
            text = "Comparte fotos en tiempo real",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        OutlinedTextField(
            value = username,
            onValueChange = onUsernameChange,
            label = { Text("Nombre de usuario") },
            placeholder = { Text("Ej: Gael, Ana, Carlos...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        Button(
            onClick = onLogin,
            enabled = username.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Entrar a la galería", fontSize = 18.sp)
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
        Text(
            text = "¡Hola, $username! 👋",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = "Bienvenido a ShareGallery",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        // Botón para seleccionar foto
        Button(
            onClick = onSelectPhoto,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("📸 Seleccionar foto", fontSize = 18.sp)
        }
        
        // Mostrar imagen seleccionada
        selectedImageUri?.let { uri ->
            Text(
                text = "Imagen seleccionada: ${uri.lastPathSegment ?: "Sin nombre"}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
            
            // Botón para subir foto
            Button(
                onClick = onUploadPhoto,
                enabled = !isUploading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                if (isUploading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Subiendo...")
                } else {
                    Text("🚀 Subir a la galería")
                }
            }
        }
        
        // Mostrar mensaje de estado
        if (uploadMessage.isNotBlank()) {
            Text(
                text = uploadMessage,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Cerrar sesión")
        }
    }
}
