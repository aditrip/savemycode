package adi.leetcode;

import java.util.ArrayList;
import java.util.List;

/*
 * Build a balanced BST from sorted array.
 */
public class BSTFromArray {

    public static void main(String[] args) {
        /*Integer[] keys = { 20, 28, 32, 35, 38, 40, 42, 48, 50, 58, 60, 62, 64,
                66, 70, 74, 76, 77, 78 };*/
        Integer[] keys = { 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
        BSTFromArray obj = new BSTFromArray();
        Node<Integer> root = obj.buildBST(keys);
        try {
            System.out.println(obj.validateBST(root));
        } catch (IllegalArgumentException ile) {
            System.out.println("INVALID BST" + ile.getMessage());
        }
        List<Node<Integer>> parents = new ArrayList<Node<Integer>>();
        parents.add(root);
        obj.printBST(parents, 1, 1);
    }

    public <T extends Comparable<T>> Node<T> buildBST(T[] keys) {
        if (keys == null || keys.length == 0) {
            return null;
        }
        Node<T> root = new Node<T>();
        int rootIdx = (keys.length - 1) / 2;
        root.key = keys[rootIdx];
        if (keys.length == 1) {
            return root;
        }
        buildBST(keys, 0, rootIdx - 1, root, true);
        buildBST(keys, rootIdx + 1, keys.length - 1, root, false);
        return root;
    }

    /*
     * The caller validates the passed parameters
     */
    private <T extends Comparable<T>> void buildBST(T[] keys, int startIdx,
                                                    int endIdx, Node<T> parent,
                                                    boolean left) {
        /*System.out.println("parent:" + parent + " start:" + startIdx
                + " endIdx:" + endIdx);*/
        if (startIdx > endIdx) {
            return;
        }
        int keyIdx = (startIdx + endIdx) / 2;
        Node<T> child = new Node<T>();
        child.key = keys[keyIdx];
        if (left) {
            parent.left = child;
            //System.out.println("Inserted:" + child + " as left of:" + parent);
        } else {
            parent.right = child;
            //System.out.println("Inserted:" + child + " as right of:" + parent);
        }
        buildBST(keys, startIdx, keyIdx - 1, child, true);
        buildBST(keys, keyIdx + 1, endIdx, child, false);
    }

    private <T extends Comparable<T>> int validateBST(Node<T> n) {
        if (n == null) {
            return 0;
        }
        if ((n.left == null && n.right == null)) {
            return 1;
        }

        if (n.left != null) {
            if (n.left.key.compareTo(n.key) > 0) {
                throw new IllegalArgumentException("Node:" + n + "is invalid");
            }
        }
        if (n.right != null) {
            if (n.right.key.compareTo(n.key) < 0) {
                throw new IllegalArgumentException("Node:" + n + "is invalid");
            }
        }
        return 1 + Math.max(validateBST(n.left), validateBST(n.right));
    }

    /**
     * 
     * @param parents - List of parent nodes including nulls for absent ones.
     * @param level - current level which is printing.
     * @param nodes - no of nodes at this level.
     */
    public <T extends Comparable<T>> void printBST(List<Node<T>> parents,
                                                   int level, int nodes) {
        if (parents == null || nodes == 0) {
            return;
        }
        nodes = 0;
        List<Node<T>> children = new ArrayList<Node<T>>();
        System.out.print("level" + level);
        int spaces = (int) (160 / Math.pow(2, level));
        int odd = 0;
        for (Node<T> p : parents) {
            for (int i = 0; i < spaces; i++) {
                System.out.print(" ");
            }
            /* right node */
            if ((odd != 0 )) {
                for (int i = 0; i < spaces; i++) {
                    System.out.print(" ");
                }
            }
            if (p != null) {
                children.add(p.left);
                children.add(p.right);
                if (p.left != null) {
                    nodes++;
                }
                if (p.right != null) {
                    nodes++;
                }
                System.out.print(p.key);
            } else {
                System.out.print("  ");
            }
            odd++;
        }
        System.out.println("\n");
        printBST(children, level+1, nodes);
    }

    /* Node Class */
    class Node<T> {
        public Node() {
            // TODO Auto-generated constructor stub
        }

        public Node<T> left;
        public Node<T> right;
        public T key;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(key);
            if (left != null) {
                sb.append(" left:" + left.key);
            }
            if (right != null) {
                sb.append(" right:" + right.key);
            }
            return sb.toString();
        }
    }
}
