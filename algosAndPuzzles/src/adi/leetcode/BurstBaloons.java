package adi.leetcode;

import java.util.Arrays;
import java.util.Comparator;

/*
 There are some spherical balloons taped onto a flat wall that represents the XY-plane. The balloons are represented as a 2D integer array points where points[i] = [xstart, xend] denotes a balloon whose horizontal diameter stretches between xstart and xend. You do not know the exact y-coordinates of the balloons.

Arrows can be shot up directly vertically (in the positive y-direction) from different points along the x-axis. A balloon with xstart and xend is burst by an arrow shot at x if xstart <= x <= xend. There is no limit to the number of arrows that can be shot. A shot arrow keeps traveling up infinitely, bursting any balloons in its path.

Given the array points, return the minimum number of arrows that must be shot to burst all balloons.

 

Example 1:

Input: points = [[10,16],[2,8],[1,6],[7,12]]
Output: 2
Explanation: The balloons can be burst by 2 arrows:
- Shoot an arrow at x = 6, bursting the balloons [2,8] and [1,6].
- Shoot an arrow at x = 11, bursting the balloons [10,16] and [7,12].
Example 2:

Input: points = [[1,2],[3,4],[5,6],[7,8]]
Output: 4
Explanation: One arrow needs to be shot for each balloon for a total of 4 arrows.
Example 3:

Input: points = [[1,2],[2,3],[3,4],[4,5]]
Output: 2
Explanation: The balloons can be burst by 2 arrows:
- Shoot an arrow at x = 2, bursting the balloons [1,2] and [2,3].
- Shoot an arrow at x = 4, bursting the balloons [3,4] and [4,5].
 

Constraints:

1 <= points.length <= 105
points[i].length == 2
-231 <= xstart < xend <= 231 - 1
 */
public class BurstBaloons {

    public static void main(String[] args) {
        BurstBaloons obj = new BurstBaloons();
        int[][] debugInterval = { { 9, 17 }, { 4, 12 }, { 4, 8 }, { 4, 8 },
                { 7, 13 }, { 3, 4 }, { 7, 12 }, { 9, 15 }, { 4, 8 }, { 4, 8 },
                { 4, 7 }, { 4, 5 }, { 4, 6 } };
        boolean debug = false;

        int[][][] intervals = { { { 1, 2 }, { 2, 3 }, { 3, 4 }, { 1, 3 } },
                { { 1, 2 }, { 1, 2 }, { 1, 2 } }, { { 1, 2 }, { 2, 3 } },
                { { 3, 9 }, { 7, 12 }, { 3, 8 }, { 6, 8 }, { 9, 10 }, { 2, 9 },
                        { 0, 9 }, { 3, 9 }, { 0, 6 }, { 2, 8 } },
                { { 1, 2 }, { 2, 3 }, { 3, 4 }, { 4, 5 } }, { { 1, 2 } },
                { { 9, 12 }, { 1, 10 }, { 4, 11 }, { 8, 12 }, { 3, 9 },
                        { 6, 9 }, { 6, 7 } },
                { { 9, 17 }, { 4, 12 }, { 4, 8 }, { 4, 8 }, { 7, 13 },
                        { 3, 4 }, { 7, 12 }, { 9, 15 } },
                { { 9, 17 }, { 4, 12 }, { 4, 8 }, { 4, 8 }, { 7, 13 },
                        { 3, 4 }, { 7, 12 }, { 9, 15 }, { 4, 8 }, { 4, 8 },
                        { 4, 7 }, { 4, 5 }, { 4, 6 } } };

        int[] outputs = { 2, 1, 1, 2, 2, 1, 2, 2, 2 };

        int[] actuals = new int[outputs.length];

        int i = 0;
        if (!debug) {
            for (int[][] in : intervals) {
                actuals[i++] = obj.findMinArrowShots(in);
            }

            for (int j = 0; j < actuals.length; j++) {
                if (actuals[j] == outputs[j]) {
                    System.out.println("Passed:" + actuals[j]);
                } else {
                    Arrays.sort(intervals[j], new PointComparator());
                    System.out.println("---- FAILED --- expected:" + outputs[j]
                            + " actual:" + actuals[j] + "  points:"
                            + Arrays.deepToString(intervals[j]));
                }
            }
        } else {
            System.out.println(obj.findMinArrowShots(debugInterval));
        }
    }

    public int findMinArrowShots(int[][] points) {
        if (points.length < 2) {
            return points.length;
        }
        if (points.length == 2) {
            if (points[0][1] >= points[1][0] && points[0][0] <= points[1][1]) {
                return 1;
            } else {
                return 2;
            }
        }

        PointComparator ptComp = new PointComparator();
        java.util.Arrays.sort(points, ptComp);

        int currPtIdx = 0;
        int endPtIdx = 0;
        int jumps = 0;

        while (currPtIdx < points.length) {
            int currEndPt = points[currPtIdx][1];
            endPtIdx = currPtIdx + 1;
            jumps++;
          
            if (points[endPtIdx][0] == currEndPt) {
                /* Handle the connect on point Edge case: Next is connected on a point, move past next,
                 * since this shot can handle both curr and next.
                 * Also move past all points which start on this point
                 * since the shot on this point would take care of all those. */
                while (endPtIdx < points.length
                        && points[endPtIdx][0] == currEndPt) {
                    endPtIdx++;
                }

                if (endPtIdx == points.length) {
                    return jumps;
                }
                if (endPtIdx == points.length - 1) {
                    return ++jumps;
                }
                jumps++;
                currPtIdx = endPtIdx;
                endPtIdx = currPtIdx + 1;
                currEndPt = points[currPtIdx][1];
            }

            while (endPtIdx < points.length
                    && points[endPtIdx][0] <= currEndPt) {
                if (points[endPtIdx][1] < currEndPt) {
                    currEndPt = points[endPtIdx][1];
                }
                endPtIdx++;
            }

            currPtIdx = endPtIdx;
            if (currPtIdx == points.length - 1) {
                return ++jumps;
            }
        }
        return jumps;
    }

    static class PointComparator implements Comparator<int[]> {
        @Override
        public int compare(int[] pt1, int[] pt2) {
            if (pt1[0] > pt2[0]) {
                return 1;
            } else if (pt1[0] < pt2[0]) {
                return -1;
            } else {
                return 0;
            }
        }
    }

}
