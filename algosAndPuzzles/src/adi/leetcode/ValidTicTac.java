package adi.leetcode;

import java.util.Arrays;

public class ValidTicTac {
    int L = 3;
    int[][] gameBoard = new int[L][L];
    /* L rows, L columns, diagonal1 and diagnoal 2 sums */
    int[] sums = new int[2*L + 2];
    int boardSum = 0;
    int wonScore = -1;
    public static void main(String[] args) {
        
        String[][] inputs = {{ "XOX","OXO","XOX"}
                             ,{"XXX","OOX","OOX"}
                             ,{"XOX","OXO","XOX"}
                             };
        boolean[] expected = {false, true, true};
        
        
        int i = 0;
        for(String[] input : inputs) {
            ValidTicTac obj = new ValidTicTac();
            boolean valid = obj.populateBoard(input);
        System.out.println("input" + Arrays.toString(input) + " valid:" + valid);
        System.out.println("Expected: valid:" + expected[i++]);
        }
    }
    
    public boolean populateBoard(String[] board) {
        System.out.println("called for board:" + Arrays.toString(board));
        if(board.length != L) {
            return false;
        }
        int r = 0;
        int c = 0;
        for(r = 0; r < L; r++) {
            String row = board[r];
            if (row.length() != L) {
                return false;
            }
            char[] rowOps = row.toCharArray();
            for(c=0; c<L; c++) {
                Character m = rowOps[c];
                if (m != ' ') {
                    if (gameBoard[r][c] == 1 || gameBoard[r][c] == -1) {
                        return false;
                    }

                    int add = (m == 'X') ? 1 : -1;
                    gameBoard[r][c] = add;
                    boardSum += add;
                    sums[r] += add;
                    sums[L + c] += add;
                    if (r == c) {
                        sums[2*L] += add;
                    } 
                    if (r + c == L - 1) {
                        sums[2*L + 1] += add;
                    }
                }
            }
        }
        
        int s = 0;
        int wonIdx = -1;
        for (int i=0; i< sums.length; i++ ) {
            s = sums[i];
            if( Math.abs(s) == L ) {
                if (wonScore == -1 ) {
                    wonScore = s;
                    wonIdx = i;
                    continue;
                }
                if(s != wonScore) {
                    return false;
                }
                if(wonIdx < L && i < L) {
                    return false;
                }
                
                if(wonIdx >= L && i >= L && wonIdx < 2*L && i < 2*L) {
                    return false;
                }
                
               /* if(wonIdx == 2*L) {
                    return false;
                } */
            }
         }
        
        if (!(boardSum == 0 || boardSum == 1)) {
            return false;
        }
        
        if (wonScore == L && boardSum == 0) {
            return false;
        }
        
        if (wonScore == -L && boardSum == 1) {
            return false;
        }
        
        return true;
    }

}
