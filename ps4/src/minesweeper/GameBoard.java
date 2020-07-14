/* Copyright (c) 2007-2017 MIT 6.005/6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper;
import java.util.Random;

/**
 * A mutable threadsafe Minesweeper game board.
 */
public class GameBoard {

    /*
     * Abstraction function:   
     *      AF(width, height, bombBoard, board) = a grid of width by height (width x height) squares
     *                                            representing a Minesweeper game board, in which the
     *                                            top-left corner is (0,0), and the board extends horizontally
     *                                            to the right in the X direction and vertically downwards
     *                                            in the Y direction. The current state of the 
     *                                            board is represented by board, 
     *                                            and bombBoard determines whether or not
     *                                            each square contains a bomb or not. 
     * 
     * Rep invariant:
     *      - width > 0
     *      - height > 0
     *      - width = board[0].length 
     *      - height = board.length
     *      - width = bombBoard[0].length 
     *      - height = bombBoard.length
     *      - Each square of board must either be '-' (untouched), "F" (flagged), " " (space, for dug and 0 neighbors that have a bomb),
     *        or integer COUNT in range [1-8] (for dug and COUNT neighbors that have a bomb)
     *      - Each square of bombBoard must be either '0' (does not have bomb) or '1' (has bomb).
     *      - Each square of bombBoard that is a '1' means that there is a bomb hidden in the
     *        corresponding square in board (and thus, the square in board must either be flagged or untouched).
     * 
     * Safety from rep exposure:
     *      - all fields are final
     *      - private static fields (IS_BOMB, FLAGGED, UNTOUCHED, NO_BOMBS) are immutable final primitive fields,
     *        and thus, their values cannot be reassigned or mutated (they are private constants shared only within GameBoard)
     *      - getBoardCopy() utilizes defensive copying to create a copy of board without rep exposure.
     *      - getHeight() and getWidth() return height and width, but are final primitives, and thus
     *        are safe from reassignment and mutation by the client.
     *      - the relationship between bombBoard and board is not compromised because constructors use defensive copying
     *        when using a client-given bomb board, and because bombBoard is never exposed to the client. Methods within
     *        GameBoard maintain the relationship between bombBoard and board.
     * Thread Safety: 
     *      - private static fields (IS_BOMB, FLAGGED, UNTOUCHED, NO_BOMBS) are immutable private final fields,
     *        and thus, their values cannot be reassigned or mutated. Therefore, they are threadsafe via immutability.
     *      - width and height are accessible through getWidth() and getHeight() respectively,
     *        but are private and immutable after being initialized in the constructor, and thus, cannot be changed.
     *      - All accesses to board and bombBoard happen within GameBoard methods 
     *        which are all guarded by lock, or by private helper functions that are
     *        called upon only guarded blocks of GameBoard methods. 
     *        lock is internal to GameBoard's rep, never exposed and
     *        thus, cannot be interfered with by clients.
     *      - the relationship between bombBoard and board is always preserved because constructors use defensive copying
     *        when using a client-given bomb board, and because bombBoard is never exposed to the client. All 
     *        mutations of bombBoard are done within this class, and are thus threadsafe because accesses and mutations
     *        are guarded by lock.
     */
   
    private static final char IS_BOMB = '1';
    private static final char FLAGGED = 'F';
    private static final char UNTOUCHED = '-';
    private static final char NO_BOMBS = ' ';  
    private final int width; //should equal board[0].length, is "x" axis
    private final int height; //should board.length, y, is "y" axis
    private final char[][] board;
    private final char[][] bombBoard;
    private final Object lock = new Object();

    /**
     * Creates a new width-by-height Minesweeper game board, where width and 
     * height are the supplied parameters, and the positions of the bombs
     * are determined by bombBoard.
     * @param width the width of the new Minesweeper game board, must be
     * a positive integer value and equal to bombBoard[0].length
     * @param height the height of the new Minesweeper game board, must be
     * a positive integer value and equal to bombBoard.length
     * @param bombBoard a width-by-height board of '0' and '1' only,
     * where '0' represents no bomb, and '1' represents that the square
     * contains a bomb. Is used to determine bomb locations for the created game board created.
     * Note to the reader: Yup, I know that the parameters are somewhat redundant, 
     * but decided to keep all parameters because when I create a new GameBoard 
     * from a file in GameServer, I parse the first line (X, Y) 
     * and use them as width and height. By keeping these parameters 
     * and then using them to make my bomb board, I'm checking that
     * the X and Y given are actually correct (via checkRep()) :D
     */
    public GameBoard(int width, int height, char[][] bombBoard){
        this.height = height;
        this.width = width;

        //uses defensive copying
        this.board = new char[height][width];
        this.bombBoard = new char[height][width];
        for (int i = 0; i < bombBoard.length; i++){
            for (int j = 0; j < bombBoard[0].length ; j++){
                this.board[i][j] = UNTOUCHED; //initialize as untouched
                this.bombBoard[i][j] = bombBoard[i][j];
            }
        }
        checkRep();
    }

