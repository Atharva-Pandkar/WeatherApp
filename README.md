


# WeatherApp

A native Android weather application that shows current conditions, daily and weekly forecasts, and location photos. Search by city or address, save favorite places, and view weather in a tabbed interface.

## Features

- **Location weather** — Uses your device’s IP to show weather for your approximate location, or search any city/address.
- **Search with autocomplete** — Type in the search bar to get location suggestions, then open full forecast and details.
- **Favorites** — Save places to a list; they appear as tabs on the home screen for quick access.
- **Today** — Current conditions, summary, and details (humidity, wind, visibility, UV index, etc.) with condition icons.
- **Weekly forecast** — Daily outlook with temperature trends and charts (Highcharts and MPAndroidChart).
- **Photos** — Location-related images for the selected place in a scrollable gallery.
- **Share** — Share the current location’s weather via Twitter from the details screen.

## Requirements

- **Android Studio** (or compatible IDE)
- **Android SDK**: compile/target SDK 31, min SDK 25
- **Google Maps API key** with Geocoding API enabled (for address search)

## Setup

### 1. Clone and open the project

```bash
git clone https://github.com/Atharva-Pandkar/WeatherApp.git
cd WeatherApp
```

Open the project in Android Studio.

### 2. Google Maps API key (required for search)

Geocoding is used to turn search queries (e.g. city names or addresses) into coordinates. The app reads the API key from `local.properties` so it is not committed.

1. In [Google Cloud Console](https://console.cloud.google.com/), create or select a project and enable the **Geocoding API** (or Maps SDK as needed).
2. Create an API key under **APIs & Services → Credentials**.
3. In the project root, copy `local.properties.example` to `local.properties` (or create `local.properties` if it doesn’t exist).
4. Add your key:

   ```properties
   GOOGLE_MAPS_API_KEY=your_api_key_here
   ```

`local.properties` is gitignored. Do not commit API keys. If a key was ever committed, rotate it in Google Cloud Console and use the new key only in `local.properties`.

### 3. Build and run

- **Build:** `./gradlew assembleDebug` (or use Android Studio’s Build menu).
- **Run:** Select a device or emulator and run the app (e.g. Run → Run 'app').

The app will use your IP-based location by default; you can also use the search bar to open weather for any location.

## Project structure

| Path | Description |
|------|-------------|
| `app/src/main/java/com/example/weatherapp/` | Application code |
| `MainActivity.java` | Home screen with tabs (current location + favorites), search, weather loading |
| `SearchableActivity.java` | Search result screen for a single location; add/remove favorite |
| `DetailsActivity.java` | Detailed view: Today, Weekly, Photos tabs; share option |
| `TodayFragment.java` | Today’s conditions and details |
| `WeeklyFragment.java` | Weekly forecast and charts |
| `PhotoFragment.java` | Location photos gallery |
| `SplashActivity.java` | Launch screen, then starts `MainActivity` |

Weather data and autocomplete are provided by the backend at `weathersearch-331907.wl.r.appspot.com`. Location from IP uses `ip-api.com`. Geocoding uses the Google Maps Geocoding API with the key from `local.properties`.

## Tech stack

- **Language:** Java 8  
- **UI:** Android Support Library (AppCompat, Design, RecyclerView, CardView, ConstraintLayout), ViewPager, TabLayout  
- **Networking:** Volley  
- **JSON:** Gson  
- **Images:** Picasso  
- **Charts:** Highcharts Android, MPAndroidChart  
- **Other:** EasyPermissions, ViewPagerIndicator, SlidingDotSplash  

## License

See the repository for license information.
