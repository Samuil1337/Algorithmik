/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package me.petrov.algorithmik.data_structures;

/**
 *
 * @author Samuil Petrov
 */
public class BinaryTree<E extends Comparable<Number>> {
    private final E value;
    private BinaryTree parent = null;
    private BinaryTree min = null;
    private BinaryTree max = null;
    
    public BinaryTree(E value) {
        if (value == null) throw new IllegalArgumentException("Value can't be null.");
        this.value = value;
    }
    
    public BinaryTree add(E value) {
        final BinaryTree<E> newNode = new BinaryTree<>(value);
        
        if (value > this.value) {
            
        }
    }
}
