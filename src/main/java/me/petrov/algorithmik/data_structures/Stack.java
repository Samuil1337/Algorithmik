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
    
    public boolean isEmpty() {
        return top == null;
    }
    
    public void push(E value) {
        top = new Element<>(value, top);
    }
    
    public E pop() {
        if (isEmpty()) {
            return null;
        }
        E value = top.getValue();
        top = top.getNext();
        return value;
    }
    
    public E peek() {
        return !isEmpty() ? top.getValue() : null;
    }
}
