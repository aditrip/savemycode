package problems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LongestPalindrome {

    HashMap<XYPair, Integer> LP = new HashMap<XYPair, Integer>();
    HashMap<Character, ArrayList<Integer>> occurences =
        new HashMap<Character, ArrayList<Integer>>();
    HashMap<Character, int[]> occurences2 =
            new HashMap<Character, int[]>();

    public static void main(String[] args) {

        List<String> inputs = new ArrayList<String>();
        inputs.add("pqabbacdefggfeqtaaa");
        inputs.add("p");
        inputs.add("pq");
        inputs.add("abba");
        inputs.add("aaaa");
        inputs.add("bbbbb");
        inputs.add("aa");
        inputs.add("bbb");
        inputs.add("caacq");
        inputs.add("dbbb");
        inputs.add("ebb");
        inputs.add("rcaacq");
        inputs.add("rbrdbbbb");
        inputs.add("rbrdbb");
        inputs.add("abcdefedcba");
        inputs.add("rbrdbbbb");
        inputs.add("pqabcdefedcbapq");
        inputs.add("dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
        inputs.add("dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
        inputs.add("dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
        inputs.add("dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
        inputs.add("dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
        inputs.add("dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
        inputs.add("dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
        inputs.add("dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
        inputs.add("pqabcdefedcbaqq");
        
        LongestPalindrome obj = new LongestPalindrome();
        long start = System.nanoTime();
        for(String s: inputs) {
          obj.longestPalindrome(s);
        }
        long end = System.nanoTime();
        System.out.println("Time Taken:" + ((end-start)/(1000*1000)) + " millis");
        long start2 = System.nanoTime();
        for(String s: inputs) {
          obj.longestPalindrome2(s);
        }
        long end2 = System.nanoTime();
        System.out.println("Time Taken:" + ((end2-start2)/(1000*1000)) + " millis");
        long start3 = System.nanoTime();
        for(String s: inputs) {
          obj.longestPalindrome3(s);
        }
        long end3 = System.nanoTime();
        System.out.println("Time Taken:" + ((end3-start3)/(1000*1000)) + " millis");
        
        long start4 = System.nanoTime();
        for(String s: inputs) {
          System.out.println( obj.longestPalindrome4(s));
        }
        long end4 = System.nanoTime();
        System.out.println("Time Taken:" + ((end4-start4)/(1000*1000)) + " millis");
    }

    public String longestPalindrome(String s) {
        occurences.clear();
        LP.clear();
        XYPair maxLP = new XYPair(0,0);
        int maxLPLength = 1;
        if (s == null || s.length() == 0 || s.length() == 1) {
            return s;
        }
        char[] arr = s.toCharArray();

        for (int i = 0; i < arr.length; i++) {
            if (occurences.get(arr[i]) == null) {
                ArrayList<Integer> list = new ArrayList<Integer>();
                list.add(i);
                occurences.put(arr[i], list);
                continue;
            }

            // Same char found. May be a palindrome with a prev char.
            ArrayList<Integer> list = occurences.get(arr[i]);
            // We know that i > j here
            for (int k = list.size() - 1; k>= 0; k--) {
                int j = list.get(k);
                XYPair pair = new XYPair(j, i);

                /*
                 * i -j > 1. 
                 * Now we can apply the gist of this problem.
                 *  LP(j,i)= LP(j+1,i-1) + 2 
                 *  provided these conditions prevail: 
                 *  1) a[j]= a[i] - trivial here. 
                 *  2) LP(j+1,i-1) is not null 
                 *  3) j != i
                 * if LP(j+1, i-1) == null, LP(j,i) is null as well. 
                 * if j=i, it
                 * is assumed that LP(j,i) = 1; 
                 * This logic is coded in method
                 * lp(int j, int i)
                 */

                int currLength = lp(j, i);
                if (currLength > 1) {
                    LP.put(pair, currLength);
                    if (currLength > maxLPLength) {
                        maxLPLength = currLength;
                        maxLP = pair;
                    }

                }

            }
            
            list.add(i);
        }
        
        return s.substring(maxLP.x, maxLP.y + 1);

    }
    
    public String longestPalindrome2(String s) {
        occurences2.clear();
        LP.clear();

        XYPair maxLP = new XYPair(0,0);
        int maxLPLength = 1;
        if (s == null || s.length() == 0 || s.length() == 1) {
            return s;
        }
        char[] arr = s.toCharArray();

        for (int i = 0; i < arr.length; i++) {
            if (occurences2.get(arr[i]) == null) {
                int[] list = new int[arr.length + 1];
                list[0] = i;
                list[1] = -1;
                occurences2.put(arr[i], list);
                continue;
            }

            // Same char found. May be a palindrome with a prev char.
            int[] list = occurences2.get(arr[i]);
            int size =0;
            while(list[size] != -1) size++;
            // We know that i > j here
            for (int k = size - 1; k>= 0; k--) {
                int j = list[k];
                XYPair pair = new XYPair(j, i);

                /*
                 * i -j > 1. 
                 * Now we can apply the gist of this problem.
                 *  LP(j,i)= LP(j+1,i-1) + 2 
                 *  provided these conditions prevail: 
                 *  1) a[j]= a[i] - trivial here. 
                 *  2) LP(j+1,i-1) is not null 
                 *  3) j != i
                 * if LP(j+1, i-1) == null, LP(j,i) is null as well. 
                 * if j=i, it
                 * is assumed that LP(j,i) = 1; 
                 * This logic is coded in method
                 * lp(int j, int i)
                 */

                int currLength = lp(j, i);
                if (currLength > 1) {
                    LP.put(pair, currLength);
                    if (currLength > maxLPLength) {
                        maxLPLength = currLength;
                        maxLP = pair;
                    }

                }

            }
            
            list[size] = i;
            list[size + 1] = -1;
        }
        
        return s.substring(maxLP.x, maxLP.y + 1);

    }
    
    public String longestPalindrome3(String s) {
        occurences2.clear();
        LP.clear();

        XYPair maxLP = new XYPair(0,0);
        int maxLPLength = 1;
        if (s == null || s.length() == 0 || s.length() == 1) {
            return s;
        }
        char[] arr = s.toCharArray();

        for (int i = 0; i < arr.length; i++) {
            if (occurences2.get(arr[i]) == null) {
                int[] list = new int[arr.length + 1];
                list[0] = i;
                list[1] = -1;
                occurences2.put(arr[i], list);
                continue;
            }

            // Same char found. May be a palindrome with a prev char.
            int[] list = occurences2.get(arr[i]);
            int size =0;
            while(list[size] != -1) size++;
            // We know that i > j here
            for (int k = size - 1; k>= 0; k--) {
                int j = list[k];
                XYPair pair = new XYPair(j, i);

                /*
                 * i -j > 1. 
                 * Now we can apply the gist of this problem.
                 *  LP(j,i)= LP(j+1,i-1) + 2 
                 *  provided these conditions prevail: 
                 *  1) a[j]= a[i] - trivial here. 
                 *  2) LP(j+1,i-1) is not null 
                 *  3) j != i
                 * if LP(j+1, i-1) == null, LP(j,i) is null as well. 
                 * if j=i, it
                 * is assumed that LP(j,i) = 1; 
                 * This logic is coded in method
                 * lp(int j, int i)
                 */

                int currLength;
                // A letter in itself is a palindrome of length 1.
                if (j == i) {
                    currLength = 1;
                }

                else if (j + 1 == i) {
                    currLength = 2;
                }
                // aba case.
                else if ((j + 1) == (i - 1)) {
                    currLength = 3;
                } else {

                XYPair subPair = new XYPair(j + 1, i - 1);
                if (LP.get(subPair) == null) {
                    currLength = 0;
                } else {

                   currLength = LP.get(subPair) + 2;
                   }
                }
                if (currLength > 1) {
                    LP.put(pair, currLength);
                    if (currLength > maxLPLength) {
                        maxLPLength = currLength;
                        maxLP = pair;
                    }

                }

            }
            
            list[size] = i;
            list[size + 1] = -1;
        }
        
        return s.substring(maxLP.x, maxLP.y + 1);

    }

    public String longestPalindrome4(String s) {
       
        if (s == null || s.length() == 0 || s.length() == 1) {
            return s;
        }
        int[][] LP1 = new int[s.length()][s.length()];
        int[] xypair = new int[2];
        int maxLPLength = 1;
        char[] arr = s.toCharArray();
        for (int i = 1; i < arr.length; i++) {
              for (int j = i; j>= 0; j--) {
               
                  if(i == j) {
                      LP1[j][i] = 1;
                      continue;
                  }
                 if(arr[i] == arr[j]) {
                    // At this point j < i
                     
                     if(i==(j+1)) {
                         //consecutive chars
                         LP1[j][i] = 2;
                     }
                     
                     else if ((j + 1) == (i - 1)) {
                         //aba case
                         LP1[j][i] = 3;
                     }
                     // Here we know i -j > 2
                     else if(LP1[j+1][i-1] == 0) {
                         LP1[j][i] = 0;
                     } 
                     
                     else {
                         LP1[j][i] = LP1[j+1][i-1] + 2;
                     }
                     
                     if(LP1[j][i] > maxLPLength){
                         maxLPLength = LP1[j][i];
                         xypair[0] = j;
                         xypair[1] = i;
                     }
                 }
             

            }

        }
        
        return s.substring(xypair[0], xypair[1] + 1);

    }

    /*
     * Caller needs to make sure j and i are valid values. We already know a[j]
     * = a[i], cos that is the only time we invoke this function
     */
    public int lp(int j, int i) {
        // A letter in itself is a palindrome of length 1.
        if (j == i) {
            return 1;
        }

        if (j + 1 == i) {
            return 2;
        }
        // aba case.
        if ((j + 1) == (i - 1)) {
            return 3;
        }

        XYPair subPair = new XYPair(j + 1, i - 1);
        if (LP.get(subPair) == null) {
            return 0;
        }

        return LP.get(subPair) + 2;

    }

    static class XYPair {
        public int x;
        public int y;

        public XYPair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof XYPair)) {
                return false;
            }

            return this.x == ((XYPair) obj).x && this.y == ((XYPair) obj).y;
        }

        @Override
        public int hashCode() {
            String s = new Integer(x).toString() + new Integer(y).toString();
            return Integer.parseInt(s);
        }

    }

}
