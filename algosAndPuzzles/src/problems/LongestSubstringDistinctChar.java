package problems;

public class LongestSubstringDistinctChar {

    private final int tableLength = 127;
    private final int bucketLength = 10;
    private CharPos[] existsTable = new CharPos[tableLength];

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        LongestSubstringDistinctChar obj = new LongestSubstringDistinctChar();
       // String s = "rzqafzqrs"; // start: 3, end: 8, length: 6
        //String s = "rzqafzq"; // start: 0, end: 4, length: 5
        //String s = "rzqafzrsqt"; // start: 3, end: 9, length: 7
        //String s = "rzqafzrsq"; // start: 2, end: 7, length: 6
        // String s = "bbbb";
        //String s = "abcabcbb"; // l = 3;
        String s = "pwwkew";
        obj.lengthOfLongestSubstring(s);

    }

    public int lengthOfLongestSubstring(String s) {
        char[] input = s.toCharArray();
        int maxStartIndex = 0;
        int maxEndIndex = 0;
        int currStartIndex = 0;
        int maxLength = 0;
        int currLength = 0;

        int i;
        for (i = 0; i < s.length(); i++) {
            char c = input[i];
            int mapIndex = c % tableLength;
            int putIndex = put(mapIndex, c, i, currStartIndex);
            if (putIndex >= 0) {
                /* A repeated char. Move currStartIndex */
                currLength = i - currStartIndex;
                System.out.println("i:" + i + " cl:" + currLength + " ml:" +
                        maxLength + " csi:" + currStartIndex + " putIndex:" +
                        putIndex);
                if (currLength > maxLength) {
                    maxStartIndex = currStartIndex;
                    maxEndIndex = i - 1;
                    maxLength = currLength;
                }

                currStartIndex = putIndex + 1;
                System.out.println("csi:" + currStartIndex);
            }

        }
        i--;
        currLength = i - currStartIndex + 1;
        System.out.println("i:" + i + " cl:" + currLength + " ml:" +
                maxLength);
        if (currLength > maxLength) {
            maxStartIndex = currStartIndex;
            maxEndIndex = i;
            maxLength = currLength;
        }

        System.out.println("MaxLength:" + maxLength + "  maxStartIndex:" +
                maxStartIndex + "  maxEndIndex:" + maxEndIndex);
        return maxLength;

    }

    /*
     * returns a positive int equal to strPos, if c already existed. Else,
     * returns -1; -1 value indicates the put was successful.
     */
    private int put(int index, char c, int strPos, int startSubStrPos) {
        CharPos head = existsTable[index];
        CharPos addNode = new CharPos(c, strPos);
        int repeatStrPos = -1;
        if (head == null) {
            head = addNode;
            existsTable[index] = head;
            return -1;
        }

        CharPos curr = head;
        while (true) {
            if (curr.c == c) {
                /* repeat character */
                repeatStrPos = curr.posIdx;
                System.out.println("at strPos:" + strPos + " found repeat char:" + c + " also occured at strPos:" + repeatStrPos );
                
                curr.posIdx = strPos;
                if (repeatStrPos >= startSubStrPos) {
                    return repeatStrPos;
                }

            }
            if (curr.next != null) {
                curr = curr.next;
            } else {
                break;
            }

        }

        curr.next = addNode;
        addNode.prev = curr;
        return -1;
    }

    public static class CharPos {
        public final int c;
        public int posIdx;
        public CharPos next;
        public CharPos prev;

        public CharPos(int c, int posIdx) {
            this.c = c;
            this.posIdx = posIdx;
        }
    }

}
