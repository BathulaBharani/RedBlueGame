#RedBlueGame
Programming Language: Java 
Java Version: Java 8+ 

ABOUT THE PROGRAM?

This is a two-pile marble game where a human player competes against a computer.
Each pile (red and blue) starts with a user-defined number of marbles. Players take turns removing 1 or 2 marbles from either pile. The computer uses a MinMax algorithm with alpha-beta pruning to make decisions.

There are two gameplay versions:
Standard: The player who causes either pile to be empty wins.
Misère: The player who causes either pile to be empty loses.


HOW TO COMPILE AND RUN THE JAVA FILE?

1: Open a Terminal or Command Prompt.
2: Navigate to the folder containing the file RedBlueGame.java. (In my case cd desktop) Creates 
   a RedBlueGame.class 
3: Compile the code using: javac RedBlueGame.java
4: Run the program using: java RedBlueGame

You will be asked to enter the following inputs:
Number of red marbles
Number of blue marbles
Game version (standard or misere)
First player (human or computer)
Depth (recommended: 2-6)

CODE STRUCTURE & FUNCTION EXPLANATION?

Main Class: RedBlueGame
Functionality: Contains all logic for gameplay, AI, evaluation, and input handling.

Constants: HUMAN, COMPUTER: Identifiers for players
STANDARD, MISERE: Game versions
WIN_SCORE, LOSE_SCORE: Used for scoring AI moves

main():
Collects user input: red/blue marbles, version, first player & depth.
Calls playGame() to start the game.

playGame():
Alternates turns between human and computer.
Uses getUserMove() or getComputerMove() depending on current player.
Ends game when either pile becomes empty.
Displays winner or loser based on game version.

getUserMove():
Prompts human player for pile and number of marbles.
Validates inputs (1 or 2 marbles only).

evaluate():
Used in AI search to score a given state.
In STANDARD: If AI ends the game, it loses.
In MISERE: If AI ends the game, it wins.

runMinMax():
MinMax algorithm with Alpha-Beta pruning.
Explores possible future states up to the specified depth.
Returns best achievable score from current position.

getComputerMove():
Tries all valid moves: red/blue, 1/2 marbles.
Calls runMinMax() to evaluate outcomes.
Chooses the move with the highest score.

showStatus() & isGameOver(): Utility methods to manage state.

EXAMPLE:

Input:

Enter red marbles: 2
Enter blue marbles: 3
Choose version (standard/misere): standard
Who goes first? (human/computer): human
Enter depth (2-6 recommended): 3

Game Flow:
Enter red marbles: 2
Enter blue marbles: 3
Choose version (standard/misere): standard
Who goes first? (human/computer): human
Enter depth (2-6 recommended): 3
Red: 2 Blue: 3
Human's turn:
Choose a pile (red or blue): blue 
How many marbles to remove: 2
Red: 2 Blue: 1
Computer's turn:
Computer removes 2 red marble(s).

Red: 0 Blue: 1
Game Over! computer wins!
Final score: 3

NOTES AND ASSUMPTIONS

Game ends as soon as one pile becomes empty.
Computer always uses optimal strategy via MinMax.
Human inputs are validated to prevent errors.
No external libraries are required.
