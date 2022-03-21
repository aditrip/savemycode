package adi.leetcode;

import java.util.HashMap;
import java.util.Map;

public class TopK2 {
    
    int[] pq = null; // min heap of frequencies
    int[] topK = null; // corresponding N.
    int K = 0;

    public static void main(String[] args) {
        int[] nums = {1,1,1,2,2,3,4,5,6,3,7,6,5,3,4,7,6,4,3,6,7,3,4};
        int k = 3;
        TopK2 obj = new TopK2();
        int [] topK = obj.topKFrequent(nums, k);
        int[] pq = obj.getPQ();
        for (int i =0; i< topK.length; i++ )
        {
            System.out.print(" " + topK[i] + ":" + pq[i]);
        }
       
    }
    
     public int[] topKFrequent(int[] nums, int k) {
         if (k <= 0 || nums == null || nums.length == 0) {
             return new int[0];
         }
         K = k;
         pq = new int[k]; // min heap of frequencies
         topK = new int[k]; // corresponding N.
         int[] freq = new int[nums.length];

         HashMap<Integer,Integer> freq = new HashMap<Integer,Integer>();
         for (int n : nums) {
             if (freq.get(n) == null) {
                 freq.put(n, 1);
             } else {
                 freq.put(n, freq.get(n) + 1);
             }
         }

         for(Map.Entry<Integer, Integer> entry: freq.entrySet()) {
             if (entry.getValue() > pq[0]) {
                 pqInsert(entry.getValue().intValue(),
                          entry.getKey().intValue());
             }
         }
         return topK;

    }
     
     public void pqInsert(int v, int n) {
         int idx = 0;
         pq[0] = v;
         topK[0] = n;
         
         if ( K < 3) {
             if ( K == 2) {
                 if (pq[0] > pq[1]) {
                     swap(pq,0,1);
                     swap(topK, 0 , 1);
                 }
             } 
             return;
         }
         /* K >=3 */
         int minIdx = minIdx(0);
         
         while(minIdx != idx) {
             swap(pq, minIdx, idx);
             swap(topK, minIdx, idx);
             idx = minIdx;
             minIdx = minIdx(idx);
         }
         
     }
     
     public int lChild(int idx) {
         return 2*(idx + 1) - 1;
     }
     
     public int rChild(int idx) {
         return 2*(idx + 1);
     }
     
     public int minIdx(int i) {
         int l = lChild(i);
         int r = rChild(i);
         if ( l >= K) {
             return i;
         } else if (r >= K) {
             return pq[l] < pq [i] ? l : i;
         }
         int minIdx = pq[l] < pq[r] ? l : r ;
         return pq[i] < pq[minIdx] ? i : minIdx;
     }
     
     public void swap(int[] a, int i, int j) {
         int temp;
         temp = a[i];
         a[i] = a[j];
         a[j] = temp;
     }
     
     int[] getPQ() {
         return pq;
     }

}

