    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package me.petrov.algorithmik.calculator;

/**
 *
 * @author Samuil Petrov
 */
public class RecursiveMath {
    
    public static void main(String[] args) {
        //System.out.println(fibonacci(5));
    }
    
    /**
     * Calculates the factorial of a given number.
     * 
     * The factorial of a non-negative integer n, denoted by n!,
     * is the product of all positive integers less than or equal to n.
     * By definition, 0! = 1! = 1.
     * 
     * @param n the number to calculate the factorial of
     * @return the factorial of n
     * @throws IllegalArgumentException if n is less than 0
     */
    public static long factorial(long n) {
        if (n < 0) throw new IllegalArgumentException("n can't be less than 0!");
        if (n == 0 || n == 1) return 1;
        return n * factorial(n -1);
    }
    
    /**
     * Calculates the nth Fibonacci number.
     * 
     * The Fibonacci sequence is a series of numbers where a number is the addition of the last two numbers,
     * starting with 0 and 1.
     * 
     * @param n the position of the Fibonacci number to calculate
     * @return the nth Fibonacci number
     * @throws IllegalArgumentException if n is less than 0
     */
    public static long fibonacci(long n) {
        if (n < 0) throw new IllegalArgumentException("n can't be less than 0!");
        if (n == 0) return 0;
        if (n == 1) return 1;
        return fibonacci(n - 1) + fibonacci(n - 2);
    }
    
    /**
     * Calculates the Greatest Common Divisor (GCD)
     * of two long integers using the Euclidean algorithm.
     * 
     * @param a the first non-negative long integer
     * @param b the second non-negative long integer
     * @return the GCD of a and b
     * @throws IllegalArgumentException if either a or b is negative
     */
    public static long gcd(long a, long b) {
        if (a < 0 || b < 0) throw new IllegalArgumentException("a and b can't be less than 0!"); 
        if (b == 0) return a;
        return gcd(b, a % b);
    }
    
    /**
     * Removes the first character from a given string.
     * 
     * @param s the input string
     * @return a new string with the first character removed
     */
    public static String removeFirstCharacter(String s) {
        String result = "";
        for (int i = 1; i < s.length(); i++) {
            result += s.charAt(i);
        }
        return result;
    }
    
    private static int decimalNum = 0;
    /**
     * Converts a binary string to its decimal equivalent.
     * 
     * This method uses a recursive approach
     * to process the binary string from left to right.
     * 
     * @param dual a string representing a binary number
     * @return the decimal equivalent of the input binary string
     */
    public static int decimal(String dual) {
        if (dual.length() > 0) {
            decimalNum *= 2;
            if (dual.charAt(0) == '1') {
                decimalNum += 1;
            }
            return decimal(removeFirstCharacter(dual));
        }
        return decimalNum;
    }
    
}
