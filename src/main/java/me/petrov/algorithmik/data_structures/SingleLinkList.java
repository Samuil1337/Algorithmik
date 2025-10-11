/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package me.petrov.algorithmik.data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Simple zero-based, singly-linked list implementation.
 * 
 * @author Samuil Petrov
 * @param <E> The data type to store in the List
 * @see me.petrov.algorithmik.data_structures.SingleLinkNode
 */
public final class SingleLinkList<E> implements Iterable<E> {
    private SingleLinkNode<E> head;
    private SingleLinkNode<E> tail;
    private int length = 0;
    
    public SingleLinkList(E... values) {
        for (E value : values) {
            add(value);
        }
    }
    
    /**
     * Whether there are elements or not.
     * @return true if there are no elements; false if there are elements
     */
    public boolean isEmpty() {
        return length == 0;
    }
    
    /**
     * Returns the size of the list.
     * @return The amount of items in the list
     */
    public int getLength() {
        return length;
    }
    
    /**
     * Appends the given element to the end of the List.
     * 
     * @param value The element to append to the list; may be {@code null}
     */
    public void add(E value) {
        if (isEmpty()) {
            head = tail = new SingleLinkNode<>(value);
            length++;
            return;
        }
        
        tail.setNext(new SingleLinkNode<>(value));
        tail = tail.getNext();
        length++;
    }
    
    /**
     * Inserts a new element at the specified position in the List.
     * 
     * The valid index range is 0..length (inclusive).
     * An index of 0 inserts at the head; 
     * an index equal to the current length appends at the tail.
     * Nodes previously at or after {@code index} are shifted one to the right.
     * 
     * @param index The position at which to insert the next element (0-based)
     * @param value The element to insert
     * @throws IndexOutOfBoundsException If {@code index} is less than 0
     *  or greater than the current length
     */
    public void insert(int index, E value) {
        if (index < 0 || index > length) {
            throw new IndexOutOfBoundsException(index);
        }
        
        if (index == 0) {
            head = new SingleLinkNode<>(value, head);
            if (length == 0) tail = head;
        } else if (index == length) {
            tail.setNext(new SingleLinkNode<>(value));
            tail = tail.getNext();
        } else {
            SingleLinkNode<E> before = head;
            for (int i = 1; i < index; i++) {
                before = before.getNext();
            }
            before.setNext(new SingleLinkNode<>(value, before.getNext()));
        }
        length++;
    }
    
    /**
     * Returns the element at the specified position in the List.
     * Valid index values are The valid index range is 0..length (exclusive).
     * 
     * @param index The position of the element to return (0-based)
     * @return The element at the specified position in this List
     * @throws IndexOutOfBoundsException if {@code index < 0 } or {@code index >= length}
     */
    public E get(int index) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException(index);
        }
        
        if (index == 0) {
            return head.getValue();
        }
        
        if (index == length - 1) {
            return tail.getValue();
        }
        
        SingleLinkNode<E> node = head.getNext();
        for(int i = 1; i < index; i++) {
            node = node.getNext();
        }
        
        return node.getValue();
    }
    
    /**
     * Replaces the element at the specified position in the List with the specified value.
     * The list's size is unchanged.
     * 
     * @param index The position of the element to replace
     * @param value The new element to set at the specified position
     * @throws IndexOutOfBoundsException if {@code index < 0} or {@code index >= length}
     */
    public void replace(int index, E value) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException(index);
        }
        
        if (index == 0) {
            head.setValue(value);
            return;
        }
        
        if (index == length - 1) {
            tail.setValue(value);
            return;
        }
        
        SingleLinkNode<E> node = head.getNext();
        for (int i = 1; i < index; i++) {
            node = node.getNext();
        }
        node.setValue(value);
    }
    
    /**
     * Removes the element at the specified position in the List.
     * 
     * Time complexity: O(n) in the worst case (when removing a node near the end),
     * because the method may need to traverse up to {@code index} nodes.
     * 
     * @param index The position of the element to remove (0-based)
     * @throws IndexOutOfBoundsException if {@code index < 0} or {@code index >= length}
     */
    public void remove(int index) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException(index);
        }
        
        if (index == 0) {
            head = head.getNext();
            length--;
            if (length == 0) tail = null;
            else if (length == 1) tail = head;
            return;
        }
        
        SingleLinkNode<E> before = head;
        for (int i = 1; i < index; i++) {
            before = before.getNext();
        }
        SingleLinkNode<E> toRemove = before.getNext();
        before.setNext(toRemove.getNext());
        if (toRemove == tail) tail = before;
        
        length--;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[ ");
        forEach(element -> sb.append(element).append(" "));
        return sb.append("]").toString();
    }

    @Override
    public Iterator<E> iterator() {
        return new SingleLinkListIterator();
    }
    
    private class SingleLinkListIterator implements Iterator<E> {
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
