package adi.leetcode;

import java.util.Arrays;

/*
 * https://leetcode.com/problems/kth-largest-element-in-an-array/
 * 215. Kth Largest Element in an Array
Medium

Given an integer array nums and an integer k, return the kth largest element in the array.

Note that it is the kth largest element in the sorted order, not the kth distinct element.

You must solve it in O(n) time complexity.
 */


/*
 * Notes:
 * Approach 1: Maintain a minHeap of size K. This minHeap contains the K largest element in the
 * array.
 * 
 * The method offer of minHeap, adds the element at the end, and then bubbles up the min
 * at the top.
 * 
 * Once K elements are added, new ones are added by removing the min. Only when new element
 * is greater than the min. Note that this is minHeap of largest elements.
 * 
 * Approach 1 Results:
 * Runtime: 45 ms, faster than 5.14% of Java online submissions for Kth Largest Element in an Array.
Memory Usage: 70.6 MB, less than 6.06% of Java online submissions for Kth Largest Element in an Array.

 * Approach 2:
 * Quick Select.
 * 
 *  select pivot. Partition elements. 
 * 
 */
public class KthLargest {

    public static void main(String[] args) {
        KthLargest obj = new KthLargest();
        int[] debug = {
                3,2,3,1,2,4,5,5,6,7,7,8,2,3,1,1,1,10,11,5,6,2,4,7,8,5,6
                        
        };
        int debug_k = 2;
        System.out.println(debug_k + " largest by quick select:" + obj.findKthLargest2(debug, debug_k));
        Arrays.sort(debug);
        System.out.println("Total Elements:" + debug.length + "->" + Arrays.toString(debug));
        
        int[] a = {
                56,3,9,7,12,15,1,3,9,41,39,47,2,0,34,16,28,3,43,0,3,9
        };
        
        
        int[] b = {
                100,90,80,70,60,50,45,42,41,40,35,33,32,31
        };
        
        int[] c = {
                1,2,3,4,5,6,7,8,9
        };
        
        int[] d = {
                2,2,2,2,1,1,1,0,0,9,99,0
        };
        
        int[] e = {
                0,56,99,22,2,12,22,1,11,12,0,10,9,0,2,10,56,10,99,10,56,0,11,56,12,56
        };
        
        int[] f = {23,20};

        int k = 2;
        System.out.println(k + " largest:" + obj.findKthLargest(a, k));
        System.out.println(k + " largest by quick select:" + obj.findKthLargest2(a, k));
        Arrays.sort(a);
        System.out.println("Total Elements:" + a.length + "->" + Arrays.toString(a));
        
        System.out.println(k + " largest:" + obj.findKthLargest(b, k));
        System.out.println(k + " largest by quick select:" + obj.findKthLargest2(b, k));
        
        System.out.println(k + " largest:" + obj.findKthLargest(c, k));
        System.out.println(k + " largest by quick select:" + obj.findKthLargest2(c, k));
        
        System.out.println(k + " largest:" + obj.findKthLargest(d, k));
        System.out.println(k + " largest by quick select:" + obj.findKthLargest2(d, k));
        
        System.out.println(k + " largest:" + obj.findKthLargest(e, k));
        System.out.println(k + " largest by quick select:" + obj.findKthLargest2(e, k));
        
        System.out.println(k + " largest:" + obj.findKthLargest(f, k));
        System.out.println(k + " largest by quick select:" + obj.findKthLargest2(f, k));
        
        System.out.println(k + " largest:" + obj.findKthLargest(f, k));
        System.out.println(k + " largest by quick select:" + obj.findKthLargest2(f, k));
        
        
        System.out.println("Total Elements:" + a.length + "->" + Arrays.toString(a));
        
        obj.insertionSort(a);
        System.out.println("Total Elements:" + a.length + "->" + Arrays.toString(a));
        obj.insertionSort(b);
        System.out.println("Total Elements:" + b.length + "->" + Arrays.toString(b));
        obj.insertionSort(c);
        System.out.println("Total Elements:" + c.length + "->" + Arrays.toString(c));
        obj.insertionSort(d);
        System.out.println("Total Elements:" + d.length + "->" + Arrays.toString(d));
        obj.insertionSort(e);
        System.out.println("Total Elements:" + e.length + "->" + Arrays.toString(e));
        obj.insertionSort(f);
        System.out.println("Total Elements:" + e.length + "->" + Arrays.toString(f));


    }
    
