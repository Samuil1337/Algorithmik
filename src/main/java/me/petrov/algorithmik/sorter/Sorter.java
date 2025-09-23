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
    
    private static int compareCounter = 0;
    
    public static void main(String args[]) {
        printArray(selectionSort(new int[]{ 2, 6, 3, 7, 1 }));
    }
    
    public static int[] insertSort(int[] numbers) {
        compareCounter = 0;
        int n = numbers.length;
        for (int i = 1; i != n; i++) {
            int element = numbers[i];
            int j = 0;
            while (numbers[j] < element) {
                j++;
                compareCounter++;
            }
            for (int k = i; k != j; k--) {
                numbers[k] = numbers[k - 1];
                compareCounter++;
            }
            numbers[j] = element;
            compareCounter++;
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
    
    public static int[] selectionSort(int[] numbers) {
        compareCounter = 0;
        for (int i = numbers.length - 1; i > 0; i--) {
            int maxIndex = 0;
            int max = numbers[maxIndex];
            for (int j = 0; j <= i; j++) {
                if (numbers[j] > max) {
                    maxIndex = j;
                    max = numbers[maxIndex];
                }
                compareCounter += 2;
            }
            numbers[maxIndex] = numbers[i];
            numbers[i] = max;
            
            compareCounter++;
        }
        System.out.println(compareCounter);
        return numbers;
    }
    
    public static String getBestSorterOnAverage() {
        int[][] arrays = new int[10][];
        for (int i = 0; i < arrays.length; i++) {
            
        }
    }
    
    public static void printArray(int[] array) {
        for (int element : array) {
            System.out.println(element);
        }
    }
    
}
