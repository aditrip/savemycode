package adi.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/*
 * https://leetcode.com/problems/search-suggestions-system
 * 
 You are given an array of strings products and a string searchWord.

Design a system that suggests at most three product names from products after each character of
 searchWord is typed. Suggested products should have common prefix with searchWord.
 If there are more than three products with a common prefix return the three lexicographically
 minimums products.

Return a list of lists of the suggested products after each character of searchWord is typed.

 

Example 1:

Input: products = ["mobile","mouse","moneypot","monitor","mousepad"], searchWord = "mouse"
Output: [
["mobile","moneypot","monitor"],
["mobile","moneypot","monitor"],
["mouse","mousepad"],
["mouse","mousepad"],
["mouse","mousepad"]
]
Explanation: products sorted lexicographically = ["mobile","moneypot","monitor","mouse","mousepad"]
After typing m and mo all products match and we show user ["mobile","moneypot","monitor"]
After typing mou, mous and mouse the system suggests ["mouse","mousepad"]
Example 2:

Input: products = ["havana"], searchWord = "havana"
Output: [["havana"],["havana"],["havana"],["havana"],["havana"],["havana"]]
Example 3:

Input: products = ["bags","baggage","banner","box","cloths"], searchWord = "bags"
Output: [["baggage","bags","banner"],["baggage","bags","banner"],["baggage","bags"],["bags"]]
 

Constraints:

1 <= products.length <= 1000
1 <= products[i].length <= 3000
1 <= sum(products[i].length) <= 2 * 104
All the strings of products are unique.
products[i] consists of lowercase English letters.
1 <= searchWord.length <= 1000
searchWord consists of lowercase English letters.
 */
public class PrefixTrie {

    public static void main(String[] args) {
        PrefixTrie obj = new PrefixTrie();
        String[] products = { "mobile", "mouse", "moneypot", "monitor",
                "mousepad", "mobs", "mobq", "moat", "mon", "monezpot",
                "monipot", "mobotor", "mobine", "mobgoat", "mobhorse",
                "mobinacle", "mob" };

        String searchWord = "mobine";
        System.out.println(" Search word:" + searchWord);
        Arrays.sort(products);
        System.out.println(" Dict:" + Arrays.toString(products));
        obj.suggestedProducts(products, searchWord);

    }

    public List<List<String>> suggestedProducts(String[] products,
                                                String searchWord) {

        List<List<String>> perKeyList = new ArrayList<List<String>>();

        ProductTrie dict = new ProductTrie();
        for (String prod : products) {
            dict.insertWord(prod);
        }

        for (int i = 1; i <= searchWord.length(); i++) {
            List<String> prods = dict.getTop3(searchWord.substring(0, i));
            if(prods.size() > 3) {
                for(int p=3 ; p < prods.size(); p++) {
                    prods.remove(p);
                }
            }
            perKeyList.add(prods);
        }
       /* for (List<String> prods : perKeyList) {
            System.out.println();
            System.out.print("[ ");
            for (String product : prods) {
                System.out.print(product + ", ");
            }
            System.out.print("]");
        } */

        return perKeyList;
    }

    static class ProductTrie {
        public static int N_CHILDREN = 26;
        List<CharList> suffixes = null;
        TNode root = new TNode();

        public void insertWord(String s) {
            char[] word = s.toCharArray();
            TNode parent = root;
            int level = 0;
            for (char c : word) {
                int idx = c - 'a';
                if (parent.children == null) {
                    parent.children = new TNode[N_CHILDREN];
                }
                if (parent.children[idx] == null) {

                    parent.children[idx] = new TNode(c, parent, idx, level);
                }
                parent = parent.children[idx];
                level++;
            }
            parent.wordEnd = true;
        }

        public List<String> getTop3(String prefixStr) {
            List<String> top3 = new ArrayList<String>();
            if (prefixStr == null || prefixStr.isEmpty()) {
                return top3;

            }
            char[] prefix = prefixStr.toCharArray();
            byte top3Count = 0;
            TNode parent = root;

            for (int c : prefix) {
                int idx = c - 'a';
                if (parent.children == null) {
                    return top3;
                }
                if (parent.children[idx] == null) {
                    return top3;
                }
                parent = parent.children[idx];
            }
            TNode prefixParent = parent;
            if (prefixParent == null) {
                return top3;
            }
            if (prefixParent.wordEnd) {
                top3.add(prefixStr);
                if (prefixParent.isTerminal()) {
                    return top3;
                }
            }

            List<CharList> suffixes =
                    getSortedSuffixes(prefixParent, 3 - top3Count);

            String suffixStr = null;
            for (CharList suffix : suffixes) {
                if (suffix.size > 0) {
                    suffixStr = new String(suffix.toArray());
                    top3.add(prefixStr.concat(suffixStr));
                }
            }

            return top3;

        }

