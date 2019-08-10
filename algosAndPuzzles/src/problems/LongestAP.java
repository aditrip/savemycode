package problems;

public class LongestAP {

    public static void main(String[] args) {
       // int[] A = {41,45,57,7,5,9,11,3,8,11,15,13,12,6,11,1,19,21,26,27,33};
        int[] A = {41,45,49,57,7,5,9,11,3,8,11,15,13,12,6,11,1,19,21,23};
        int n = A.length;
        //dynamically allocated memory in heap.worst case memory theta(n2)
        Node[][] DP = new Node[n][n];
        
        int maxAPLength = 0;
        int winnerAPDistance = 1;
        int winnerLastIndex = 0;
        /*
         * Distance Matrix (DP): X axis is columns
         *                       Y axis is row.
         * represents distances from A[c] to A[r] and APLength for that distance.
         *         
         * At any index in array A, there are APs of different distances
         * coming in. All such distances are stored in the matrix.
         * That is for every pair in the array i,j; d = A[j] - A[i].
         * 
         * And APLength at an index i is found by adding the AP Length
         * from the index the AP is coming from.
         * 
         * so AP(j] = AP(i) + 1 iff there exists a distance d in the row i of distance matrix.
         * This above logic is captured in updateAPLength;
         * 
         * note that updateAPLength can be made as O(1) by creating a hashMap for every row in distance matrix.
         * For now, it is not using any extra space, and it is theta(c).
         * 
         */
        for(int r=0;r<n;r++) {
            for(int c=0; c<r; c++) {
               DP[c][r] = new Node();
               DP[c][r].distance = A[r]-A[c];
               /*
                * This APDistance is coming from index c to index r.
                * find the maxAPLength for this distance on index c.
                * and increment that APLength for this distance on index r.
                */
               int apLength = updateAPLength(DP,c,r);
               if(apLength > maxAPLength) {
                   maxAPLength = apLength;
                   winnerAPDistance= DP[c][r].distance;
                   winnerLastIndex=r;
               }
            }
        }
        
        System.out.println("MaxAPLength="+(maxAPLength+1) +"  AP series distanced at:" + winnerAPDistance + "   lastIndex:" + winnerLastIndex);

        System.out.println("Array was:");
        for(int k=0; k<n; k++){
            System.out.print(k+":"+A[k]+"  ");
        }
        
        System.out.println("\n Distance Matrix was:");
        for(int p=0; p<n; p++){
            System.out.println("\n Row:"+ p);
            for(int q=0; q<p; q++) {
                if(DP[q][p] != null) {
                    System.out.print(DP[q][p] + "  ");
                }
            }
            
        }
    }
    
    /*
     * Check all columns of c-th row.
     * c-th row contains all AP distances coming into the array index 'c'.
     * find the maxAPLength for distance DP[c][r].
     */
    static int updateAPLength(Node[][] DP, int c, int r) {
        
        int d = DP[c][r].distance;
        
        /* Find the max apLength for the given distance.*/
        int maxAPLength = 0;
        for(int i=0; i<c; i++) {
            if(i != r && DP[i][c].distance == d) {
                if(DP[i][c].apLength >= maxAPLength) {
                   DP[c][r].apLength = DP[i][c].apLength + 1;
                   maxAPLength = DP[i][c].apLength;
                }
            }
        }
        
        return DP[c][r].apLength;
        
    }
    
    static class Node {
        public int distance;
        public int apLength;
        @Override
        public String toString() {
            return distance+":"+apLength;
        }
    }

}
