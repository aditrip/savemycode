package adi.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import adi.leetcode.TreeUtils.Node;

public class NAryTree {
    private ArrayList<Integer> level = new ArrayList<Integer>();
    private ArrayList<Node> q = new ArrayList<Node>();
    private List<ArrayList<Integer>> levels =
            new ArrayList<ArrayList<Integer>>();
    private List<Integer> result = new ArrayList<Integer>();

    public static void main(String[] args) {
        //Integer[] serRoot = { 1, null, 3, 2, 4, null, 5, 6 };
        Integer[] serRoot = { 1, null, 2, 3, 4, 5, null, null, 5, 6, 7, null,
                8, 9, null, null, null, 10, null, null, 11 };

        NAryTree obj = new NAryTree();
        Node root = TreeUtils.deserRoot(serRoot);
        System.out.println(obj.levelOrder(root));
        System.out.println(obj.postOrder(root));

        System.out.println("intput:" + Arrays.asList(serRoot));
        System.out.println("output:"
                + TreeUtils.serRoot(TreeUtils.deserRoot(serRoot)));

    }

    public List<ArrayList<Integer>> levelOrder(Node root) {
        q.add(root);
        int levelLen = q.size();
        while (!q.isEmpty()) {

            for (int i = 0; i < levelLen; i++) {
                Node n = q.get(i);
                level.add(n.key);
                if (n.children != null && !n.children.isEmpty()) {
                    q.addAll(n.children);
                }
            }
            levels.add(level);
            level = new ArrayList<Integer>();
            for (int i = 0; i < levelLen; i++) {
                q.remove(0);
            }
            levelLen = q.size();
        }
        return levels;
    }

    public List<Integer> postOrder(Node root) {
        if (root == null) {
            return result;
        }
        if (root.children != null) {
            for (Node c : root.children) {
                System.out.println("Node:" + c);
                postOrder(c);
            }
        }
        result.add(root.key);
        return result;
    }

    /* recursive */
    public int maxDepth(Node root) {
        int maxH = 0;
        if (root == null) {
            return 0;
        }
        if (root.children == null || root.children.isEmpty()) {
            return 1;
        }

        for (Node c : root.children) {
            int cDepth = maxDepth(c);
            if (cDepth > maxH) {
                maxH = cDepth;
            }
        }
        return 1 + maxH;
    }

}
