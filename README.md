# 🌡️📱 Weather & Temperature Control System

**Authors:** Joel Teodoro Gómez, Jon Jordi Salvadó Pérez  
**Date:** 17/06/2023  

---

## **Introduction** 🌟

This project combines **embedded systems** and **Android development** through three integrated exercises:  

1. **Temperature control using a Thermistor, Rotary Sensor, and RGB LCD display**  
2. **WeatherApp with Bluetooth integration for sensor data**  
3. **Android WeatherApp fetching real-time weather from OpenWeatherMap API**  

The goal is to demonstrate **modular design, sensor interaction, visual feedback with LEDs, user interfaces, and integration with external services**.

---

## **1️⃣ Temperature Control with LCD and Sensors**

**Components & Design Decisions**:

- **Thermistor Class** 🌡️  
  - Calculates temperature from analog input using the **Steinhart-Hart equation**.  
  - Encapsulation ensures **modularity and reusability**.  

- **RotarySensor Class** 🔄  
  - Reads analog input to adjust the desired temperature.  
  - Class structure provides **clean abstraction**.  

- **Grove_LCD_RGB_Backlight Class** 🖥️  
  - Controls RGB LCD via **I2C**.  
  - Functions like `displayOn`, `setRGB`, and `print` allow **easy interaction**.  

- **Main Function** 🔧  
  - Instantiates sensors and LCD.  
  - Continuously reads temperature and rotary sensor values.  
  - Adjusts LEDs and LCD colors to indicate **heating, cooling, or stable temperature**.

**Testing** ✅:

- Welcome message displays at startup.  
- Rotary sensor ranges modify the temperature:  
  - `0-0.05` → increase by 10°C  
  - `0.05-0.75` → stable  
  - `>0.75` → decrease by 10°C  
- LED colors:  
  - 🔴 Red → heating  
  - 🟢 Green → stable  
  - 🔵 Blue → cooling  

**Conclusion** 🎯:  
- Efficient and modular control of temperature and LCD.  
- Visual feedback improves **usability and user experience**.  
- Sleep delays ensure **system stability** and smooth operation.

---

## **2️⃣ WeatherApp with Bluetooth Integration** 📡

**Design Decisions**:

- Embedded system reads temperature and light sensors using **Thermistor** and **LightSensor** classes.  
- Data transmitted via **Bluetooth** using `BufferedSerial`.  
- Modular design ensures **clean code and maintainability**.  

- Android app:  
  - Retrieves weather data for a city via **OpenWeatherMap API**.  
  - MVC structure separates **UI, logic, and Bluetooth communication**.  
  - Uses **Volley** for network requests and **Handler** for thread communication.  

**Testing** ✅:

- UI tests verified correct display of temperature, sky state, and search bar functionality.  
- Edge cases handled for unusual input and extreme weather.  
- Bluetooth communication tested for stability and reliability.

**Conclusion** 🎯:  
- Embedded system and Android app work seamlessly together.  
- Modular code ensures easy updates and extension.  
- Reliable and responsive UI with real-time sensor integration.

---

## **3️⃣ Android WeatherApp (OpenWeatherMap API)** 🌤️

**Design Decisions**:

- **API**: OpenWeatherMap for comprehensive weather data.  
- **UI**: Clean and intuitive with dynamic weather icons.  
- **Error Handling**: Notifies users of empty city fields or network issues.  
- **Decimal Formatting**: Consistent temperature presentation.  
- **Security Note**: API key currently in source; best practice is **config file or environment variable**.  

**Manual** 📝:

1. Download and unzip the project.  
2. Obtain OpenWeatherMap API key.  
3. Insert API key in `MainActivity.java`.  
4. Build and run in Android Studio on emulator or device.  
5. Enter city name → click **Get Weather** → display weather info.  

**Testing** ✅:

- Ensured accurate display of temperature, sky condition, humidity, wind, and pressure.  
- UI dynamically updates with icons and colors based on weather.  
- Error messages guide users in case of invalid input or network failure.  

**Conclusion** 🎯:  

- WeatherApp provides a **robust and user-friendly experience**.  
- Modular code and MVC design ensure maintainability.  
- Integrated with external APIs and embedded sensors, demonstrating **comprehensive system design**.

---

## **About** 💻

This repository contains three integrated projects demonstrating **embedded systems, Android development, and weather applications**. It highlights:

- Sensor data acquisition and control  
- Bluetooth communication  
- Modular OOP design in C++ (Mbed) and Java  
- User-friendly Android UI with dynamic feedback  
- Integration with external APIs  

Perfect for showcasing skills in **IoT, mobile development, and system design**.

---

