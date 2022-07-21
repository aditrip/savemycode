package adi.leetcode;

/*
 * Observations:
 * 1. Every palindrome in the mid will be of the form:
 * "*bb*" or "aba". Call this mid segment. The min palindrome.
 * 
 *  This algorithm iterates and searches for such points/segments.
 * 
 * 2.An "aba" can be middle of more than one palindrome.
 * 
 * 3.An "*bb*" can be a mid segment of only one array.
 * 
 * 4.Hence the mid segment is iterated again
 *   
 * 
 * 
 * 
 * Algorithm Idea.
 * 1. Start from mid of array. And iterate both ways from mid.
 * 2. Idea is to find the mid point of the palindrome.
 * 3. Each element can be a mid of a different palindrome.
 * 4. If iteration from mid towards ends, reaches to a point where longest
 *    palindrome can not beat the current max, exit.
 * 
 */
public class LongestPalindrome {
    int maxL = 1;
    int[] maxEnds = new int[2];

    public static void main(String[] args) {
        String debug_str = "aacabdkacaa";
        boolean debug = false;

        LongestPalindrome obj = new LongestPalindrome();

        String[] inputs = { "dccdef", "ddcccccddefgklm",
                "qrszadefcfedazabcdefghijbabababqq", "bdadef", "ccc", "aa",
                "dddd", "eeeee", "dcccd", "dccd", "ddccccdd",
                "zeqfghtppqdddcccccdddai", "tppqdddcccccdddaizfqaaggrinm",
                "ababababa" /* full sting is a palindrome*/,
                "babadada" /* "adada" */, "aacabdkacaa" /* aca */ };

        String[] outputs = new String[inputs.length];

        int i = 0;
        if (!debug) {
            for (String in : inputs) {
                outputs[i++] = obj.longestPalindrome(in);
                System.out.println(in + ":" + outputs[i - 1]);
            }

            for (int j = 0; j < outputs.length; j++) {
                System.out.println(inputs[j] + ":" + outputs[j]);
            }
        } else {
            System.out.println(debug_str + ":"
                    + obj.longestPalindrome(debug_str));
        }

    }

    public String longestPalindrome(String s) {

        if (s.length() <= 1) {
            return s;
        }
        if (s.length() == 1) {
            if (s.charAt(0) == s.charAt(1)) {
                return s;
            } else {
                return s.substring(0, 1);
            }
        }
        maxL = 1;
        maxEnds = new int[2];
        char[] a = s.toCharArray();
        findMaxPalindrome(a);
        return (s.substring(maxEnds[0], maxEnds[1] + 1));

    }

    public void findMaxPalindrome(char[] a) {
        if (a == null || a.length == 0 || a.length == 1) {
            return;
        }
        if (a.length == 2) {
            if (a[0] == a[1]) {
                maxL = 2;
                maxEnds[1] = 1;
            }
            return;
        }

        int mid = (a.length - 1) >>> 1;
        int idx = mid;
        int[] ends = null;
        int maxPossible = a.length;
        for (int d = 0; d <= 1; d++) {
            idx = mid;

            while (idx > 0 && idx < a.length - 1) {
                if (d == 0) {
                    maxPossible = 2 * (a.length - idx - 1) + 1;
                } else {
                    maxPossible = 2 * idx + 1;
                }
                if (maxL >= maxPossible) {
                    break;
                }
                ends = minEnds(a, idx);
                if (ends != null) {
                    findMax(a, ends);
                }
                if (d == 0) {
                    idx++;
                } else {
                    idx--;
                }
            }
        }

    }

    public void findMax(char[] a, int[] ends) {

        int idx1 = ends[0];
        int idx2 = ends[1];
        while (idx1 - 1 >= 0 && idx2 + 1 < a.length) {
            if (a[idx1 - 1] == a[idx2 + 1]) {
                idx1--;
                idx2++;
            } else {
                break;
            }
        }
        updateMaxL(idx1, idx2);

    }

    private void updateMaxL(int idx1, int idx2) {
        if (maxL < (idx2 - idx1 + 1)) {
            maxL = (idx2 - idx1 + 1);
            maxEnds[0] = idx1;
            maxEnds[1] = idx2;
        }
    }

    private int[] minEnds(char[] a, int p) {
        int[] ends = new int[2];
        ends[0] = ends[1] = p;

        if (p - 1 >= 0 && p + 1 < a.length && a[p + 1] == a[p - 1]) {
            ends[1] = p + 1;
            ends[0] = p - 1;

            if (a[p] == a[p + 1]) {
                p = ends[1];
                while (p + 1 < a.length && a[p + 1] == a[p]) {
                    p++;
                }
                ends[1] = p;
                p = ends[0];
                while (p - 1 >= 0 && a[p - 1] == a[p]) {
                    p--;
                }
                ends[0] = p;
            }
            return ends;
        } else if (p + 1 < a.length && a[p + 1] == a[p]) {
            p++;
            while (p + 1 < a.length && a[p + 1] == a[p]) {
                p++;
            }
            ends[1] = p;
            p = ends[0];
            while (p - 1 >= 0 && a[p - 1] == a[p]) {
                p--;
            }
            ends[0] = p;
            return ends;
        } else if (p - 1 >= 0 && a[p - 1] == a[p]) {
            while (p - 1 >= 0 && a[p - 1] == a[p]) {
                p--;
            }
            ends[0] = p;
            return ends;
        } else if (p + 2 < a.length && a[p + 2] == a[p]) {
            ends[1] = p + 2;
            return ends;
        } else if (p - 2 >= 0 && a[p - 2] == a[p]) {
            ends[0] = p - 2;
            return ends;
        } else {
            return null;
        }
    }

}
