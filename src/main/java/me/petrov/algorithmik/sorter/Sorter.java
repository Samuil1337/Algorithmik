/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package me.petrov.algorithmik.sorter;

/**
 *
 * @author Samuil Petrov
 */
public class Sorter {
    
    public static void main(String args[]) {
        printArray(insertSort(new int[]{ 2, 6, 3, 7, 1, 5 }));
    }
    
    public static int[] insertSort(int[] numbers) {
        int n = numbers.length;
        for (int i = 1; i != n; i++) {
            int element = numbers[i];
            int j = 0;
            while (numbers[j] < element) {
                j++;
            }
            for (int k = i; k != j; k--) {
                numbers[k] = numbers[k - 1];
            }
            numbers[j] = element;
        }
        
        return numbers;
    }
    
    public static int[] insertSortEnhanced(int[] numbers) {
        // = { 2, 6, 3, 7, 1, 5 };
        int n = numbers.length;
        for (int i = 1; i < n; i++) {
            int element = numbers[i];
            int j = i - 1;
            while (j >= 0 && numbers[j] > element) {
                numbers[j + 1] = numbers[j];
                j--;
            }
            numbers[j + 1] = element;
        }
        
        return numbers;
    }
    
    public static void printArray(int[] array) {
        for (int element : array) {
            System.out.println(element);
        }
    }
    
}
