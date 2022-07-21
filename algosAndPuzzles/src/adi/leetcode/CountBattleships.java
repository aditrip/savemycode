package adi.leetcode;

import java.util.ArrayList;
import java.util.List;

public class CountBattleships {

    public static void main( String[] args) {
        CountBattleships obj = new CountBattleships();
        List<char[][]> boards = new ArrayList<char[][]>();
        boards.add(new char[][] {{'X','.','.','X'},{'.','.','.','X'},{'.','.','.','X'}});
        boards.add(new char[][] {{'.'}});
        boards.add(new char[][] {{'X','.','X','.','X'},
                                 {'.','.','.','.','X'},
                                 {'.','X','X','.','.'},
                                 {'X','.','.','.','X'},
                                 {'.','.','.','.','.'},
                                 {'X','X','X','.','X'}});
        boards.add(new char[][] {
         {'.','X','.','.','X'} 
        ,{'.','X','.','.','X'}                                                          
        ,{'.','.','.','.','X'}                                                          
        ,{'X','.','X','X','.'}                                                          
        ,{'X','.','.','.','X'}
        });
        
        int[] expected = {2, 0, 8, 5};
        int i = 0;
        for (char[][] board: boards) {
        System.out.println("Expecting:" + expected[i++]);
        System.out.println("Actual:" + obj.countBattleships(board));
        
        }
    }
    
    public int countBattleships(char[][] board) {
        int count = 0;
        int R = board.length;
        int C = board[0].length;
        boolean[] countOff = new boolean[C];
        for(int r=0; r<R; r++) {
            for (int c=0; c<C; c++) {
                if(board[r][c] == 'X' && !countOff[c]) {
                    count++;
                    
                    /* For single 'X' case only */
                    if(c == C - 1 ||  board[r][c+1] != 'X') {
                        if (c == 0 || board[r][c-1] != 'X') {
                            countOff[c] = true;
                        }
                    }
                    /* Board is assumed to be valid.
                     * No adjacent battleships are placed.
                     * Other wise countOff[c] should be set to
                     * true for all X. And validation needs to be done.
                     */
                    if(!countOff[c]) {
                        while (c < C && board[r][c] == 'X') {
                            c++;
                        }
                        if (c < C && board[r][c] != 'X') {
                            c--;
                        }
                    }
                } else if (board[r][c] == '.' && countOff[c]) {
                        countOff[c] = false;
                }
            }
        }
        
        return count;
        
    }
    

}
