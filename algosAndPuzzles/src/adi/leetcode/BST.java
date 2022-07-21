package adi.leetcode;

import java.util.ArrayList;
import java.util.List;

import adi.leetcode.MergeInterval.INode;

public class BST<T extends Comparable<T>> {

    public BNode<T> root;

    public BNode<T> min() {
        return min(root);
    }

    public BNode<T> min(BNode<T> node) {
        if (node == null) {
            return null;
        }
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public BNode<T> max(BNode<T> node) {
        if (node == null) {
            return null;
        }
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    public BNode<T> succ(BNode<T> node) {
        if (node == null) {
            return null;
        }
        if (node.right != null) {
            return min(node.right);
        }
        BNode<T> parent = node.parent;
        while (parent != null && parent.right == node) {
            parent = parent.parent;
        }
        return parent;

    }

    public BNode<T> pred(BNode<T> node) {
        if (node == null) {
            return null;
        }
        if (node.left != null) {
            return max(node.left);
        }
        BNode<T> parent = node.parent;
        while (parent != null && parent.left == node) {
            parent = parent.parent;
        }

        return parent;
    }

    public void insert(T key, T val) {
        BNode<T> node = new BNode<T>(key, val);
        if (root == null) {
            root = node;
            return;
        }

        BNode<T> parent = root;

        while (parent != null) {
            if (key.compareTo(parent.key) > 0) {
                if (parent.right == null) {
                    parent.right = node;
                    node.parent = parent;
                }
                parent = parent.right;
            } else {
                if (parent.left == null) {
                    parent.left = node;
                    node.parent = parent;
                }
                parent = parent.left;
            }
        }
        updateAggVal(node);
    }

    /*
     * Override this method to use AggVal in some
     * other way. Default implementation is that
     * aggValue is the max of all values in it's
     * sub tree
     * 
     */
    protected void updateAggVal(BNode<T> node) {
        while (node.parent != null) {
            if (node.parent.aggVal.compareTo(node.aggVal) < 0) {
                node.parent.aggVal = node.aggVal;
            }
            node = node.parent;
        }
    }

    public void insertKeys(T[] randomKeys) {
        for (T key : randomKeys) {
            insert(key, null);
        }
    }

    /* 
     * Get the minimum node whose key is GTE fromKey.
     * That is basically finds the next successor GTE
     * fromKey in the sorted order of keys.
     * 
     * Goes the binary search way, starting from root.
     * Updates the nextMin to a new nextMin if:
     *  1. currNodeKey >= fromKey and
     *  2. currNodeKey < prevMin [That is max min less than fromKey]
     *  */
    public BNode<T> getNextMin(T fromKey, T max_val) {
        BNode<T> currNode = root;
        T nextMin = max_val;
        BNode<T> nextMinNode = null;

        while (currNode != null) {
            if (currNode.key.equals(fromKey)) {
                return currNode;
            }
            if (currNode.key.compareTo(fromKey) > 0) {
                if (currNode.key.compareTo(nextMin) < 0) {
                    nextMin = currNode.key;
                    nextMinNode = currNode;
                }
                currNode = currNode.left;
            } else {
                currNode = currNode.right;
            }
        }
        
        return nextMinNode;

    }

    /*
     * Uses sorted keys to build a balanced BST using
     * binary search approach.
     * Nodes have no value. Only key.
     */
    public BNode<T> buildBalancedBST(T[] sortedKeys) {
        if (sortedKeys == null || sortedKeys.length == 0) {
            return null;
        }
        BNode<T> root = new BNode<T>();

        int rootIdx = (sortedKeys.length - 1) / 2;
        root.key = sortedKeys[rootIdx];
        if (sortedKeys.length == 1) {
            return root;
        }
        buildBalancedBST(sortedKeys, 0, rootIdx - 1, root, true);
        buildBalancedBST(sortedKeys, rootIdx + 1, sortedKeys.length - 1, root,
                         false);
        return root;
    }

    /*
     * The caller validates the passed parameters
     */
    private void buildBalancedBST(T[] sortedKeys, int startIdx, int endIdx,
                                  BNode<T> parent, boolean left) {
        /*System.out.println("parent:" + parent + " start:" + startIdx
                + " endIdx:" + endIdx);*/
        if (startIdx > endIdx) {
            return;
        }
        int keyIdx = (startIdx + endIdx) / 2;
        BNode<T> child = new BNode<T>();
        child.key = sortedKeys[keyIdx];
        if (left) {
            parent.left = child;
            //System.out.println("Inserted:" + child + " as left of:" + parent);
        } else {
            parent.right = child;
            //System.out.println("Inserted:" + child + " as right of:" + parent);
        }
        child.parent = parent;
        buildBalancedBST(sortedKeys, startIdx, keyIdx - 1, child, true);
        buildBalancedBST(sortedKeys, keyIdx + 1, endIdx, child, false);
    }

    private <T extends Comparable<T>> int validateBST(BNode<T> n) {
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
    public <T extends Comparable<T>> void printBST(List<BNode<T>> parents,
                                                   int level, int nodes) {
        if (parents == null || nodes == 0) {
            return;
        }
        nodes = 0;
        List<BNode<T>> children = new ArrayList<BNode<T>>();
        System.out.print("level" + level);
        int spaces = (int) (160 / Math.pow(2, level));
        int odd = 0;
        for (BNode<T> p : parents) {
            for (int i = 0; i < spaces; i++) {
                System.out.print(" ");
            }
            /* right node */
            if ((odd != 0)) {
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
        printBST(children, level + 1, nodes);
    }

}
