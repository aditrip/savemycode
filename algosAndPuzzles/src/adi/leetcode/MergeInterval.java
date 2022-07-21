package adi.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class MergeInterval {

    static class Pair<P, Q> {
        public P first;
        public Q second;

        public Pair(P first, Q second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Pair)) {
                return false;
            }
            return first.equals(o) && second.equals(o);
        }
    }

    public static void main(String[] args) {

        List<int[][]> intervalsList = new ArrayList<int[][]>();
        intervalsList.add(new int[][] { { 1, 3 }, { 2, 6 }, { 8, 10 },
                { 15, 18 } });
        intervalsList.add(new int[][] { { 1, 3 }, { 4, 5 }, { 6, 7 },
                { 8, 19 } });
        intervalsList.add(new int[][] { { 1, 3 }, { 3, 5 }, { 5, 7 },
                { 7, 19 } });
        intervalsList.add(new int[][] { { 1, 3 } });
        intervalsList.add(new int[][] { { 1, 3 }, { 2, 6 }, { 2, 15 },
                { 8, 10 }, { 15, 18 } });
        intervalsList.add(new int[][] { { 1, 4 }, { 0, 0 } });
        intervalsList.add(new int[][] { { 2, 3 }, { 2, 2 }, { 3, 3 }, { 1, 3 },
                { 5, 7 }, { 2, 2 }, { 4, 6 } });
        intervalsList.add(new int[][] { { 2, 3 }, { 2, 2 }, { 3, 3 }, { 1, 3 },
                { 5, 7 }, { 2, 2 }, { 4, 6 } });
        intervalsList.add(new int[][] { { 4, 4 }, { 3, 5 }, { 2, 3 }, { 1, 1 },
                { 3, 3 }, { 1, 3 }, { 2, 2 }, { 0, 0 }, { 5, 5 }, { 0, 0 },
                { 4, 6 } });

        MergeInterval obj = new MergeInterval();

      /*  for (int[][] intervals : intervalsList) {
            System.out.println(" ***** ");
            obj.merge(intervals);
        } */

        for (int[][] intervals : intervalsList) {
            System.out.println("#####" + str(intervals));
            obj.mergeArr(intervals);
            System.out.println("Expected:" + str(obj.merge(intervals)));
        }

        List<int[][]> debugList = new ArrayList<int[][]>();
        
        debugList.add(new int[][] {{362,367},{314,315},{133,138},{434,443},{202,203},{144,145},{229,235},
            {205,212},{314,323},{128,129},{413,414},{342,345},{43,49},{333,342},{173,178},{386,391},{131,133},{157,163},{187,190},
            {186,186},{17,19},{63,69},{70,79},{386,391},{98,102},{236,239},{195,195},{338,338},{169,170},{151,153},{409,416},{377,377},
            {90,96},{156,165},{182,186},{371,372},{228,233},{297,306},{56,61},{184,190},{401,403},{221,228},{203,212},{39,43},{83,84},
            {66,68},{80,83},{32,32},{182,182},{300,306},{235,238},{267,272},{458,464},{114,120},{452,452},{372,375},{275,280},{302,302},
            {5,9},{54,62},{237,237},{432,439},{415,421},{340,347},{356,358},{165,168},{15,17},{259,265},{201,204},{192,197},{376,383},
            {210,211},{362,367},{481,488},{59,64},{307,315},{155,164},{465,467},{55,60},{20,24},{297,304},{207,210},{322,328},{139,142},
            {192,195},{28,36},{100,108},{71,76},{103,105},{34,38},{439,441},{162,168},{433,433},{368,369},{137,137},{105,112},{278,280},
            {452,452},{131,132},{475,480},{126,129},{95,104},{93,99},{394,403},{70,78}});
        
        debugList.add(new int[][]{{9,11},{430,435},{56,56},{323,330},{47,51},{354,358},{194,202},{286,290},{149,158},{121,127},
            {208,212},{271,278},{69,78},{33,33},{359,360},{386,394},{84,90},{175,177},{78,79},{241,248},{267,272},{164,165},
            {113,115},{107,112},{384,392},{291,293},{204,207},{231,234},{352,356},{96,100},{77,79},{284,287},{150,152},{5,5},
            {163,171},{409,409},{193,196},{243,250},{228,228},{274,276},{78,83},{56,60},{480,489},{259,264},{255,260},{249,251},
            {189,194},{198,199},{197,202},{123,123},{154,157},{142,149},{106,111},{6,10},{235,235},{298,303},{346,352},{299,307},
            {345,346},{363,363},{266,268},{433,441},{350,353},{499,506},{38,41},{408,410},{156,156},{392,396},{436,444},{301,304},
            {31,32},{41,48},{158,160},{407,410},{103,104},{104,106},{235,244},{30,35},{372,373},{133,133},{4,7},{455,457},
            {443,450},{479,480},{245,247},{255,261},{83,91},{5,6},{340,343},{97,101},{36,37},{166,167},{442,448},{357,363},{77,79},
            {428,432},{235,238},{298,306},{230,237},{262,270},{409,418},{456,459},{17,21},{86,93},{79,82}});
        List<String> expectedList = new ArrayList<String>();
        expectedList.add("[[5,9],[15,19],[20,24],[28,38],[39,49],[54,69],[70,79],[80,84],"
                + "[90,112],[114,120],[126,129],[131,138],[139,142],[144,145],[151,153],[155,168],[169,170],"
                + "[173,178],[182,190],[192,197],[201,212],[221,239],[259,265],[267,272],[275,280],[297,306],"
                + "[307,328],[333,347],[356,358],[362,367],[368,369],[371,375],[376,383],[386,391],[394,403],"
                + "[409,421],[432,443],[452,452],[458,464],[465,467],[475,480],[481,488]]");
        expectedList.add("");
        Iterator<String> expectedItr = expectedList.iterator();
        for(int[][] debug : debugList) {
            obj.mergeArr(debug);
            System.out.println(expectedItr.next());
        }
    }

    public static String str(int[][] intervals) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < intervals.length; i++) {
            sb.append("{ " + intervals[i][0] + ", " + intervals[i][1]
                    + " }, ");
        }
        return sb.toString();

    }

    public static String str(int[] a) {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        for (int i = 0; i < a.length; i++) {
            sb.append(a[i] + ", ");
        }
        sb.append(" }");
        return sb.toString();

    }

    public int[][] mergeArr(int[][] intervals) {
        ArrayTree aTree = new ArrayTree(intervals);
        System.out.println("sortedKeys:" + str(aTree.sortedKeys));
        System.out.println("maxEnds:" + str(aTree.maxEnds));
        int[][] merged = aTree.mergeOverlaps();
        return merged;
    }

    /*
     * Start from the interval having the minimum start.
     * 
     *  If thisInterval does not overlap with any interval,
     *  add it to the merge list and take the nextInterval,
     *  start after end of thisInterval.
     *  
     */
    public int[][] merge(int[][] intervals) {
        ITree iTree = new ITree();
        for (int[] interval : intervals) {
            iTree.addInterval(interval);
        }
        /* Merge overlapping intervals */
        INode nextMinNode = iTree.min();
        int nextEnd = Integer.MAX_VALUE;
        int[][] merged = new int[intervals.length][2];
        int i = 0;
        do {
            nextEnd = iTree.getMaxEnd(nextMinNode.end);
            /*
             * Link all overlapping nodes.
             */
            int linkedEnd = nextEnd;
            do {
                nextEnd = linkedEnd;
                linkedEnd = iTree.getMaxEnd(linkedEnd);
            } while (nextEnd != linkedEnd);

            int[] overlapInt = new int[2];
            overlapInt[0] = nextMinNode.key;
            overlapInt[1] = nextEnd;
            merged[i++] = overlapInt;
            if (nextEnd == nextMinNode.key) {
                /* If key and val are same for minNode,
                 * then increment val, else it will keep
                 * finding the same node.
                 * Two nodes with same key and value are
                 * considered as same node.
                 */
                nextEnd++;
                /* It is possible that this node was already part of
                 * previous set of overlapping intervals.
                 * So don't add it again.
                 * But in case this node was not covered,
                 * consider it's lteMaxEnd.
                 */
                if (i - 2 >= 0) {
                    int lastEnd = merged[i - 2][1];
                    if (nextMinNode.end <= lastEnd) {
                        /* Before removing this node,
                         * get it's maxEnd and update
                         * the last added interval's end if required.
                         */
                        int lteMaxEnd =
                                iTree.getMaxEnd(iTree.lteMaxEnd(nextMinNode));
                        if (lteMaxEnd > (nextEnd - 1)) {
                            nextEnd = lteMaxEnd;
                            merged[i - 2][1] = lteMaxEnd;
                        }
                        i--;
                    }
                }
            }
            nextMinNode = iTree.getNextMin(nextEnd);

        } while (nextMinNode != null);

        if (intervals.length > i) {
            int[][] truncated = new int[i][2];
            System.arraycopy(merged, 0, truncated, 0, i);
            merged = truncated;
        }
        for (int[] interval : merged) {
            System.out.println(interval[0] + ":" + interval[1]);
        }
        return merged;
    }

    /* Pardon the violation of data encapsulation rule .. not writing
     * getter setter. everything is public for below structures.
     */
    static class INode {
        public INode left = null;
        public INode right = null;
        public INode parent = null;

        public int key;
        public int end; // value is end point

        /* max of all end values of it's children */
        public int maxEnd;

        public INode(int key, int end) {
            this.key = key;
            this.end = end;
        }
    }

    static class ITree {
        INode root;

        public void addInterval(int[] in) {
            if (root == null) {
                root = new INode(in[0], in[1]);
                root.maxEnd = in[1];
            } else {
                insertInterval(in);
            }
        }

        private void insertInterval(int[] in) {
            INode parent = root;
            INode insNode = new INode(in[0], in[1]);
            while (parent != null) {
                if (in[0] <= parent.key) {
                    if (parent.left != null) {
                        parent = parent.left;
                    } else {
                        parent.left = insNode;
                        break;
                    }
                } else {
                    if (parent.right != null) {
                        parent = parent.right;
                    } else {
                        parent.right = insNode;
                        break;
                    }
                }
            }

            insNode.parent = parent;
            insNode.maxEnd = in[1];

            if (parent.maxEnd < in[1]) {
                propogateMaxEnd(parent, in[1]);
            }

        }

        public INode min() {
            return min(root);
        }

        public INode min(INode n) {
            INode currNode = n;
            while (currNode.left != null) {
                currNode = currNode.left;
            }
            return currNode;
        }

        /* 
         * Get the minimum node whose key is GTE fromKey.
         * That is basically finds the next successor GTE
         * fromKey in the sorted order of keys.
         * 
         * Goes the binary search way, starting from root.
         * Updates the nextMin to a new nextMin if:
         *  1. currNodeKey >= fromKey and
         *  2. currNodeKey < prevNextMin
         *   
         */
        public INode getNextMin(int fromKey) {
            INode currNode = root;
            int nextMin = Integer.MAX_VALUE;
            INode nextMinNode = null;

            while (currNode != null) {
                if (currNode.key == fromKey) {
                    return currNode;
                }
                if (currNode.key > fromKey) {
                    if (currNode.key < nextMin) {
                        nextMin = currNode.key;
                        nextMinNode = currNode;
                    }
                    currNode = currNode.left;
                } else {
                    currNode = currNode.right;
                }
            }
            return nextMinNode;
        }

        public INode successor(INode n) {
            if (n.right != null) {
                return min(n.right);
            }
            INode parent = n.parent;

            while (parent != null && parent.right == n) {
                n = parent;
                parent = n.parent;
            }
            return parent;
        }

        private void propogateMaxEnd(INode parent, int newEnd) {
            while (parent != null) {
                if (parent.maxEnd < newEnd) {
                    parent.maxEnd = newEnd;
                    parent = parent.parent;
                } else {
                    break;
                }
            }
        }

        public void getAllChildrenSorted(INode parent,
                                         List<INode> sortedChildren) {
            //List<INode> sortedChildren = new ArrayList<INode>();
            /* InOrder traversal - Recursive (TODO: iterative) */
            if (parent == null) {
                return;
            }

            if (parent.left != null) {
                getAllChildrenSorted(parent.left, sortedChildren);
            }

            sortedChildren.add(parent);

            if (parent.right != null) {
                getAllChildrenSorted(parent.right, sortedChildren);
            }

        }

        /*
         * Get maxEnd amongst all nodes less than INode n's key.
         * 
         */
        private int lteMaxEnd(INode n) {
            if (n.left == null) {
                return n.end;
            } else {
                return Math.max(n.end, n.left.maxEnd);
            }
        }

        /* This function finds the max end point amongst all the
         * intervals whose startPt is less than equal to the passed
         * lastStartPt.
         * 
         * The lastStartPt passed is typically the maxEnd of overlapping
         * intervals.
         * 
         * If there are no intervals less than lastStartPt, MIN_VALUE
         * is returned.
         * 
         * This algorithm uses the fact that maxEndPt on a node is max
         * of end points under it.
         * 
         * Algorithm:
         * 1. Follow the BSTs insertPath for key with lastStartPt.
         * 2. Whenever the path takes a right edge, update the
         *    runningMaxEnd with the parent node of that edge.
         * 
         */
        public int getMaxEnd(int lastStartPt) {

            int runningMaxEnd = Integer.MIN_VALUE;

            if (root == null) {
                return runningMaxEnd;
            }

            INode currNode = root;

            while (currNode != null) {
                if (currNode.key == lastStartPt) {
                    return Math.max(runningMaxEnd, lteMaxEnd(currNode));
                } else if (lastStartPt > currNode.key) {
                    /* Going to take the right edge,
                     * update runningMaxEnd */
                    runningMaxEnd =
                            Math.max(runningMaxEnd, lteMaxEnd(currNode));
                    currNode = currNode.right;

                } else {
                    currNode = currNode.left;
                }
            }

            return runningMaxEnd;
        }
    }

    /* Sorted array implementation */
    static class ArrayTree {
        int cutOff = 20;
        int[][] sortedKeys;
        int[] maxEnds;

        public ArrayTree(int[][] intervals) {
            addAll(intervals);
        }

        public int[][] mergeOverlaps() {
            /* Merge overlapping intervals */
            int lastEndPt = -1;
            int[] minNode = null;
            int prevMaxEnd = 0;
            int maxEndIdx = 0;
            int maxEnd = 0;
            int minNodeIdx = 0;
            int[][] merged = new int[sortedKeys.length][2];
            int mergedIdx = 0;
            
            do {
                minNodeIdx = getMinStartGT(lastEndPt, maxEndIdx);
                if (minNodeIdx == -1) {
                    break;
                }
                minNode = sortedKeys[minNodeIdx];
                int[] mergedInt = new int[2];
                mergedInt[0] = minNode[0];
                maxEnd = minNode[1];
                
                do {
                    prevMaxEnd = maxEnd;
                    maxEndIdx = getMaxIdxLTE(prevMaxEnd, maxEndIdx);
                    maxEnd = maxEnds[maxEndIdx];
                    
                } while (maxEnd != prevMaxEnd);
                
                mergedInt[1] = maxEnd;
                merged[mergedIdx++] = mergedInt;
                lastEndPt = maxEnd;
                
            } while (true);


            if (sortedKeys.length > mergedIdx) {
                int[][] truncated = new int[mergedIdx][2];
                System.arraycopy(merged, 0, truncated, 0, mergedIdx);
                merged = truncated;
            }
            for (int[] interval : merged) {
                System.out.println(interval[0] + ":" + interval[1]);
            }
            return merged;
        }

        private void addAll(int[][] intervals) {
            long startTime = System.nanoTime();
            Arrays.sort(intervals, new StartPtComp());
            long endTime = System.nanoTime();
            if ((endTime - startTime)/(1000) > 1) {
                System.out.println((endTime - startTime)/(1000) + " micro seconds for sorting.");
            }
            maxEnds = new int[intervals.length];
            maxEnds[0] = intervals[0][1];
            for (int i = 1; i < intervals.length; i++) {
                if (maxEnds[i - 1] > intervals[i][1]) {
                    maxEnds[i] = maxEnds[i - 1];
                } else {
                    maxEnds[i] = intervals[i][1];
                }
            }
            sortedKeys = intervals;
        }

        public int[] min() {
            return sortedKeys[0];
        }

        /*
         * next min interval whose start is after fromKey.
         * fromKey is not included.
         */
        public int getMinStartGT(int fromKey, int fromIdx) {
            int idx = search(fromKey, fromIdx, false);
            if (idx == -1) {
                return idx;
            }
            if (sortedKeys[idx][0] == fromKey) {
                if (idx < sortedKeys.length - 1) {
                    return idx + 1;
                } else {
                    return -1;
                }
            }
            return idx;
        }

        /** This function finds the index of max end point amongst all the
        * intervals whose startPt is less than equal to the passed
        * fromKey(inclusive).
        * 
        * The fromKey passed is typically the maxEnd of overlapping
        * intervals.
        * 
        * fromIdx is startIdx of the search space.
        * 
        * If there are no intervals less than or equal to fromKey, -1
        * is returned.
        * 
        */
        public int getMaxIdxLTE(int fromKey, int fromIdx) {
          int maxStartIdx = search(fromKey, fromIdx, true);
          return maxStartIdx;

        }

        /*
         * search space is from startIdx (inclusive) to sortedKeys.length - 1.
         * 
         * returns the index in sorted keys:
         * if key is found, returns the max Idx having the key.
         * else if lte is true, returns the max key less than key.
         * else if lte is false, returns the min key greater than key.
         *
         */
        private int search(int searchKey, int startIdx, boolean lte) {
            if (startIdx < 0 || startIdx >= sortedKeys.length) {
                return -1;
            }
            int endIdx = sortedKeys.length - 1;
            int minKey = sortedKeys[startIdx][0];
            int maxKey = sortedKeys[endIdx][0];
            if (searchKey == maxKey) {
                return endIdx;
            }
            if (searchKey == minKey) {
                int idx = startIdx;
                while(idx < endIdx && sortedKeys[++idx][0] == searchKey);
                return idx > startIdx ? idx - 1 : startIdx;
            }
            if (searchKey > maxKey) {
                return lte ? endIdx : -1;
            }
            if (searchKey < minKey) {
                if (lte) return -1;
                
                int idx = startIdx;
                while(idx < endIdx && sortedKeys[++idx][0] == searchKey);
                return idx > startIdx ? idx - 1 : startIdx;
            }
            /* search key is within search range : startIdx-endIdx */

            if ((endIdx - startIdx) < cutOff) {
                /* Do a sequential search */
                return seqSearch(searchKey, startIdx, lte, endIdx);
            }
            
            
            /* Binary search.
             */
            return binSearch(searchKey, startIdx, lte, endIdx);
        }

        private int binSearch(int searchKey, int startIdx, boolean lte,
                              int endIdx) {
            int s = startIdx;
            int e = endIdx;
            int mid = s;

            while (e >= s) {
                mid = (s + e) >>> 1;
                if (sortedKeys[mid][0] < searchKey) {
                    s = mid + 1;
                    if (s <= endIdx && sortedKeys[s][0] > searchKey) {
                        return lte ? (s-1) : s;
                    }
                } else if (sortedKeys[mid][0] > searchKey) {
                    e = mid - 1;
                    if (e >= startIdx && sortedKeys[e][0] < searchKey) {
                        return lte ? e : e + 1;
                    }
                } else {
                    int idx = mid;
                    while (idx <= endIdx &&
                            sortedKeys[idx][0] == searchKey) {
                        idx ++;
                    }
                    return idx - 1;
                }
            }
            if ( s > mid) {
                if (lte) {
                    return mid;
                } 
            } else if (e < mid) {
                if (!lte) {
                    return mid;
                }
            }
            return -1 * (s + 1);
        }

        private int seqSearch(int searchKey, int startIdx, boolean lte,
                              int endIdx) {
            int idx = startIdx;
            while (idx <= endIdx
                    && sortedKeys[idx][0] < searchKey) {
                idx++;
            }
            if (sortedKeys[idx][0] == searchKey) {
                while(idx <= endIdx &&
                        sortedKeys[idx][0] == searchKey) {
                    idx++;
                }
                return idx - 1;
            } else if (lte) {
                return idx - 1;
            } else {
                return idx;
            }
        }
    }
    
    static class StartPtComp implements Comparator<int[]> {

        @Override
        public int compare(int[] o1, int[] o2) {
            if (o1 == null || o2 == null) {
                throw new NullPointerException();
            }
            if (o1[0] > o2[0]) {
                return 1;
            }
            if (o1[0] < o2[0]) {
                return -1;
            }
            if (o1[0] == o2[0]) {
                return 0;
            }
            throw new IllegalArgumentException();
        }

    }
    
    static class IntervalsIterator {
        int[] starts;
        int[] maxEnds;

        public IntervalsIterator(int[][] intervals) {
            addAll(intervals);
        }

        public int[][] mergeOverlaps() {
          return new int[0][0];
        
        }
        
        private void addAll(int[][] intervals) {
            Arrays.sort(intervals, new StartPtComp());
            maxEnds = new int[intervals.length];
            maxEnds[0] = intervals[0][1];
            for (int i = 1; i < intervals.length; i++) {
                if (maxEnds[i - 1] > intervals[i][1]) {
                    maxEnds[i] = maxEnds[i - 1];
                } else {
                    maxEnds[i] = intervals[i][1];
                }
            }
            
        }

    }

}
