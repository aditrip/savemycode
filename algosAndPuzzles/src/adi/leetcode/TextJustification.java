package adi.leetcode;

import java.util.LinkedList;
/*
 * HARD [Doesn't look like hard ...]
 * https://leetcode.com/problems/text-justification/
 * 
 * # 68.
 * 
 Given an array of strings words and a width maxWidth,
 format the text such that each line has exactly maxWidth characters
  and is fully (left and right) justified.

You should pack your words in a greedy approach;
that is, pack as many words as you can in each line. Pad extra spaces ' '
when necessary so that each line has exactly maxWidth characters.

Extra spaces between words should be distributed as evenly as possible.
If the number of spaces on a line does not divide evenly between words,
the empty slots on the left will be assigned more spaces than the slots on the right.

For the last line of text, it should be left-justified,
and no extra space is inserted between words.

Note:

A word is defined as a character sequence consisting of non-space characters only.
Each word's length is guaranteed to be greater than 0 and not exceed maxWidth.
The input array words contains at least one word.
 

Example 1:

Input: words = ["This", "is", "an", "example", "of", "text", "justification."], maxWidth = 16
Output:
[
   "This    is    an",
   "example  of text",
   "justification.  "
]
Example 2:

Input: words = ["What","must","be","acknowledgment","shall","be"], maxWidth = 16
Output:
[
  "What   must   be",
  "acknowledgment  ",
  "shall be        "
]
Explanation: Note that the last line is "shall be    " instead of "shall     be", because the last line must be left-justified instead of fully-justified.
Note that the second line is also left-justified because it contains only one word.
Example 3:

Input: words = ["Science","is","what","we","understand","well","enough","to","explain","to","a","computer.","Art","is","everything","else","we","do"], maxWidth = 20
Output:
[
  "Science  is  what we",
  "understand      well",
  "enough to explain to",
  "a  computer.  Art is",
  "everything  else  we",
  "do                  "
]
 

Constraints:

1 <= words.length <= 300
1 <= words[i].length <= 20
words[i] consists of only English letters and symbols.
1 <= maxWidth <= 100
words[i].length <= maxWidth
 */
import java.util.List;

public class TextJustification {
    public static void main(String[] aregs) {
        /* String[] words = { "Science", "is", "what", "we", "understand", "well",
                "enough", "to", "explain", "to", "a", "computer.", "Art", "is",
                "everything", "else", "we", "do" };
        int maxWidth = 20;*/

       /* String[] words =
                { "What", "must", "be", "acknowledgment", "shall", "be" };

        int maxWidth = 16; */
        /*
         * Expected:
         * ["What   must   be","acknowledgment  ","shall be        "]
         * 
         * Actual:
         * What   must   be
           acknowledgment 
           shall be         
         */
        String[] words =
            { "Listen","to","many,","speak","to","a","few." };

        int maxWidth = 6;
        
        /*
         * ["Listen","to    ","many, ","speak ","to   a","few.  "]
         */


        List<String> lines;
        TextJustification obj = new TextJustification();
        lines = obj.fullJustify(words, maxWidth);

        for (String line : lines) {
            System.out.println(line);
        }
    }

    public List<String> fullJustify(String[] words, int maxWidth) {
        List<String> lines = new LinkedList<String>();

        int currWordIdx = 0;
        int lineStartIdx = currWordIdx;
        int lineLen = 0;
        int spaceSlots = 0;
        int extraSpaces = 0;
        int remSpaceEndIdx = -1;
        boolean lastLine = false;

        while (currWordIdx < words.length) {
            lineStartIdx = currWordIdx;
            lineLen = 0;
            spaceSlots = 0;
            extraSpaces = 0;
            remSpaceEndIdx = -1;

            while (true) {
                if (currWordIdx > lineStartIdx) {
                    spaceSlots++;
                    lineLen++;
                }

                if (lineLen > maxWidth) {
                    if (spaceSlots > 1)
                        spaceSlots--;
                    break;
                } else if (lineLen == maxWidth) {
                    /* can't end with space */
                    extraSpaces++;
                    if (spaceSlots > 1)
                       spaceSlots--;
                    break;
                }
                lineLen += words[currWordIdx++].length();

                if (lineLen > maxWidth) {
                    currWordIdx--;
                    lineLen -= words[currWordIdx].length();
                    /* decrement the last space */
                    lineLen--;
                    if (spaceSlots > 1) {
                        spaceSlots--;
                    }
                    extraSpaces = maxWidth - lineLen;
                    break;
                }
                if (currWordIdx == words.length) {
                    /* last word, it is the last line. */
                    lastLine = true;
                    extraSpaces = maxWidth - lineLen;
                    break;
                }
                if (lineLen == maxWidth) {
                    break;
                }
            }

            int extraSpacePerSlot = 0;
            int remSpaces = 0;

            if (spaceSlots > 0) {
                extraSpacePerSlot = extraSpaces / spaceSlots;
                remSpaces = extraSpaces % spaceSlots;
                if (remSpaces > 0) {
                    remSpaceEndIdx = remSpaces;
                }
            }

            boolean singleWord = false;
            if (currWordIdx <= lineStartIdx + 1) {
                singleWord=true;
            }
            int nSpaces = singleWord ? extraSpacePerSlot : 1 + extraSpacePerSlot ;

            char[] line = new char[maxWidth];
            int l = 0;
            String word = null;
            for (int i = lineStartIdx; i < currWordIdx; i++) {
                word = words[i];
                for (char c : word.toCharArray()) {
                    line[l++] = c;
                }
                if (l >= maxWidth) {
                    break;
                }
                if (lastLine) {
                    if (i == currWordIdx - 1) {
                        for (int s = 0; s < extraSpaces; s++) {
                            line[l++] = ' ';
                        }
                        break;
                    } else {
                        line[l++] = ' ';
                        continue;
                    }
                }
                for (int s = 0; s < nSpaces; s++) {
                    line[l++] = ' ';
                }
                if ((i - lineStartIdx) < remSpaceEndIdx) {
                    line[l++] = ' ';
                }
            }
            lines.add(new String(line));

        }

        return lines;

    }

}
