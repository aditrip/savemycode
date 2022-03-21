package adi.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TreeUtils {
    /* Build the n-ary Tree from it's serialized format, i.e int[].
     * In the serialization format, children of different parents are
     * seperated by null. 
     *                         1
     *               2     3        4     5
     *               N   5 6  7   8   9
     *                   N 10 N   11
     *                     
     * will be [1, null,2,3,4,5,null,null,5,6,7,null,8,9,null,null,null,10,null,null,11]
     * 
     * */
    public static Node deserRoot(Integer[] serRoot) {
        if (serRoot[0] == null) {
            return null;
        }
        Node root = new Node();
        root.key = serRoot[0];
        Integer serVal = null;
        List<Node> allParents = new ArrayList<Node>();
        List<Node> allChildren = new ArrayList<Node>();
        List<Node> nodeChildren = new ArrayList<Node>();
        allParents.add(root);
        if (serRoot.length < 3) {
            return root;
        }
        int parentIdx = 0;

        /* start from children of root, i.e i=2 */
        for (int i = 2; i < serRoot.length; i++) {
            serVal = serRoot[i];
            while (serVal != null) {
                Node child = new Node();
                child.key = serVal;
                nodeChildren.add(child);
                i++;
                if (i < serRoot.length) {
                    serVal = serRoot[i];
                } else {
                    break;
                }
            }
            if (!nodeChildren.isEmpty()) {
                allChildren.addAll(nodeChildren);
                allParents.get(parentIdx).children = nodeChildren;
                nodeChildren = new ArrayList<Node>();
            }
            parentIdx++;
            if (parentIdx >= allParents.size()) {
                parentIdx = 0;
                allParents = allChildren;
                allChildren = new ArrayList<Node>();
            }
        }

        return root;
    }

    public static List<Integer> serRoot(Node root) {
        List<Node> parents = new ArrayList<Node>();
        List<Node> childLevel = new ArrayList<Node>();
        if (root == null) {
            return null;
        } else if (root.children == null || root.children.isEmpty()) {
            return Arrays.asList(new Integer[] { root.key, null });
        }
        List<Integer> serRoot = new ArrayList<Integer>();
        /* serialize root */
        serRoot.add(root.key);
        serRoot.add(null);
        parents.add(root);

        /* serialize the tree below root */
        while (!parents.isEmpty()) {
            for (Node p : parents) {
                if (p.children != null && !p.children.isEmpty()) {
                    childLevel.addAll(p.children);
                    for (Node child : p.children) {
                        serRoot.add(child.key);
                    }
                }
                /* parent changing */
                serRoot.add(null);
            }
            /* level changing */
            parents = childLevel;
            childLevel = new ArrayList<Node>();
        }
        return serRoot;
    }

    /* Build the binary Tree from it's serialized format, i.e int[].
     * In the serialization format, every null node is represented by
     * null. parent change is encoded by a null.
     *                     58
     *             38                70
     *      28            42       62       76
     *            32   40        60    64       78
     *               35       50             77  
     *                     
     * will be [58, null,38,70,null,28,42,null,62,76,null,null,32,null,40,null,null,60,64,null,null,78,
     * null,null,35,null,null,null,null,50,null,null,null,null,null,77]
     * 
     * */
    public static <T> BNode<T> deserBRoot(T[] serRoot) {
        BNode<T> root = null;
        BNode<T> parent = null;
        BNode<T> node = null;
        List<BNode<T>> allParents = new ArrayList<BNode<T>>();
        List<BNode<T>> allChildren = new ArrayList<BNode<T>>();
        int parentIdx = 0;

        /* start from root, i.e i=0 */
        int i = 0;
        while (i < serRoot.length) {
            T key = serRoot[i++];
            node = key != null ? new BNode<T>(key) : null;
            if (i == 1) {
                if (node == null) {
                    return null;
                }
                root = node;
                allParents.add(root);
                i++; // move to left child;
                continue;
            }
            parent = allParents.get(parentIdx++);
            /* Add children for this parent */
            for (int c = 0; c < 2; c++) {
                if (c == 0) {
                    parent.left = node;
                    System.out.println("left node:" + node + "  added to:" + parent + " at index:" + i);
                    /* Move to right child */
                    if (i >= serRoot.length) {
                        break;
                    }
                    key = serRoot[i];
                    System.out.println(" right key:" + key + " at index:" + i);
                } else {
                    parent.right = node;
                    System.out.println("right node:" + node + "  added to:" + parent);
                }
                if (node != null) {
                    allChildren.add(node);
                }
                if (c == 0) {
                    /* Get right node */
                    node = key != null ? new BNode<T>(key) : null;
                }
            }
            /* parent changing */
            System.out.println("parents:" + allParents);
            System.out.println("children:" + allChildren);
            if (++i >= serRoot.length) {
                break;
            }
            key = serRoot[i++];
            if (key != null) {
                throw new IllegalArgumentException(" null expected at index:"
                        + i + " instead of element: " + key);
            }

            if (parentIdx >= allParents.size()) {
                parentIdx = 0;
                allParents = allChildren;
                allChildren = new ArrayList<BNode<T>>();
            }
        }
        return root;
    }

    public static <T> List<T> serBRoot(BNode<T> root) {
        List<BNode<T>> parents = new ArrayList<BNode<T>>();
        List<BNode<T>> childLevel = new ArrayList<BNode<T>>();
        List<T> serRoot = new ArrayList<T>();
        if (root == null) {
            return null;
        } else if (root.right == null && root.left == null) {
            serRoot.add(root.key);
            serRoot.add(null);
            return serRoot;
        }

        /* serialize root */
        serRoot.add(root.key);
        serRoot.add(null);
        parents.add(root);

        /* serialize the tree below root */
        while (!parents.isEmpty()) {
            for (BNode<T> p : parents) {
                if (p.left != null) {
                    childLevel.add(p.left);
                    serRoot.add(p.left.key);
                } else {
                    serRoot.add(null);
                }
                if (p.right != null) {
                    childLevel.add(p.right);
                    serRoot.add(p.right.key);
                } else {
                    serRoot.add(null);
                }
                /* parent changing */
                serRoot.add(null);
            }
            /* level changing */
            //serRoot.add(null);
            parents = childLevel;
            childLevel = new ArrayList<BNode<T>>();
        }
        return serRoot;
    }

    /**
     * Build a balanced BST from sorted keys array.
     * @param sortedKeys
     * @return
     */
    public <T extends Comparable<T>> BNode<T>
            buildBalancedBST(T[] sortedKeys) {
        if (sortedKeys == null || sortedKeys.length == 0) {
            return null;
        }
        BNode<T> root = new BNode<T>();
        int rootIdx = (sortedKeys.length - 1) / 2;
        root.key = sortedKeys[rootIdx];
        if (sortedKeys.length == 1) {
            return root;
        }
        buildBST(sortedKeys, 0, rootIdx - 1, root, true);
        buildBST(sortedKeys, rootIdx + 1, sortedKeys.length - 1, root, false);
        return root;
    }

    /*
     * The caller validates the passed parameters
     */
    public static <T extends Comparable<T>> void
            buildBST(T[] sortedKeys, int startIdx, int endIdx, BNode<T> parent,
                     boolean left) {
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
        buildBST(sortedKeys, startIdx, keyIdx - 1, child, true);
        buildBST(sortedKeys, keyIdx + 1, endIdx, child, false);
    }

    public static <T extends Comparable<T>> int validateBST(BNode<T> n) {
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

    public static <T extends Comparable<T>> void printBST(BNode<T> root) {
        List<BNode<T>> parents = new ArrayList<BNode<T>>();
        parents.add(root);
        printBST(parents, 1, 1);
    }

    /**
     * 
     * @param parents - List of parent nodes including nulls for absent ones.
     * @param level - current level which is printing.
     * @param nodes - no of nodes at this level.
     */
    private static <T extends Comparable<T>> void
            printBST(List<BNode<T>> parents, int level, int nodes) {
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

    static class Node {
        public int key;
        public List<Node> children;

        public Node() {
        }

        public Node(int _val) {
            key = _val;
        }

        public Node(int _val, List<Node> _children) {
            key = _val;
            children = _children;
        }
        
        @Override
        public String toString() {
            return key + " children:" + children;
        }
    };

    static class BNode<T> {
        public T key;
        public BNode<T> left;
        public BNode<T> right;

        public BNode() {
        }

        public BNode(T _val) {
            key = _val;
        }

        public BNode(T _val, BNode<T> _left, BNode<T> _right) {
            key = _val;
            left = _left;
            right = _right;
        }

        @Override
        public String toString() {
            return "key:" + key + (left != null ? " l:" + left.key : " l:N")
                    + (right != null ? (" r:" + right.key) : (" r: N "));
        }
    };

}
