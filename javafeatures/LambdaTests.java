package com.adi.javafeatures;

public class LambdaTests {
    
   public static void main(String[] args) {
       //String s = "Aditya";
       validateAndExecute( s -> { System.out.println(s); });
       System.out.println(Integer.MAX_VALUE);
       
   }
   
   public static void validateAndExecute(Task t) {
       String s = "Aditya";
       
       t.doWork(s);
   }
   
   static interface Task {
       
       public void doWork(String s);
   }
            
       
   

}
