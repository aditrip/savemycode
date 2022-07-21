package adi.leetcode;

import java.util.Arrays;
import java.util.Comparator;

/*
 * https://leetcode.com/problems/longest-increasing-path-in-a-matrix/
 * 
 * 329. Longest Increasing Path in a Matrix
 * 
 * Given an m x n integers matrix, return the length of the longest increasing path in matrix.

From each cell, you can either move in four directions:
 left, right, up, or down. You may not move
 diagonally or move outside the boundary (i.e., wrap-around is not allowed).
 
 Input: matrix = [[9,9,4],[6,6,8],[2,1,1]]
Output: 4
Explanation: The longest increasing path is [1, 2, 6, 9].

Input: matrix = [[3,4,5],[3,2,6],[2,2,1]]
Output: 4
Explanation: The longest increasing path is [3, 4, 5, 6]. Moving diagonally is not allowed.

Example 3:

Input: matrix = [[1]]
Output: 1

 
[[7,7,5],[2,4,6],[8,2,0]]
Output: 4

Constraints:

    m == matrix.length
    n == matrix[i].length
    1 <= m, n <= 200
    0 <= matrix[i][j] <= 231 - 1

 */
public class LongestIncMatrixPath {
    int M;
    int N;
    int L;
    int[][] matrix;
    
    public static void main(String[] args) {
        
        boolean debug = false;
        LongestIncMatrixPath obj = new LongestIncMatrixPath();
        int[][] debug_matrix = { 
                
                    {3,4,5,6,7},
                    {8,9,10,9,8},
                    {7,6,5,4,3},
                    {2,1,0,-1,-2}
                
        };
        
        int[][][] matrices = {
                {{7,7,5},
                {2,4,6},
                {8,2,0}},
                {
                    {9,9,4},
                    {6,6,8},
                    {2,1,1}},
                {{3,4,5},{3,2,6},{2,2,1}},
                {
                    {3,4,5,6,7},
                    {8,9,10,9,8},
                    {7,6,5,4,3},
                    {2,1,0,-1,-2}
                },
                {
                    {3,4,5,6,7},
                    {8,9,10,9,8},
                    {7,6,5,4,3},
                    {2,1,0,-1,-2},
                    {-7,-6,-5,-4,-3},
                    {-8,-9,-10,-11,-12},
                    {-100,-16,-15,-14,-13}
                },
                {{1}}
        };
        
        int[] actuals= {4,4,4,8,23,1};
        
        if (debug) {
        System.out.println(obj.longestIncreasingPath(debug_matrix));
        } else {
            for (int[][] matrix : matrices) {
                System.out.println(obj.longestIncreasingPath(matrix));
            }
        }
    }
    
    public int longestIncreasingPath(int[][] matrix) {
        this.matrix = matrix;
        M = matrix.length;
        N = matrix[0].length;
        
        L = M * N;
        
        int maxLP = 1;
        int[] LP = new int[L];
        for (int i = 0; i < L; i++) {
            LP[i] = 1;
        }
        
        int[][] sortedKeys = getSortedIndex();
        
        for(int[] entry: sortedKeys) {
            int idx = entry[0];
            int er = idx/N;
            int ec = idx%N;
            int r = er;
            int c = ec;
            int max = 1;
                    
            int[] incIndices = getIncIndices(idx);
            for(int k=0; k < 4; k++) {
                r = er;
                c = ec;
                if (incIndices[k] == 1)
                {
                    if ( k == 0) {
                        c = c -1;
                    } else if (k == 1) {
                        c = c + 1;
                    } else if ( k == 2) {
                        r = r - 1;
                    } else if (k == 3) {
                        r = r + 1;
                    }
                   int nextLP = LP[r*N + c] + 1;
                   if (nextLP > max) {
                       max = nextLP;
                       if (max  > maxLP) {
                           maxLP = max ;
                       }
                   }
                }
            }
            LP[idx] = max;

        }
        
        return maxLP;
        
    }
    
    /*
     * left, right, up, down
     * c-1, c+1, r-1, r+1
     */
    int[] getIncIndices(int idx) {
        int[] dirs = new int[4];

        int r = idx/N;
        int c = idx%N;
        int val = matrix[r][c];
        
        if (c - 1 >= 0 && matrix[r][c-1] > val) {
            dirs[0] = 1;
        }
        if (c + 1 < N && matrix[r][c+1] > val) {
            dirs[1] = 1;
        }
        if (r - 1 >= 0 && matrix[r - 1][c] > val) {
            dirs[2] = 1;
        }
        if (r + 1 < M && matrix[r + 1][c] > val) {
            dirs[3] = 1;
        }
        return dirs;
        
    }
    
    public int[][] getSortedIndex() {
        
        
        int[][] ret = new int[L][2];
        
        for(int i = 0; i < L; i++) {
            ret[i][0] = i;
            ret[i][1] = matrix[i/N][i%N];
        }
        
        Arrays.sort(ret, new PairComparator());
        return ret;
        
        
    }
    
    static class PairComparator implements Comparator<int[]> {

        @Override
        public int compare(int[] o1, int[] o2) {
            if(o1[1] > o2[1]) {
             return -1;
            } else if(o1[1] < o2[1]) {
                return 1;
            } else {
                return 0;
            }
        }
        
    }


}
