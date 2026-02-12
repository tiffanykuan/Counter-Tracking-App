# 📱 Counter Tracker App  
### COEN 390 – Programming Assignment 1  
**Tiffany Kuan**  
Concordia University  

---

## 📌 Overview

Counter Tracker is an Android application developed for COEN 390.  
The app allows users to track three customizable events, monitor totals, and store data persistently across sessions.

This project demonstrates structured Android development, persistent storage, and clean separation between UI and data logic.

---

## 🚀 Features

- 🔢 Track 3 independent event counters  
- 📊 Automatic total counter calculation  
- ✏️ Rename events in the Settings page  
- 📝 Chronological event history tracking  
- 💾 Persistent storage using SharedPreferences  
- 🔄 Reset counters with confirmation dialog  

---

## 🛠 Tech Stack

- **Java**
- **Android Studio**
- **XML Layouts**
- **SharedPreferences**
- MVC-style architecture

---

## 📂 Project Structure
app/
├── java/com/example/...
│ ├── MainActivity.java
│ ├── SettingsActivity.java
│ ├── CounterPreferences.java
│
├── res/
│ ├── layout/
│ ├── values/
│ ├── drawable/

---


---

## 💾 Data Persistence

The application stores the following using SharedPreferences:

- Event names  
- Individual counter values  
- Total count  
- Counter limit  
- Event press history (stored as JSON)

All data persists even after the application is closed.

---

## ▶️ How to Run

1. Clone the repository:

git clone https://github.com/yourusername/your-repo-name.git


2. Open the project in Android Studio  
3. Sync Gradle  
4. Run on an emulator or physical Android device  

---

## 🎯 Concepts Demonstrated

- Android Activity lifecycle management  
- UI design using XML  
- Persistent data storage  
- Object-oriented programming  
- Separation of concerns  
- Basic MVC structure  

---

## 👩🏻‍💻 Author

**Tiffany Kuan**  
Computer Engineering Student  
Concordia University