        /*
         * Get suffixes below prefixEndNode. prefixEndNode not included.
         * Do not call this if prefixEndNode is Terminal.
         * 
         * Algorithm - 1) maintain currSuffix which has letters till the current
         * parent. This invariant is important. Add and delete last node when
         * a word end is encountered.
         * 
         * 2) maintain currChildren - iterate - if wordend is encountered,
         *    add this letter to currSuffix and the result to suffixes. Return if done.
         * 
         * 3) if non-terminal is encountered, add to currSuffix and get the new minSuffix
         *    for this non-terminal parent. CurrChildren now changes.
         *    
         * 3) When currChildren is done, getNextChildren while maintaining the currSuffix.
         * 
         */
        public List<CharList> getSortedSuffixes(TNode prefixEndNode,
                                                int count) {
            suffixes = new LinkedList<CharList>();
            if (prefixEndNode.isTerminal())
                return suffixes;

            int minLevel = prefixEndNode.level;
            /*
             * When parent is moved to next, parentPrefix is populated.
             */
            CharList currSuffix = new CharList();

            TNode[] currChildren = null;
            TNode currEndNode = prefixEndNode;
            TNode currParent = prefixEndNode;
            AtomicReference<TNode> nodeRef = new AtomicReference<TNode>();
            int i = 0;
            while (i < count) {
                nodeRef.set(currEndNode);
                CharList minSuffix = getMinSuffix(nodeRef);
                /* parent points to the last node (terminal char) in the minSuffix */
                currEndNode = nodeRef.get();
                currParent = currEndNode.parent;
                currChildren = currParent.children;

                currSuffix = CharList.concat(currSuffix, minSuffix);
                /* Remove terminal node, currSuffix is maintained at parent */

                currSuffix.delLast();

                addSuffix(currSuffix, currEndNode);

                if (suffixes.size() >= count) {
                    return suffixes;
                }

                if (!currEndNode.isTerminal()) {
                    currSuffix.add(currEndNode.c);
                    continue;
                }

                /* MinSuffix ended at terminal node and more suffixes required.
                 * 1. Iterate on currChildren till non-terminal or end.
                 *    While iterating, add terminal to suffixes. */

                do {
                    currEndNode = getNextChild(currChildren,
                                               currEndNode.slotIdx + 1);
                    if (currEndNode != null && currEndNode.isTerminal()) {
                        addSuffix(currSuffix, currEndNode);
                        if (suffixes.size() >= count) {
                            return suffixes;
                        }
                    }
                } while (currEndNode != null && currEndNode.isTerminal());

                if (currEndNode != null) {
                    /* A non terminal node */
                    if (currEndNode.wordEnd) {
                        addSuffix(currSuffix, currEndNode);
                        if (suffixes.size() >= count) {
                            return suffixes;
                        }
                    }

                    /* new minSuffix excluding this currEndNode  will be found,
                     * so add this currEndNode to currSuffix*/
                    currSuffix.add(currEndNode.c);
                } else {
                    /* node ended. Find the next non terminal parent. */
                    currParent = getNextParent(currParent, minLevel,
                                               currSuffix, count);
                    if (suffixes.size() >= count || currParent == null) {
                        return suffixes;
                    }
                    currEndNode = currParent;
                }
            }
            return suffixes;

        }

        private void addSuffix(CharList currSuffix, TNode termNode) {
            currSuffix.add(termNode.c);
            suffixes.add(currSuffix.clone());
            currSuffix.delLast();
        }

        /*
         * suffix DOES NOT INCLUDE character of parent node.
         * Since this parent will be part of the prefix typically.
         * 
         * Suffix starts from the left most child of the parent.
         * The nodes on the suffix path are recorded in minPath.
         * 
         * Terminal character is included in minPath and suffix.
         * 
         * Do not call this for isTerminal parent.
         * 
         * parent is the last terminal node after this method finishes.
         */
        public CharList getMinSuffix(AtomicReference<TNode> parentRef) {
            TNode parent = parentRef.get();

            if (parent.isTerminal()) {
                return null;
            }

            CharList minSuffix = new CharList();
            if (parent.wordEnd) {
                parent = getNextChild(parent.children, 0);
                minSuffix.add(parent.c);
            }
            while (!parent.wordEnd) {
                TNode firstChild = getNextChild(parent.children, 0);
                if (firstChild == null) {
                    return minSuffix;
                }
                minSuffix.add(firstChild.c);
                parent = firstChild;
            }
            parentRef.set(parent);
            return minSuffix;
        }

