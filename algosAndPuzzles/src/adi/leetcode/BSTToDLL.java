package adi.leetcode;

public class BSTToDLL {

    
 public Node treeToDoublyList(Node n) {
        
        if (n == null) {
            return null;
        }

        /* Case 4. leaf node - make a circular DLL of a single node. */
        if (n.left == null && n.right == null) {
            n.left = n;
            n.right = n;
            return n;
        }

        /* Not a leaf node. Assume the child node is already a DLL. */
        Node leftListHead = null;
        Node rightListHead = null;

        if (n.left != null) {
            leftListHead = treeToDoublyList(n.left);
        }

        if (n.right != null) {
            rightListHead = treeToDoublyList(n.right);
            /* Now handle the case no 1-3. */

            if (leftListHead != null) {
                /* Case 3: Both left and right sublist exists */
                /* Connect to Node n.
                 * 1. left sublist's tail [leftListHead.left is tail] forward ptr to Node n.
                 * 2. Node n's backward ptr to tail of leftList.
                 * 3. right sublist's head left ptr to Node n.
                 * 4. Node n's forward ptr to rightSubList head.
                 * Head is leftListHead.
                 *  */
                Node tail = rightListHead.left;
                leftListHead.left.right = n;
                n.left = leftListHead.left;
                rightListHead.left = n;
                n.right = rightListHead;

                /*Node n connected, now create circular link. */
                leftListHead.left = tail;
                /* Connect tail to head */
                tail.right = leftListHead;

            } else {
                /* Case 2. No left subList, head is this node n.
                 * First connect Node n */
                Node tail = rightListHead.left;
                /* Connect n - fwd and backward */
                n.right = rightListHead;
                rightListHead.left = n;
                 /* create circular link*/
                n.left = tail;
                tail.right = n;
            }
        } else {
            /* Case 1. no right sublist. 
             * Connect Node n,
             * Make left sublist circular with Node n */
            Node tail = leftListHead.left;
            tail.right = n;
            n.left = tail;
            /* n is the new tail now */
            leftListHead.left = n;
            n.right = leftListHead;
        }

        return leftListHead != null ? leftListHead : n;
    
        
    }
 
   static class Node {
       public int val;
       public Node left;
       public Node right;

       public Node() {}

       public Node(int _val) {
           val = _val;
       }

       public Node(int _val,Node _left,Node _right) {
           val = _val;
           left = _left;
           right = _right;
       }
   };
}
