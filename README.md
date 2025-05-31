# Android Tic-Tac-Toe Game

A classic Tic-Tac-Toe game implemented as an Android application where players can compete against the CPU. The game features a clean user interface, game statistics tracking, and persistent game history using SQLite database.

## Features

- Classic Tic-Tac-Toe gameplay (3x3 grid)
- Play against CPU opponent
- Game duration tracking
- Game history logging
- Persistent storage using SQLite database
- Win/Draw/Lose status tracking
- Date and time recording for each game

## Game Rules

1. The game is played on a 3x3 grid
2. Player uses "O" (orange) and CPU uses "X" (cyan)
3. Players take turns placing their marks in empty cells
4. First player to get 3 of their marks in a row (horizontally, vertically, or diagonally) wins
5. If all cells are filled and no player has won, the game is a draw

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

## Game States

- **Active Game**: Player and CPU take turns
- **Game Over**: Displayed when there's a winner or draw
- **New Game**: Reset all positions and start fresh