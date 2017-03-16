//------------------------------------------------------------------//
// GameManager.java                                                 //
//                                                                  //
// Game Manager for 2048                                            //
//                                                                  //
// Author:  W16-CSE8B-TA group                                      //
// Date:    1/17/16                                                 //
//------------------------------------------------------------------//
/* Name: Brian Guthrie
 * Login: cs8bwany
 * Date: February 4, 2016
 * File: GameManager.java
 * Sources of Help: Ujjwal Gulecha, piazza.com, 
 * 			docs.oracle.com, stackoverflow.com
 *
 * This program is used to establish how to play the Game as well as
 * create the environment by initializing a Game constructor containing
 * both the Board class(used to create the board for the 2048 game). By
 * In addition, it also has a constructor which will recall a saved game
 * board that we initially saved in the Board class. Lastly, we call play
 * which is the main purpose of this Class which prints the controls and
 * allows user input to direct where he wants the numbers to move using the
 * Direction class which relates to his input (h, j, k, l).
 *
 */
import java.util.*;
import java.io.*;


/* Name: GameManager.class
 * Purpose: Creates the environment to play the 2048 game.
 * Constructors: GameManager(with int boardSize)
 * 		 GameManager(with String inputFile)
 * Methods: play() = main function to play, calls all the methods
 * 			in Board.java that will make 2048 play
 */
public class GameManager {
	// Instance variables
	private Board board; // The actual 2048 board
	private String outputFileName; // File to save the board to when exiting

	/* Name: GameManager (constructor)
	 * Purpose: Creates a fresh game by initializing instance variables passed
	 * Parameters: int boardSize = size of the board for the board variable
	 * 		String outputBoard = file the game saves too
	 * 		Random random = a random to help generate the numbers in Board
	 * Return: this.(instance variable)
	 */
	public GameManager(int boardSize, String outputBoard, Random random) {
		//Initialize the board in Game manager, with boardSize
		//and random
		this.board = new Board(boardSize, random);
		//Initialize the outputFileName by sending it to the
		//file to store it
		this.outputFileName = outputBoard;	

	}

	/* Name: GameManager (constructor)
	 * Purpose: Initializes a game that was once played and calls it so we can
	 * 		finish it.
	 * Parameters: String inputBoard = The file that we saved the game too
	 * 		String outputBoard = file the game saves too
	 * 		Random random = random to help generate the numbers in Board
	 * Return: this.(instance variable)
	 */
	public GameManager(String inputBoard, String outputBoard, Random random) throws IOException {

		this.board = new Board(inputBoard, random);
		//Initialize the outputFile
		this.outputFileName = outputBoard;
	}

	// TODO PSA3
	// Main play loop
	// Takes in input from the user to specify moves to execute
	// valid moves are:
	//      k - Move up
	//      j - Move Down
	//      h - Move Left
	//      l - Move Right
	//      q - Quit and Save Board
	//
	//  If an invalid command is received then print the controls
	//  to remind the user of the valid moves.
	//
	//  Once the player decides to quit or the game is over,
	//  save the game board to a file based on the outputFileName
	//  string that was set in the constructor and then return
	//
	//  If the game is over print "Game Over!" to the terminal

	/* Name: play()
	 * Purpose: Create the environment for players to play the game
	 * Parameters: None
	 * Returns: None
	 */
	public void play() throws IOException {
		//Prints the controls of the game
		printControls();
		//Print the board
		System.out.print(this.board.toString());

		//Scan in user input
		Scanner input = new Scanner(System.in);
		//Initialize the Direction
		Direction dir;
		//Loop for player choices
		String moveDirection = ""; 
		while(input.hasNext()){
			//Ask for user input
			System.out.println("User Enter Move:");
			//Get the input they asked
			moveDirection = input.next();

			//If the direction they selected is up
			if(moveDirection.equals("k")){
				//Direction is up
				dir = Direction.UP;
				//If the piece can move, move it and add new tile
				if(this.board.canMove(dir)){
					this.board.move(dir);
					this.board.addRandomTile();
					System.out.print(this.board.toString());
				}
				//If not, print statement, contols and ask for another input
				else {
					System.out.println("Can't perform action. Try again.");
					printControls();
				}
			}
			//If the direction they selected is down
			else if(moveDirection.equals("j")){    
				//Direction is down
				dir = Direction.DOWN;
				if(this.board.canMove(dir)){
					this.board.move(dir);
					this.board.addRandomTile();
					System.out.print(this.board.toString());
				}
				else{
					System.out.println("Can't perform action. Try again.");
					printControls();
				}
			}
			//If the direction they selected is left
			else if(moveDirection.equals("h")){
				//Direction is left
				dir = Direction.LEFT;
				if(this.board.canMove(dir)){
					this.board.move(dir);
					this.board.addRandomTile();
					System.out.print(this.board.toString());
				}
				else{
					System.out.println("Can't perform action. Try again.");
					printControls();
				}
			}
			//If the direction they selected is right
			else if(moveDirection.equals("l")){
				//Direction is right
				dir = Direction.RIGHT;
				if(this.board.canMove(dir)){
					this.board.move(dir);
					this.board.addRandomTile();
					System.out.print(this.board.toString());
				}
				else{
					System.out.println("Can't perform action. Try again.");
					printControls();
				}
			}
			//If user selects q, it will save the board and exit
			else if(moveDirection.equals("q")){
				//Save the board
				this.board.saveBoard(outputFileName);
				//return or exit
				return;
			}
			else{ 
				System.out.println("move invalid, please try again");
			}

			//If the Game is over
			if(this.board.isGameOver())
			{
				System.out.println("GAME OVER!");
				return;
			}
		}

		input.close();



	}	

	// Print the Controls for the Game
	private void printControls() {
		System.out.println("  Controls:");
		System.out.println("    k - Move Up");
		System.out.println("    j - Move Down");
		System.out.println("    h - Move Left");
		System.out.println("    l - Move Right");
		System.out.println("    q - Quit and Save Board");
		System.out.println();
	}
}
