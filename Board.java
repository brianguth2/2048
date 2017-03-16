//------------------------------------------------------------------//
// Board.java                                                       //
//                                                                  //
// Class used to represent a 2048 game board                        //
//                                                                  //
// Author:  W16-CSE8B-TA group                                      //
// Date:    1/17/16                                                 //
//------------------------------------------------------------------//

/**
 * Sample Board
 * <p/>
 * 0   1   2   3
 * 0   -   -   -   -
 * 1   -   -   -   -
 * 2   -   -   -   -
 * 3   -   -   -   -
 * <p/>
 * The sample board shows the index values for the columns and rows
 * Remember that you access a 2D array by first specifying the row
 * and then the column: grid[row][column]
 */

/* Name: Brian Guthrie
 * Login: cs8bwany
 * Date: February 4, 2016
 * File: Board.java
 * Sources of Help: Ujjwal Gulecha, piazza.com, 
 * 			docs.oracle.com, stackoverflow.com
 *
 * This class creates and establishes the rules for what Board represents.
 * It has two different constructors, one creating a fresh board, and the
 * other is constructing a game that was once played. It also contains
 * a method that will save the game, which takes the current status of the
 * board and sends it to an output file. In addition, it also utilizes the
 * ability to add a random tile if in the board. It will also have a new
 * feature that is not in the original 2048 game, which is the ability to
 * rotate the matrix.
 */
import java.util.*;
import java.io.*;

/* Name: Board.class
 * Purpose: Contains the necessary elements and arguments of the board/grid
 * 		for the game.
 * Constructors: Board(int boardSize, Random random) = creates fresh board
 * 		 Board(String inputBoard, Random random) = recalls saved board
 * Methods: saveGame(String outputBoard) = saves game into the string parameter
 * 	    addRandomTile() = adds a random tile in the board slot
 * 	    rotate(boolean rotateClockwise) = rotates the game clockwise if true
 * 	    					otherwise it is counterclockwise.
 * 	    move(Direction direction) = moves a direction using the helper of the 
 * 	    				helper methods created
 * 	    isGameOver() = checks if the tiles can't move anymore
 * 	    canMove(Direction direction) = checks if a tile can be moved in a 
 * 	    					direction using the helper methods
 */
public class Board {
    public final int NUM_START_TILES = 2;
    public final int TWO_PROBABILITY = 90;
    public final int GRID_SIZE;


    private final Random random;
    private int[][] grid;
    private int score;

    /* Name: Board (Constructor)
     * Purpose: Creates a freshboard that displays size of grid,
     * 		score, and the grid containing two start tiles
     * Parameters: int boardSize = size of the grid
     * 	       Random random = get solve the addRandomTile problem
     */
    public Board(int boardSize, Random random) {
        //Check the size of boardSize
        if(boardSize == 0)
        {
            System.out.print("Board is not created. No size");
        }
        //Initialize GRID_SIZE as the boardSize
        GRID_SIZE = boardSize;
        //Initialize the random
        this.random = random;
        //Initialize the score
        this.score = 0;
        //Create a blank grid with size GRID_SIZE
        this.grid = new int[GRID_SIZE][GRID_SIZE];
        //Put two random tiles on the board to start game
        this.addRandomTile();		
        this.addRandomTile();		


    }

    /* Name: Board (Constructor)
     * Purpose: Creates a board that was once saved, getting it's
     * 		size of grid, score, and contents of grid.
     * Parameters: String inputBoard = the saved game file
     * 	       Random random = get to solve the addRandomTile
     */
    public Board(String inputBoard, Random random) throws IOException {
        //Get the file from the input
        File file = new File(inputBoard);
        //Scan it's contents
        Scanner input = new Scanner(file);
        //Create an ArrayList to store the numbers we get from the file
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        //Loop through to get contents
        while(input.hasNextInt())
        {
            //Put the contents into the ArrayList
            numbers.add(input.nextInt());
        }
        //Set the GRID_SIZE as the first number (size of board)
        GRID_SIZE = numbers.get(0);
        //Set the score to the second number (score of saved game)
        this.score = numbers.get(1);
        //Create the grid
        this.grid = new int[GRID_SIZE][GRID_SIZE];
        //Initialize an int for the next numbers in ArrayList
        int k = 2;
        //Loop through contents in grid
        for(int i = 0; i < GRID_SIZE; i++){
            for(int j = 0; j < GRID_SIZE; j++){
                //Set the numbers in the ArrayList as the grid numbers
                this.grid[i][j] = numbers.get(k);
                k++;
            }
        }
        //Initialize Random
        this.random = random;
    }

