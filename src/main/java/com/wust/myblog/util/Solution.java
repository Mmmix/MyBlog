package com.wust.myblog.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;


public class Solution {


    Stack<Integer> stack1 = new Stack<Integer>();
    Stack<Integer> stack2 = new Stack<Integer>();

    public void push(int node) {
        stack1.push(node);
    }

    public int pop() {
        int a;
        if (stack2.empty()){
            while (!stack1.empty()){
                a=stack1.pop();
                stack2.push(a);
            }
        }
        a=stack2.pop();
        return a;
    }
}

class main {

    public static void main(String[] args) {
        int[][] array = {{1, 2, 3}, {4, 5, 6}};
        Solution solution = new Solution();

        solution.push(1);
        solution.push(2);
        System.out.println(solution.pop());
        System.out.println(solution.pop());
    }
}