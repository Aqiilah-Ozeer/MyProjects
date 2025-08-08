# Tic-Tac-Toe Game
#### Video Demo:  https://youtu.be/3xIocrkAMog
#### Description:

Welcome to the Tic-Tac-Toe Game project! This project is a Python implementation of the classic Tic-Tac-Toe game with varying levels of difficulty. The game allows players to choose between three levels of difficulty: easy, medium, and hard, each with different grid sizes and obstacles.

### Project Files

- **main.py**: This is the main file that runs the game. It includes the following functions:
  - `main()`: The main function that initializes the game, sets the difficulty level, and manages the game loop.
  - `difficulty(l)`: Prompts the player to choose a difficulty level and returns the corresponding grid size.
  - `initialise_grid(l)`: Initializes the game grid based on the chosen difficulty level and places obstacles.
  - `display(g)`: Displays the current state of the game grid using the `tabulate` library.
  - `player_move(m, g, p)`: Handles the player's move, ensuring the input is valid and the chosen cell is not occupied.
  - `three_in_row(r)`: Checks if there are three consecutive identical symbols in a row.
  - `gamestatus(g, l, p)`: Checks the current status of the game to determine if there is a winner or if the game is a draw.
  - `switch(p)`: Switches the current player from "X" to "O" or vice versa.

### Game Description

The game starts by welcoming the player and prompting them to choose a difficulty level: easy, medium, or hard. Each level corresponds to a different grid size:
- **Easy**: 3x3 grid with no obstacles.
- **Medium**: 4x4 grid with 2 obstacles.
- **Hard**: 5x5 grid with 3 obstacles.

Once the difficulty level is chosen, the game initializes the grid and places obstacles randomly (if applicable). The game then enters a loop where players take turns to make their moves by entering the row and column numbers. The game checks for valid moves and updates the grid accordingly.

The game checks for a win condition by looking for three consecutive identical symbols in rows, columns, or diagonals. If a player wins, the game announces the winner and ends. If the grid is full and there is no winner, the game declares a draw.

I debated a bit with the rules for medium and hard, they originally were win by having four consecutive identical symbols in rows, columns, or diagonals or by having five consecutive identical symbols in rows, columns, or diagonals with obstacles respectively. But I was not satisfied and changed them to the current ones.

### Design Choices

- **Grid Initialization**: The grid is initialized based on the chosen difficulty level, with obstacles placed randomly to increase the challenge.
- **Input Validation**: The game ensures that player inputs are valid and within the grid's range. It also checks if the chosen cell is already occupied.
- **Win Condition**: The game checks for three consecutive identical symbols in rows, columns, and diagonals to determine the winner.
- **Dynamic Grid Size**: The game supports different grid sizes, making it more versatile and challenging.

This project demonstrates the use of Python for creating a simple yet engaging game, with a focus on input validation, grid manipulation, and game logic. Enjoy playing Tic-Tac-Toe!
