package problems;

import java.util.ArrayList;

public class WordGrid {

    public static void main(String[] args) {

    }

    public boolean exist(char[][] board, String word) {

        if (board == null || word == null || word.length() == 0 ||
                board.length == 0 || board[0].length == 0) {
            return false;
        }

        int C = board.length;
        int R = board[0].length;

        if (R == 1) {
            return word.equals(new String(board[0]));
        }

        ArrayList<Integer>[] index = new ArrayList[26];

        int k = 0;
        int distinctLetters = 0;

        for (int r = 0; r < R; r++) {
            for (int c = 0; c < C; c++) {
                char ch = board[c][r];
                int firstLetter = 'A';
                if (ch >= 'a') {
                    firstLetter = 'a';
                }
                if (index[ch - firstLetter] == null) {
                    index[ch - firstLetter] = new ArrayList<Integer>();
                    distinctLetters++;
                }
                index[ch - firstLetter].add(k);
            }
            k++;
        }
    }
    
    public static int[] getColRow(int k, int C, int R) {
        
        int[] colRow = new int[2];
        colRow[0] = k%C;
        colRow[1] = k/C;
        return colRow;
        
    }
    
    public static 

}
