package adi.leetcode;

/* This O(n) algorithm is based on connecting separate connected components.
 * A component is represented by a root node. The component tree is a flat tree.
 * It does not grow beyond a height of two. The reason for that is the way this
 * algorithm works as described below.
 * 
 * 
 * Algorithm:
 * 1. The first card with a new type or new value is the root of the connected
 *    component. A root array is maintained. Initialized to their own card ids.
 * 2. When a new card is encountered there are four cases.
 *    a) new card - that is neither it's type nor it's value is seen earlier.
 *       Will be the root of the connected component.
 *    b) card matches an existing type but not existing value.
 *       Joins the existing component. It's root is the first card of this type.
 *    c) card matches an existing value but not existing type.
 *       Same as above.
 *    d) card matches both.
 * 3. 2a is trivial. 2b and 2c - new card joins an existing component. Their root
 *    is the first card arrived for the particular type or value.
 * 4. case 2d. This is a union of two components.
 *    Here root of smaller component gets another root - the root of bigger component.
 *    The component size of bigger component is added up.
 *    
 */
public class ConnectedCards {
    /*
     * type bucket contains root component id for each type.
     * component id is the first card id encountered for a particular type.
     * card id is the index of the card in the cards array plus 1. Starts from 1. 
     */
    private int[] typeBucket = null;

    /* value bucket contains component id for every value in the card array */
    private int[] valBucket = null;
    
    /* 
     * Maintains the root component id for each card. */
    private int[] roots = null; 
    
    /* Size of connected components */
    int[] componentSize = null;

    public static void main(String[] args) {
        /*
         * Spade = 0,
         * Heart = 1,
         * Diamond = 2,
         * Club = 3
         */
        /*
         * A card is a pair of two ints<type,value>
         */
        
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

        ConnectedCards obj = new ConnectedCards();
        int i=0;
        for (int[][] cards: games) {
            actual[i++] =  obj.findDiameter(cards);
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

    public int findDiameter(int[][] cards) {
        /* Init*/
        typeBucket = new int[4];
        valBucket = new int[13];

        roots = new int[cards.length]; 
        for(int i=0; i< cards.length; i++) {
            roots[i] = i+1;
        }

        componentSize = new int[cards.length + 1];
        int maxComponentSize = 0;

        /* Iterate over each card. Maintain typeBucket and valBucket.
         * Every time a card is encountered, it either does not belong to
         * an existing component or belongs to one, or belongs to two.
         * Corresponding component sizes are incremented. 
         * 
         * cards[c][0] is the type id.
         * cards[c][1] is the value id.
         */
        for(int c=0; c < cards.length; c++) {
            int cardId = c + 1;
            addCard(cards[c], cardId);
        }


        for(int f: componentSize) {
            if (f > maxComponentSize) {
                maxComponentSize = f;
            }
        }

        return maxComponentSize;
    }
    
    /* Returns the component id of the new card.
     * Maintains all arrays */
    private int addCard(int[]card, int cardId) {
        int typeRootId = getRootId(typeBucket[card[0]]);
        int valRootId = getRootId(valBucket[card[1]]);
        boolean isType = typeRootId !=0 ? true: false;
        boolean isVal = valRootId !=0 ? true: false;
        int rootId = 0;
        if(!isType && !isVal) {
            /* The card does not belong to any existing component.
             * Create a new connected component. From here on,
             * any new card having this type or this value will belong
             * to this connected component.
             */
            rootId = cardId;
        } else {
            /* This card belongs to one or two connected component(s).
             */

            if (!(isType && isVal)) {
                /* Belongs to only one component. */
                if (isType) {
                    rootId = typeRootId;
                } else {
                    rootId = valRootId;
                }

            } else {
                /* Belongs to two different componentIds. */
                if (typeRootId == valRootId) {
                    /* Roots are same, so this card actually belongs to
                     * one component - rootId.
                     */
                    rootId = typeRootId;
                } else {
                    /* A Union of two different component is required */
                    rootId = union(typeRootId, valRootId);
                }

            }

        }
        /* update arrays for the new card */
        typeBucket[card[0]] = rootId;
        valBucket[card[1]] = rootId;
        componentSize[rootId]++;
        roots[cardId - 1] = rootId;
        return rootId;
    }
    
    /* When union of two connected components happen,
     * root of bigger connected component becomes the root of smaller connected
     * component. If sizes are equal, any component can be root.
     * 
     *  index of array is cardId - 1.
     *  
     *  Note that component roots is a flat tree. So max height of tree
     *  can be only two. Hence this is O(1) operation.
     */
    private int getRootId(int componentId) {
        if (componentId == 0 || roots[componentId -1] == 0) {
            return 0;
        }
        int rootId = componentId;
        while (roots[rootId - 1] != rootId) {
            rootId = roots[rootId -1];
        }
        return rootId;
    }
    
    /* returns new rootId */
    private int union(int rootId1, int rootId2) {
        int compSize1 = componentSize[rootId1];
        int compSize2 = componentSize[rootId2];
        int newRootId = compSize1 >= compSize2 ? rootId1 : rootId2;
        componentSize[newRootId] = compSize1 + compSize2;
        if (newRootId == rootId1) {
            roots[rootId2 - 1] = newRootId; 
        } else {
            roots[rootId1 - 1] = newRootId;
        }
        
        return newRootId;
    }

}
