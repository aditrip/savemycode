package adi.leetcode;

public class ProductOfNumbers {
    public static byte BUF_POW = 10;
    public static int BUF_SIZE = 1 << BUF_POW;
    int[] currBuf = null;
    int[][] bufPool = null;
    int absIdx = 0;
    int relIdx = 0;
    int[][] chunkProds = null;
    int[] currChunkProds = null;
    int[] cumChunkProds = null;
    int prodRelIdx = 0;
    int cumChunkProdIdx = 0;
    int currProduct = 1;
    int lastIdxOfZero = -1;

    public static byte CHUNK_POW = 3;
    public static int PRODS_CHUNK_SIZE = 1 << CHUNK_POW;
    public static int N_BUFFS = (40000 / BUF_SIZE) + 1;

    public static void main(String[] args) {
        String[] ops = { "ProductOfNumbers", "add", "add", "add", "add", "add",
                "getProduct", "getProduct", "getProduct", "add",
                "getProduct" };

        int[] ks = { Integer.MIN_VALUE, 3, 0, 2, 5, 4, 2, 3, 4, 8, 2 };

        Integer[] outs = { Integer.MIN_VALUE, Integer.MIN_VALUE,
                Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE,
                Integer.MIN_VALUE, 20, 40, 0, Integer.MIN_VALUE, 32 };
        Integer[] actuals = new Integer[outs.length];

        ProductOfNumbers obj = new ProductOfNumbers();
        int i = 0;
        for (String op : ops) {
            if (op.equals("getProduct")) {
                actuals[i] = obj.getProduct(ks[i]);
            }
            if (op.equals("add")) {
                obj.add(ks[i]);
                actuals[i] = null;
            }
            i++;
        }

        for (int a = 0; a < actuals.length; a++) {
            System.out.println(actuals[a]);
        }

        test1();

    }

    /* At every 5 adds, one product for 4 different ks  */
    public static void test1() {
        BUF_POW = 3;
        CHUNK_POW = 1;
        BUF_SIZE = 8;
        PRODS_CHUNK_SIZE = 2;
        N_BUFFS = (40000 / BUF_SIZE) + 1;

        int[] nums = { 2, 3, 0, 2, 5, 4, 2, 3, 4, 8, 2, 2, 3, 3, 3, 3, 5, 2, 3,
                5, 2, 3, 4, 0, 6, 9, 8, 11, 13, 17 };

        int[] ks = { 2, 3, 4, 5, 1, 2, 7, 10, 4, 5, 6, 7, 2, 3, 11, 20, 1, 2,
                3, 4, 1, 2, 4, 6 };

        int[] outs = { 10, 0, 0, 0, 8, 32, 7680, 0, 54, 108, 864, 3536, 15, 30,
                133200, 0, 6, 0, 0, 0, 17, 221, 19448, 1050192 };

        Integer[] actuals = new Integer[outs.length];

        ProductOfNumbers obj = new ProductOfNumbers();

        for (int i = 0; i < nums.length; i++) {
            obj.add(nums[i]);

            if ((i + 1) % 5 == 0) {
                for (int k = 0; k < 4; k++) {
                    actuals[4 * (i / 5) + k] =
                            obj.getProduct(ks[4 * (i / 5) + k]);
                }
            }
        }

        System.out.println("Expected:actual");
        for (int a = 0; a < actuals.length; a++) {
            System.out.println(outs[a] + ":" + actuals[a]);
        }

    }

    public ProductOfNumbers() {
        bufPool = new int[N_BUFFS][BUF_SIZE];
        chunkProds = new int[N_BUFFS][BUF_SIZE / PRODS_CHUNK_SIZE];
    }

    public void add(int num) {
        if ((absIdx & (BUF_SIZE - 1)) == 0) {
            currBuf = new int[BUF_SIZE];
            bufPool[absIdx / BUF_SIZE] = currBuf;
            relIdx = 0;
            currChunkProds = new int[BUF_SIZE / PRODS_CHUNK_SIZE];
            chunkProds[absIdx / BUF_SIZE] = currChunkProds;
            cumChunkProds = new int[N_BUFFS * currChunkProds.length];
            prodRelIdx = 0;
            currProduct = 1;
        }

        if (num == 0) {
            lastIdxOfZero = absIdx;
        }

        currProduct *= num;

        currBuf[relIdx++] = num;
        absIdx++;

        /*
         * ProdIdx   Chunk
         *    0       0-9
         *    1       10-19
         *    69      690-699
         *    99      990-999
         *    0       1000 - 1009
         *    1       1010 - 1019
         *    
         * cumProdIdx  Chunk [0 - currProdIdx*10 - 1]
         *    0         0-9;  p0
         *    1         0-19; p0*p1 
         *    2         0-29; p0*p1*p2
         *    ..
         *    i         0- (10*i - 1);
         *    
         *    index i has p0*p1*...pi 
         *    
         *    So, ps*...*pe = p[e]/p[s-1]
         *    
         *    
         */
        if (relIdx != 0 && (relIdx & (PRODS_CHUNK_SIZE - 1)) == 0) {
            currChunkProds[prodRelIdx++] = currProduct;

            if (cumChunkProdIdx == 0) {
                cumChunkProds[cumChunkProdIdx++] = currProduct;
            } else {
                cumChunkProds[cumChunkProdIdx] =
                        currProduct * cumChunkProds[cumChunkProdIdx - 1];
                cumChunkProdIdx++;
            }
            currProduct = 1;
        }
    }

