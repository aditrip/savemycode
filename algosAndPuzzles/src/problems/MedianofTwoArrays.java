package problems;

public class MedianofTwoArrays {

    public static void main(String[] args) {
        int[] a = { 1,2,4};
        int[] b = {3};

        System.out.println(findMedianSortedArrays(a, b));

    }

    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {

        int m = nums1.length;
        int n = nums2.length;
        boolean even = (m + n) % 2 == 0 ? true : false;
        int lowerMedianCount = (m + n) / 2;
        long lowerMedianNumber = Long.MIN_VALUE;
        long higherMedianNumber = Long.MIN_VALUE;
        boolean found = false;
        
        if(!even) {
            lowerMedianCount++;
        } 

        int i = 0;
        int j = 0;
        int count = 0;
        while (i < m && j < n) {
            if (nums1[i] <= nums2[j]) {
                lowerMedianNumber = nums1[i];
                i++;
            } else {
                lowerMedianNumber = nums2[j];
                j++;
            }
            count++;
            if (count == lowerMedianCount) {
                found = true;
                break;
            }
        }

        if (found & !even) {
            return lowerMedianNumber;
        }

        if (found) {
            if (i < m && j < n) {
                higherMedianNumber =
                    (nums1[i] <= nums2[j]) ? nums1[i] : nums2[j];
            } else if (i < m) {
                higherMedianNumber = nums1[i];
            } else  {
                higherMedianNumber = nums2[j];
            }

            return (lowerMedianNumber + higherMedianNumber) / 2.0f;
        }

        // One Array finished and we have not reached the median yet.
        int remCount = lowerMedianCount - count;
        if(remCount != 0) {
            remCount--;
        }
        if (i < m) {
            lowerMedianNumber = nums1[i + remCount];
            if (even) {
                higherMedianNumber = nums1[i + remCount + 1];
            }

        } else  {
            lowerMedianNumber = nums2[j + remCount];
            if (even) {
                higherMedianNumber = nums2[j + remCount + 1];
            }
        }

        if(even) {
        return (lowerMedianNumber + higherMedianNumber) / 2.0f;
        } else {
            return lowerMedianNumber;
        }

    }

}
