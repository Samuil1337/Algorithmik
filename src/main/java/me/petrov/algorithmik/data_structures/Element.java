/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package me.petrov.algorithmik.data_structures;

/**
 *
 * @author samuil.petrov
 */
public class Element<E> {
    final E value;
    private Element<E> successor;
    
    public Element(E value) {
        this.value = value;
    }
    
    public Element(E value, Element<E> successor) {
        this.value = value;
        this.successor = successor;
    }
    
    public Element<E> getSuccessor() {
        return successor;
    }
    
    public void setSuccessor(Element<E> successor) {
        this.successor = successor;
    }
}
