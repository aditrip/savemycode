package adi.leetcode;

import java.util.Arrays;
import java.util.Comparator;

/*
   Given an array of intervals where intervals[i] = [starti, endi],
   return the minimum number of intervals you need to remove to make the
   rest of the intervals non-overlapping.

 

Example 1:

Input: intervals = [[1,2],[2,3],[3,4],[1,3]]
Output: 1
Explanation: [1,3] can be removed and the rest of the intervals are non-overlapping.
Example 2:

Input: intervals = [[1,2],[1,2],[1,2]]
Output: 2
Explanation: You need to remove two [1,2] to make the rest of the intervals non-overlapping.
Example 3:

Input: intervals = [[1,2],[2,3]]
Output: 0
Explanation: You don't need to remove any of the intervals since they're already non-overlapping.

Constraints:

1 <= intervals.length <= 105

intervals[i].length == 2

-5 * 104 <= starti < endi <= 5 * 104

 */

/* Crux of this algorithm:
 * #1: given that if we consider any two overlapping intervals, then one of
 *     them has to be removed. The one with max overlaps can be removed.
 * 
 * #2: In sorted array by starPt, if idx + 1 does not overlap with idx,
 *     then no other interval overlaps with interval at idx.
 *      
 * This leads to the idea that we can iterate a sorted array considering
 * first two elements only, where firstIdx moves as one interval is removed.
 * 
 * Details:
 * Connecting intervals are not considered overlapping interval.
 * 
 * Terminologies and notes:
 * 1. sArr - Sorted Array on start pt of intervals.[sIdx to end]
 * 2. eArr - Sorted Array on end pt of intervals. [Not required]
 * 3. si - start Pt value of the interval being considered.
 * 4. ei - end Pt value of the interval being considered.
 * 
 * sj and ej are used for all the overlapping intervals.
 * 
 * 5. eiIdx - max index in sArr, s.t index interval's startPt < ei.
 * 6. sIdx - startIdx in sPtArr. [It is incremented in this algorithm]
 * 
 * Interval i and j overlap if:
 *    ej > si && sj < ei. [Equal to is not considered overlapping here]
 * 
 * Although this implies all overlapping intervals for a given interval i,
 * is an intersection of two lists : from siIdx to eiIdx in sArr and
 *                                   from min idx s.t ej > si to end of eArr.
 * 
 * But, given that if we consider any two overlapping intervals, then one of
 * them has to be removed, we only need first list.
 * 
 * This is because in sArr, all intervals after siIdx to eiIdx overlaps, since
 * their startPts are b/w si and ei. And intervals before siIdx are no more
 * overlapping in this algorithm.
 * 
 * Basic Algorithm:
 *    1. Sort iNodes by start Pt. sArr
 *    2. Compare intervals at sIdx and (sidx + 1) while (sidx + 1) < sArr.length
 *       delIdx = compareOverlaps(sIdx).
 *       
 *       2a: compareOverlaps(sIdx)
 *            - getOverlaps(sidx) and getOverlaps(sIdx+1)
 *            - return sidx or sidx + 1 depening on which ever is greater.
 *    3. if delIdx = sIdx + 1,
 *          - sArr[sIdx + 1] = sArr[sIdx]
 *       sIdx++; 
 *       
 *    getOverlaps(sIdx):
 *      - binSearch LTE ei of interval at sIdx in sArr, starting from sIdx.
 *      - if si = ei, decrement foundIdx until si < ei.
 *      - return foundIdx - sIdx.
 *      
 *      Note that all intervals before sIdx are no more overlapping and all
 *      intervals > si in sArr and < ei overlap.
 *
 */
public class RemoveOverlaps {

