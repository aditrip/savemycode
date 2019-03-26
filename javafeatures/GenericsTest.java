package com.adi.javafeatures;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class GenericsTest {

    
    public static void main(String[] args) {
        List<?> l = new LinkedList<String>();
        ((LinkedList<String>)l).add("one");
        ((LinkedList<String>)l).add("two");
        printList(l);
        
    }
    
    public static void printList(List<?> l) {
       Iterator<?> it = l.iterator();
        while(it.hasNext()) {
            System.out.println(it.next());
        }
    }
}
