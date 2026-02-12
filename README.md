Counter Tracking Android Application

📌 Overview

This project is an Android application developed for COEN 390 – Programming Assignment 1.

The application allows users to:

Track counts for 3 customizable events

View total count

Store data persistently using SharedPreferences

View event history

Reset counters (with confirmation)

The app follows an MVC-style structure and emphasizes clean separation between UI logic and data persistence.
--------------------------------------------------------------------------------------------------------------
🚀 Features
🔢 Counter Management

Three customizable event buttons

Individual count tracking per event

Automatic total counter update

Persistent storage (data remains after app closes)

📝 Event History

Chronological tracking of button presses

Option to display:

Event names

Event button numbers (1–3)

⚙️ Settings Page

Rename events

Configure counter limits

Reset counters with confirmation dialog

🛠️ Technologies Used

Java

Android Studio

XML (UI Layouts)

SharedPreferences

MVC Design Pattern

📂 Project Structure
app/
 ├── java/com/example/...
 │     ├── MainActivity.java
 │     ├── SettingsActivity.java
 │     ├── CounterPreferences.java
 │
 ├── res/
 │     ├── layout/
 │     ├── values/
 │     ├── drawable/

🔹 MainActivity

Handles:

Counter button clicks

UI updates

Navigation to Settings

🔹 CounterPreferences

Handles:

Persistent storage

Storing counts

Storing event names

Managing total count

Saving history log

💾 Data Storage

The application uses SharedPreferences to store:

Event names

Individual counts

Total count

Counter limit

Event history (stored as JSON array)

This ensures all data persists between app sessions.

▶️ How to Run

Clone the repository:

git clone https://github.com/yourusername/your-repo-name.git


Open in Android Studio

Sync Gradle

Run on:

Android Emulator
OR

Physical Android device

📸 Screenshots

(You can add screenshots here later)

![Main Screen](screenshots/main.png)
![Settings Screen](screenshots/settings.png)

📖 Learning Objectives

This assignment demonstrates:

Android activity lifecycle management

UI design using XML

Persistent data storage

MVC-style architecture

User input validation

Clean Java object design

🧠 Future Improvements

Dark mode support

Material Design enhancements

SQLite database integration

Export history feature

👩🏻‍💻 Author

Tiffany Kuan
Computer Engineering
Concordia University
