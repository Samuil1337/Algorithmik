/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package me.petrov.algorithmik.data_structures;

import java.util.ArrayList;

/**
 *
 * @author Samuil Petrov
 */
public class BinaryTree<E extends Comparable<E>> {
    private E value;
    private BinaryTree<E> parent, left, right = null;
    
    public BinaryTree(E value) {
        if (value == null) throw new IllegalArgumentException("Value can't be null.");
        this.value = value;
    }
    
    public BinaryTree(E value, BinaryTree<E> parent) {
        this(value);
        this.parent = parent;
    }
    
    public BinaryTree(E value, BinaryTree<E> parent, BinaryTree<E> left, BinaryTree<E> right) {
        this(value, parent);
        this.left = left;
        this.right = right;
    }
    
    public void add(E value) {
        if (value == null) throw new IllegalArgumentException("Value can't be null.");
        int result = value.compareTo(this.value);

        if (result < 0) {
            if (left == null) left = new BinaryTree<>(value, this);
            else left.add(value);
        } else if (result > 0) {
            if (right == null) right = new BinaryTree<>(value, this);
            else right.add(value);
        }
    }
    
    public void remove(E value) {
        if (value == null) throw new IllegalArgumentException("Value can't be null.");
        int result = value.compareTo(this.value);
    }
    
    public ArrayList<E> preOrder() {
        ArrayList<E> result = new ArrayList<>();

        result.add(this.value);
        if (this.left != null) result.addAll(this.left.preOrder());
        if (this.right != null) result.addAll(this.right.preOrder());
        
        return result;
    }
    
    public ArrayList<E> inOrder() {
        ArrayList<E> result = new ArrayList<>();

        if (this.left != null) result.addAll(this.left.inOrder());
        result.add(this.value);
        if (this.right != null) result.addAll(this.right.inOrder());
        
        return result;
    }
    
    public ArrayList<E> postOrder() {
        ArrayList<E> result = new ArrayList<>();

        if (this.left != null) result.addAll(this.left.postOrder());
        if (this.right != null) result.addAll(this.right.postOrder());
        result.add(this.value);
        
        return result;
    }
}