    /* Name: saveBoard()
     * Purpose: Save the contents of the board and send it to an output
     * 		file called outputBoard.
     * Parameter: String outputBoard = the file that will contain the saved
     * 					game.
     * Return: void
     */	
    public void saveBoard(String outputBoard) throws IOException {
        //Initialize a PrintWriter to store the file
        PrintWriter saveGame = new PrintWriter(outputBoard);
        //Print the size of GRID_SIZE
        saveGame.print(GRID_SIZE);
        //Move to next line
        saveGame.println();
        //Print the score we have
        saveGame.print(this.score);
        //Print next line
        saveGame.println();
        //Loop through the grid
        for(int i = 0 ; i < this.grid.length; i++){
            for(int j = 0; j < this.grid[i].length; j++){
                //Print the first row, then after this loops finishes,
                //print to new line and run again till done.
                saveGame.print(this.grid[i][j] + " ");

            }
            saveGame.println();
        }
        //Close the PrintWriter
        saveGame.close();
    }

    /* Name: addRandomTile()
     * Purpose: Counts the number of empty slots and if it finds a spot
     * 		randomly, it will put a 2 or a 4 in that slot.
     * Parameters: none
     * Return: void
     */
    public void addRandomTile() {
        //Create a counter
        int counter = 0;
        //Loop through grid to see what spots are zero
        for(int i = 0; i < this.grid.length; i++){
            for(int j = 0; j < this.grid[i].length; j++){
                if(this.grid[i][j] == 0){
                    counter++;
                }
            }
        }
        //If there are spots with value 0, it's game over.
        if(counter == 0){
            System.out.println("GAME OVER!");
            return;
        }
        //Initialize a change tile variable
        int changeTile = 0;
        //Get location, which will select a random spot
        int location = random.nextInt(counter);
        //Get a value to try and see which number gets placed
        int value = random.nextInt(100);
        //Create a value to get the slot we want, end up being
        //same same as location so -1
        int chooseSlot = -1;
        //Now find the probability for the tile being placed
        if(value < TWO_PROBABILITY){	
            changeTile = 2;
        }
        else{
            changeTile = 4;
        }
        //Finally, Loop again to put that tile into an empty slot
        for(int i = 0; i < this.grid.length; i++){
            for(int j = 0; j < this.grid[i].length; j++){
                //Count the slots again, so we can choose to slow
                if(grid[i][j] == 0){
                    chooseSlot++;
                }
                //If the location is the same the ith slot we want, put it in
                if(chooseSlot == location){
                    grid[i][j] = changeTile;
                    return;
                }
            }
        }
    }	

