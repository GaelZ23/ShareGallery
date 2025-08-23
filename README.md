# 📱 ShareGallery

Una aplicación moderna para compartir fotos en tiempo real entre dispositivos móviles y TV.

## ✨ Características

### 🎨 Diseño Moderno y Atractivo
- **Paleta de colores moderna**: Utiliza una combinación elegante de indigo, violeta y cyan
- **Gradientes sutiles**: Efectos visuales suaves que mejoran la experiencia del usuario
- **Bordes redondeados**: Diseño moderno con esquinas suaves
- **Sombras y elevación**: Efectos de profundidad para una interfaz más rica
- **Tipografía mejorada**: Sistema de tipografía consistente y legible

### 📱 Pantalla Móvil
- **Login elegante**: Pantalla de inicio con logo animado y gradientes
- **Interfaz intuitiva**: Botones grandes y fáciles de usar
- **Feedback visual**: Indicadores de carga y mensajes de estado
- **Animaciones suaves**: Transiciones fluidas entre estados
- **Diseño responsivo**: Se adapta a diferentes tamaños de pantalla

### 📺 Pantalla TV
- **Galería en tiempo real**: Muestra fotos subidas desde móviles
- **Grid adaptativo**: Organización automática de fotos
- **Cards atractivas**: Diseño moderno para cada foto
- **Estados visuales**: Carga, error y vacío bien diseñados
- **Header destacado**: Título con gradiente y logo

### 🎭 Componentes Animados
- **Botones con animación**: Efectos de escala y transiciones
- **Indicadores de carga**: Animaciones suaves durante procesos
- **Mensajes de éxito**: Notificaciones animadas
- **Contenido con fade**: Apariciones y desapariciones suaves

## 🎨 Paleta de Colores

