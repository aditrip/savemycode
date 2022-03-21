package problems;

public class IsSingleEditToSortedArray {

    public static void main(String[] args) {
        int[][] arrays = { { 4, 2, 3 }, { 4, 2, 1 }, { 3, 4, 2, 3 },
                { 1, 2, 3 }, { 1, 3, 2 }, { 2, 3, 3, 2, 4 }, { 1, 2, 4, 5, 3 },
                { 1, 2, 5, 3, 3 }, {1,3,4,2,5} };
        boolean[] expected = { true, false, false, true, true, true, true,
                true, true };

        boolean[] actual = new boolean[expected.length];

        for (int i = 0; i < actual.length; i++) {
             actual[i] = isSingleEdit(arrays[i]);
        }

        System.out.println("Expected : ");
        for (int i = 0; i < actual.length; i++)
            System.out.print(expected[i] + ", ");
        System.out.println("Actual   : ");
        for (int i = 0; i < actual.length; i++)
            System.out.print(actual[i] + ", ");
        System.out.println();

        int[] checkThisNums = {1,3,4,2,5};
        System.out.println(isSingleEdit(checkThisNums));

    }

    public static boolean isSingleEdit(int[] nums) {

        /* Empty array is non editable since there is nothing to edit */

        if (nums == null || nums.length == 0) {
            return false;
        }

        /* A Single pair is always fixable in one mutation */
        if (nums.length <= 2) {
            return true;
        }

        /* number of violating pairs */
        int editPairs = 0;
        /* Violation pair. A violation is between two numbers */
        int first= -1;
        int second = -1;
        
        /* Which index was edited in the pair */
        int editedIndex = -1;
        
        /* Go through all pairs */
        for (int i = 0; i < nums.length -1 ; i++) {
            first = i;
            second = i + 1;
           
            /*
             * A pair violates if first > second
             * A violating pair is fixable if:
             * a) 
             */

                if (nums[first] > nums[second]) {
                    editPairs++;
                    if (editPairs > 1) {
                        return false;
                    }
                    
                    if(!pairFixable(nums,first,second)) {
                        return false;
                    }
 
            }
        }

      return true;


    }
    
    /* Called only for the first violating pair
     * That is, array before pair is ascending sorted.
     * known facts: first > second
     *            : first >= first -1
     *              derived:
     *              first -1 > second
     *            
     *  
     *  Can first be reduced since it is greater than second?
     *  
     *  first can not be reduced if: 
     *        first == first - 1 (can't reduce further)
     *        or
     *        second < first -1 
     *  
     *  Can second be increased till first?
     *  second can not be increased if:
     *       second >= second + 1
     *       or
     *       first > second + 1 
     *         
     *        
     *  */
    
    private static boolean pairFixable(int[] nums, int first, int second) {
        
        
        /* Note: boundary pairs are always fixable */
        if (first == 0 || second == nums.length - 1) {
            return true;
        }
        
        /*
         * If first can be reduced, return true
         */
       if (!((nums[first] == nums[first - 1]) || (nums[second] < nums[first -1]))) {
           
           return true;
       }
       
       /*
        * If second can be increased return true
        */
       
       if(!((nums[second] >= nums[second +1])||(nums[first] > nums[second+1]))) {
           return true;
       }
        
       return false;
    }

}
