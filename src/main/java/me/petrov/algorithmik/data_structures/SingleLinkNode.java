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
class SingleLinkNode<E> {
    /** The value of the element */
    private E value;
    /** The pointer to its successor */
    private SingleLinkNode<E> next;
    
    public SingleLinkNode(E value) {
        this.value = value;
    }
    
    public SingleLinkNode(E value, SingleLinkNode<E> next) {
        this.value = value;
        this.next = next;
    }
 
    public E getValue() {
        return value;
    }
    
    public void setValue(E value) {
        this.value = value;
    }
    
    public SingleLinkNode<E> getNext() {
        return next;
    }
    
    public void setNext(SingleLinkNode<E> next) {
        this.next = next;
    }
 
}
