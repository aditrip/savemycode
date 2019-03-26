package leet;

public class Test {

    public static void main(String[] args) {
        for(int i=0; i<=0; i++) {
            System.out.println(i);
        }
        
        byte b = 0;
        while(b<127) {
            b++;
            //System.out.println(b);
        }
        
        int[] a1 = { 1,2,3,4};
        int [] a2= {5,6,7,8};
        int[] a3 = {9,10,11,12};
        
        int[][] ints = new int[3][4];
        ints[0]=a1;
        ints[1]=a2;
        ints[2]=a3;
        
        int offset=0;
        int chunkSize=ints[0].length;
        int[] copyArray = new int[ints.length*chunkSize];
        for(int i=0; i<ints.length; i++) {
            
            System.arraycopy(ints[i], 0, copyArray, offset, chunkSize);
            offset+=chunkSize;
        }
        System.out.println("\n");
        for(int j=0; j < copyArray.length; j++) {
            System.out.print(copyArray[j]+"  ");
        }
    }

}
