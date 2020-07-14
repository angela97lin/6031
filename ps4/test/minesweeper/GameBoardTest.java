/* Copyright (c) 2007-2017 MIT 6.005/6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for the GameBoard abstract data type.
 */
public class GameBoardTest {
    
    /*
     * Testing strategy for GameBoard:
     * 
     * look():
     *      - width, height:
     *          - width = 1, >1
     *          - height = 1, >1
     *          - width == height, width != height
     *      - board squares:
     *          - has bombs: 0, 1, >1 
     *          - has flags ("F"): 0, 1, >1
     *          - has spaces (for dug and 0 neighbors that have a bomb)
     *          - has integer COUNT in range [1-8] (for dug and COUNT neighbors that have a bomb)
     *          
     * dig(int x, int y):
     *      - x , y:
     *          - x = 0, 1, 1 < x < board[0].length, x > board[0].length
     *          - y = 0, 1, 1 < y < board.length, y > board.length
     *          - x == y, x != y
     *      - square (x,y):
     *          - is an untouched square 
     *              - contains a bomb
     *              - does not contain a bomb
     *                  - has 0 untouched neighbor squares with bombs
     *                  - has 1 untouched neighbor squares with bombs
     *                  - has > 1 untouched neighbor squares with bombs
     *          - is NOT an untouched square
     *              - is flagged
     *  
     * flag(int x, int y):
     *      - x , y:
     *          - x = 0, 1, 1 < x < board[0].length, x > board[0].length
     *          - y = 0, 1, 1 < y < board.length, y > board.length
     *          - x == y, x != y
     *      - square (x,y):
     *          - is an untouched square 
     *          - is NOT an untouched square (flagged, revealed integer, space)
     *          
     * deflag(int x, int y):
     *      - x , y:
     *          - x = 0, 1, 1 < x < board[0].length, x > board[0].length
     *          - y = 0, 1, 1 < y < board.length, y > board.length
     *          - x == y, x != y
     *      - square (x,y):
     *          - is a flagged square 
     *          - is NOT a flagged square (untouched, revealed integer, space)
     */
   
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    //=============================================TESTING FOR constructor BEGINS=================================================
    
