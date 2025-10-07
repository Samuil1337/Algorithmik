/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package me.petrov.algorithmik.data_structures;

/**
 *
 * @author Samuil Petrov
 * @param <E> The data type to store in the Queue
 */
public class Queue<E> {
    private Element<E> head = null;
    private Element<E> tail = null;
    
    public boolean isEmpty() {
        return head == null;
    }
    
    public void enqueue(E value) {
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
    
    public E dequeue() {
        if (isEmpty()) {
            return null;
        }
        
        E value = head.getValue();
        head = head.getNext();
        if (isEmpty()) tail = null;
        
        return value;
    }
    
    public E peek() {
        return !isEmpty() ? head.getValue() : null;
    }
}
