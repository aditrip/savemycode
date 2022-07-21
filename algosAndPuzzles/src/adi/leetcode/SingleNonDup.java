package adi.leetcode;

import java.util.Arrays;

public class SingleNonDup {

    public static void main(String[] args) {
        SingleNonDup obj = new SingleNonDup();
        /*
         * from first two inputs:
         * 2n + 1 side has the nonDup.
         * take mid:
         * if (a[mid] == a[mid -1])
         *    if start to mid is 2n + 1 form, go left, else right.
         *    that is,
         *    if ((mid + 1 - s) & 1 == 1), e = mid - 2; else s=mid + 1; 
         * else 
         *    if ( mid to end is 2n + 1 form go right, else left)
         */
        int[][] nums = {
                {1,2,2,3,3},
                {2,2,3,3,1},
                {2,2,3,3,4,4,5,5,6,7,7},
                {2,2,3},
                {3,2,2},
                {2,2,3,4,4,5,5,6,6,7,7,8,8}
        };
        
        int actual, expected;
        
        for(int[] num : nums) {
            actual = obj.singleNonDuplicateTest(num);
            expected = obj.singleNonDuplicate(num);
            if (expected != actual) {
                System.out.println(" Failed:" + " Expected:" + expected +
                                   " Actual:" + actual + 
                                   " input:" + Arrays.toString(num));
            } else {
                System.out.println("passed.");
            }
        }
    }
    
    public int singleNonDuplicate(int[] nums) {
        int n = nums.length;
        if (n == 1) {
            return nums[0];
        }
        if (nums[n - 1] != nums[n-2]) {
            return nums[n-1];
        }
        if (nums[0] != nums[1]) {
            return nums[0];
        }

        int s = 0; int e = nums.length -1;
        int mid;
        while (e >= s) {
            mid = (s+e) >>> 1;
            if (nums[mid] == nums[mid -1]) {
                if (((mid + 1 - s) & 1 ) == 1) {
                    e = mid - 2;
                } else {
                    s = mid + 1;
                }
                continue;
            }else if (nums[mid] == nums[mid + 1]) {
                if (((e + 1 - mid) & 1 ) == 1) {
                    s = mid + 2;
                } else {
                    e = mid - 1;
                }
            } else {
                return nums[mid];
            }
            
        }
        return -1;
    }

    
    public int singleNonDuplicateTest(int[] nums) {
        if (nums.length == 1) {
            return nums[0];
        }

        for (int i=1; i<nums.length; i++) {
            if ((i & 1) == 1 && (nums[i] ^ nums[i-1]) != 0) {
                return nums[i-1];
            }
            if ( i == nums.length - 1 && (i & 1) == 0) {
                return nums[i];
            }
            
        }
        return -1;
    }

}
