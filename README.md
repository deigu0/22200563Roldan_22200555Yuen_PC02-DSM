# Diario Espacial - Práctica Calificada 02 (DSM 2026-2)

Aplicación Android en **Kotlin** y **Jetpack Compose** que consume datos de la API abierta de la NASA y los almacena en una base de datos local **SQLite** mediante **Room**.

---

## 👨‍💻 Responsabilidades por Integrante

### **Alumno 1 - Opción Imágenes (APOD)**
* **Consumo API (Retrofit)**: Integración con el endpoint `wp-json/wp/v2/apod-basic` de APOD (`https://science.nasa.gov/`).
* **Base de datos Local (Room)**: Entidad `ApodEntity`, DAO `ApodDao` y persistencia con Room Database.
* **Mapeo explícito**: Conversión de DTO (`ApodDto`) a Entidad Room (`ApodEntity`) con preservación de estado `isFavorite`.
* **Interfaz de Usuario (Jetpack Compose)**:
  * **Pantalla de Lista APOD**: Muestra los últimos 7 días de imágenes astronómicas ordenadas de la más reciente a la más antigua. Distingue contenido en video (mostrando `"Contenido en video"` sin intentar cargar la imagen).
  * **Pantalla de Detalle**: Muestra la imagen HD en grande, título, fecha, explicación completa y créditos si están disponibles.
  * **Pantalla de Mis Imágenes Favoritas**: Acceso desde la parte superior de la lista para gestionar y visualizar las imágenes marcadas como favoritas.
  * **Gestión de Favoritos**: Botón interactivo (estrella/corazón) para marcar o desmarcar favoritos desde la lista o el detalle.

### **Alumno 2 - Opción Asteroides (NeoWs)**
* *(Sección a implementar por el Alumno 2 en su rama respectiva)*. Se ha dejado la pestaña y pantalla placeholder `AsteroidsPlaceholderScreen` configurada en la navegación compartida.

---

## 🛠️ Tecnologías y Arquitectura

* **Lenguaje**: Kotlin
* **UI**: Jetpack Compose (Material 3) + Navigation Compose
* **Arquitectura**: MVVM + Repository Pattern
* **Base de datos Local**: Room (SQLite)
* **Red**: Retrofit 2 + Gson + OkHttp Logging Interceptor
* **Carga de Imágenes**: Coil Compose
* **Seguridad de API Key**: Configurado vía `local.properties` (`NASA_API_KEY`) y accedido desde `BuildConfig.NASA_API_KEY`.

---

## 📁 Estructura del Proyecto

```
app/src/main/java/com/esan/diarioespacial/
├── data/
│   ├── api/
│   │   ├── ApodApiService.kt
│   │   └── ApodDto.kt
│   ├── local/
│   │   ├── ApodDao.kt
│   │   ├── ApodEntity.kt
│   │   └── AppDatabase.kt
│   └── repository/
│       └── ApodRepository.kt
├── ui/
│   ├── screens/
│   │   ├── ApodListScreen.kt
│   │   ├── ApodDetailScreen.kt
│   │   ├── ApodFavoritesScreen.kt
│   │   └── AsteroidsPlaceholderScreen.kt
│   ├── theme/
│   │   ├── Color.kt
│   │   └── Theme.kt
│   └── viewmodel/
│       └── ApodViewModel.kt
├── navigation/
│   └── AppNavigation.kt
└── MainActivity.kt
```

---

## 🔑 Configuración de API Key

Para ejecutar la aplicación localmente, añade tu clave en `local.properties`:

```properties
NASA_API_KEY=TU_CLAVE_NASA
```

---

## 🤝 Acuerdo de Base Común

Se definió una navegación principal compartida con `NavigationBar` (pestañas "Imágenes" y "Asteroides") y la base de datos `AppDatabase` extensible para agregar la tabla DAO del Alumno 2.
