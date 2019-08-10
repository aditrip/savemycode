package problems;

public class MedianTwoSortedArrays {

    public static void main(String[] args) {
        int[] a1 = { 3, 16, 23, 35 };
        int[] a2 = { 9, 17, 18, 25, 27, 29 };
        
        MedianTwoSortedArrays obj = new MedianTwoSortedArrays();
        
        double m = obj.findMedianSortedArrays(a1,a2);
        System.out.println(m);

    }

    public double findMedianSortedArrays(int[] a1, int[] a2) {

        int l1 = a1.length;
        int l2 = a2.length;
        boolean even = (l1 + l2) % 2 == 0 ? true : false;
        int firstMedianIdx = (l1 + l2 -1) / 2;
        int s1 = 0; // start index
        int e1 = a1.length - 1; // end index
        int s2 = 0;
        int e2 = a2.length - 1;
        int m1, m2; // median index

        while (true) {

            m1 = (e1 - s1) / 2;
            m2 = (e2 - s2) / 2;

            if (a1[m1] == a2[m2]) {
                double median = getMedian(a1, a2, even, m1, m2,
                                          firstMedianIdx,true);
                if (median >= 0) {
                    return median;
                } else if (m1 + m2 + 1 < firstMedianIdx) {
                    s1 = m1 + 1;
                    s2 = m2 + 1;
                } else {
                    e1 = m1 - 1;
                    e2 = m2 - 1;
                }

            } else if (a1[m1] > a2[m2]) {
                int idx2 = bSearchEqualOrLess(a2,s2,e2, a1[m1]);
                double median = getMedian(a1, a2, even, m1, idx2,
                                          firstMedianIdx,true);
                if(median >= 0) {
                    return median;
                }
                
                e1 = m1 -1;
                s2 = m2 + 1;
                

            } else {
                int idx1 = bSearchEqualOrLess(a1,s1,e1, a2[m2]);
                double median = getMedian(a1, a2, even, idx1, m2,
                                          firstMedianIdx,false);
                if(median >= 0) {
                    return median;
                }
                
                e2 = m2 -1;
                s1 = m1 + 1;
            }

        }

    }

    /* returns index of the element which is either equal to 
     * elem or less than elem;
     */
    private int bSearchEqualOrLess(int[] a, int s, int e, int elem) {
       int m;
       
       if( s == e) {
           if(a[e] < elem) {
               return e;
           }
       }
       while( s>=0 && e< a.length) {
           m = (s + e)/2;
           if(a[m] == elem) {
               return m;
           } else if ( a[m] > elem) {
               e = m -1;
               if(a[e] < elem) {
                   return e;
               }
           } else {
               s = m + 1;
               if(a[s] > elem) {
                   return s - 1;
               }
           }
           
           
       }
       
       return -1;
       
    }

    private double getMedian(
                             int[] a1,
                             int[] a2,
                             boolean even,
                             int m1,
                             int m2,
                             int firstMedianIdx,
                             boolean firstGreater) {
        if (m1 + m2 + 1 == firstMedianIdx) {
            if (even) {
                int n = findSucc(a1, a2, m1, m2, firstGreater);
                return (a1[m1] + n) / 2;
            }
            return a1[m1];

        }
        return -1;

    }

    /*
     * Find the predecessor of the given elem.
     * if firstGreater is true, elem is a1[m1] or vice versa.
     * Note that this predecessor could be a2[m2] itself.
     * 
     */
    private int findPrev(int[] a1, int[] a2, int m1, int m2, boolean firstGreater) {
        int elem1;
        int elem2;

    
        if (firstGreater) {
           elem1 = a2[m2];
           elem2 = a1[m1-1];
        } else {
            elem1 = a1[m1];
            elem2 = a2[m2-1];
        }
        
        int elem = (elem1 > elem2) ?  elem1:  elem2;
        return elem;
    }

}
