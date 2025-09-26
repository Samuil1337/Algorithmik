/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package me.petrov.algorithmik.mathler;

/**
 *
 * @author Samuil Petrov
 */
public class RecursiveMath {
    
    public static void main(String[] args) {
        //System.out.println(fibonacci(5));
    }
    
    public static long faculty(long n) {
        if (n < 0) throw new IllegalArgumentException("n can't be less than 0!");
        if (n == 0 || n == 1) return 1;
        return n * faculty(n -1);
    }
    
    public static long fibonacci(long n) {
        // Fibonacci isn't defined for n<0
        if (n < 0) throw new IllegalArgumentException("n can't be less than 0!");
        // Default cases of Fibonacci
        if (n == 0) return 0;
        if (n == 1) return 1;
        // Calculate the value by adding the two previous ones (Fibonacci)
        return fibonacci(n - 1) + fibonacci(n - 2);
    }
    
    public static long greatestCommonDenominator(long a, long b) {
        if (b < 0) throw new IllegalArgumentException("b can't be less than 0!"); 
        if (b == 0) return a;
        return greatestCommonDenominator(b, a % b);
    }
    
    public static String stringMinusOne(String s) {
        String result = "";
        for (int i = 1; i < s.length(); i++) {
            result += s.charAt(i);
        }
        return result;
    }
    
    private static double decimalNum = 0;
    public static int decimal(String dual) {
        if (dual.length() > 0) {
            decimalNum *= 2;
            if (dual.charAt(0) == '1') {
                decimalNum += 1;
            }
            return decimal(stringMinusOne(dual));
        }
    }
    
}