    //covers constructor with parameters int width, int height, char[][] bombBoard
    @Test
    public void testConstructorWithBoard(){
        final char[][] test = { { '1', '1', '0', '0' },
                                { '0', '0', '0', '0' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '1', '0' }
                              };
        GameBoard testBoard = new GameBoard(4, 4, test);   
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("board was not as expected at "+i, expected[i], actual[i]);
        }
    }
    
    //covers constructor with parameters int width, int height
    @Test
    public void testConstructorWithoutBoard(){
        GameBoard testBoard = new GameBoard(12, 12);   
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
                                    { '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
                                    { '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
                                    { '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
                                    { '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
                                    { '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
                                    { '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
                                    { '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
                                    { '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
                                    { '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
                                    { '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
                                    { '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("board was not as expected at "+i, expected[i], actual[i]);
        }
        
    }
    //=============================================TESTING FOR look BEGINS=================================================

    //covers case where width = 1, height = 1
    //                  has 1 bomb
    @Test
    public void testLookWidthOneHeightOne(){
        final char[][] test = { { '1' } };
        GameBoard testBoard = new GameBoard(1, 1, test);   
        String expected = "-\n";
        String actual = testBoard.look();
        assertEquals("look was not as expected", expected, actual);
    }
    
    //covers case where width > 1, height = 1
    //                  has 1 bomb
    @Test
    public void testLookWidthGreaterThanOneHeightOne(){
        final char[][] test = { { '1', '0' } };
        GameBoard testBoard = new GameBoard(2, 1, test);   
        String expected = "- -\n";
        String actual = testBoard.look();
        assertEquals("look was not as expected", expected, actual);
    }
    
    //covers case where width > 1, height = 1
    //                  has > 1 bomb
    @Test
    public void testLookWidthOneHeightGreaterThanOne(){
        final char[][] test = { { '1' },
                                { '1' } };
        GameBoard testBoard = new GameBoard(1, 2, test);   
        String expected = "-\n"
                        + "-\n";
        String actual = testBoard.look();
        assertEquals("look was not as expected", expected, actual);
    }
    
    //covers case where width > 1, height > 1
    //                  has 0 bombs
    @Test
    public void testLookWidthAndHeightGreaterThanOne(){
        final char[][] test = { { '0', '0' },
                                { '0', '0' } };
        GameBoard testBoard = new GameBoard(2, 2, test);   
        String expected = "- -\n"
                        + "- -\n";
        String actual = testBoard.look();
        assertEquals("look was not as expected", expected, actual);
    }
    
    //covers case where width > 1, height > 1
    //                  has > 1 bomb
    //                  width == height
    //                  has 0 flags
    @Test
    public void testLookWidthAndHeightEqual(){
        final char[][] test = { { '1', '0', '0' },
                                { '1', '0', '1' },
                                { '1', '0', '1' }
                                  };
        GameBoard testBoard = new GameBoard(3, 3, test);   
        String expected = "- - -\n"
                        + "- - -\n"
                        + "- - -\n";
        String actual = testBoard.look();
        assertEquals("look was not as expected", expected, actual);
    }
    
    //covers case where width > 1, height > 1
    //                  width != height
    @Test
    public void testLookWidthAndHeightNotEqual(){
        final char[][] test = { { '1', '0', '0' },
                                { '1', '0', '1' }
                                  };
        GameBoard testBoard = new GameBoard(3, 2, test);   
        String expected = "- - -\n"
                        + "- - -\n";
        String actual = testBoard.look();
        assertEquals("look was not as expected", expected, actual);
    }
    
    //covers case where width > 1, height > 1
    //                  width != height
    //                  has 1 flag
    @Test
    public void testLookHasOneFlag(){
        final char[][] test = { { '1', '0', '0' },
                                { '1', '0', '1' }
                                  };
        GameBoard testBoard = new GameBoard(3, 2, test);   
        testBoard.flag(2, 1);
        String expected = "- - -\n"
                        + "- - F\n";
        String actual = testBoard.look();
        assertEquals("look was not as expected", expected, actual);
    }
    
    //covers case where width > 1, height > 1
    //                  width != height
    //                  has > 1 flag
    @Test
    public void testLookHasMoreThanOneFlag(){
        final char[][] test = { { '1', '0', '0' },
                                { '1', '0', '1' }
                                  };
        GameBoard testBoard = new GameBoard(3, 2, test);   
        testBoard.flag(1, 0);
        testBoard.flag(2, 1);
        String expected = "- F -\n"
                        + "- - F\n";
        String actual = testBoard.look();
        assertEquals("look was not as expected", expected, actual);
    }
    
    //covers case where width > 1, height > 1
    //                  width != height
    //                  has integer (neighbor w/ bomb)
    @Test
    public void testLookHasInteger(){
        final char[][] test = { { '1', '0', '0' },
                                { '1', '0', '1' }
                                  };
        GameBoard testBoard = new GameBoard(3, 2, test);   
        testBoard.dig(1, 0);
        String expected = "- 3 -\n"
                        + "- - -\n";
        String actual = testBoard.look();
        assertEquals("look was not as expected\n"+actual, expected, actual);
    }
    
    //covers case where width > 1, height > 1
    //                  width != height
    //                  has spaces (neighbor w/o bomb)
    @Test
    public void testLookHasSpaces(){
        final char[][] test = { { '1', '0', '0' },
                                { '0', '0', '0' }
                                  };
        GameBoard testBoard = new GameBoard(3, 2, test);   
        testBoard.dig(2, 1);
        String expected = "- 1  \n"
                        + "- 1  \n";
        String actual = testBoard.look();
        assertEquals("look was not as expected\n"+actual, expected, actual);
    }
    
    //=============================================TESTING FOR look ENDS=================================================

    //=============================================TESTING FOR dig BEGINS=================================================
    
    //covers case where x = 0, y = 0
    //                  x == y
    //                  square is an untouched square with a bomb
    @Test
    public void testDigUntouchedBomb(){
        final char[][] test = { { '1', '0', '0' },
                                { '0', '0', '0' },
                                { '0', '0', '1' }
                                  };
        GameBoard testBoard = new GameBoard(3, 3, test);   
        testBoard.dig(0, 0);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { ' ', ' ', ' ' },
                                    { ' ', '1', '1' },
                                    { ' ', '1', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("dig was not as expected at "+i, expected[i], actual[i]);
        }
    }
    
    //covers case where x = x < board[0].length, 1 < y < board.length
    //                  x != y
    //                  square is an untouched square with a bomb
    @Test
    public void testDigUntouchedBomb2(){
        final char[][] test = { { '1', '0', '0', '0' },
                                { '0', '0', '0', '1' },
                                { '0', '0', '0', '1' }
                              };
        GameBoard testBoard = new GameBoard(4, 3, test);   
        boolean bombed = testBoard.dig(3, 1);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', '-' },
                                    { '-', '-', '-', '1' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("dig was not as expected at "+i, expected[i], actual[i]);
        }
        assertTrue("bombed should be true", bombed);
    }
    
    
    //covers case where x = x < board[0].length, 1 < y < board.length
    //                  x != y
    //                  square is an untouched square with a bomb
    @Test
    public void testDigUntouchedBomb3(){
        final char[][] test = { { '1', '1', '0', '0' },
                                { '0', '0', '0', '0' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '1', '0' }
                              };
        GameBoard testBoard = new GameBoard(4, 4, test);   
        testBoard.dig(1, 0);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '1', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("flag was not as expected at "+i, expected[i], actual[i]);
        }
    }
    
    //covers case where x = x < board[0].length, 1 < y < board.length
    //                  x != y
    //                  square is an untouched square with a bomb
    @Test
    public void testDigUntouchedBomb4(){
        final char[][] test = { { '1', '0', '0', '0' },
                                { '0', '0', '0', '1' },
                                { '0', '0', '0', '0' }
                              };
        GameBoard testBoard = new GameBoard(4, 3, test);   
        boolean bombed = testBoard.dig(3, 1);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '1', ' ', ' ' },
                                    { '1', '1', ' ', ' ' },
                                    { ' ', ' ', ' ', ' ' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("dig was not as expected at "+i, expected[i], actual[i]);
        }
        assertTrue("bombed should be true", bombed);
    }
    
    //covers case where x = x < board[0].length, 1 < y < board.length
    //                  x != y
    //                  square is an untouched square with a bomb
    @Test
    public void testDigUntouchedBomb5(){
        final char[][] test = { { '1', '1', '0', '0' },
                                { '0', '1', '0', '1' },
                                { '0', '1', '0', '0' }
                              };
        GameBoard testBoard = new GameBoard(4, 3, test);   
        boolean bombed = testBoard.dig(3, 1);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '2', ' ' },
                                    { '-', '-', '3', ' ' },
                                    { '-', '-', '2', ' ' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("dig was not as expected at "+i, expected[i], actual[i]);
        }
        assertTrue("bombed should be true", bombed);
    }
    
    //covers case where x = 1, y = 1
    //                  x == y
    //                  square is an untouched square with no bomb
    //                  has 0 untouched neighbor squares with bombs
    @Test
    public void testDigUntouchedEmptyNoNeighoringBombs(){
        final char[][] test = { { '0', '0', '0', '0', '1' },
                                { '0', '0', '0', '0', '0' },
                                { '0', '0', '0', '0', '0' },
                                { '0', '1', '0', '0', '0' },
                                { '0', '1', '0', '0', '0' }
                              };
        GameBoard testBoard = new GameBoard(5, 5, test);   
        boolean bombed = testBoard.dig(1, 1);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { ' ', ' ', ' ', '1', '-' },
                                    { ' ', ' ', ' ', '1', '1' },
                                    { '1', '1', '1', ' ', ' ' },
                                    { '-', '-', '2', ' ', ' ' },
                                    { '-', '-', '2', ' ', ' ' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("dig was not as expected at "+i, expected[i], actual[i]);
        }
        assertFalse("bombed should be false", bombed);
    }
    
    //covers case where x = 1, 1 < y < board.length
    //                  x != y
    //                  has 1 untouched neighbor squares with bombs
    @Test
    public void testDigUntouchedEmptyOneNeighoringBomb(){
        final char[][] test = { { '1', '1', '0', '0' },
                                { '0', '0', '0', '1' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '0', '0' },
                                { '0', '0', '0', '0' }
                              };
        GameBoard testBoard = new GameBoard(4, 5, test);   
        boolean bombed = testBoard.dig(1, 3);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '1', '-', '-' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("dig was not as expected at "+i, expected[i], actual[i]);
        }
        assertFalse("bombed should be false", bombed);
    }
    
    //covers case where x = x < board[0].length, 1 < y < board.length
    //                  x == y
    //                  has 0 untouched neighbor squares with bombs
    @Test
    public void testDigXEqualsY(){
        final char[][] test = { { '1', '1', '0', '0' },
                                { '0', '0', '0', '1' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '0', '0' },
                                { '0', '0', '0', '0' }
                              };
        GameBoard testBoard = new GameBoard(4, 5, test);   
        boolean bombed = testBoard.dig(3, 3);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '2', '1' },
                                    { '1', '1', '1', ' ' },
                                    { ' ', ' ', ' ', ' ' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("dig was not as expected at "+i, expected[i], actual[i]);
        }
        assertFalse("bombed should be false", bombed);
    }
    
    //covers case where x = 1 < x < board[0].length, 1 < y < board.length
    //                  x != y
    //                  has 0 untouched neighbor squares with bombs
    @Test
    public void testDigXNotEqualsYContainsNoNeighboringBomb(){
        final char[][] test = { { '1', '1', '0', '0' },
                                { '0', '0', '0', '0' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '1', '0' },
                                { '0', '0', '0', '1' }
                              };
            GameBoard testBoard = new GameBoard(4, 5, test);   
            boolean bombed = testBoard.dig(3, 0);
            final char[][] actual = testBoard.getBoardCopy();
            final char[][] expected = { { '-', '-', '1', ' ' },
                                        { '-', '-', '2', ' ' },
                                        { '-', '-', '2', '1' },
                                        { '-', '-', '-', '-' },
                                        { '-', '-', '-', '-' }
                                      };
            for (int i = 0; i < expected.length ; i++){
                assertArrayEquals("dig was not as expected at "+i, expected[i], actual[i]);
            }
            assertFalse("bombed should be false", bombed);
        }
    
    
    //covers case where x = 1 < x < board[0].length, 1 < y < board.length
    //                  x != y
    //                  has  1 untouched neighbor square with bomb
    @Test
    public void testDigXNotEqualsYOneNeighbor(){
        final char[][] test = { { '1', '1', '0', '0' },
                                { '0', '0', '0', '0' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '1', '0' },
                                { '0', '0', '0', '1' }
                              };
        GameBoard testBoard = new GameBoard(4, 5, test);   
        boolean bombed = testBoard.dig(2, 0);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '1', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("dig was not as expected at "+i, expected[i], actual[i]);
        }
        assertFalse("bombed should be false", bombed);
    }
    
    //covers case where x = 1 < x < board[0].length, 1 < y < board.length
    //                  x != y
    //                  has > 1 untouched neighbor squares with bombs
    @Test
    public void testDigXNotEqualsYMoreThanOneNeighbor(){
        final char[][] test = { { '1', '1', '0', '0' },
                                { '0', '0', '0', '0' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '1', '0' },
                                { '0', '0', '0', '1' }
                              };
        GameBoard testBoard = new GameBoard(4, 5, test);   
        boolean bombed = testBoard.dig(0, 1);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', '-' },
                                    { '3', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("dig was not as expected at "+i, expected[i], actual[i]);
        }
        assertFalse("bombed should be false", bombed);
    }
    
    //covers case where x = 1 < x < board[0].length, 1 < y < board.length
    //                  x != y
    //                  is flagged
    @Test
    public void testDigFlagged(){
        final char[][] test = { { '1', '1', '0', '0' },
                                { '0', '0', '0', '0' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '1', '0' },
                                { '0', '0', '0', '1' }
                              };
        GameBoard testBoard = new GameBoard(4, 5, test);   
        testBoard.flag(3, 0);
        boolean bombed = testBoard.dig(3, 0);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', 'F' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("flag/dig was not as expected at "+i, expected[i], actual[i]);
        }
        assertFalse("bombed should be false", bombed);
    }

    //covers case where x = 1 < x < board[0].length, 1 < y < board.length
    //                  x != y
    //                  is flagged, has bomb under
    @Test
    public void testDigFlaggedBomb(){
        final char[][] test = { { '1', '1', '0', '1' },
                                { '0', '0', '0', '0' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '1', '0' },
                                { '0', '0', '0', '1' }
                              };
        GameBoard testBoard = new GameBoard(4, 5, test);   
        testBoard.flag(3, 0);
        boolean bombed = testBoard.dig(3, 0);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', 'F' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("flag/dig was not as expected at "+i, expected[i], actual[i]);
        }
        assertFalse("bombed should be false", bombed);
    }
    
    //covers case where x = 1 < x < board[0].length, 1 < y < board.length
    //                  x != y
    //                  is flagged, then deflagged
    @Test
    public void testDigFlaggedAndDeflagged(){
        final char[][] test = { { '1', '1', '0', '0' },
                                { '0', '0', '0', '0' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '1', '0' },
                                { '0', '0', '0', '1' }
                              };
        GameBoard testBoard = new GameBoard(4, 5, test);   
        testBoard.flag(0, 1);
        testBoard.deflag(0, 1);
        boolean bombed = testBoard.dig(0, 1);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', '-' },
                                    { '3', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("dig was not as expected at "+i, expected[i], actual[i]);
        }
        assertFalse("bombed should be false", bombed);
    }
    
    //covers case where x = 1 < x < board[0].length, 1 < y < board.length
    //                  x != y
    //                  is flagged, then dug
    @Test
    public void testFlaggedDig(){
        final char[][] test = { { '0', '0', '0', '0' },
                                { '0', '0', '0', '0' },
                                { '1', '1', '0', '0' },
                                { '0', '0', '1', '0' },
                                { '0', '0', '0', '1' }
                              };
        GameBoard testBoard = new GameBoard(4, 5, test);   
        testBoard.flag(0, 1);
        boolean bombed = testBoard.dig(1, 0);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { ' ', ' ', ' ', ' ' },
                                    { 'F', '2', '1', ' ' },
                                    { '-', '-', '2', '1' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("dig was not as expected at "+i, expected[i], actual[i]);
        }
        assertFalse("bombed should be false", bombed);
    }
    
    //covers case where x = 1 < x < board[0].length, 1 < y < board.length
    //                  x != y
    //                  is flagged, then dug with no neighboring bombs
    @Test
    public void testFlaggedDigNoNeighboringBombs(){
        final char[][] test = { { '1', '1', '0', '0' },
                                { '0', '0', '0', '0' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '0', '0' },
                                { '1', '0', '0', '0' }
                              };
        GameBoard testBoard = new GameBoard(4, 5, test);   
        testBoard.flag(2, 3);
        boolean bombed = testBoard.dig(3, 0);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '1', ' ' },
                                    { '-', '-', '2', ' ' },
                                    { '-', '-', '1', ' ' },
                                    { '-', '2', 'F', ' ' },
                                    { '-', '1', ' ', ' ' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("dig was not as expected at "+i, expected[i], actual[i]);
        }
        assertFalse("bombed should be false", bombed);
    }
    
    //covers case where x > board[0].length, 1 < y < board.length
    //                  x != y
    @Test
    public void testDigXOutOfBounds(){
        final char[][] test = { { '1', '1', '0', '1' },
                                { '0', '0', '0', '0' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '1', '0' },
                                { '0', '0', '0', '1' }
                              };
        GameBoard testBoard = new GameBoard(4, 5, test);   
        boolean bombed = testBoard.dig(6, 0);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("dig was not as expected at "+i, expected[i], actual[i]);
        }
        assertFalse("bombed should be false", bombed);
    }
    
    //covers case where 1 < x < board[0].length, y > board.length
    //                  x != y
    @Test
    public void testDigYOutOfBounds(){
        final char[][] test = { { '1', '1', '0', '1' },
                                { '0', '0', '0', '0' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '1', '0' },
                                { '0', '0', '0', '1' }
                              };
        GameBoard testBoard = new GameBoard(4, 5, test);   
        boolean bombed = testBoard.dig(0, 6);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("dig was not as expected at "+i, expected[i], actual[i]);
        }
        assertFalse("bombed should be false", bombed);
    }
    
    //covers case where x > board[0].length, y > board.length
    //                  x != y
    @Test
    public void testDigXAndYOutOfBounds(){
        final char[][] test = { { '1', '1', '0', '1' },
                                { '0', '0', '0', '0' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '1', '0' },
                                { '0', '0', '0', '1' }
                              };
        GameBoard testBoard = new GameBoard(4, 5, test);   
        boolean bombed = testBoard.dig(6, 6);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("dig was not as expected at "+i, expected[i], actual[i]);
        }
        assertFalse("bombed should be false", bombed);
    }
    
    //covers case where x = x < board[0].length, 1 < y < board.length
    //                  x != y
    @Test
    public void testDigGG(){
        final char[][] test = { { '1', '1', '1', '1' },
                                { '1', '1', '0', '1' },
                                { '1', '1', '1', '1' }
                              };
        GameBoard testBoard = new GameBoard(4, 3, test);   
        boolean bombed = testBoard.dig(2, 1);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', '-' },
                                    { '-', '-', '8', '-' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("dig was not as expected at "+i, expected[i], actual[i]);
        }
        assertFalse("bombed should be false", bombed);
    }
    
    //covers case where x = x < board[0].length, 1 < y < board.length
    //                  x != y
    @Test
    public void testDigMiscFlagAndDig(){
        final char[][] test = { { '1', '1', '1', '1' },
                                { '0', '0', '0', '1' },
                                { '0', '0', '0', '1' },
                                { '0', '0', '0', '1' },
                                { '1', '1', '1', '1' },
                                { '1', '1', '1', '1' },
                                { '1', '1', '1', '1' }
                              };
        GameBoard testBoard = new GameBoard(4, 7, test);   
        testBoard.flag(1, 1);
        boolean bombed = testBoard.dig(1, 2);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', '-' },
                                    { '2', 'F', '5', '-' },
                                    { ' ', ' ', '3', '-' },
                                    { '2', '3', '5', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("dig was not as expected at "+i, expected[i], actual[i]);
        }
        assertFalse("bombed should be false", bombed);
    }
    
    //covers case where x = x < board[0].length, 1 < y < board.length
    //                  square is an untouched square with a bomb
    @Test
    public void testDigUntouchedDecrementBombCount(){
        final char[][] test = { { '1', '1', '1', '1' },
                                { '1', '1', '1', '1' },
                                { '1', '1', '1', '1' }
                              };
        GameBoard testBoard = new GameBoard(4, 3, test);   
        boolean bombed = testBoard.dig(3, 1);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', '-' },
                                    { '-', '-', '-', '5' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("dig was not as expected at "+i, expected[i], actual[i]);
        }
        assertTrue("bombed should be true", bombed);
        
        boolean bombedAgain = testBoard.dig(2, 1);
        final char[][] actualAgain = testBoard.getBoardCopy();
        final char[][] expectedAgain = { { '-', '-', '-', '-' },
                                         { '-', '-', '7', '4' },
                                         { '-', '-', '-', '-' }
                                       };
        for (int i = 0; i < expectedAgain.length ; i++){
            assertArrayEquals("dig was not as expected at "+i, expectedAgain[i], actualAgain[i]);
            
        }
        assertTrue("bombedAgain should be true", bombedAgain);
    }
    
    //covers case where x = x < board[0].length, 1 < y < board.length
    //                  square is an untouched square with a bomb
    @Test
    public void testDigUntouchedTripleBomb(){
        final char[][] test = { { '1', '0', '0', '0' },
                                { '0', '0', '1', '1' },
                                { '0', '0', '0', '1' }
                              };
        GameBoard testBoard = new GameBoard(4, 3, test);   
        boolean bombed = testBoard.dig(3, 1);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', '-' },
                                    { '-', '-', '-', '2' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("dig was not as expected at "+i, expected[i], actual[i]);
        }
        assertTrue("bombed should be true", bombed);
        
        boolean bombedAgain = testBoard.dig(2, 1);
        final char[][] actualAgain = testBoard.getBoardCopy();
        final char[][] expectedAgain = { { '-', '-', '-', '-' },
                                         { '-', '-', '1', '1' },
                                         { '-', '-', '-', '-' }
                                       };
        for (int i = 0; i < expectedAgain.length ; i++){
            assertArrayEquals("dig again was not as expected at "+i, expectedAgain[i], actualAgain[i]);
            
        }
        assertTrue("bombedAgain should be true", bombedAgain);
        
        boolean bombedYetAgain = testBoard.dig(3, 2);
        final char[][] actualYetAgain = testBoard.getBoardCopy();
        final char[][] expectedYetAgain = { { '-', '-', '-', '-' },
                                            { '1', '1', ' ', ' ' },
                                            { ' ', ' ', ' ', ' ' }
                                          };
        for (int i = 0; i < expectedYetAgain.length ; i++){
            assertArrayEquals("dig yet again was not as expected at "+i, expectedYetAgain[i], actualYetAgain[i]);
            
        }
        assertTrue("bombedYetAgain should be true", bombedYetAgain);
    }
    
    //=============================================TESTING FOR dig ENDS=================================================
    
    //=============================================TESTING FOR flag BEGINS=================================================
    
    //covers case where x = 0, y = 0
    //                  x == y
    //                  square is an untouched square
    @Test
    public void testFlagUntouchedXZeroYZero(){
        final char[][] test = { { '1', '1', '0', '0' },
                                { '0', '0', '0', '0' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '1', '0' }
                              };
        GameBoard testBoard = new GameBoard(4, 4, test);   
        testBoard.flag(0, 0);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { 'F', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("flag was not as expected at "+i, expected[i], actual[i]);
        }
    }
    
    //covers case where x = 1, y = 1
    //                  x == y
    //                  square is an untouched square
    @Test
    public void testFlagUntouchedXOneYOne(){
        final char[][] test = { { '1', '1', '0', '0' },
                                { '0', '0', '0', '0' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '1', '0' }
                              };
        GameBoard testBoard = new GameBoard(4, 4, test);   
        testBoard.flag(1, 1);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', '-' },
                                    { '-', 'F', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("flag was not as expected at "+i, expected[i], actual[i]);
        }
    }
    
    //covers case where 1 < x < board[0].length , 1 < y < board.length
    //                  x == y
    //                  square is an untouched square
    @Test
    public void testFlagUntouchedXEqualsY(){
        final char[][] test = { { '1', '1', '0', '0' },
                                { '0', '0', '0', '0' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '1', '0' }
                              };
        GameBoard testBoard = new GameBoard(4, 4, test);   
        testBoard.flag(2, 2);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', 'F', '-' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("flag was not as expected at "+i, expected[i], actual[i]);
        }
    }
    
    //covers case where 1 < x < board[0].length , 1 < y < board.length
    //                  x != y
    //                  square is an untouched square
    @Test
    public void testFlagUntouchedXNotEqualsY(){
        final char[][] test = { { '1', '1', '0', '0' },
                                { '0', '0', '0', '0' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '1', '0' }
                              };
        GameBoard testBoard = new GameBoard(4, 4, test);   
        testBoard.flag(3, 2);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', 'F' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("flag was not as expected at "+i, expected[i], actual[i]);
        }
    }
    
    //covers case where x = 0, y = 0
    //                  x == y
    //                  square is NOT an untouched square (flagged)
    @Test
    public void testFlagNotUntouchedXZeroYZero(){
        final char[][] test = { { '1', '1', '0', '0' },
                                { '0', '0', '0', '0' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '1', '0' }
                              };
        GameBoard testBoard = new GameBoard(4, 4, test);   
        testBoard.flag(2, 2);
        testBoard.flag(2, 2);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', 'F', '-' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("flag was not as expected at "+i, expected[i], actual[i]);
        }
    }
    
    //covers case where x = 1, y = 1
    //                  x != y
    //                  square is NOT an untouched square (space)
    @Test
    public void testFlagNotUntouchedXOneYOne(){
        final char[][] test = { { '1', '1', '0', '0' },
                                { '0', '0', '0', '0' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '1', '0' }
                              };
        GameBoard testBoard = new GameBoard(4, 4, test);   
        testBoard.dig(3, 0);
        testBoard.flag(3, 0);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '1', ' ' },
                                    { '-', '-', '2', ' ' },
                                    { '-', '-', '2', '1' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("flag was not as expected at "+i, expected[i], actual[i]);
        }
     }
    
    //covers case where 1 < x < board[0].length , 1 < y < board.length
    //                  square is not an untouched square (integer)
    @Test
    public void testFlagNotUntouchedXEqualsY(){
        final char[][] test = { { '1', '1', '1', '0' },
                                { '0', '0', '1', '1' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '1', '0' }
                              };
        GameBoard testBoard = new GameBoard(4, 4, test);   
        testBoard.dig(3, 0);
        testBoard.flag(3, 0);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', '3' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("flag was not as expected at "+i, expected[i], actual[i]);
        }
    }
    
    //covers case where x > board[0].length , 1 < y < board.length
    //                  x != y
    @Test
    public void testFlagXOutOfBounds(){
        final char[][] test = { { '1', '1', '0', '0' },
                                { '0', '0', '0', '0' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '1', '0' }
                              };
        GameBoard testBoard = new GameBoard(4, 4, test);   
        testBoard.flag(6, 0);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("flag was not as expected at "+i, expected[i], actual[i]);
        }
    }
    
    //covers case where 1 < x < board[0].length , y > board.length
    //                  x != y
    //                  square is an untouched square
    @Test
    public void testFlagYOutOfBounds(){
        final char[][] test = { { '1', '1', '0', '0' },
                                { '0', '0', '0', '0' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '1', '0' }
                              };
        GameBoard testBoard = new GameBoard(4, 4, test);   
        testBoard.flag(0, 12);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("flag was not as expected at "+i, expected[i], actual[i]);
        }
    }
    
    //covers case where x > board[0].length , y > board.length
    //                  x != y
    @Test
    public void testFlagXAndYOutOfBounds(){
        final char[][] test = { { '1', '1', '0', '0' },
                                { '0', '0', '0', '0' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '1', '0' }
                              };
        GameBoard testBoard = new GameBoard(4, 4, test);   
        testBoard.flag(6, 12);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("flag was not as expected at "+i, expected[i], actual[i]);
        }
    }

    
    //=============================================TESTING FOR flag ENDS=================================================
    
    //=============================================TESTING FOR unflag BEGINS=================================================
    
    //covers case where x = 0, y = 0
    //                  x == y
    //                  square is a flagged square
    @Test
    public void testDeflagFlaggedXZeroYZero(){
        final char[][] test = { { '1', '1', '0', '0' },
                                { '0', '0', '0', '0' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '1', '0' }
                              };
        GameBoard testBoard = new GameBoard(4, 4, test);   
        testBoard.flag(0, 0);
        testBoard.deflag(0, 0);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("flag was not as expected at "+i, expected[i], actual[i]);
        }
    }
    
    //covers case where x = 1, y = 1
    //                  x == y
    //                  square is a flagged square
    @Test
    public void testDeflagFlaggedXOneYOne(){
        final char[][] test = { { '1', '1', '0', '0' },
                                { '0', '0', '0', '0' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '1', '0' }
                              };
        GameBoard testBoard = new GameBoard(4, 4, test);   
        testBoard.flag(1, 1);
        testBoard.deflag(1, 1);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("flag was not as expected at "+i, expected[i], actual[i]);
        }
    }
    
    //covers case where 1 < x < board[0].length , 1 < y < board.length
    //                  x == y
    //                  square is a flagged square
    @Test
    public void testDeflagFlaggedXEqualsY(){
        final char[][] test = { { '1', '1', '0', '0' },
                                { '0', '0', '0', '0' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '1', '0' }
                              };
        GameBoard testBoard = new GameBoard(4, 4, test);   
        testBoard.flag(4, 4);
        testBoard.deflag(4, 4);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("flag was not as expected at "+i, expected[i], actual[i]);
        }
    }
    
    //covers case where 1 < x < board[0].length , 1 < y < board.length
    //                  x != y
    //                  square is a flagged square
    @Test
    public void testDeflagFlaggedXNotEqualsY(){
        final char[][] test = { { '1', '1', '0', '0' },
                                { '0', '0', '0', '0' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '1', '0' }
                              };
        GameBoard testBoard = new GameBoard(4, 4, test);   
        testBoard.flag(2, 0);
        testBoard.deflag(2, 0);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("flag was not as expected at "+i, expected[i], actual[i]);
        }
    }
    
    //covers case where x = 0, y = 0
    //                  x == y
    //                  square is NOT a flagged square
    @Test
    public void testDeflagNotFlaggedXZeroYZero(){
        final char[][] test = { { '1', '1', '0', '0' },
                                { '0', '0', '0', '0' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '1', '0' }
                              };
        GameBoard testBoard = new GameBoard(4, 4, test);   
        testBoard.deflag(0, 0);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("flag was not as expected at "+i, expected[i], actual[i]);
        }
    }
    
    //covers case where x = 1, y = 1
    //                  x != y
    //                  square is NOT a flagged square (space)
    @Test
    public void testDeflagNotFlaggedXOneYOne(){
        final char[][] test = { { '1', '1', '0', '0' },
                                { '0', '0', '0', '0' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '1', '0' }
                              };
        GameBoard testBoard = new GameBoard(4, 4, test);   
        testBoard.dig(3, 0);
        testBoard.deflag(3, 0);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '1', ' ' },
                                    { '-', '-', '2', ' ' },
                                    { '-', '-', '2', '1' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("flag was not as expected at "+i, expected[i], actual[i]);
        }
     }
    
    //covers case where 1 < x < board[0].length , 1 < y < board.length
    //                  x != y
    //                  square is not a flagged square (integer)
    @Test
    public void testDeflagNotFlaggedXEqualsY(){
        final char[][] test = { { '1', '1', '1', '0' },
                                { '0', '0', '1', '1' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '1', '0' }
                              };
        GameBoard testBoard = new GameBoard(4, 4, test);   
        testBoard.dig(3, 0);
        testBoard.deflag(3, 0);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', '3' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("flag was not as expected at "+i, expected[i], actual[i]);
        }
    }
    
    //covers case where 1 < x < board[0].length , 1 < y < board.length
    //                  x != y
    //                  square is not a flagged square (untouched)
    @Test
    public void testDeflagNotFlaggedXNotEqualsY(){
        final char[][] test = { { '1', '1', '0', '0' },
                                { '0', '0', '0', '0' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '1', '0' }
                              };
        GameBoard testBoard = new GameBoard(4, 4, test);   
        testBoard.deflag(2, 1);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("flag was not as expected at "+i, expected[i], actual[i]);
        }
    }
    //covers case where x > board[0].length , 1 < y < board.length
    //                  x != y
    @Test
    public void testDeflagXOutOfBounds(){
        final char[][] test = { { '1', '1', '0', '0' },
                                { '0', '0', '0', '0' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '1', '0' }
                              };
        GameBoard testBoard = new GameBoard(4, 4, test);   
        testBoard.deflag(10, 0);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("flag was not as expected at "+i, expected[i], actual[i]);
        }
    }
    
    //covers case where 1 < x < board[0].length , y > board.length
    //                  x != y
    //                  square is an untouched square
    @Test
    public void testDeflagYOutOfBounds(){
        final char[][] test = { { '1', '1', '0', '0' },
                                { '0', '0', '0', '0' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '1', '0' }
                              };
        GameBoard testBoard = new GameBoard(4, 4, test);   
        testBoard.deflag(0, 10);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("flag was not as expected at "+i, expected[i], actual[i]);
        }
    }
    
    //covers case where x > board[0].length , y > board.length
    //                  x != y
    @Test
    public void testDeflagXAndYOutOfBounds(){
        final char[][] test = { { '1', '1', '0', '0' },
                                { '0', '0', '0', '0' },
                                { '0', '1', '0', '0' },
                                { '0', '0', '1', '0' }
                              };
        GameBoard testBoard = new GameBoard(4, 4, test);   
        testBoard.deflag(10, 10);
        final char[][] actual = testBoard.getBoardCopy();
        final char[][] expected = { { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' },
                                    { '-', '-', '-', '-' }
                                  };
        for (int i = 0; i < expected.length ; i++){
            assertArrayEquals("flag was not as expected at "+i, expected[i], actual[i]);
        }
    }
}
