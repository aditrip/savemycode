package adi.leetcode;

import java.util.Arrays;
import java.util.HashMap;

public class Solution {

    public static void main(String[] args) {
        Solution obj = new Solution();
        int[] nums = {3,2,4};
        int target = 6;
        System.out.println(Arrays.toString( obj.twoSum(nums, target)));

    }

    public int[] twoSum(int[] nums, int target) {
        int[] ret = new int[2];
        if (nums.length == 2) {
            ret[0] = 0;
            ret[1] = 1;
            return ret;
        }
        HashMap<Integer, Integer> m = new HashMap<Integer,Integer>();
        int idx1 = 0;
        Integer idx2 = 1;
        if (nums[0] + nums[1] == target) {
            ret[0] = 0;
            ret[1] = 1;
            return ret;
        }
        /* populate */
        for (int i = 0; i < nums.length; i++) {
            m.put(nums[i], i);
        }
        int sKey;

        for(; idx1 < nums.length - 1; idx1++) {
            sKey = target - nums[idx1];
            idx2 = m.get(sKey);
            if ( idx2 != idx1 && idx2 != null) {
                ret[0] = idx1;
                ret[1] = idx2;
                return ret;
            }
        }
        
        return ret;
    }
}