    /*
     * Edge cases:
     * For lastRelIdxWithProd:
     *   1. relIdx - 1 = 729. lastRelIdxWithProd = 729.
     *   2. relIdx - 1 = 720. lastRelIdxWithProd = 719.
     *   3. relIdx - 1 = 723. lastRelIdxWithProd = 719. 
     *   4. relIdx - 1 = 4.   lastRelIdxWithProd = -1.
     *   5. relIdx - 1 = 9.   lastRelIdxWithProd = 9.
     *   6. relIdx - 1 = 0.   lastRelIdxWithProd = -1;
     * 
     * If first buf and lastBuf are different, fistRelIdxWithProd is required.
     * For firstRelIdxWithProd:
     *   1. startRelIdx = 729. firstRelIdxWithProd = 730.
     *   2. startRelIdx = 720. firstRelIdxWithProd = 720.
     *   3. startRelIdx = 723. firstRelIdxWithProd = 730. 
     *   4. startRelIdx = 4.   firstRelIdxWithProd = 10.
     *   5. startRelIdx = 0.   firstRelIdxWithProd = 0.
     *   6. startRelIdx = 999. firstRelIdxWithProd = 1000;
     *   
     */
    public int getProduct(int k) {

        if (k <= 0) {
            return 0;
        }
        int startIdx = absIdx - k;
        if (lastIdxOfZero >= startIdx) {
            return 0;
        }

        int startRelIdx = startIdx % BUF_SIZE;
        int product = 1;
        /* If product array can not be used: */
        if (k < PRODS_CHUNK_SIZE) {
            for (int i = startRelIdx; i < relIdx; i++) {
                product *= currBuf[i];
            }
            return product;
        }
        if (k == PRODS_CHUNK_SIZE) {
            return currChunkProds[prodRelIdx - 1];
        }

        boolean kInCurrBuf = (k <= BUF_SIZE) ? true : false;

        int lastRelIdxWithProd = ((relIdx) / PRODS_CHUNK_SIZE) > 0
                ? (relIdx - relIdx % PRODS_CHUNK_SIZE) - 1 : -1;

        int lastRelProdIdx = (lastRelIdxWithProd >= 0)
                ? lastRelIdxWithProd / PRODS_CHUNK_SIZE : -1;

        /* Check if this index actually exists in prod buf */
        int firstRelIdxWithProd = ((startRelIdx % PRODS_CHUNK_SIZE) == 0)
                ? (startRelIdx - startRelIdx % PRODS_CHUNK_SIZE)
                : (startRelIdx - startRelIdx % PRODS_CHUNK_SIZE)
                        + PRODS_CHUNK_SIZE;

        int firstRelProdIdx = firstRelIdxWithProd / PRODS_CHUNK_SIZE;

        /* Get the currBuf (last buf since it is already positioned there,
         * this is a streaming operation.)
         * 
         * Process end segment. 
         */
        int startProdIdx = kInCurrBuf ? firstRelProdIdx : 0;

        if (lastRelProdIdx >= 0) {
            for (int i = startProdIdx; i <= lastRelProdIdx; i++) {
                product *= currChunkProds[i];
            }
        }

        int remStartIdx = lastRelIdxWithProd >= 0 ? lastRelIdxWithProd + 1 : 0;
        /* Process rem of end segment after prodIdx range.
         * Basically in modular arithmetic 0 to relIdx -1 */
        if (lastRelProdIdx < relIdx - 1) {
            for (int i = remStartIdx; i < relIdx; i++) {
                product *= currBuf[i];
            }
        }

        if (kInCurrBuf) {
            /* only currBuf which is captured in last segment. */
            if (firstRelIdxWithProd > startRelIdx) {
                for (int i = startRelIdx; i < firstRelIdxWithProd; i++) {
                    product *= currBuf[i];
                }
            }
            return product;

        }

        /*
         * get to the start segment.
         * before that finish intermediate buffers if any.
         */

        int firstBufIdx = startIdx / BUF_SIZE;
        int lastBufIdx = (absIdx - 1) / BUF_SIZE;

        /* intermediate buffers */
        if (lastBufIdx - firstBufIdx >= 2) {
            for (int bufIdx = firstBufIdx + 1; bufIdx < lastBufIdx; bufIdx++) {
                int[] prodBuf = chunkProds[bufIdx];
                for (int i = 0; i < prodBuf.length; i++) {
                    product *= prodBuf[i];
                }
            }
        }

        /* process start segment. */

        int[] numBuf = bufPool[firstBufIdx];
        int[] prodBuf = chunkProds[firstBufIdx];

        for (int i = firstRelProdIdx; i < prodBuf.length; i++) {
            product *= prodBuf[i];
        }

        for (int i = startRelIdx; i < firstRelProdIdx; i++) {
            product *= numBuf[i];
        }

        return product;
    }

}
