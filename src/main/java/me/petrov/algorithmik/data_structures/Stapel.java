/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package me.petrov.algorithmik.data_structures;

/**
 *
 * @author Samuil Petrov
 */
public class Stapel<E> {
    private Element<E> top = null;
    
    public void push(E value) {
        top = new Element(value, top);
    }
    
    public E pop() {
        E value = top.value;
        top = top.getSuccessor();
        return value;
    }
    
    public E top() {
        return top.value;
    }
}
