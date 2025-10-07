/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package me.petrov.algorithmik.data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Simple LIFO stack implementation.
 * 
 * @author Samuil Petrov
 * @param <E> The data type to store on the Stack
 * @see me.petrov.algorithmik.data_structures.Element
 */
public class Stack<E> implements Iterable<E> {
    private Element<E> top = null;
    private long size = 0;
    
    /**
     * Checks whether there are elements on the stack.
     * @return true if empty, false if at least one item is present
     */
    public boolean isEmpty() {
        return top == null;
    }
    
    /**
     * Returns the amount of items on the stack.
     * @return The amount of items in the collection
     */
    public long size() {
        return size;
    }
    
    /**
     * Puts the given value on top of the stack.
     * @param value The value to put
     */
    public void push(E value) {
        top = new Element<>(value, top);
        size++;
    }
    
    /**
     * Removes the element on top of the stack and provides its value.
     * @return The value of the top element or null if the stack is empty
     */
    public E pop() {
        if (isEmpty()) {
            return null;
        }

        E value = top.getValue();
        top = top.getNext();
        size--;
        
        return value;
    }
    
    /**
     * Returns the element in top of the stack.
     * @return The value of the top element or null if the stack is empty
     */
    public E peek() {
        return !isEmpty() ? top.getValue() : null;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        for (E element : this) {
            sb.append(element).append(" ");
        }
        return sb.append("]").toString();
    }

    @Override
    public Iterator<E> iterator() {
        return new StackIterator();
    }
    
    private class StackIterator implements Iterator<E> {
        private Element<E> current = top;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public E next() {
            if (current == null) {
                throw new NoSuchElementException();
            }
            
            E value = current.getValue();
            current = current.getNext();
            return value;
        }
        
    }
}
