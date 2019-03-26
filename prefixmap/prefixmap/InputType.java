package com.sleepycat.util.prefixmap;

/*
 * For byte sequence, input type will BYTE.
 * For strings, (CharSequence) input type can be in int
 * to accommodate all UTF-8 chars.
 */
public enum InputType {
    
    BYTE,SHORT,INT;

}
