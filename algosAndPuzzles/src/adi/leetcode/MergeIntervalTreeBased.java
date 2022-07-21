package adi.leetcode;

public class MergeIntervalTreeBased {
    
    
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
                nextEnd++;
                    if (i - 2 >= 0) {
                    int lastEnd = merged[i-2][1];
                    if (nextMinNode.end <= lastEnd) {
                        
                        /* Before removing this node,
                         * get it's maxEnd and update
                         * the last added interval's end if required.
                         */
                        int lteMaxEnd = iTree.getMaxEnd(iTree.lteMaxEnd(nextMinNode));
                        if (lteMaxEnd > (nextEnd -1)) {
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
        for(int[] interval : merged) {
            System.out.println(interval[0] +":" + interval[1]);
        }
        return merged;
    }


    static class ITree {
        INode root;

        public void addInterval(int[] in) {
            if (root == null) {
                root = new INode(in[0], in[1]);
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
                updateMaxEnd(parent, in[1]);
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


        private void updateMaxEnd(INode parent, int newEnd) {
            while (parent != null) {
                if (parent.maxEnd < newEnd) {
                    parent.maxEnd = newEnd;
                    parent = parent.parent;
                } else {
                    break;
                }
            }
        }

        public int lteMaxEnd(INode n) {
            if (n.left == null) {
                return n.end;
            } else {
                return Math.max(n.end, n.left.maxEnd);
            }
        }

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


}
