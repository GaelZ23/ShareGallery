# 📱📺 ShareGallery

Una aplicación Android para crear galerías compartidas en tiempo real entre dispositivos móviles y Android TV usando Firebase.

## 🎯 Descripción del Proyecto

ShareGallery permite a los usuarios subir fotos desde sus teléfonos móviles y verlas en tiempo real en una TV Android. Cada foto se muestra junto al nombre de usuario que la subió, creando una experiencia social de galería compartida.

## ✨ Características Principales

- **📤 Subida de fotos** desde galería del móvil
- **📺 Visualización en tiempo real** en Android TV
- **👤 Sistema de usuarios** con nombres de usuario
- **🔥 Sincronización en tiempo real** con Firebase
- **📱 Multi-plataforma** (Móvil + TV)
- **🎨 Interfaz moderna** con Jetpack Compose

## 🏗️ Arquitectura del Proyecto

El proyecto está estructurado en módulos para una mejor organización y reutilización de código:

```
ShareGallery/
├── :app/           # Módulo principal de la aplicación
├── :shared/        # Lógica de negocio y Firebase (compartido)
├── :mobile/        # Funcionalidades específicas para móvil
└── :tv/            # Funcionalidades específicas para TV
```

### **Módulos:**

- **`:app`**: Aplicación principal que integra todos los módulos
- **`:shared`**: Contiene la lógica de Firebase, modelos de datos y repositorios
- **`:mobile`**: Interfaz y funcionalidades específicas para dispositivos móviles
- **`:tv`**: Interfaz y funcionalidades específicas para Android TV

## 🛠️ Tecnologías Utilizadas

- **Kotlin 2.0.21** - Lenguaje de programación
- **Jetpack Compose** - UI moderna declarativa
- **Firebase Firestore** - Base de datos en tiempo real
- **Firebase Storage** - Almacenamiento de imágenes
- **Material Design 3** - Sistema de diseño
- **MVVM Architecture** - Patrón de arquitectura
- **Gradle 8.10.0** - Sistema de build

## 📋 Requisitos del Sistema

- **Android Studio**: Arctic Fox o superior
- **Min SDK**: API 27 (Android 8.1)
- **Target SDK**: API 35 (Android 15)
- **Compile SDK**: API 35
- **Java**: Versión 11

## 🚀 Instalación y Configuración

### **1. Clonar el repositorio**
```bash
git clone https://github.com/tu-usuario/ShareGallery.git
cd ShareGallery
```

### **2. Abrir en Android Studio**
- Abrir Android Studio
- Seleccionar "Open an existing project"
- Navegar a la carpeta del proyecto y seleccionarla

### **3. Configurar Firebase**
- Crear proyecto en [Firebase Console](https://console.firebase.google.com/)
- Habilitar Firestore Database y Storage
- Descargar `google-services.json` y colocarlo en la carpeta `app/`
- Configurar reglas de seguridad en Firestore y Storage

### **4. Sincronizar proyecto**
- Hacer clic en "Sync Project with Gradle Files"
- Esperar a que se descarguen todas las dependencias

### **5. Ejecutar la aplicación**
- Conectar dispositivo Android o usar emulador
- Hacer clic en "Run" (▶️)

## 📱 Uso de la Aplicación

### **En Móvil:**
1. Ingresar nombre de usuario
2. Seleccionar foto desde galería
3. La foto se sube automáticamente a Firebase
4. Aparece en la TV en tiempo real

### **En TV:**
1. Abrir la aplicación en Android TV
2. Las fotos se muestran automáticamente
3. Cada foto incluye el nombre del usuario
4. Actualización en tiempo real

## 🔧 Configuración de Firebase

### **Firestore Database:**
```javascript
// Reglas básicas de seguridad
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /photos/{photoId} {
      allow read, write: if true; // Para desarrollo
    }
  }
}
```

### **Storage:**
```javascript
// Reglas básicas de seguridad
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /{allPaths=**} {
      allow read, write: if true; // Para desarrollo
    }
  }
}
```

## 📁 Estructura de Datos

### **Colección Photos:**
```json
{
  "id": "auto-generated",
  "username": "nombre_usuario",
  "imageUrl": "https://firebase-storage-url",
  "timestamp": "2024-01-01T00:00:00Z",
  "fileName": "photo_123.jpg"
}
```

## 🚧 Estado del Proyecto

- [x] Configuración base del proyecto
- [x] Configuración de Jetpack Compose
- [x] Configuración de Firebase
- [ ] Implementación de modelos de datos
- [ ] Interfaz de usuario móvil
- [ ] Interfaz de usuario TV
- [ ] Funcionalidad de subida de fotos
- [ ] Sincronización en tiempo real
- [ ] Testing y optimización

## 🤝 Contribuir

Este es un proyecto escolar, pero si quieres contribuir:

1. Fork el proyecto
2. Crear una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 👨‍💻 Autor

**Gael Raul** - Proyecto escolar de desarrollo Android

## 🙏 Agradecimientos

- Google por Android y Firebase
- JetBrains por Kotlin
- La comunidad de Android por las librerías utilizadas

## 📞 Contacto

- **Proyecto**: [ShareGallery](https://github.com/tu-usuario/ShareGallery)
- **Issues**: [Reportar problemas](https://github.com/tu-usuario/ShareGallery/issues)

---

**⭐ Si te gusta el proyecto, dale una estrella en GitHub!**
