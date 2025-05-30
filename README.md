# Android Tic-Tac-Toe Game

A classic Tic-Tac-Toe game implemented as an Android application where players can compete against the CPU. The game features a clean user interface, game statistics tracking, and persistent game history using SQLite database.

## Features

- 🎮 Classic Tic-Tac-Toe gameplay (3x3 grid)
- 🤖 Play against CPU opponent
- ⏱️ Game duration tracking
- 📊 Game history logging
- 💾 Persistent storage using SQLite database
- 🎯 Win/Draw/Lose status tracking
- 📅 Date and time recording for each game
- 🔄 Play again functionality

## Game Rules

1. The game is played on a 3x3 grid
2. Player uses "O" (orange) and CPU uses "X" (cyan)
3. Players take turns placing their marks in empty cells
4. First player to get 3 of their marks in a row (horizontally, vertically, or diagonally) wins
5. If all cells are filled and no player has won, the game is a draw

## Technical Details

### Database Structure
The game uses SQLite database to store game history with the following table structure:

```sql
CREATE TABLE GamesLog (
    gameID INTEGER PRIMARY KEY AUTOINCREMENT,
    PlayDate varchar(50),
    PlayTime varchar(50),
    duration int,
    winningStatus text
)
```

### Game Statistics Tracked
- Game date and time
- Game duration (in seconds)
- Game outcome (Win/Lose/Draw)

## Implementation Details

- Built using Java for Android
- Uses Android SQLite for data persistence
- Implements View.OnClickListener for button interactions
- Features responsive UI with dynamic button states
- Includes game state management and win condition checking

## Requirements

- Android Studio
- Minimum Android SDK version: API level that supports SQLite
- Target Android SDK version: Latest stable version

## Installation

1. Clone the repository
2. Open the project in Android Studio
3. Build and run the application on an Android device or emulator

## Usage

1. Launch the application
2. The game starts automatically with the player's turn
3. Tap on any empty cell to place your "O"
4. The CPU will automatically place its "X"
5. Continue playing until someone wins or the game ends in a draw
6. View the game result and duration
7. Click "Continue" to start a new game

## Game States

- **Active Game**: Player and CPU take turns
- **Game Over**: Displayed when there's a winner or draw
- **New Game**: Reset all positions and start fresh

## Contributing

Feel free to submit issues and enhancement requests!

## License

This project is open source and available under the MIT License. 