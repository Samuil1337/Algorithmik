/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package me.petrov.algorithmik.data_structures;

/**
 *
 * @author Samuil Petrov
 * @param <E> The data type to store on the Stack
 */
public class Stack<E> {
    private Element<E> top = null;
    private long size = 0;
    
    public boolean isEmpty() {
        return top == null;
    }
    
    public long size() {
        return size;
    }
    
    public void push(E value) {
        top = new Element<>(value, top);
        size++;
    }
    
    public E pop() {
        if (isEmpty()) {
            return null;
        }

        E value = top.getValue();
        top = top.getNext();
        size--;
        
        return value;
    }
    
    public E peek() {
        return !isEmpty() ? top.getValue() : null;
    }
}
