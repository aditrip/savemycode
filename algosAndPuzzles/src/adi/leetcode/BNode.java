package adi.leetcode;


public class BNode<T extends Comparable<T>> {

    public BNode<T> left;
    public BNode<T> right;
    public BNode<T> parent;
    public T key;
    public T val;
    public T aggVal;
    
    public BNode() {}
    
    public BNode(T key) {
        this.key = key;
    }
    
    public BNode(T key, T val) {
        this.key = key;
        this.val = val;
    }
    

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        if (left != null) {
            sb.append(" l:" + left.key);
        } else {
            sb.append(" l:" + "N");
        }
        if (right != null) {
            sb.append(" r:" + right.key);
        }else {
            sb.append(" r:" + "N");
        }

        if (parent != null) {
            sb.append(" p:" + parent.key);
        } else {
            sb.append(" p:" + "N");
        }
        
        sb.append(" v:" + val + " g:" + aggVal);
        return sb.toString();
    }
}