        /*
         * fromIdx included in the search.
         * returns null if no next child exists.
         * 
         */
        public TNode getNextChild(TNode[] itrNode, int fromIdx) {
            if (itrNode == null || itrNode.length == 0
                    || fromIdx > N_CHILDREN - 1) {
                return null;
            }

            for (int i = fromIdx; i < N_CHILDREN; i++) {
                if (itrNode[i] != null) {
                    return itrNode[i];
                }
            }
            return null;
        }

        /* returns next non-terminal parent node below minLevel.
         * currSuffix last char is of currParent.
         * Maintain currSuffix at parent level. */

        public TNode getNextParent(TNode currParent, int minLevel,
                                   CharList currSuffix, int count) {
            if (currParent.level <= minLevel) {
                return null;
            }
            TNode grandParent = currParent.parent;
            TNode nextParent = null;
            while (nextParent == null) {
                nextParent = getNextChild(grandParent.children,
                                          currParent.slotIdx + 1);
                if (nextParent == null) {
                    /* Move one level up */
                    currSuffix.delLast();
                    currParent = grandParent;
                    if (currParent.level <= minLevel) {
                        return null;
                    }
                    grandParent = currParent.parent;
                } else if (nextParent.isTerminal()) {
                    currParent = nextParent;
                    currSuffix.delLast();
                    addSuffix(currSuffix, currParent);
                    currSuffix.add(nextParent.c);
                    if (suffixes.size() >= count) {
                        return currParent;
                    }
                    nextParent = null;
                } else {
                    /* got next parent .. replace this parent from suffix */
                    currSuffix.delLast();
                    if (nextParent.wordEnd) {
                        addSuffix(currSuffix, nextParent);
                        if (suffixes.size() >= count) {
                            return nextParent;
                        }
                    }
                    currSuffix.add(nextParent.c);
                    currParent = nextParent;
                }
            }
            return nextParent;
        }
    }

    /*
     * 
     */
    static class TNode {
        public boolean wordEnd = false;
        public TNode[] children = null;
        public char c;
        public TNode parent;
        public int slotIdx;
        public int level;

        public TNode(char c, TNode parent, int parentIdx, int level) {
            this.c = c;
            this.parent = parent;
            this.slotIdx = parentIdx;
            this.level = level;
        }

        public TNode() {
            this.c = 0;
            this.parent = null;
            this.level = -1;
        }

        public boolean isTerminal() {
            return children == null;
        }

        @Override
        public String toString() {
            return " char:" + c + " level:" + level + " parentIdx:" + slotIdx
                    + " children:" + Arrays.deepToString(children);
        }
    }

    static class CharNode {
        char ch;
        CharNode next;
        CharNode prev;

        public CharNode(char ch) {
            this.ch = ch;
        }
    }

    static class CharList {
        CharNode head = null;
        CharNode tail = null;
        int size = 0;

        public void clear() {
            head = null;
            tail = null;
            size = 0;
        }

        public void add(char ch) {
            CharNode cn = new CharNode(ch);
            if (head == null) {
                head = cn;
                tail = cn;
            } else {
                tail.next = cn;
                cn.prev = tail;
                tail = cn;
            }
            size++;
        }

        public void delLast() {
            if (size <= 0) {
                return;
            }
            if (size == 1) {
                head = null;
                tail = null;
                size = 0;
                return;
            }
            tail = tail.prev;
            tail.next = null;
            size--;
        }

        public void delLast(int n) {
            if (size <= n) {
                head = null;
                tail = null;
                size = 0;
                return;
            }
            if (n == 0) {
                return;
            }
            for (int i = 0; i < n; i++) {
                tail = tail.prev;
            }
            size -= n;
        }

        public static CharList concat(CharList l1, CharList l2) {
            CharList l3 = new CharList();
            CharNode[] heads = { l1.head, l2.head };

            for (CharNode n : heads) {
                while (n != null) {
                    l3.add(n.ch);
                    n = n.next;
                }
            }
            return l3;
        }

        public CharList clone() {
            CharList clone = new CharList();
            CharNode n = head;
            while (n != null) {
                clone.add(n.ch);
                n = n.next;
            }
            return clone;
        }

        public char[] toArray() {
            char[] arr = new char[size];
            CharNode n = head;
            int i = 0;
            while (n != null) {
                arr[i++] = n.ch;
                n = n.next;
            }
            return arr;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            CharNode n = head;

            while (n != null) {
                sb.append(n.ch);
                n = n.next;
            }

            return sb.toString();
        }
    }

}
