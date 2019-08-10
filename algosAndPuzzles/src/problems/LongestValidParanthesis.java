package problems;

public class LongestValidParanthesis {

    public static void main(String[] args) {
        
        LongestValidParanthesis obj = new LongestValidParanthesis();
        //String s = "((";
        //String s = "))";
        //String s = "))(";
        //String s = "))())";
        //String s = ")()()";
        //String s = ")))()()";
        String s = "(()()((()()())(("; // len 8
        //String s = "(()";
        //String s = ")()())";
        
        System.out.println(obj.longestValidParentheses(s));
        

    }
    
    public int longestValidParentheses(String s) {
        if(s == null || s.length() == 0) {
            return 0;
        }
        
        char OPEN = '(';
        char[] pars = s.toCharArray();
        Stack stack = new Stack(s.length());
        int maxL = 0;
        int currL = 0;
        for(int i = 0; i< pars.length; i++) {
            if (pars[i] == OPEN) {
                stack.push(i);
            } else {
                try {
                   stack.pop();
                   currL = i - stack.peek();
                   if(currL > maxL) {
                    maxL = currL;
                   }
                }catch(Exception e) {
                    /* stack is empty.
                     * CLOSE is being pushed.
                     * move startIdx to this string index and continue.
                     */
                    stack.setStartIdx(i);
                    continue;
                }
            }
        }
        
        return maxL;
    }
    
    private static class Stack {
        private int curr = 0;
        private int[] stack;
        private int startIdx =-1;
        
        public void setStartIdx(int startIdx) {
            this.startIdx = startIdx;
        }

        public Stack(int capacity) {
            stack = new int[capacity];
        }
        
        public void push(int elem) {
            stack[curr++]=elem;
        }
        
        public int pop() throws Exception {
            if(curr>0) {
              return stack[--curr];
            }
            throw new Exception("Stack Underflow");
        }
        
        public int peek() throws Exception{
            if(curr>0) {
                return stack[curr-1];
              }
            return startIdx;
        }
        
        }
    
}
