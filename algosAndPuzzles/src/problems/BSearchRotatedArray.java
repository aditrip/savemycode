package problems;

public class BSearchRotatedArray {
    
    public static void main(String[] args) {
        int[] nums = {16,23,32,34,-8, -4, 0, 12, 14,15};
        //int[] nums = {34,-8, -4, 0, 12, 14,15,16,23,32};
        //int[] nums = {5,4};
        //int[] nums = {3,4};
        //int[] nums = {3,4,5,1,2};
        //int[] nums = {1};
        BSearchRotatedArray obj = new BSearchRotatedArray();
        int target = 32;
        int idx = obj.search(nums, target);
        System.out.println("idx:"+ idx);
    }
    
    
    public int search(int[] nums, int target) {
        if(nums == null || nums.length == 0) {
            return -1;
        }
        long minFirstArray = nums[0];
        boolean targetInFirstArray = true;
        if(nums[nums.length -1] > nums[0]) {
            if(target > nums[nums.length -1] || target < nums[0]) {
                return -1;
            } 
            minFirstArray = Integer.MAX_VALUE + 1;
            
        }
        else if(target < minFirstArray) {
            targetInFirstArray = false;
        }
        int s = 0;
        int e = nums.length - 1;
        int mid; 
        while(e>=s) {
            mid = s + ((e - s) / 2);
            if(nums[mid] == target) {
                return mid;
            } 
            if((targetInFirstArray && (nums[mid] >= minFirstArray))
                // mid and target are in first array. So usual bin search.
                ||
                // mid and target are in second array, so usual bin search.
                (!targetInFirstArray && (nums[mid] < minFirstArray))) {
                if(target > nums[mid]) {
                    s = mid + 1;
                } else {
                    e = mid - 1;
                }
            } else if (targetInFirstArray && (nums[mid] < minFirstArray)) {
                //We have reached second array but target is in first.
                e = mid -1;
                
            } else if (!targetInFirstArray && (nums[mid] >= minFirstArray)) {
               // We have reached first array but target is in second.
                s = mid + 1;
                
            } else {
                return -1;
            }
        }
        
        return -1;
        
    }

}
