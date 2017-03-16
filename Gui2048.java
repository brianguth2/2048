/** Gui2048.java */
/** PSA8 Release */

/* Name: Brian Guthrie
 * Login: cs8bwany
 * Date: 3/3/2016
 * File: Gui2048.java
 * Sources of Help: piazza.com, Ujjwal Gulecha
 */

import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;
import java.io.*;

/* Name: Gui2048.java
 * Purpose: Create a GUI version of the game 2048 that takes in
 *          user input through keyboard rather than print to console
 */
public class Gui2048 extends Application
{
    private String outputBoard; // The filename for where to save the Board
    private Board board; // The 2048 Game Board

    private static final int TILE_WIDTH = 106;

    private static final int TEXT_SIZE_LOW = 55; // Low value tiles (2,4,8,etc)
    private static final int TEXT_SIZE_MID = 45; // Mid value tiles 
    //(128, 256, 512)
    private static final int TEXT_SIZE_HIGH = 35; // High value tiles 
    //(1024, 2048, Higher)

    // Fill colors for each of the Tile values
    private static final Color COLOR_EMPTY = Color.rgb(238, 228, 218, 0.35);
    private static final Color COLOR_2 = Color.rgb(238, 228, 218);
    private static final Color COLOR_4 = Color.rgb(237, 224, 200);
    private static final Color COLOR_8 = Color.rgb(242, 177, 121);
    private static final Color COLOR_16 = Color.rgb(245, 149, 99);
    private static final Color COLOR_32 = Color.rgb(246, 124, 95);
    private static final Color COLOR_64 = Color.rgb(246, 94, 59);
    private static final Color COLOR_128 = Color.rgb(237, 207, 114);
    private static final Color COLOR_256 = Color.rgb(237, 204, 97);
    private static final Color COLOR_512 = Color.rgb(237, 200, 80);
    private static final Color COLOR_1024 = Color.rgb(237, 197, 63);
    private static final Color COLOR_2048 = Color.rgb(237, 194, 46);
    private static final Color COLOR_OTHER = Color.BLACK;
    private static final Color COLOR_GAME_OVER = Color.rgb(238, 228, 218, 0.73);

    private static final Color COLOR_VALUE_LIGHT = Color.rgb(249, 246, 242); 
    // For tiles >= 8

    private static final Color COLOR_VALUE_DARK = Color.rgb(119, 110, 101); 
    // For tiles < 8

    private GridPane pane;

    /** Add your own Instance Variables here */
    private Text[][] text;
    private Rectangle[][] rec;
    private Text gameScore;
    private Scene scene;
    
