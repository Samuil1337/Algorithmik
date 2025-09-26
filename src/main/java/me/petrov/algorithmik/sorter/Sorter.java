/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package me.petrov.algorithmik.sorter;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Samuil Petrov
 */
public class Sorter {
    
    private static long compareCounter = 0;
    
    public static void main(String args[]) {
        //printArray(selectionSort(new int[]{ 2, 6, 3, 7, 1 }));
        testSorters(1);
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
        //System.out.println(compareCounter);
        return numbers;
    }
    
    public static String getBestSorterOnAverage() {
        return getBestSorterOnAverage(new Random());
    }
    
    public static String getBestSorterOnAverage(Random random) {
        // create original array
        int[][] selectionSortArray = new int[5][];
        for (int i = 0; i < selectionSortArray.length; i++) {
            selectionSortArray[i] = new int[(int) Math.pow(10, i + 1)];
        }
        // fill original array with random numbers
        for (int i = 0; i < selectionSortArray.length; i++) {
            for (int j = 0; j < selectionSortArray[i].length; j++) {
                selectionSortArray[i][j] = random.nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
            }
        }
        // create deep copy of original array
        int[][] insertSortArray = new int[selectionSortArray.length][];
        for (int i = 0; i < selectionSortArray.length; i++) {
            insertSortArray[i] = Arrays.copyOf(selectionSortArray[i], selectionSortArray[i].length);
        }
        
        // test selectionSort
        long selectionCounter = 0;
        long selectionTimer = System.nanoTime();
        for (int[] array : selectionSortArray) {
            selectionSort(array);
            selectionCounter += compareCounter;
        }
        selectionTimer = (System.nanoTime() - selectionTimer) / 1_000_000;
        // test inserSort
        long insertCounter = 0;
        long insertTimer = System.nanoTime();
        for (int[] array : insertSortArray) {
            insertSort(array);
            insertCounter += compareCounter;
        }
        insertTimer = (System.nanoTime() - insertTimer) / 1_000_000;
        
        // check which one won
        double insertToSelectRatio = (double)insertCounter / selectionCounter;
        String chart = 
                "\nInsert sort compare ops    : " + insertCounter + '\n'
                + "Insert sort time           : " + insertTimer + " ms\n"
                + "Selection sort compare ops : " + selectionCounter + '\n'
                + "Selection sort time        : " + selectionTimer + " ms\n"
                + "Insert to selection ratio  : " + insertToSelectRatio + '\n';
        if (insertCounter == selectionCounter) {
            return "[Both are equal]" + chart;
        } else if (insertCounter > selectionCounter) {
            return "[Selection sort is better]" + chart;
        } else {
            return "[Insert sort is better]" + chart;
        }
    }
    
    public static void testSorters(int passes) {
        System.out.println("Testing each sorting algorithm...");
        for (int i = 0; i < passes; i++) {
            System.out.println("Pass #" + (i + 1));
            System.out.println("--------");
            System.out.println(getBestSorterOnAverage());
        }
    }
    
    public static void printArray(int[] array) {
        for (int element : array) {
            System.out.println(element);
        }
    }
    
}
