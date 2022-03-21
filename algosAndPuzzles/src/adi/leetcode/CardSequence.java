package adi.leetcode;

import java.util.ArrayList;
import java.util.HashSet;

public class CardSequence {


    static int max = 0;

    public static void main(String[] args) {
        int[][][] games = {
                {
                    {1,3}
                },
                {
                    {1,3}, {2,3}
                },
                {
                    {1,3}, {1,4}
                },
                {
                    {1,3}, {2,1}, {0,2}, {0,3}
                },
                {
                   {1,3}, {1,4}, {0,2},
                   {2,5}, {2,3}, {3,4},
                   {3,7}
                },
                {
                  {1,3}, {1,4}, {3,3},
                  {0,4}, {0,2}, {2,5},
                  {2,3}, {3,4}, {3,7}
                },
                {
                    {1,3}, {1,4}, {3,3},
                    {0,8}, {0,9}, {2,5},
                    {2,3}, {3,4}, {3,7}
                },
                {
                    {0,1}, {0,2}, {1,1},
                    {1,2}, {2,3}, {3,3},
                    {3,4}, {3,5}, {3,6},
                    {3,7}, {2,7}, {3,8}
                },
                {
                    {0,1}, {0,2}, {0,3},
                    {2,3}, {2,4}, {3,5},
                    {3,6}, {3,7}, {1,7},
                    {3,3}, {1,8}, {1,9}
                }

         };

        int[] expected = {1,2,2,3,6,9,7,8,12};
        int[] actual = new int[expected.length];

                
        
     
        
        int i=0;
        for (int[][] cards: games) {
            actual[i++] =  CardSequence.findDiameter(cards);
        }
        
        int r=0;
        for(int e: expected) {
            if (actual[r++] != e) {
                System.out.println("game #" + r + " gave wrong results." +
                                  " expected:" + e + " actual:" + actual[r-1]);
            } else {
                System.out.println("game #" + r + " Max length:" + e);
            }
        }

    }

    static int findDiameter(int[][] cards) {
        HashSet visited = new HashSet();
        int max = 0;
        int count[] = new int[cards.length];
        for (int i = 0; i < cards.length; i++) {
            int[] currCard = cards[i];
            visited.add(currCard);
            helper(cards, currCard, visited, count, i);

            max = Math.max(max, count[i]);
        }
        return max;

    }

    private static void helper(int[][] cards, int[] currCard,
                               HashSet visited, int[] count, int cIndex) {

        ArrayList children = getLinks(cards, currCard);
        if (children.size() > 0) {
            count[cIndex] = count[cIndex] + 1;
        }
        for (int i = 0; i < children.size(); i++) {
            int[] tcard = (int[]) children.get(i);
            if (!visited.contains(tcard)) {
                visited.add(tcard);
                helper(cards, tcard, visited, count, cIndex);
            }
        }
    }

    private static String getCardName(int[] card) {
        return card[0] + ":" + card[1];
    }

    private static ArrayList getLinks(int[][] cards, int[] currCard) {
        ArrayList list = new ArrayList();
        for (int[] card : cards) {
            if (card != currCard) {
                if (card[0] == currCard[0] || card[1] == currCard[1]) {
                    list.add(card);
                }
            }
        }
        return list;
    }
}


