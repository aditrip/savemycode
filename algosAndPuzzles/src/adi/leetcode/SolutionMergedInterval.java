package adi.leetcode;

import java.util.Arrays;
import java.util.Comparator;

class SolutionMergedInterval {
    static class Pair<P, Q> {
       public P first;
       public Q second;

       public Pair(P first, Q second) {
           this.first = first;
           this.second = second;
       }

       @Override
       public boolean equals(Object o) {
           if (!(o instanceof Pair)) {
               return false;
           }
           return first.equals(o) && second.equals(o);
       }
   }

    public int[][] merge(int[][] intervals) {
        ArrayTree arrT = new ArrayTree(intervals);
       return arrT.mergeOverlaps();
   }


 
   /* Sorted array implementation */
   static class ArrayTree {

       int[][] sortedKeys;
       int[] maxEnds;

       public ArrayTree(int[][] intervals) {
           addAll(intervals);
       }

       public int[][] mergeOverlaps() {
           /* Merge overlapping intervals */
           int[] nextMinNode = min();
           int nextEnd = Integer.MAX_VALUE;
           int[][] merged = new int[sortedKeys.length][2];
           int i = 0;
           do {
               Pair<Integer, Integer> nextEndPair = getMaxEnd(nextMinNode[1]);
               nextEnd = nextEndPair.first;
               int[] overlapInt = new int[2];
               overlapInt[0] = nextMinNode[0];
               overlapInt[1] = nextEnd;
               merged[i++] = overlapInt;
               nextMinNode = getNextMin(nextEnd);

           } while (nextMinNode != null);

           if (sortedKeys.length > i) {
               int[][] truncated = new int[i][2];
               System.arraycopy(merged, 0, truncated, 0, i);
               merged = truncated;
           }
           for (int[] interval : merged) {
               System.out.println(interval[0] + ":" + interval[1]);
           }
           return merged;
       }

       private void addAll(int[][] intervals) {
           Arrays.sort(intervals, new StartPtComp());
           maxEnds = new int[intervals.length];
           maxEnds[0] = intervals[0][1];
           for (int i = 1; i < intervals.length; i++) {
               if (maxEnds[i - 1] > intervals[i][1]) {
                   maxEnds[i] = maxEnds[i - 1];
               } else {
                   maxEnds[i] = intervals[i][1];
               }
           }
           sortedKeys = intervals;
       }

       public int[] min() {
           return sortedKeys[0];
       }

       /*
        * next min interval whose start is after fromKey.
        * fromKey is not included.
        */
       public int[] getNextMin(int fromKey) {
           int gteIdx = binSearch(fromKey);
           if (gteIdx < 0) {
               return null;
           }
           if (gteIdx == sortedKeys.length - 1) {
               if (sortedKeys[gteIdx][0] > fromKey) {
                   return sortedKeys[gteIdx];
               }
               return null;
           }

           return sortedKeys[gteIdx + 1];
       }

       /** This function finds the max end point amongst all the
       * intervals whose startPt is less than equal to the passed
       * lastStartPt.
       * 
       * The lastStartPt passed is typically the maxEnd of overlapping
       * intervals.
       * 
       * If there are no intervals less than lastStartPt, MIN_VALUE
       * is returned.
       * 
       * @param lastStartPt - consider all intervals which start before or
       *                      on this start point.
       * @return Pair of maxEnd and index of maxEnd into maxEnds array.
       */
       public Pair<Integer, Integer> getMaxEnd(int lastStartPt) {
           int gteIdx = binSearch(lastStartPt);
           if (gteIdx < 0) {
               return new Pair<Integer, Integer>(Integer.MIN_VALUE,
                                                 Integer.MIN_VALUE);
           }
           int maxEnd = maxEnds[gteIdx];
           int[] nextNode = null;
           do {
               while (gteIdx < maxEnds.length && maxEnds[gteIdx++] == maxEnd)
                   ;
               if (gteIdx >= sortedKeys.length - 1) {
                   break;
               }
               nextNode = sortedKeys[gteIdx];
               maxEnd = maxEnds[gteIdx];


           } while (nextNode[0] == maxEnd);
           if (gteIdx == sortedKeys.length) gteIdx--;
           return new Pair<Integer, Integer>(maxEnds[gteIdx], gteIdx);
       }

       /*
        * return the max index whose startPt <= gteStartPt.
        * 
        * returns the index between 0 and sortedKeys.length - 1,
        * even if gteStartPt is less than min or more than max.
        */
       private int binSearch(int gteStartPt) {
           if (sortedKeys.length < 100) {
               /* Do a sequential search */
               int idx = 0;
               while (idx < sortedKeys.length
                       && sortedKeys[idx][0] <= gteStartPt) {
                   idx++;
               }
               idx = (idx > 0 ? idx - 1 : 0);
               if (sortedKeys[idx][0] <= gteStartPt) {
                   return idx;
               } else {
                   return -1;
               }
           }

           /* Find first idx less than gteStartPt,
            * then iterate till it is greater.
            * If gteStartPt is not in sortedKeys, binSearch
            * will stop at an index it could have been there
            * if it existed.
            * 
            * Edge cases:
            */
           int s = 0;
           int e = sortedKeys.length - 1;
           int mid;

           while (e > s) {
               mid = (s + e) >>> 1;
               if (sortedKeys[mid][0] > gteStartPt) {
                   s = mid + 1;
               } else if (sortedKeys[mid][0] < gteStartPt) {
                   e = mid - 1;
               } else {
                   int idx = mid;
                   while (sortedKeys[idx++][0] == gteStartPt
                           && idx <= sortedKeys.length)
                       ;
                   return mid == 0 ? mid : idx - 1;
               }
           }
           int idx = s;
           if (sortedKeys[idx][0] < gteStartPt) {
               while (sortedKeys[idx][0] < gteStartPt
                       && idx < sortedKeys.length) {
                   idx++;
               }
               if (sortedKeys[idx - 1][0] <= gteStartPt) {
                   return idx - 1;
               }
               return -1;

           }
           if (sortedKeys[idx][0] > gteStartPt) {
               while (sortedKeys[idx--][0] > gteStartPt && idx >= 0)
                   ;
               idx = ((idx <= 0) ? 0 : (idx + 1));
               if (sortedKeys[idx][0] <= gteStartPt) {
                   return idx;
               }
               return -1;

           }
           return -1;
       }

       static class StartPtComp implements Comparator<int[]> {

           @Override
           public int compare(int[] o1, int[] o2) {
               if (o1 == null || o2 == null) {
                   throw new NullPointerException();
               }
               if (o1[0] > o2[0]) {
                   return 1;
               }
               if (o1[0] < o2[0]) {
                   return -1;
               }
               if (o1[0] == o2[0]) {
                   return 0;
               }
               throw new IllegalArgumentException();
           }

       }
   }

   
}