    /**
     * Creates a new width-by-height Minesweeper game board, where width and 
     * height are the supplied parameters, and each square contains
     * a bomb with probability .25.
     * @param width the width of the new Minesweeper game board, must be a positive integer value
     * @param height the height of the new Minesweeper game board, must be a positive integer value
     */
    public GameBoard(int width, int height){
        this.height = height;
        this.width = width;
        this.board = new char[height][width];
        this.bombBoard = new char[height][width];
        Random random = new Random();
        //note: made these because checkstyle
        //doesn't like them as magic numbers, but
        //not entirely sure if this is more clear :(
        int oneHundredPercent = 100; 
        int twentyFivePercent = 25;
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width ; j++){
                this.board[i][j] = UNTOUCHED; //initialize as untouched
                if (random.nextInt(oneHundredPercent) < twentyFivePercent){
                    this.bombBoard[i][j] = '1';
                } else {
                    this.bombBoard[i][j] = '0';
                }
            }
        }
        checkRep();
    }

    /**
     * Return the width of the game board.
     * @return an integer representing the width of the game board.
     */
    int getWidth(){
       return width;
    }
    
    /**
     * Return the height of the game board.
     * @return an integer representing the height of the game board.
     */
    int getHeight(){
       return height;
    }
    
    /**
     * Check that the rep invariant holds.
     */
    private void checkRep(){
        assert (width > 0);
        assert (height > 0);
        assert (width == board[0].length);
        assert (height == board.length);
        assert (width == bombBoard[0].length);
        assert (height == bombBoard.length);

        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[0].length ; j++){
                boolean isValid = Character.toString(board[i][j]).matches("\\s|\\-|F|[1-8]");
                assert (isValid);
            }
        }    
        for (int i = 0; i < bombBoard.length; i++){
            for (int j = 0; j < bombBoard[0].length ; j++){
                boolean isValid = Character.toString(bombBoard[i][j]).matches("0|1");
                assert (isValid);
                if (bombBoard[i][j] == '1'){ //check relationship between bombBoard and board
                    assert (board[i][j] == UNTOUCHED || board[i][j] == FLAGGED);
                }
            }
        }
    }

    /**
     * Returns a copy of the current game board.
     * @return a copy (via defensive copying) 
     * of the current game board.
     */
    public char[][] getBoardCopy(){
        //Uses defensive copying.
        synchronized (lock){
            char[][] boardCopy = new char[height][width];
            for (int i = 0; i < board.length; i++){
                for (int j = 0; j < board[0].length ; j++){
                    boardCopy[i][j] = this.board[i][j];
                }
            }
            return boardCopy;
        }
    }

    /**
     * Determines whether or not the square with coordinates x,y is 
     * within the bounds of our game board.
     * @param x the x coordinate of the square
     * @param y the y coordinate of the square
     * @return true if square with coordinates x,y
     * is NOT within the bounds of our game board (and thus,
     * not valid square of the board), false otherwise.
     */
    private boolean outOfBounds(int x, int y){
        boolean xOutOfBounds = (x < 0 || x >= board[0].length);
        boolean yOutOfBounds = (y < 0 || y >= board.length);
        return (xOutOfBounds || yOutOfBounds);
    }

    /**
     * Determines whether or not the square with coordinates x,y
     * on our board contains a bomb.
     * @param x the x coordinate of the square
     * @param y the y coordinate of the square
     * @return true if square with coordinates x,y 
     * contains a bomb, false otherwise
     */
    private boolean isBomb(int x, int y){
        return (!outOfBounds(x, y) && bombBoard[y][x] == IS_BOMB);
    }

    /**
     * Determines whether or not the square with coordinates x,y
     * on our board is currently untouched ('-').
     * @param x the x coordinate of the square
     * @param y the y coordinate of the square
     * @return true if square with coordinates x,y 
     * is untouched, false otherwise
     */
    private boolean isUntouched(int x, int y){
        return (!outOfBounds(x, y) && board[y][x]== UNTOUCHED);
    }

    /**
     * Determines whether or not the square with coordinates x,y
     * on our board is a dug square with at least one neighboring bomb
     * @param x the x coordinate of the square
     * @param y the y coordinate of the square
     * @return true if square with coordinates x,y 
     * is a dug square with at least one neighboring bomb, false otherwise
     */
    private boolean isRevealedBombCount(int x, int y){
        //note: I am only matching 1-8 because this method is only used
        //when a player has dug up a bomb. Thus, revealed (dug) neighbors of the bomb square
        //should have been displaying an integer from 1-8, inclusive.
        return (!outOfBounds(x, y) && Character.toString(board[y][x]).matches("[1-8]"));
    }

    /**
     * Determines whether or not the square with coordinates x,y
     * on our board is flagged.
     * @param x the x coordinate of the square
     * @param y the y coordinate of the square
     * @return true if square with coordinate x,y 
     * is flagged, false otherwise
     */
    private boolean isFlagged(int x, int y){
        return (!outOfBounds(x, y) && board[y][x] == FLAGGED);
    }

    /**
     * Get the number of neighboring bombs surrounding the square with coordinates x,y.
     * @param x the x coordinate of the square
     * @param y the y coordinate of the square
     * @return the number of neighboring bombs surrounding the square
     * with coordinates x,y; should be a number between 0-8, inclusive.
     */
    private int getNumberOfNeighboringBombs(int x, int y){
        int total = 0;
        if (isBomb(x-1,y-1)) total += 1; //left-top
        if (isBomb(x-1,y)) total += 1;//left-center
        if (isBomb(x-1,y+1)) total += 1;//left-bottom
        if (isBomb(x,y-1)) total += 1;//center-top
        if (isBomb(x,y+1)) total += 1;//center-bottom
        if (isBomb(x+1,y-1)) total += 1;//right-top
        if (isBomb(x+1,y)) total += 1;//right-center
        if (isBomb(x+1,y+1)) total += 1;//right-bottom
        return total;
    }

    /**
     * Sets the state of the square with coordinates x,y to dug,
     * which reveals the number of bombs surrounding the square. 
     * The number of bombs is represented by a space meaning 0 bombs,
     * or an integer from 1-8, inclusive.
     * @param x the x coordinate of the square
     * @param y the x coordinate of the square
     */
    private void setDugState(int x, int y){
        if (isUntouched(x, y)){
            int numberOfNeighboringBombs = getNumberOfNeighboringBombs(x, y);
            if (numberOfNeighboringBombs == 0){
                board[y][x] = NO_BOMBS;
            } else {
                board[y][x] = Integer.toString(numberOfNeighboringBombs).charAt(0);
            }
        }
        checkRep();
    }

    /**
     * Helper function used to dig a square with coordinates x,y.
     * Handles the specified procedure where if the square x,y 
     * has no neighbor squares with bombs, 
     * then for each of x,y’s untouched neighbor squares, also
     * change said square to dug and repeat this step 
     * recursively for said neighbor square unless 
     * said neighbor square was already dug before said change.
     * Used as a helper function for dig(x,y).
     * @param x the x coordinate of the square to dig
     * @param y the y coordinate of the square to dig
     */
    private void digSquare(int x, int y){
        //square must be an untouched square, 
        //so (as per instructions) change to dug state
        if (isUntouched(x, y)){
            setDugState(x, y);
            if (getNumberOfNeighboringBombs(x, y) == 0){
                //has no neighboring bombs; for each untouched neighbor,
                //change square to DUG and recurse
                digSquare(x-1, y-1);
                digSquare(x-1, y);
                digSquare(x-1, y+1);
                digSquare(x, y-1);
                digSquare(x, y+1);
                digSquare(x+1, y-1);
                digSquare(x+1, y);
                digSquare(x+1, y+1);
            }
        }
        checkRep();
    }

    /**
     * Updates the bomb count of the square with coordinates x,y.
     * Helper function used in updateNeighbors(x,y), which is a helper function of dig(x,y), 
     * and is only called upon when player has dug up a bomb;
     * thus, is called when bomb counts must be updated to reflect the removal of the bomb the player has dug up.
     * Squares with 0 neighboring bombs are represented by a space (' '),
     * and squares with 1-8 (inclusive) neighboring bombs are represented by
     * an integer COUNT, where COUNT is the number of neighboring bombs.
     * @param x the x coordinate of the square to update bomb count for
     * @param y the y coordinate of the square to update bomb count for
     */
    private void updateBombCount(int x, int y){
        int numberOfBombs = getNumberOfNeighboringBombs(x, y);
        if (numberOfBombs == 0){ 
            //1 gets decremented to 0, which is expressed as ' '
            board[y][x] = NO_BOMBS; 
        } else {
            board[y][x] = Integer.toString(numberOfBombs).charAt(0);            
        }
        checkRep();
    }

    /**
     * Updates the bomb count of the revealed (dug) neighbors of the square with coordinates x,y.
     * Used only as a helper function in dig(x,y), and only called upon when player has dug up a bomb;
     * thus, is called when bomb counts must be updated to reflect the removal of the bomb the player has dug up.
     * @param x the x coordinate of the square to update the bomb count of the neighbors of
     * @param y the y coordinate of the square to update the bomb count of the neighbors of
     */
    private void updateNeighbors(int x, int y){
        if (!outOfBounds(x-1, y-1) && isRevealedBombCount(x-1,y-1)){ //left-top
            updateBombCount(x-1, y-1);
        }
        if (!outOfBounds(x-1, y) && isRevealedBombCount(x-1,y)){ //left-center
            updateBombCount(x-1, y);
        }
        if (!outOfBounds(x-1, y+1) && isRevealedBombCount(x-1,y+1)) { //left-bottom
            updateBombCount(x-1, y+1);
        }
        if (!outOfBounds(x, y-1) && isRevealedBombCount(x,y-1)) { //center-top
            updateBombCount(x, y-1);
        }
        if (!outOfBounds(x, y+1) && isRevealedBombCount(x,y+1)) { //center-bottom
            updateBombCount(x, y+1);
        }
        if (!outOfBounds(x+1, y-1) && isRevealedBombCount(x+1,y-1)) { //right-top
            updateBombCount(x+1, y-1);
        }
        if (!outOfBounds(x+1, y) && isRevealedBombCount(x+1,y)) { //right-center
            updateBombCount(x+1, y);
        }
        if (!outOfBounds(x+1, y+1) && isRevealedBombCount(x+1,y+1)) { //right-bottom
            updateBombCount(x+1, y+1);
        }
        checkRep();
    }

    /**
     * Returns a String representation of the current state of the game board.
     * @return a string representing the current state of the game board,
     * in which each row is separated by a valid newline character, and 
     * each square in a row is separated by a space.
     */
    String look(){
        synchronized (lock){
            String retString = "";
            for (int i = 0; i < board.length; i++){
                for (int j = 0; j < board[0].length ; j++){
                    retString += this.board[i][j];
                    if (j != board[0].length-1){
                        retString += " ";
                    }
                }
                retString += "\n";
            }
            return retString;
        }
    }

    /**
     * Digs a square at the given coordinates x,y (which represents the x-th value in line y) on our board.
     * If x,y is not a valid coordinate, or square x,y is not in the untouched state, does nothing.
     * If square contains a bomb, bomb is removed from the board, and bomb counts in 
     * adjacent squares are updated.
     * If square has no neighbors with bombs (regardless of whether or not square was a bomb), then each
     * of the square's neighbors in the untouched states are dug; this process is done recursively for 
     * neighboring squares unless they were already in the dug state.
     * @param x the x coordinate of the square to dig
     * @param y the y coordinate of the square to dig
     * @return true if square x,y contains a bomb, false otherwise
     */
    boolean dig(int x, int y){
        synchronized(lock){
            boolean blewUp = false;
            if (outOfBounds(x, y) || !isUntouched(x, y)){
                //x, y is not a valid coordinate or is not in untouched state; do nothing!
                return blewUp;
            } 
            if (isBomb(x, y)){
                bombBoard[y][x] = '0'; //change bomb board to remove bomb so we don't BOOM! again...
                updateNeighbors(x, y); 
                blewUp = true; 
            }
            digSquare(x, y);
            checkRep();
            return blewUp;
        }
    }

    /**
     * Flag the square at the given coordinates.
     * If x,y is a valid board coordinate and is currently in the untouched state, then change it to be 
     * in the flagged state. Otherwise, does nothing.
     * @param x the x coordinate of the square to flag
     * @param y the y coordinate of the square to flag
     */
    void flag(int x, int y){
        synchronized (lock){
            if (!outOfBounds(x, y) && isUntouched(x, y)){
                //x, y is a valid coordinate and square x,y is in untouched state; 
                //change it to be in flagged state!
                board[y][x] = FLAGGED;
            } 
            checkRep();
        }
    }

    /**
     * Deflag the square at the given coordinates.
     * If x,y is a valid board coordinate and is currently flagged, then change it to be 
     * in the untouched state. Otherwise, does nothing.
     * @param x the x coordinate of the square to deflag
     * @param y the y coordinate of the square to deflag
     */
    void deflag(int x, int y){
        synchronized (lock){
            if (!outOfBounds(x, y) && isFlagged(x, y)){
                //x, y is a valid coordinate and square x,y is in flagged state; 
                //change it to be in untouched state!
                board[y][x] = UNTOUCHED;
            } 
            checkRep();
        }
    }

}
