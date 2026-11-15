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
* **Consumo API (Retrofit)**: Integración con el endpoint `neo/rest/v1/feed` de NeoWs (`https://api.nasa.gov/`).
* **Base de datos Local (Room)**: Entidad `AsteroidEntity`, DAO `AsteroidDao` integrados en `AppDatabase`.
* **Mapeo explícito**: Conversión de DTOs (`NeoWsFeedResponseDto`, `NeoWsAsteroidDto`) a Entidad Room (`AsteroidEntity`), preservando el estado `isFavorite`.
* **Interfaz de Usuario (Jetpack Compose)**:
  * **Pantalla de Lista Asteroides**: Muestra todos los asteroides de los últimos 7 días en una sola lista, ordenados por fecha de aproximación (más recientes primero).
  * **Formato de datos**: Nombre, fecha de aproximación, diámetro máximo estimado en metros, velocidad en km/h y distancia de paso en km, redondeados a un máximo de 2 decimales y con su unidad.
  * **Identificación de Peligro**: Los asteroides potencialmente peligrosos (`is_potentially_hazardous_asteroid == true`) se distinguen con una etiqueta destacada `"⚠️ POTENCIALMENTE PELIGROSO"`.
  * **Filtro interactivo**: Interruptor `"Solo potencialmente peligrosos"` en la parte superior para filtrar la lista dinámicamente.
  * **Pantalla de Detalle**: Nombre, ID NASA, fecha de aproximación, diámetros estimado mín y máx, velocidad, distancia y estado de peligrosidad.
  * **Pantalla "Mis Asteroides Favoritos"**: Acceso desde la parte superior de la lista para gestionar los asteroides guardados como favoritos.
  * **Gestión de Favoritos**: Botón interactivo (corazón/estrella) para marcar o desmarcar asteroides como favoritos desde la lista o la pantalla de detalle.

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
│   │   ├── ApodDto.kt
│   │   ├── NeoWsApiService.kt
│   │   └── NeoWsDto.kt
│   ├── local/
│   │   ├── ApodDao.kt
│   │   ├── ApodEntity.kt
│   │   ├── AsteroidDao.kt
│   │   ├── AsteroidEntity.kt
│   │   └── AppDatabase.kt
│   └── repository/
│       ├── ApodRepository.kt
│       └── AsteroidRepository.kt
├── ui/
│   ├── screens/
│   │   ├── ApodListScreen.kt
│   │   ├── ApodDetailScreen.kt
│   │   ├── ApodFavoritesScreen.kt
│   │   ├── AsteroidListScreen.kt
│   │   ├── AsteroidDetailScreen.kt
│   │   └── AsteroidFavoritesScreen.kt
│   ├── theme/
│   │   ├── Color.kt
│   │   └── Theme.kt
│   └── viewmodel/
│       ├── ApodViewModel.kt
│       └── AsteroidViewModel.kt
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

Se definió una navegación principal compartida con `NavigationBar` (pestañas "Imágenes" y "Asteroides") y la base de datos `AppDatabase` que integra tanto la entidad y DAO del Alumno 1 (`ApodEntity`, `ApodDao`) como las del Alumno 2 (`AsteroidEntity`, `AsteroidDao`).