    /*
     * a.length - pivotIdx = k;
     * or find pivotIdx = a.length - k;
     * 
     * 
     */
    public int findKthLargest2(int[] nums, int k) {
        if (nums.length < 10) {
            insertionSort(nums);
            return nums[nums.length - k];
        }
        if (k == 1) {
            return findMax(nums)[0];
        }
        if (k == 2) {
            return findMax(nums)[1];
        }
        
        if (k == nums.length) {
            return findMin(nums)[0];
        }
        if (k == nums.length - 1) {
            return findMin(nums)[1];
        }
        
        int keyIdx = nums.length - k;
        int s = 0;
        int e = nums.length - 1;
        
        int pivotIdx = -1;
        
        while(pivotIdx != keyIdx) {
            pivotIdx = partition(nums, s, e);
            if (pivotIdx == keyIdx) {
                break;
            }
            if (pivotIdx < keyIdx) {
                s = pivotIdx + 1;
            } else {
                e = pivotIdx - 1;
            }
        }
        
        return nums[pivotIdx];
              
    }
    /*
     * return partition idx.
     */
    public int partition(int[] a, int s, int e) {
       
        /*
         * TODO: This should not be called for less than 3 elements.
         * So these initial few lines are reduntant.
         * Use insertion sort for less than 10 elements.
         */
        if ( e == s) {
            return e;
        }
        if ( (e - s) == 1) {
            if (a[e] < a[s]) {
                swap(a,e,s);
            }
            return e;
        }
        int p = placePivot(a, s, e);
        /*
         * pivot element is at e - 1.
         * s < pivot < e.
         */
        int pivot=a[p];
        int i =s;
        int j = e - 1;
        /*
         * Note that it stops at equal to pivot element.
         * And the way s,e and pivot are placed, this
         * is guranteed not to fall off the edges.
         */
        while(true) {
            while(a[++i] < pivot);
            while(a[--j] > pivot);
            if (i < j) {
                swap(a,i,j);
            } else {
                break;
            }
        }
        
        /* swap pivot with i */
        
        swap(a,i, e-1);
        return i;
        
        
    }
    
    int placePivot(int[] a, int s , int e) {
        
        int mid = (s + e) >>> 1;
        
        if (a[s] > a[mid]) {
            swap(a, s, mid);
        }
        if (a[e] < a[s]) {
            swap(a, e, s);
        }
        if (a[mid] > a[e]) {
            swap(a,e,mid);
        }
        
        swap(a, mid, e -1);
        
        return e - 1;
    }
    
    public void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }
    
    
    public void insertionSort(int[] a) {
        if (a.length <= 1) {
            return;
        }
        
        int l = a.length;
        int minIdx = 0;
        for(int i=0; i<l; i++) {
            minIdx = i;
            for (int j=i; j<l; j++) {
                if (a[j] < a[minIdx]) {
                    minIdx = j;
                }
            }
            swap(a, i, minIdx);
        }
        
    }
    
    int[] findMax(int[] a) {
        int[] maxs = new int[2];
        
        if (a[0] < a[1]) {
            swap(a,0,1);
        }
        int max1 = a[0];
        int max2 = a[1];
        
        for (int i=2; i<a.length; i++) {
            if (a[i] > max1) {
                max2 = max1;
                max1 = a[i];
            }else if (a[i] > max2) {
                max2 = a[i];
            }  
        }
        
        maxs[0] = max1;
        maxs[1] = max2;
        
        return maxs;
    }
    
    int[] findMin(int[] a) {
        int[] mins = new int[2];
        
        if (a[0] > a[1]) {
            swap(a,0,1);
        }
        int min1 = a[0];
        int min2 = a[1];
        
        for (int i=2; i<a.length; i++) {
            if (a[i] < min1) {
                min2 = min1;
                min1 = a[i];
            } else if (a[i] < min2) {
                min2 = a[i];
            } 
        }
        
        mins[0] = min1;
        mins[1] = min2;
        
        return mins;
    }
    
    public int findKthLargest(int[] nums, int k) {
        MinHeap pq = new MinHeap(k);
        for (int num: nums) {
            pq.add(num);
        }
        
        return pq.peek();
    }
    
    static class MinHeap {
        int max = Integer.MIN_VALUE;
        int capacity;
        int size = 0;
        int[] arr;
        
        public MinHeap(int k) {
            capacity = k;
            arr = new int[k + 1];
        }
        
        public void add(int n) {
            size++;
            if (size > capacity) {
                if (n > peek()) {
                    poll(n);
                }
                return;
            }
            offer(n);
            
        }
        
        int poll(int newVal) {
            int oldVal = arr[1];
            arr[1] = newVal;
            minHeapify(1);
            return oldVal;
        }
        
        int peek() {
            return arr[1];
        }
        
        void minHeapify(int pIdx) {
            if (pIdx > capacity/2) {
                return;
            }
            
            if(pIdx < 1) {
                return;
            }
           
            int num = arr[pIdx];
            int minChildIdx;
            while(pIdx != -1) {
                minChildIdx = getSmallerChildIndex(pIdx);
                if(minChildIdx == -1) {
                    return;
                }
                if (arr[minChildIdx] < num) {
                    arr[pIdx] = arr[minChildIdx];
                    pIdx = minChildIdx;
                    arr[pIdx] = num;
                } else {
                    return;
                }
            }
        }
        
        int getSmallerChildIndex(int parentIdx) {
            if (parentIdx > capacity/2) {
                return - 1;
            }
            
            if (parentIdx == 0) {
                return -1;
            }
            if (left(parentIdx) == -1 && right(parentIdx) == -1) {
                return -1;
            }
            if (right(parentIdx) == -1) {
                return left(parentIdx);
            }
            
            if(arr[right(parentIdx)] <= arr[left(parentIdx)]) {
                return right(parentIdx);
            } else {
                return left(parentIdx);
            }
        }
        int parent(int idx) {
            return idx/2;
        }
        
        int left(int idx) {
            return 2*idx > capacity ? -1 : 2*idx;
        }
        
        int right(int idx) {
            return (2*idx + 1) > capacity ? -1 : (2*idx + 1);
        }
        
        void offer(int n) {
            int idx = size;
            arr[idx] = n;
            int p;
            while(idx != 1) {
                p = parent(idx);
                if (arr[p] > n) {
                    arr[idx] = arr[p];
                    arr[p] = n;
                }
                idx = p;
            }
        }
    }

}
