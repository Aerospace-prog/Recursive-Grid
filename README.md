# Recursive Grid

An Android application built with Kotlin and Jetpack Compose that implements an interactive 3x3 number grid with ripple and lock mechanics.

## How It Works

- A 3x3 grid is displayed on screen. Every cell starts at 0.
- Tapping a cell increments its value by 1.
- **Rule A**: When a cell's value becomes divisible by 3 (3, 6, 9, ...), the cell to its right is decremented by 1. Cells in the last column have no right neighbor, so nothing happens.
- **Rule B**: When a cell's value becomes divisible by 5 (5, 10, 15, ...), the cell below it is incremented by 2. Cells in the bottom row have no neighbor below, so nothing happens.
- **Locked state**: Any cell that reaches 15 or higher turns red and becomes locked. A locked cell cannot be tapped, and ripple effects from neighbors cannot change its value.

## Visual Design

| State | Background | Text Color |
|-------|-----------|------------|
| Even number | Light Gray (#E0E0E0) | Black |
| Odd number | Navy Blue (#1A237E) | White |
| Locked (>= 15) | Red (#FF0000) | White |

All cells have 4dp rounded corners and a 2dp offset black shadow.

## Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose (no XML layouts)
- **Architecture**: ViewModel with StateFlow
- **Min SDK**: 24
- **Target SDK**: 35

## Project Structure

```
app/src/main/java/com/example/recursion_grid/
    MainActivity.kt              -- Entry point
    GridViewModel.kt             -- Game state and rules
    ui/
        GridCell.kt              -- Single cell composable
        RecursionGridScreen.kt   -- 3x3 grid layout
        theme/
            Color.kt             -- Color definitions
            Theme.kt             -- Material3 theme
            Type.kt              -- Typography
app/src/test/java/com/example/recursion_grid/
    GridViewModelTest.kt         -- Unit tests for all game rules
```

## Build and Run

```bash
# Run unit tests
./gradlew testDebugUnitTest

# Build debug APK
./gradlew assembleDebug
```

The debug APK is generated at `app/build/outputs/apk/debug/app-debug.apk`.
