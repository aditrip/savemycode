package adi.leetcode;

public class MaxSubArray {
    
    public static void main (String[] args) {
        MaxSubArray obj = new MaxSubArray();
        int[][] probs = {
                {-2,-1},
                //{-3,-2,1,2,2,0,1,0},
                //{-2,1,-3,4,-1,2,1,-5,4},
                //{-2,-3,-1}
                };
        //int[] nums = {-2,1,-3,4,-1,2,1,-5,4};
        //int[] nums = {-2,-3,-1};
        for (int[] nums: probs)
        System.out.println("Max Sum:" + obj.maxSubArray(nums));
    }
    
    public int maxSubArray(int[] nums) {
        int maxSum = nums[0];
        int currSum = nums[0];
        int idx1 = 0;
        int idx2 = 0;

        int base = nums[0] > 0 ? 0 : nums[0];
        for (int i=1; i<nums.length; i++) {
            System.out.println("i:" + i + " nums[i]:" + nums[i] +" currSum:" + currSum + " base:" + base +
                               " maxSum:" + maxSum + " idx1:" + idx1 + " idx2:" + idx2);
            currSum += nums[i];
            if (currSum > base && (currSum - base) > maxSum) {
                maxSum = currSum - base;
                idx2 = i;
            } else {
                if (nums[i] > maxSum) {
                    maxSum = nums[i];
                    idx1 = idx2 = i;
                    continue;
                }
                /* gobble up negative numbers and check for new global min */
                while (++i < nums.length && nums[i] < 0) {
                    if (maxSum < 0) {
                        if (nums[i] > maxSum) {
                            maxSum = nums[i];
                            idx1 = idx2 = i;
                        }
                    }
                    currSum += nums[i];
                }
                if ( i == nums.length) {
                    return maxSum;
                }
                i--; 
                if (currSum < base) {
                    /* new global minimum */
                    base = currSum;
                    idx1 = i+1;
                }
            }
        }
        System.out.println("Max sum occurs between indexes:" + idx1 + " and " + idx2);
        return maxSum;
    }

}
