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
- **Firebase**: Base de datos en tiempo real
- **Kotlin Coroutines**: Programación asíncrona
- **MVVM Architecture**: Patrón de arquitectura limpia

## 📋 Requisitos

- Android 6.0 (API 23) o superior
- Kotlin 1.8+
- Android Studio Hedgehog o superior

## 🛠️ Instalación

1. Clona el repositorio:
```bash
git clone https://github.com/tu-usuario/ShareGallery.git
```

2. Abre el proyecto en Android Studio

3. Configura Firebase:
   - Crea un proyecto en Firebase Console
   - Descarga `google-services.json` y colócalo en la carpeta `app/`
   - Habilita Firestore Database

4. Ejecuta la aplicación:
```bash
./gradlew assembleDebug
```

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

## 🤝 Contribuir

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para detalles.

## 👨‍💻 Autor

**Gael Raul** - [@gaelraul](https://github.com/gaelraul)

---

⭐ Si te gusta este proyecto, ¡dale una estrella!