    public static void main(String[] args) {
        RemoveOverlaps obj = new RemoveOverlaps();
        int[][] debugInterval = {{-52,31},{-73,-26},{82,97},{-65,-11},{-62,-49},{95,99},{58,95},{-31,49},{66,98},{-63,2},{30,47},{-40,-26}};
        boolean debug = false;

        int[][][] intervals = { 
                { { 1, 2 }, { 2, 3 }, { 3, 4 }, { 1, 3 } },
                { { 1, 2 }, { 1, 2 }, { 1, 2 } },
                { { 1, 2 }, { 2, 3 } },
                {{-52,31},{-73,-26},{82,97},{-65,-11},{-62,-49},{95,99},{58,95},{-31,49},{66,98},{-63,2},{30,47},{-40,-26}}

                };

        int[] outputs = { 
                1,
                2,
                0,
                7};

        int[] actuals = new int[outputs.length];

        int i = 0;
        if (!debug) {
            for (int[][] in : intervals) {
                actuals[i++] = obj.eraseOverlapIntervals(in);
            }

            for (int j = 0; j < actuals.length; j++) {
                if (actuals[j] == outputs[j]) {
                    System.out.println("Passed:" + actuals[j]);
                } else {
                    System.out.println("---- FAILED --- expected:" + outputs[j]
                            + " actual:" + actuals[j]);
                }
            }
        } else {
            System.out.println(obj.eraseOverlapIntervals(debugInterval));
        }
    }

    public int eraseOverlapIntervals(int[][] intervals) {
        Arrays.sort(intervals, new IntervalComparator());
        int sIdx = 0;
        int removed = 0;

        /* Compare first two intervals for max overlaps and remove one */
        while (sIdx + 1 <= intervals.length - 1) {
            int[] in1 = intervals[sIdx];
            int[] in2 = intervals[sIdx + 1];
            
            /* If interval1 does not overlap, continue */
            if (in2[0] >= in1[1]) {
                sIdx++;
                continue;
            }
            /* Interval1 overlaps with interval2 */
            
            /* If interval1 endIdx is lte interval2 endIdx,
             * interval2 is removed.
             */
            if (in1[1] <= in2[1]) {
                /* remove in2 */
                intervals[sIdx + 1] = intervals[sIdx];
            } else {
                /* remove in1
                 */
                sIdx++;
            }
            removed++;
            sIdx++;
        }

        return removed;
    }

    /* Assumes intervals before idx are non overlapping */
    int getOverlaps(int[][] sArr, int idx) {
        if (idx == sArr.length - 1) {
            return 0;
        }
        int[] interval = sArr[idx];
        int length = interval[1] - interval[0];
        int eiIdx = -1;
        if (length > 5 && (sArr.length - 1 - idx) > 5) {
            eiIdx = lteSearch(sArr, interval[1], idx);
            if (eiIdx <= idx) {
                return 0;
            }
            if (sArr[eiIdx][0] == interval[1]) {
                while (eiIdx > idx && sArr[eiIdx][0] == interval[1]) {
                    eiIdx--;
                }
            }
        } else {
            eiIdx = idx + 1;
            while (eiIdx <= (sArr.length - 1)
                    && (sArr[eiIdx][0] < interval[1])) {
                eiIdx++;
            }
            if (eiIdx > (sArr.length - 1) || sArr[eiIdx][0] >= interval[1]) {
                eiIdx--;
            }
        }

        return eiIdx - idx;

    }

    /* return index of lte sPt */
    int lteSearch(int[][] sArr, int sPt, int sIdx) {

        int s = sIdx;
        int e = sArr.length - 1;

        int mid = -1;

        while (e >= s) {
            mid = (s + e) >>> 1;
            int mPt = sArr[mid][0];
            if (mPt == sPt) {
                return mid;
            }
            if (sPt > mPt) {
                s = mid + 1;
            } else {
                e = mid - 1;
            }
        }
        return e;

    }

    static class IntervalComparator implements Comparator<int[]> {

        @Override
        public int compare(int[] i1, int[] i2) {
            if (i1[0] < i2[0]) {
                return -1;
            } else if (i1[0] > i2[0]) {
                return 1;
            } else
                return 0;
        }

    }

}
