/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package me.petrov.algorithmik.data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Simple FIFO queue implementation.
 * 
 * @author Samuil Petrov
 * @param <E> The data type to store in the Queue
 * @see me.petrov.algorithmik.data_structures.SingleLinkNode
 */
public final class Queue<E> implements Iterable<E> {
    private SingleLinkNode<E> head = null;
    private SingleLinkNode<E> tail = null;
    private long size = 0;
    
    public Queue(E... values) {
        for (E value : values) {
            enqueue(value);
        }
    }
    
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
            head = tail = new SingleLinkNode<>(value);
            return;
        }
        
        /*
        SingleLinkNode<E> tail = head;
        while (tail.getNext() != null) {
            tail = tail.getNext();
        }
        */
        
        tail.setNext(new SingleLinkNode<>(value));
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
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[ ");
        forEach(element -> sb.append(element).append(" "));
        return sb.append("]").toString();
    }

    @Override
    public Iterator<E> iterator() {
        return new QueueIterator();
    }
    
    private class QueueIterator implements Iterator<E> {
        private SingleLinkNode<E> current = head;

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