    /* Name: rotate()
     * Purpose: Extra feature in 2048 game that will rotate the board
     * 		clockwise or counterclockwise.
     * Parameter: boolean rotateClockwise = if true it will rotate clockwise
     * 					if false it will rotate counterclockwise
     * Return: void
     */
    public void rotate(boolean rotateClockwise) {
        //Create a copy of the array
        int[][] rotated = new int[GRID_SIZE][GRID_SIZE];
        //If you want to turn clockwise
        if(rotateClockwise == true)
        {
            //Loop through the grid
            for(int i = 0; i < rotated.length; i++){
                for(int j = 0; j < rotated[i].length; j++){
                    //rotated rows are the columns, and the rows are
                    //the descending order of each original row
                    rotated[j][rotated.length-1-i] = this.grid[i][j];
                }
            }
            //Assign the grid to the rotated
            this.grid = rotated;	
        }
        //If you want to turn counterclockwise
        else
        {
            //Loop through the grid
            for(int i = 0; i < rotated.length; i++){
                for(int j = 0; j < rotated[i].length; j++){
                    //rotated's rows are the columns values in descending
                    //order and rotated's columns are the rows
                    rotated[rotated[i].length-1-j][i] = this.grid[i][j];
                }
            }
            //Assign this grid to the rotated one
            this.grid = rotated;
        }
    }	
    //Complete this method ONLY if you want to attempt at getting the extra credit
    //Returns true if the file to be read is in the correct format, else return
    //false
    public static boolean isInputFileCorrectFormat(String inputFile) {
        //The try and catch block are used to handle any exceptions
        //Do not worry about the details, just write all your conditions inside the
        //try block
        try {
            //write your code to check for all conditions and return true if it satisfies
            //all conditions else return false
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /////////////////////////////////////////FIX//////

    /* Name: moveRight()
     * Purpose: helper method for move() which focuses solely on
     * 		moving a tile to the right
     * Parameters: none
     * Returns: void
     */
    private void moveRight(){
        //loop through grid looking at right side
        for(int i = 0; i < this.grid.length; i++){
            for(int j = this.grid[i].length-1; j > 0; j--){
                //if the tile you have is 0 and the one on left is a number
                //switch them
                if(this.grid[i][j-1] > 0 && this.grid[i][j] == 0){
                    int temp = this.grid[i][j];
                    this.grid[i][j] = this.grid[i][j-1];
                    this.grid[i][j-1] = temp;
                }
                //if they are equal add them
                if(this.grid[i][j-1] > 0 && this.grid[i][j] == this.grid[i][j-1]){
                    //add them but make negative so it doesn't combine multiple
                    //tiles at once
                    this.grid[i][j] = -(this.grid[i][j] + this.grid[i][j-1]);
                    //set the tile you moved to 0
                    this.grid[i][j-1] = 0;
                    //update score
                    this.score += Math.abs(this.grid[i][j]);
                }
            }
        }
    }	

    /* Name: moveUp()
     * Parameters: this is a helper method for move() which focuses
     * 		solely on moving a tile up
     * Parameters: none
     * Returns: void
     */
    private void moveUp(){
        //loop through grid looking at top
        for(int i = 0; i < this.grid.length-1; i++){
            for(int j = 0; j < this.grid[i].length; j++){
                //if the tile you have is 0 and tile below is a number
                //switch them so it moves up
                if(this.grid[i+1][j] > 0 && this.grid[i][j] == 0)
                {
                    int temp = this.grid[i][j];
                    this.grid[i][j] = this.grid[i+1][j];
                    this.grid[i+1][j] = temp;
                }
                //if they are equal, add them
                if(this.grid[i+1][j] > 0 && this.grid[i+1][j] == this.grid[i][j])
                {
                    //add them but make negative so they don't combine multiple
                    //tiles at once
                    this.grid[i][j] = -(this.grid[i][j] + this.grid[i+1][j]);
                    this.grid[i+1][j] = 0;
                    //update score
                    this.score += Math.abs(this.grid[i][j]);
                }
            }
        }
    }

    /* Name: moveLeft()
     * Purpose: this is a helper method for move() which solely
     * 		focuses on moving a tile left
     * Parameters: none
     * Returns: void
     */
    private void moveLeft(){
        //Loop through grid looking at left
        for(int i = 0; i < this.grid.length; i++){
            for(int j = 0; j < this.grid[i].length-1; j++){
                //if the tile you have is 0 and the tile to the right
                //is a number, switch them
                if(this.grid[i][j+1] > 0 && this.grid[i][j] == 0){
                    int temp = this.grid[i][j];
                    this.grid[i][j] = this.grid[i][j+1];
                    this.grid[i][j+1] = temp;
                }
                //if the tiles are equal, combine them
                if(this.grid[i][j+1] > 0 && this.grid[i][j] == this.grid[i][j+1]){
                    //make number negative
                    this.grid[i][j] = -(this.grid[i][j] + this.grid[i][j+1]);
                    this.grid[i][j+1] = 0;
                    //update score
                    this.score += Math.abs(this.grid[i][j]);
                }
            }
        }
    }

    /* Name: moveDown()
     * Purpose: this is a helper method for move() which solely
     * 		focuses on moving a tile down
     * Parameters: none
     * Returns: void
     */
    private void moveDown(){
        //Loop through grid looking at bottom
        for(int i = this.grid.length-1; i > 0; i--){
            for(int j = 0; j < this.grid[i].length; j++){
                //if the tile you have is 0 and the one above is a
                //number, switch them
                if(this.grid[i-1][j] > 0 && this.grid[i][j] == 0){
                    int temp = this.grid[i][j];
                    this.grid[i][j] = this.grid[i-1][j];
                    this.grid[i-1][j] = temp;
                }
                //if they are equal, combine them
                if(this.grid[i][j] >0 && this.grid[i][j] == this.grid[i-1][j]){
                    //make number negative
                    this.grid[i][j] = -(this.grid[i][j] + this.grid[i-1][j]);
                    this.grid[i-1][j] = 0;
                    //update score
                    this.score += Math.abs(this.grid[i][j]);
                }
            }
        }
    }

    /* Name: changeSign()
     * Purpose: in each move method, I combined the numbers to be
     * 		a negative number to ensure that they do not
     * 		combine if there are multiple moves (2 2 4 8 ->
     * 		0 0 0 16). This changes that numer back to positive
     * Parameters: none
     * Returns: void
     */
    private void changeSign(){
        //Loop through grid
        for(int i = 0; i < this.grid.length; i++){
            for(int j = 0; j < this.grid[i].length; j++){
                //If any number is negative
                if(this.grid[i][j] < 0){
                    //Make positive
                    this.grid[i][j] = Math.abs(this.grid[i][j]);
                }
            }
        }
    }


    /* Name: move()
     * Purpose: this method checks that if a tile can move in a 
     * 		specified direction, then it will move, calling
     * 		the helper methods above.
     * Parameters: Direction direction = specifices a direction for tile to go
     * Returns: return true if it can move and will move
     * 		return false if it can't move
     */
    public boolean move(Direction direction) {
        //Check if the Direction is assigned to left
        if (direction.equals(Direction.LEFT)){
            //If it's left, make sure it can move left
            if(canMove(direction = Direction.LEFT))
            {
                //for any time it can move left
                while(canMove(direction)){
                    //move left
                    moveLeft();
                }
                //Since I changed the sign earlier, change it back
                changeSign();
                //return true
                return true;
            }
        }
        //Check if the Direction is assigned to up
        if (direction.equals(Direction.UP)){
            //If it's up, make sure it can move up
            if(canMove(direction = Direction.UP))
            {
                //while it can move up
                while(canMove(direction)){
                    //Move up
                    moveUp();
                }
                //Since I changed sign earlier, change it back
                changeSign();
                //return true
                return true;
            }
        }
        //Check if the Direction is assigned to down
        if (direction.equals(Direction.DOWN)){
            //If it's down, make sure it can move down
            if(canMove(direction = Direction.DOWN))
            {
                //while it can move down
                while(canMove(direction)){
                    //Move down
                    moveDown();
                }
                //change the sign as before
                changeSign();
                //return true
                return true;
            }
        }
        //Check if the Direction is assigned to right
        if (direction.equals(Direction.RIGHT)){
            //If it's right, make it can move right
            if(canMove(direction = Direction.RIGHT))
            {
                //while it can move right
                while(canMove(direction)){
                    //move right
                    moveRight();
                }
                //change the sign again
                changeSign();
                //return true
                return true;
            }
        }
        //if none work, return false
        return false;
    }

    /* Name: isGameOver()
     * Purpose: this checks to see that if the tiles cannot
     * 		move anymore, then the game is over
     * Parameters: none
     * Returns: return false if it can move
     * 		return true if it can't move
     */
    public boolean isGameOver() {
        //Initialize direction
        Direction direction;
        //If it can move in any direction, return false
        //because game isn't over
        if(canMove(direction=Direction.UP))
        {
            return false;
        }	
        if(canMove(direction=Direction.DOWN))
        {
            return false;
        }
        if(canMove(direction=Direction.LEFT))
        {
            return false;
        }
        if(canMove(direction=Direction.RIGHT))
        {
            return false;
        }
        //if it can't move at all, then game is over
        //return true
        return true;
    }

    /* Name: canMoveLeft()
     * Purpose: helper method for canMove which solely focuses
     * 		on whether a tile can move left
     * Parameter: none
     * Returns: return true if it can be moved or it can be combined
     * 		return false if it can't
     */
    private boolean canMoveLeft(){
        //Loop through the grid
        for(int i = 0;i < this.grid.length; i++){
            for(int j = this.grid[i].length-1; j > 0; j--){
                //if the tile you have is a number and the one to the
                //left is 0, return true 
                if(this.grid[i][j] > 0 && this.grid[i][j-1]==0){
                    return true;
                }
                //if they are equal, return true
                if(this.grid[i][j]>0 && this.grid[i][j] == this.grid[i][j-1]){
                    return true;
                }
            }
        }
        //return false if not
        return false;
    }

    /* Name: canMoveUP()
     * Purpose: helper method for canMove which solely focus on 
     * 		whether a tile can move up
     * Parameter: none
     * Returns: return true if it can be moved or combined
     * 		return false if it can't
     */
    private boolean canMoveUp(){
        //Loop through grid
        for(int j = 0; j < this.grid.length; j++){
            for(int i = this.grid.length-1; i>0 ; i--){
                //if the tile you have is a number and one below
                //is zero, return true
                if(grid[i][j]>0 && grid[i-1][j] == 0){
                    return true;
                }
                //if they are equal, return true
                if(this.grid[i-1][j] > 0 && this.grid[i-1][j] == this.grid[i][j]){

                    return true;
                }
            }
        }
        //if not, return false
        return false;
    }


    /* Name: canMoveDown()
     * Purpose: helper method for canMove which solely focuses 
     * 		on whether a tile can move down
     * Parameters: none
     * Returns: return true if it can be moved or combined
     * 		return false if not
     */
    private boolean canMoveDown(){
        //Loop through grid
        for(int i = 0; i < this.grid.length-1; i++){
            for(int j = 0; j < this.grid[i].length; j++){
                //if the tile you have is a number and the one above
                //is zero, return true
                if(this.grid[i][j] > 0 && this.grid[i+1][j] == 0){
                    return true;
                }
                //if they are equal, return true
                if(this.grid[i][j] > 0 && this.grid[i][j] == this.grid[i+1][j])
                {
                    return true;
                }
            }
        }
        //if not, return false
        return false;
    }

    /* Name: canMoveRight()
     * Purpose: helper method for canMove which solely focuses 
     * 		on whether or not a tile can move right
     * Parameters: none
     * Returns: return true if it can be moved or combined
     * 		return false if not
     */
    private boolean canMoveRight(){
        //Loop through grid
        for(int i = 0; i < this.grid.length; i++){
            for(int j = 0; j < this.grid[i].length-1; j++){
                //if the tile you have is a number and the one right
                //is zero, return true
                if(this.grid[i][j] > 0 && this.grid[i][j+1] == 0){
                    return true;
                }
                //if they are equal, return true
                if(this.grid[i][j]>0 && this.grid[i][j] == this.grid[i][j+1])
                {
                    return true;
                }
            }
        }
        //if not, return false
        return false;
    }

    /* Name: canMove()
     * Purpose: used to check whether or not a tile can be moved or
     * 		combined by specifying a direction and calling
     * 		the helper methods above
     * Parameters: Direction direction = used to specify the direction
     * Returns: return true if the helper method is true
     * 		return false if it can't be moved in that direction
     */
    public boolean canMove(Direction direction) {
        //if the direction assigned is the direction implicated
        //return that direction (the boolean statements above)
        if(direction.equals(Direction.UP))
        {	
            return canMoveUp();
        }
        if(direction.equals(Direction.LEFT))
        {	
            return canMoveLeft();
        }

        if(direction.equals(Direction.DOWN))
        {
            return canMoveDown();
        }
        if(direction.equals(Direction.RIGHT))
        {
            return canMoveRight();
        }
        //If it can't move, reture false
        return false;
    }

    // Return the reference to the 2048 Grid
    public int[][] getGrid() {
        return grid;
    }

    // Return the score
    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        StringBuilder outputString = new StringBuilder();
        outputString.append(String.format("Score: %d\n", score));
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int column = 0; column < GRID_SIZE; column++)
                outputString.append(grid[row][column] == 0 ? "    -" :
                        String.format("%5d", grid[row][column]));

            outputString.append("\n");
        }
        return outputString.toString();
    }

}
