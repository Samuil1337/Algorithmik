/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package me.petrov.algorithmik.data_structures;

/**
 * A simple element for dynamic data structures.
 * Contains a value and links to the next item.
 * 
 * @author Samuil Petrov
 * @param <E> The data type of the element to store
 */
class Element<E> {
    /** The value of the element */
    private final E value;
    /** The pointer to its successor */
    private Element<E> next;
    
    public Element(E value) {
        this.value = value;
    }
    
    public Element(E value, Element<E> next) {
        this.value = value;
        this.next = next;
    }
    
    public Element<E> getNext() {
        return next;
    }
    
    public void setNext(Element<E> next) {
        this.next = next;
    }
    
    public E getValue() {
        return value;
    }
}
