/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package me.petrov.algorithmik.data_structures;

/**
 * Simple FIFO queue implementation.
 * 
 * @author Samuil Petrov
 * @param <E> The data type to store in the Queue
 * @see me.petrov.algorithmik.data_structures.Element
 */
public class Queue<E> {
    private Element<E> head = null;
    private Element<E> tail = null;
    private long size = 0;
    
    /**
     * Checks whether there are elements in the queue.
     * @return true if empty, false if at least one item is present
     */
    public boolean isEmpty() {
        return head == null;
    }
    
    /**
     * Returns the amount of items in the queue.
     * @return The amount of items in the collection
     */
    public long size() {
        return size;
    }
    
    /**
     * Puts the given value at the end of the queue.
     * @param value The value to put
     */
    public void enqueue(E value) {
        size++;
        
        if (isEmpty()) {
            head = tail = new Element<>(value);
            return;
        }
        
        /*
        Element<E> tail = head;
        while (tail.getNext() != null) {
            tail = tail.getNext();
        }
        */
        
        tail.setNext(new Element<>(value));
        tail = tail.getNext();
    }
    
    /**
     * Removes the first element in line and provides its value.
     * @return The value of the first item or null if the queue is empty
     */
    public E dequeue() {
        if (isEmpty()) {
            return null;
        }
        
        E value = head.getValue();
        head = head.getNext();
        if (isEmpty()) tail = null;
        
        size--;
        return value;
    }
    
    /**
     * Returns the element first in line.
     * @return The value of the first item or null if the queue is empty
     */
    public E peek() {
        return !isEmpty() ? head.getValue() : null;
    }
}
