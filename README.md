# Weather-History-App
Abstract of App
The Weather History App retrieves and displays maximum and minimum temperatures for a specified date, utilizing a free weather API and storing data locally for offline access.
## Breif
This Android application is designed to help users download and store weather history data from the Open-Meteo API, which provides historical weather data ranging from 1940 to 2023. It allows users to input a date and year and displays the maximum and minimum temperature on that date, along with other weather parameters available from the API. Additionally, the app provides functionality to store this information in a local database for offline access.

## Features

- Utilizes the Open-Meteo API to access historical weather data.
- Accepts user input for date and year.
- Displays the maximum and minimum temperature on the given date, along with other weather parameters available from the API.
- Stores weather data in a local ROOM database for offline access.
- Handles cases where the date is in the future by using the average of the last 10 available years' temperatures.

## Technologies Used

- Kotlin
- Jetpack Compose
- Material3 Theme
- Coroutine methods
- Thread
- UI Toolkit
- ROOM Database for local storage
- JSON file for country name to latitude and longitude mapping

## Implementation Details

### 1. Weather Data Retrieval
- Utilizes the Open-Meteo API to fetch historical weather data based on user input.
- Parses JSON files received from the API to extract relevant temperature information and other weather parameters.

### 2. User Interface
- Designed using Jetpack Compose for a modern and reactive UI.
- Handles user input validation and displays proper error messages.

### 3. Database Integration
- Implements ROOM database to create relevant schema for storing weather data.
- Inserts retrieved weather data into the database for offline access.
- Sends queries to retrieve maximum and minimum temperatures for a given date.

### 4. Network Connectivity
- Checks for network connectivity before attempting to download weather data.
- Provides appropriate error messages and fallback mechanisms for offline scenarios.

## API Link

The historical weather data is retrieved from the [Open-Meteo API](https://archive-api.open-meteo.com/v1/archive?latitude=28.7041&longitude=77.1025&start_date=2014-03-11&end_date=2014-04-11&daily=temperature_2m_max,temperature_2m_min,rain_sum,wind_speed_10m_max,wind_gusts_10m_max,weather_code,shortwave_radiation_sum,precipitation_sum,wind_direction_10m_dominant).

## Screenshots
<img src="https://github.com/Yogender21505/Weather-History-App/assets/104339650/75a0e66d-c083-4a59-afba-035f1405f2fe" alt="Screenshot 1" width="300" height="600">
<img src="https://github.com/Yogender21505/Weather-History-App/assets/104339650/06cd6a96-c9d1-42b4-95c8-7fe589071aa4" alt="Screenshot 2" width="300" height="600">
<img src="https://github.com/Yogender21505/Weather-History-App/assets/104339650/7d6712be-6e2a-4cb8-8267-454b2f0c2753" alt="Screenshot 3" width="300" height="600">
<img src="https://github.com/Yogender21505/Weather-History-App/assets/104339650/2256591c-ad98-4c74-8393-8bd85a704ae6" alt="Screenshot 4" width="300" height="600">
<img src="https://github.com/Yogender21505/Weather-History-App/assets/104339650/7802b82d-4668-492b-88a5-5ab90ee3388e" alt="Screenshot 5" width="300" height="600">
<img src="https://github.com/Yogender21505/Weather-History-App/assets/104339650/24fd9bae-1b81-49a8-9ce0-f0e4d6ccbc56" alt="Screenshot 6" width="300" height="600">





## How to Run

1. Clone this repository to your local machine.
2. Open the project in Android Studio.
3. Build and run the application on an Android device or emulator.

## Contributors

- [Yogender Kumar]

Feel free to contribute by opening issues or submitting pull requests!
