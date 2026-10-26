# Smart Pantry Manager

## Description
An Android application written in Java that helps users reduce food waste by tracking pantry ingredients and suggesting recipes that can be made using ONLY the ingredients the user already has.

## Database Choice: SQLite
I chose SQLite because:
- It is built directly into Android (no internet needed)
- Data persists locally on the device between sessions
- No external server or account setup required
- Perfect for a single-user, offline-first application

## Setup Instructions
1. Clone this repository
2. Open the project in Android Studio
3. Wait for Gradle sync to complete
4. Click Run 'app' to launch on emulator or physical device

## Features
- Add, edit, and delete pantry items (full CRUD)
- 18 pre-loaded recipes in the database
- Strict matching algorithm (only shows recipes where EVERY ingredient is present)
- Plural/singular ingredient name handling (e.g., "tomato" matches "tomatoes")
- Bottom navigation between Pantry, Recipes, and Settings screens
- Input validation on all forms
- Settings saved via SharedPreferences

## Screens
1. Pantry List (RecyclerView)
2. Add/Edit Ingredient (form with validation)
3. Suggested Recipes (strict matching)
4. Recipe Detail (full recipe view)
5. Settings (toggles and preferences)

## Technologies
- Java
- Android Studio
- SQLite (SQLiteOpenHelper)
- RecyclerView with custom Adapters
- Fragments and BottomNavigationView
- Material Design components