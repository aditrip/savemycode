package adi.leetcode;

import adi.leetcode.TreeUtils.*;

public class Driver {

    public static void main(String[] args) {
        Integer[] keys = {58, null,38,70,null,28,42,null,62,76,null,null,32,null,40,null,null,60,64,null,null,78,
                           null,null,35,null,null,null,null,50,null,null,null,null,null,77};
        BNode<Integer> root = TreeUtils.deserBRoot(keys);
        TreeUtils.validateBST(root);
        TreeUtils.printBST(root);

    }

}
