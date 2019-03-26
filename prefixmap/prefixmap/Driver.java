package com.sleepycat.util.prefixmap;

import java.io.IOException;

public class Driver {

    public static void main(String[] args) throws IOException {
        // String inputValues[] = {"cab", "cabby", "cabbyter", "cat", "catdat",
        // "come" , "dome" };
        // long outputValues[] = {3,5,9,15,23,33,45};
        // String outputValues[] = {"pala", "palse", "para", "parse" , "passe",
        // "paste", "peste" };

        // String inputValues[] = {"c","ca", "cab", "col" , "cold", "colt"};
        // long outputValues[] = {3,5,9,15,23,33,45};
        // String outputValues[] = {"petr", "petral", "petraleum", "petrif",
        // "petrified" , "pest" };

        // String inputValues[] = {"saba","sabe", "sabo", "sach" , "sack",
        // "sacs", "sade", "sadr", "sapa", "sapl", "sapt", "sea", "sempa",
        // "setde", "sole"};
        // long outputValues[] =
        // {70,75,80,85,90,95,100,105,110,115,120,125,130,135,140,145};

        long inputValues[] = { 4, 256, 4540, 4876, 5968, 8989, 98888, 120120,
                204204, 306306, 408408, 602602, 806806 };
          long outputValues[] = { 8, 1024, 2048, 5126, 10468, 10968, 12462,
                14680, 16660, 24800, 48600, 49900, 51000 }; 
        
       /* int inputValues[] = { 4, 256, 4540, 4876, 5968, 8989, 98888, 120120,
                204204, 306306, 408408, 602602, 806806 };
        int outputValues[] = { 8, 1024, 2048, 5126, 10468, 10968, 12462,
                14680, 16660, 24800, 48600, 49900, 51000 }; */

        for (int i = 0; i < inputValues.length; i++) {
            System.out.println(inputValues[i] + " : " + outputValues[i]);
        }

        LongOutputPrefixOperations outputOps = LongOutputPrefixOperations
                                                                         .getInstance();
        
        /*IntOutputPrefixOperations outputOps = IntOutputPrefixOperations
                .getInstance(); */
        PrefixMapBuilder<Long> builder = new PrefixMapBuilder<Long>(
                                                                    InputType.BYTE,
                                                                    outputOps);

        int offset = 0;
        for (int i = 0; i < inputValues.length; i++) {
            System.out.println("\n\n ################################### ADDINPUT Input:" +
                    inputValues[i] + "  Output:" + outputValues[i] +
                    " ###########################################################");

            builder.add(inputValues[i], outputValues[i]);

        }
        PrefixMap<Long> pfm = builder.finish();
        // FST<CharsRef> fst = builder.finish();
        int inputIdx = 12;

        System.out.println("READ OUTPUT for an input. say get method of the LSNMap.");

        long inputLong = inputValues[inputIdx];
        //int inputInt = inputValues[inputIdx];
        System.out.println("READ OUTPUT for input:" + inputLong +
                "  verify output as:" + outputValues[inputIdx] +
                ". say get method of the LSNMap.");
        Long returnedOutput = pfm.get(inputLong);
        //Integer returnedOutput = pfm.get(inputInt);

        System.out.println("\n\nGIVEN INPUT=" + inputLong + "  MAPPEDOUTPUT:" +
                returnedOutput);

    }

}