    @Override
    public void start(Stage primaryStage)
    {
        // Process Arguments and Initialize the Game Board
        processArgs(getParameters().getRaw().toArray(new String[0]));

        // Create the pane that will hold all of the visual objects
        pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        pane.setStyle("-fx-background-color: rgb(187, 173, 160)");
        // Set the spacing between the Tiles
        pane.setHgap(15); 
        pane.setVgap(15);

        /** Add your Code for the GUI Here */

        //Create the scene
        scene = new Scene(pane, 500, 500);
        //Title the GUI
        primaryStage.setTitle("Game2048");

        //Create a Text object for the gameName
        Text gameName = new Text();
        gameName.setText("2048");
        //Set the font
        gameName.setFont(Font.font("Times New Roman", FontWeight.BOLD, 40));
        //set the color
        gameName.setFill(Color.BLACK);

        //Create a text for the score of the game
        gameScore = new Text();
        //set the score
        gameScore.setText("Score: " + board.getScore());
        //set the font
        gameScore.setFont(Font.font("Times New Roman", FontWeight.BOLD, 25));
        //set the color
        gameScore.setFill(Color.BLACK);

        //Add them to pane and make sure they are placed correctly at top
        pane.add(gameName, 0, 0);
        pane.add(gameScore, board.GRID_SIZE/2, 0);
        pane.setColumnSpan(gameName, board.GRID_SIZE/2);
        pane.setColumnSpan(gameScore, board.GRID_SIZE/2);

        //Get the board and initialize the arrays for Rectangles and Texts
        int[][] boardGrid = this.board.getGrid();   
        this.rec = new Rectangle[board.GRID_SIZE][board.GRID_SIZE];
        this.text = new Text[board.GRID_SIZE][board.GRID_SIZE];                

        //Loop through the grid
        for(int i = 0; i < boardGrid.length; i++){
            for(int j = 0; j < boardGrid[i].length; j++){
                //create a reference variable
                int theTile = boardGrid[i][j];
                //Create the rectangles with width and height
                this.rec[i][j] = new Rectangle();
                this.rec[i][j].setWidth(60);
                this.rec[i][j].setHeight(60);
                //if the number associated is 0, color Empty
                this.rec[i][j].setFill(COLOR_EMPTY);
                //if it's greater that 0
                if(theTile > 0){
                    this.rec[i][j].setFill(setTileColor(theTile));
                }

                //Create the text object
                this.text[i][j] = new Text();
                //if it's greater than 0
                if(theTile > 0){
                    //set the text as the number in tile, font, and color
                    this.text[i][j].setText(""+theTile);
                    this.text[i][j].setFont(Font.font("Times New Roman", 
                                FontWeight.BOLD, 30));
                    this.text[i][j].setFill(setTextColor(theTile));
                }

                //Make sure it's centered
                GridPane.setHalignment(this.text[i][j], HPos.CENTER);
                //add them below the score and title
                pane.add(this.rec[i][j], j, i+1);
                pane.add(this.text[i][j],j,i+1);
            }
        }
        //enable keypressing
        scene.setOnKeyPressed(new myKeyHandler());

        //set the scene and then show GUI
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    /** Add your own Instance Methods Here */
    /* Name: setTileColor()
     * Purpose: Helper method to set the tile colors for the GUI
     * Parameters: int theTile = the number on the grid
     * Returns = the instance color variable associated with the
     *              number
     */
    private Color setTileColor(int theTile){
        //if the tile is assigned to one of these numbers, set the
        //tile color to be that color
        if(theTile == 2){
            return COLOR_2;
        }
        else if(theTile == 4){
            return COLOR_4;
        }
        else if(theTile == 8){
            return COLOR_8;
        }
        else if(theTile == 16){
            return COLOR_16;
        }
        else if(theTile == 32){
            return COLOR_32;
        }
        else if(theTile == 64){
            return COLOR_64;
        }
        else if(theTile == 128){
            return COLOR_128;
        }
        else if(theTile == 256){
            return COLOR_256;
        }
        else if(theTile == 512){
            return COLOR_512;
        }
        else if(theTile == 1024){
            return COLOR_1024;
        }
        else if(theTile == 2048){
            return COLOR_2048;
        }
        else if(theTile > 2048){
            return COLOR_OTHER;
        }
        else{
            return COLOR_EMPTY;
        }
    }
    
    /* Name: setTextColor()
     * Purpose: To check if the text associated with the tile so 
     *              we can fill that text with a specific color
     * Parameters: int theText =  the tile number we assocaite out
     *                              text with
     * Returns: A dark color value if it's less than 8. Else
     *          A light color value
     */
    private Color setTextColor(int theText){
        if(theText < 8){
            return COLOR_VALUE_DARK;
        } 
        return COLOR_VALUE_LIGHT;
    }

    /* Name: updateBoard()
     * Purpose: Helper method for when a move is cast, it updates
     *          the GUI board with the new values casted by move
     * Parameters: none
     * Returns: void
     */
    private void updateBoard(){
        //Reference variable for board
        int[][] boardGrid = board.getGrid();
        //loop through board
        for(int i = 0; i < boardGrid.length; i++){
            for(int j = 0; j < boardGrid[i].length; j++){
                //if any value is 0, set the empty color and don't show text
                if(boardGrid[i][j] == 0){
                    this.rec[i][j].setFill(COLOR_EMPTY);
                    this.text[i][j].setText("");
                }
                //Else
                else if(boardGrid[i][j] > 0){
                    //Set color and text associated with number in board
                    this.rec[i][j].setFill(setTileColor(boardGrid[i][j]));                  
                    this.text[i][j].setText(""+boardGrid[i][j]);
                    this.text[i][j].setFill(setTextColor(boardGrid[i][j]));
                    //Uf the numbers are bigger, decrease the font size
                    if(boardGrid[i][j] < 128){
                        this.text[i][j].setFont(Font.font("Times New Roman", 
                                    FontWeight.BOLD, 30));
                    }
                    if(boardGrid[i][j] >= 128 && boardGrid[i][j] < 1024){
                        this.text[i][j].setFont(Font.font("Times New Roman", 
                                    FontWeight.BOLD, 20));
                    }
                    if(boardGrid[i][j] >= 1024){
                        this.text[i][j].setFont(Font.font("Times New Roman", 
                                    FontWeight.BOLD, 10));
                    }
                } 
            }
        }
    }
    /* Name: updateScore()
     * Purpose: Helper method for after the move on board, score changes
     *          so we get that value
     * Parameters: none
     * Returns: void
     */
    private void updateScore(){
        //get score and put it into GUI
        board.getScore();
        gameScore.setText("Score: "+board.getScore());
        gameScore.setFont(Font.font("Times New Roman", FontWeight.BOLD, 25));
    }

    /* Name: myKeyHandler()
     * Purpose: Inner class to recognize keyboard input and check for
     *          game updates
     */
    private class myKeyHandler implements EventHandler<KeyEvent>{
        //Create a check for isGameOVer
        boolean check = false;
        /* Name: handle()
         * Purpose: method to handle our keyboard input
         * Parameters: KeyEvent e = the input from out keyboard
         * Returns: void
         */
        @Override
        public void handle (KeyEvent e){ 
            //If check is true, return
            if(check){
                return;
            }
            //If isGameOver is not true
            if(!board.isGameOver()){ 
                //If keycode UP
                if(e.getCode()==KeyCode.UP){
                    //check if it can move up, move up, then update
                    if(board.canMove(Direction.UP)){
                        if(board.move(Direction.UP)){
                            board.addRandomTile();
                            updateBoard();
                            updateScore();
                            //print to console
                            System.out.println("Moving Up");
                        }
                    }
                }
                //If keycode LEFT
                if(e.getCode()==KeyCode.LEFT){
                    //check if it can move left, move left, then update
                    if(board.canMove(Direction.LEFT)){
                        if(board.move(Direction.LEFT)){
                            board.addRandomTile();                  
                            updateBoard();
                            updateScore();
                            //print to console
                            System.out.println("Moving Left");
                        }
                    }
                }
                //If keycode RIGHT
                if(e.getCode()==KeyCode.RIGHT){
                    //check if it can move right, move right, then update
                    if(board.canMove(Direction.RIGHT)){
                        if(board.move(Direction.RIGHT)){
                            board.addRandomTile();
                            updateBoard();
                            updateScore();                    
                            //print to console
                            System.out.println("Moving Right");
                        }
                    }
                }
                //If keyCode DOWN
                if(e.getCode()==KeyCode.DOWN){
                    //check if it can move down, move down, then update
                    if(board.canMove(Direction.DOWN)){
                        if(board.move(Direction.DOWN)){
                            board.addRandomTile();
                            updateBoard();
                            updateScore();                            
                            //print to console
                            System.out.println("Moving Down");
                        }
                    }
                }
                //If keycode R
                if(e.getCode()==KeyCode.R){
                    //Rotate and update
                    board.rotate(true);
                    updateBoard();
                    //Print to console
                    System.out.println("Rotating Clockwise");
                }
                //If keyCode S
                if(e.getCode()==KeyCode.S){
                    try{
                        //Print to outputBoard
                        board.saveBoard(outputBoard);
                        //print to console
                        System.out.println("Saving Game to: "+outputBoard);
                    }
                    catch(Exception e1){
                        System.out.println("saveBoard threw an exception");
                    }
                }
           }
        //if isGameOver is true
        if(board.isGameOver()){
            //Create the rectangle, fill it, and add it to the pane
            Rectangle gameO = new Rectangle();
            gameO.widthProperty().bind(scene.widthProperty());
            gameO.heightProperty().bind(scene.heightProperty());
            gameO.setFill(COLOR_GAME_OVER);
            pane.add(gameO,0,0,board.GRID_SIZE,board.GRID_SIZE+1);
            //set it's alignment
            GridPane.setHalignment(gameO, HPos.CENTER);
            GridPane.setValignment(gameO, VPos.CENTER);

            //Create the text for game over, set font, and fill it
            Text gameOText = new Text();
            gameOText.setText("GAME OVER!");
            gameOText.setFont(Font.font("Comic Sans", 
                        FontWeight.BOLD, 40));
            gameOText.setFill(COLOR_VALUE_DARK);
            //Set alignment
            GridPane.setHalignment(gameOText, HPos.CENTER);
            GridPane.setValignment(gameOText, VPos.CENTER);
            //Add it to pane
            pane.add(gameOText,0,0,board.GRID_SIZE,board.GRID_SIZE+1);
            //Print to console
            System.out.println("GAME OVER!");
            //change the check to true because game over
            check = true;
        }
    }
}





/** DO NOT EDIT BELOW */

// The method used to process the command line arguments
private void processArgs(String[] args)
{
    String inputBoard = null;   // The filename for where to load the Board
    int boardSize = 0;          // The Size of the Board

    // Arguments must come in pairs
    if((args.length % 2) != 0)
    {
        printUsage();
        System.exit(-1);
    }

    // Process all the arguments 
    for(int i = 0; i < args.length; i += 2)
    {
        if(args[i].equals("-i"))
        {   // We are processing the argument that specifies
            // the input file to be used to set the board
            inputBoard = args[i + 1];
        }
        else if(args[i].equals("-o"))
        {   // We are processing the argument that specifies
            // the output file to be used to save the board
            outputBoard = args[i + 1];
        }
        else if(args[i].equals("-s"))
        {   // We are processing the argument that specifies
            // the size of the Board
            boardSize = Integer.parseInt(args[i + 1]);
        }
        else
        {   // Incorrect Argument 
            printUsage();
            System.exit(-1);
        }
    }

    // Set the default output file if none specified
    if(outputBoard == null)
        outputBoard = "2048.board";
    // Set the default Board size if none specified or less than 2
    if(boardSize < 2)
        boardSize = 4;

    // Initialize the Game Board
    try{
        if(inputBoard != null)
            board = new Board(inputBoard, new Random());
        else
            board = new Board(boardSize, new Random());
    }
    catch (Exception e)
    {
        System.out.println(e.getClass().getName() + 
                " was thrown while creating a " +
                "Board from file " + inputBoard);
        System.out.println("Either your Board(String, Random) " +
                "Constructor is broken or the file isn't " +
                "formated correctly");
        System.exit(-1);
    }
}

// Print the Usage Message 
private static void printUsage()
{
    System.out.println("Gui2048");
    System.out.println("Usage:  Gui2048 [-i|o file ...]");
    System.out.println();
    System.out.println("  Command line arguments come in pairs of the "+ 
            "form: <command> <argument>");
    System.out.println();
    System.out.println("  -i [file]  -> Specifies a 2048 board that " + 
            "should be loaded");
    System.out.println();
    System.out.println("  -o [file]  -> Specifies a file that should be " + 
            "used to save the 2048 board");
    System.out.println("                If none specified then the " + 
            "default \"2048.board\" file will be used");  
    System.out.println("  -s [size]  -> Specifies the size of the 2048" + 
            "board if an input file hasn't been"); 
    System.out.println("                specified.  If both -s and -i" + 
            "are used, then the size of the board"); 
    System.out.println("                will be determined by the input" +
            " file. The default size is 4.");
}
}