### Tema Claro
- **Primario**: Indigo moderno (#6366F1)
- **Secundario**: Violeta (#8B5CF6)
- **Terciario**: Cyan (#06B6D4)
- **Fondo**: Gris muy claro (#FAFAFA)
- **Superficie**: Blanco puro

### Tema Oscuro
- **Primario**: Indigo claro (#A5B4FC)
- **Secundario**: Violeta claro (#C4B5FD)
- **Terciario**: Cyan claro (#67E8F9)
- **Fondo**: Azul muy oscuro (#0F172A)
- **Superficie**: Azul oscuro (#1E293B)

## 🚀 Tecnologías

- **Jetpack Compose**: UI moderna y declarativa
- **Material Design 3**: Sistema de diseño actualizado
- **Firebase Realtime Database**: Base de datos en tiempo real
- **Kotlin Coroutines**: Programación asíncrona
- **MVVM Architecture**: Patrón de arquitectura limpia

## 📋 Requisitos

- Android 6.0 (API 23) o superior
- Kotlin 1.9.22
- Android Studio Hedgehog o superior
- Cuenta de Google para Firebase
- Conexión a Internet para sincronización

## 🛠️ Instalación

1. Clona el repositorio:
```bash
git clone https://github.com/tu-usuario/ShareGallery.git
```

2. Abre el proyecto en Android Studio

3. Ejecuta la aplicación:
```bash
./gradlew assembleDebug
```

## ⚙️ Configuraciones Adicionales

### 🔥 Configuración de Firebase

#### 1. Crear Proyecto Firebase
1. Ve a [Firebase Console](https://console.firebase.google.com/)
2. Haz clic en **"Crear un proyecto"**
3. Ingresa un nombre para tu proyecto (ej: "ShareGallery")
4. **Desactiva** Google Analytics (opcional para desarrollo)
5. Haz clic en **"Crear proyecto"**

#### 2. Configurar Realtime Database
1. En el menú lateral, selecciona **"Realtime Database"**
2. Haz clic en **"Crear base de datos"**
3. Selecciona **"Comenzar en modo de prueba"** (para desarrollo)
4. Elige la ubicación más cercana a tu región
5. Haz clic en **"Listo"**

#### 3. Configurar Reglas de Seguridad
1. En Realtime Database, ve a la pestaña **"Reglas"**
2. Reemplaza las reglas existentes con:
```json
{
  "rules": {
    ".read": true,
    ".write": true
  }
}
```
3. Haz clic en **"Publicar"**

#### 4. Descargar google-services.json
1. En la configuración del proyecto, haz clic en el ícono de **⚙️** (engranaje)
2. Selecciona **"Configuración del proyecto"**
3. En la pestaña **"General"**, busca **"Tus apps"**
4. Haz clic en **"Agregar app"** → **"Android"**
5. Ingresa el **Package name**: `com.gaelraul.sharegallery`
6. Haz clic en **"Registrar app"**
7. Descarga el archivo **`google-services.json`**
8. Coloca el archivo en la carpeta **`app/`** del proyecto

### 📱 Configuración del Dispositivo Móvil

#### 1. Habilitar Instalación de Fuentes Desconocidas
1. Ve a **Configuración** → **Seguridad**
2. Activa **"Fuentes desconocidas"** o **"Instalar apps desconocidas"**
3. Permite la instalación desde **Android Studio** o **Explorador de archivos**

#### 2. Permisos de Galería
1. Al abrir la app por primera vez, acepta los permisos de **"Acceso a fotos y medios"**
2. Si no aparecen, ve a **Configuración** → **Apps** → **ShareGallery** → **Permisos**
3. Activa **"Almacenamiento"** y **"Fotos y medios"**

### 📺 Configuración del Android TV

#### 1. Instalar la Aplicación
1. Transfiere el APK de TV a tu Android TV
2. Usa **ADB** o **Explorador de archivos** para instalarlo
3. O compila directamente en el TV desde Android Studio

#### 2. Configuración de Red
1. Asegúrate de que el TV esté en la **misma red WiFi** que los móviles
2. Verifica que no haya restricciones de firewall
3. El TV debe tener acceso a Internet para Firebase

### 🔧 Configuración del Proyecto

#### 1. Verificar Dependencias
Asegúrate de que el archivo `gradle/libs.versions.toml` contenga:
```toml
[versions]
kotlin = "1.9.22"
composeCompiler = "1.5.8"
firebaseBom = "32.7.0"

[libraries]
firebase-database = { group = "com.google.firebase", name = "firebase-database-ktx" }
```

#### 2. Verificar build.gradle.kts
En `app/build.gradle.kts` debe estar:
```kotlin
plugins {
    id("com.google.gms.google-services")
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-database-ktx")
}
```

### 🚨 Solución de Problemas Comunes

#### Error: "google-services.json is missing"
- Verifica que el archivo esté en la carpeta `app/`
- Asegúrate de que el package name coincida
- Reinicia Android Studio

#### Error: "Permission denied"
- Verifica los permisos en el dispositivo
- Asegúrate de que la app tenga acceso a la galería
- Reinicia la aplicación

#### Error: "Database connection failed"
- Verifica la conexión a Internet
- Confirma que las reglas de Firebase permitan lectura/escritura
- Verifica que la URL de la base de datos sea correcta

#### Las imágenes no se muestran en TV
- Verifica que el Base64 se esté generando correctamente
- Confirma que la red WiFi sea la misma
- Revisa los logs de Firebase Console

### 📊 Verificación de Funcionamiento

#### 1. Prueba de Conexión
1. Abre la app en móvil y TV
2. Sube una foto desde el móvil
3. Verifica que aparezca en Firebase Console
4. Confirma que se muestre en la TV

#### 2. Prueba de Tiempo Real
1. Sube múltiples fotos rápidamente
2. Verifica que aparezcan en la TV sin recargar
3. Confirma que el username se muestre correctamente

## 📱 Uso

### Móvil
1. Abre la aplicación en tu dispositivo móvil
2. Ingresa tu nombre de usuario
3. Selecciona una foto de tu galería
4. Sube la foto para compartirla

### TV
1. Abre la aplicación en tu dispositivo Android TV
2. Las fotos subidas desde móviles aparecerán automáticamente
3. Disfruta de la galería en tiempo real

## 🎨 Personalización

### Colores
Los colores se pueden personalizar en `app/src/main/java/com/gaelraul/sharegallery/ui/theme/Color.kt`

### Tipografía
La tipografía se puede modificar en `app/src/main/java/com/gaelraul/sharegallery/ui/theme/Type.kt`

### Componentes
Los componentes animados están en `app/src/main/java/com/gaelraul/sharegallery/ui/components/AnimatedComponents.kt`


## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para detalles.

## 👨‍💻 Autores

**Gael** - [@GaelZ23](https://github.com/GaelZ23)
**Raul** - [@Wyvern-Knight](https://github.com/Wyvern-Knight)

---

⭐ Si te gusta este proyecto, ¡dale una estrella!
