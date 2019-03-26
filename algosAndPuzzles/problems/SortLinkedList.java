package com.adi.problems;

import java.util.concurrent.atomic.AtomicInteger;

public class SortLinkedList {

    static LinkedList<Integer> l = new LinkedList<Integer>();
    public static void main(String[] args) {
        l.add(4);
        l.add(9);
        l.add(14);
        l.add(3);
        l.add(2);
        l.add(6);
        l.add(7);
        
        System.out.println(l);
        quickSort(l.head, l.tail);
        System.out.println("Sorted List:\n" + l);

    }
    

    public static <T extends Comparable<T>> void quickSort(
                                                           Node<T> start,
                                                           Node<T> end) {

        if (start == null || end == null) {
            return;
        }
        /* one element is already sorted */
        if (start.index >= end.index || start == end)
            return;
        /* find a pivot, sample 3 items if available. */
        Node<T> pivot = start;
        if ((end.index - start.index) >= 2) {
            /* check if pivot is next or next.next */
            if (pivot.next.compareTo(pivot) > 0) {
                if (pivot.next.next.compareTo(pivot.next) > 0) {
                    pivot = pivot.next;
                }
            }
            else if (pivot.compareTo(pivot.next.next) > 0 || pivot.next.compareTo(
                                                                             pivot.next.next) > 0) {
                pivot = pivot.next.next;

            }
        }

        System.out.println("pivot found:" + pivot + " and linked list now is: \n" + l);

        /* Move pivot at the start, and start scanning from start + 1 */
        if (pivot != start) {
            System.out.println("swapped pivot");
            swapValues(start, pivot);
            pivot = start;
            System.out.println(l);
        }

        /*
         * Start J pointer from end looking for smaller than pivot, and start I
         * pointer from start looking for larger elements. swap I and J when
         * found. This moves smaller elements towards start and larger elements
         * towards end. don't let I and J cross, as that would mean there are
         * no more elements which can be swapped.
         * 
         * Now one of I or J would have crossed and reached till end. If J
         * holds it's position that means beyond J all elements are larger, and
         * J is at a smaller element. swap pivot and J. partition at J.
         * 
         * if I holds it's position, I is at larger element and all elements
         * before I are smaller.Can't swap with pivot now. swap I-1 an pivot,
         * partition at I-1.
         * 
         * if I and J are at same position. If this position's element is less
         * than pivot, swap I and pivot and partition at I. If this position is
         * greater than pivot, swap pivot and I-1 and partition at I-1
         * 
         */

        Node<T> I = start;
        Node<T> J = end;

        while (I != null && J != null && I.index < J.index) {

            while (I.index < J.index) {
                if (I.compareTo(pivot) > 0) {
                    break;
                }
                I = I.next;
            }

            while (J.index > I.index) {
                if (J.compareTo(pivot) < 0) {
                    break;
                }
                J = J.prev;
            }

            if (I != null && J != null && (I.index < J.index)) {
                swapValues(I, J);
                System.out.println("Elements swapped: " + l);

            }
        }

        /*
         * Now Partition. 
         * The above loop should exit at I==J,
         * because as soon as these pointers become equal, all loop exits.
         * 
         */
        System.out.println("assert I==J is:" + (I == J));
        if (I == null && J != null) {
            swapValues(pivot, J);
            System.out.println("Elements swapped when J holds: " + l);
            partition(J, start, end);
        } else if (J == null && I != null) {
            swapValues(I.prev, pivot);
            System.out.println("Elements swapped when I holds: " + l);
            partition(I.prev, start, end);
        } else if (I != null && J != null) {
            /*
             * This is possible only when I=J. assert the same.
             */
            assert I == J;

            if (I == J) {
                if (I.compareTo(pivot) < 0) {
                    swapValues(I, pivot);
                    System.out.println("Elements swapped when I==J: " + l);
                    partition(I, start, end);
                } else {
                    swapValues(I.prev, pivot);
                    System.out.println("Elements swapped when I==J: " + l);
                    partition(I.prev, start, end);

                }
            }
        }

    }

    public static <T extends Comparable<T>> void partition(
                                                           Node<T> p,
                                                           Node<T> start,
                                                           Node<T> end) {

        quickSort(start, p.prev);
        quickSort(p.next, end);

    }

    public static <T extends Comparable<T>> void swapValues(
                                                            Node<T> I,
                                                            Node<T> J) {

        T temp;
        temp = I.val;
        I.val = J.val;
        J.val = temp;

    }

    /*
     * swap nodes I and J.
     * Doesn't work for adjacent elements.
     * Need to see for other cases and correct it.
     */
    public static <T extends Comparable<T>> void swapNodes(
                                                           Node<T> I,
                                                           Node<T> J) {

        Node<T> temp = I.clone();
        if(I.prev != null) {
            I.prev.next = J;
        }
        if(I.next != null) {
            I.next.prev = J;
        }
        I.prev = J.prev;
        I.next = J.next;
       
        
        I.index = J.index;
        I.val = J.val;

        if(J.prev != null) {
            J.prev.next = I;
        }
        if(J.next != null) {
            J.next.prev = J;
        }
        J.prev = temp.prev;
        J.next = temp.next;
        
        J.index = temp.index;
        J.val = temp.val;


    }

    static class LinkedList<T extends Comparable<T>> {
        public Node<T> head = null;
        public Node<T> tail = null;
        public volatile int size;
        public AtomicInteger index = new AtomicInteger();

        public synchronized void add(Node<T> n) {
            if (head == null) {
                head = n;
                tail = n;
                size = 1;
            } else {
                int i = index.incrementAndGet();
                n.index = i;
                n.prev = tail;
                tail.next = n;
                tail = n;
                size++;
                
            }
        }
        
        public synchronized void add(T val) {
            Node<T> n = new Node<T>(val);
            add(n);
        }

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer(head.toString());
            Node<T> n = head.next;
            do {
                sb.append(" " + n.toString());
                n = n.next;
            } while (n!= null);

            return sb.toString();

        }

        // Don't need remove for now.

    }

    static class Node<T extends Comparable<T>> implements Comparable<Node<T>> {

        public T val;
        public Node<T> next;
        public Node<T> prev;
        public int index;

        public Node(T val) {
            this.val = val;
        }
        @Override
        public int compareTo(Node<T> o) {
            if (o == null)
                throw new NullPointerException();
            return val.compareTo(o.val);
        }

        @Override
        public String toString() {
            return val.toString();
        }

        @Override
        public Node<T> clone() {
            try {
                return (Node<T>) super.clone();
            } catch (CloneNotSupportedException e) {
                //System.out.println(e);
                Node<T> n = new Node<T>(this.val);
                n.prev = this.prev;
                n.next = this.next;
                n.val = this.val;
                n.index = this.index;
                return n;
            }
        }

    }

}
