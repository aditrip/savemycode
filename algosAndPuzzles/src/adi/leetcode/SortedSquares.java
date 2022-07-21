package adi.leetcode;

import java.util.Arrays;

import adi.leetcode.BurstBaloons.PointComparator;

/*
 * LEVEL: EASY.
 * Given an integer array nums sorted in non-decreasing order,
  return an array of the squares of each number sorted in non-decreasing order.

 

Example 1:

Input: nums = [-4,-1,0,3,10]
Output: [0,1,9,16,100]
Explanation: After squaring, the array becomes [16,1,0,9,100].
After sorting, it becomes [0,1,9,16,100].
Example 2:

Input: nums = [-7,-3,2,3,11]
Output: [4,9,9,49,121]
 

Constraints:

1 <= nums.length <= 104
-104 <= nums[i] <= 104
nums is sorted in non-decreasing order.
 */
public class SortedSquares {

    public static void main(String[] args) {
        SortedSquares obj = new SortedSquares();
        int[] debug_nums = {-23,-22,-21,-19,-16,-14,-3,-2,-1,1,4,8,9,13,14,15,15,15,16,19,20,21,21,22,23};
        boolean debug = true;

        int[][] numsArr = { { -4, -1, 0, 3, 10 }, { -7, -3, 2, 3, 11 },
                { -5, -3, -2, -1 }, { -2, 0 }, 
                {-3,0,2},
                {-3, -1, 0 },
                {-3,-3,-2,1},
                {-23,-22,-21,-19,-16,-14,-3,-2,-1,1,4,8,9,13,14,15,15,15,16,19,20,21,21,22,23}
                };

        int[][] outputs = new int[numsArr.length][];

        int i = 0;
        if (!debug) {
            for (int[] in : numsArr) {
                System.out.println("Input:"
                        + Arrays.toString(in));
                outputs[i++] = obj.sortedSquares(in);
            }

            for (int j = 0; j < outputs.length; j++) {
                if (isSorted(outputs[j])) {
                    System.out.println("Passed:"
                            + Arrays.toString(outputs[j]));
                } else {
                    System.out.println("---- FAILED --- :"
                            + " Sorted Array: " + Arrays.toString(outputs[j]) +
                            " input Array:" + Arrays.toString(numsArr[j]));
                }
            }
        } else {
            System.out.println(Arrays.toString(obj.sortedSquares(debug_nums)));
        }

    }

    public static boolean isSorted(int[] a) {
        if (a == null || a.length == 0 || a.length == 1)
            return true;
        for (int i = 1; i < a.length; i++) {
            if (a[i] < a[i - 1]) {
                return false;
            }
        }
        return true;
    }

    /* Approach#1 : Find the lastNegIdx, and use this as
    split point for two sorted arrays - first one desc, second asc.
    Merge them. Pros - simple code. Cons - longer run time.
    Merge is O(n) but then a new array will be used anyway,
    since inplace would be a mess of a code. KIS. More mem.
    
    
    Approach#2 : find the indexes of negative numbers in sorted
    order array. The last idx in neg arr will be the min inserted
    slot.You first place all positive num sqrs at indexes shifted
    left by 1, and then this one. And so on.
    This approach does log n comparisons, although solution
    remains O(n) anyway since all numbers are iterated.
    But, let's try this for a bit of fun and run time minimization.
    Can be done inPlace since slotIdx are known apriori.
    */
    public int[] sortedSquares(int[] nums) {
        int lastNegIdx = -1;
        if (nums == null || nums.length == 0) {
            return nums;
        }
        if (nums.length == 1) {
            nums[0] = nums[0] * nums[0];
            return nums;
        }
        boolean reverse = false;
        boolean forward = false;
        /* Get split Idx (lastNegIdx) */

        if (nums[nums.length - 1] <= 0) {
            reverse = true;
        } else if (nums[0] >= 0) {
            forward = true;
        } else {
            /* small array optimization .. hmm this could
            have been avoided in first approach */
            if (nums[1] >= 0) {
                lastNegIdx = 0;
            } else if (nums[2] >= 0) {
                lastNegIdx = 1;
            } else {
                /* lastNegIdx > 1 */
                lastNegIdx = lteSearch(nums, 0, 0, nums.length - 1);
                if (nums[lastNegIdx] == 0) {
                    lastNegIdx--;
                }
            }
        }

        if (forward || reverse) {
            int lastIdx = nums.length >> 1;
            if (reverse && (nums.length & 1) == 0) {
                lastIdx--;
            }
            for (int i = 0; i < nums.length; i++) {
                if (reverse && i > lastIdx) {
                    break;
                }
                int sqr = nums[i] * nums[i];
                if (forward) {
                    nums[i] = sqr;
                } else {
                    int tmp = nums[nums.length - 1 - i]
                            * nums[nums.length - 1 - i];
                    nums[nums.length - 1 - i] = sqr;
                    nums[i] = tmp;
                }
            }
            return nums;
        }

        /* lastNegIdx >= 0 */
        int[] sortedSqrs = new int[nums.length];
        int posIdx = lastNegIdx + 1;
        int negIdx = lastNegIdx;
        int slotIdx = 0;
        int idx = 0;

        for (; negIdx >= 0 && posIdx < nums.length; negIdx--) {
            while (negIdx >= 0 && (-1 * nums[negIdx]) < nums[posIdx]) {
                sortedSqrs[idx++] = nums[negIdx] * nums[negIdx];
                negIdx--;
            }
            if (negIdx < 0 ) break;
            slotIdx = lteSearch(nums, -1 * nums[negIdx], posIdx, nums.length - 1);
            while (posIdx <= slotIdx) {
                sortedSqrs[idx++] = nums[posIdx] * nums[posIdx];
                posIdx++;
            }
            sortedSqrs[idx++] = nums[negIdx] * nums[negIdx];
            
        }

        while (posIdx < nums.length) {
            sortedSqrs[idx++] = nums[posIdx] * nums[posIdx];
            posIdx++;
        }
        
        while (negIdx >= 0) {
            sortedSqrs[idx++] = nums[negIdx] * nums[negIdx];
            negIdx--;
        }

        return sortedSqrs;

    }

    /* If skey is less than the min in this array, returns -1.
     * if skey is equal to an existing key, returns min index of
     * all equal keys.
     * If skey is not equal to any, returns the max key which is
     * less than sKey. */
    public int lteSearch(int[] nums, int sKey, int start, int end) {
        if (start > end) {
            return -1;
        }
        if (end == start) {
            if (nums[start] > sKey) {
                return -1;
            } else {
                return start;
            }
        }
        if (nums[start] > sKey) {
            return -1;
        } else if (nums[start] == sKey) {
            return start;
        }
        if ((end - start) < 20) {
            int idx = start;
            while (idx <= end && nums[idx] < sKey) {
                idx++;
            }
            if (idx > end || (idx <= end && nums[idx] > sKey)) {
                idx--;
            }

            return idx;
        }

        int s = start;
        int e = end;
        if (nums[s] == sKey) {
            return s;
        }

        while (e >= s) {
            int mid = (s + e) >>> 1;
            int midKey = nums[mid];
            if (sKey == midKey) {
                while (mid > start && nums[mid - 1] == midKey) {
                    mid--;
                }
                return mid;
            }
            if (sKey > midKey) {
                s = mid + 1;
            } else {
                e = mid - 1;
            }
        }
        return e;

    }

}
