/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package me.petrov.algorithmik.class_society;

/**
 *
 * @author Samuil Petrov
 */
public class PersonTest {
    
    public static void main(String[] args) {
        Person alex = new Person();
        Person jim = new Person("Jim", 34, Person.Sex.MALE);
        Person ingo = new Person("Ingo", 14, Person.Sex.FEMALE);
        Person george = new Person("George", 67, Person.Sex.MALE);
        Person person = new Person("Meier", 54, Person.Sex.MALE);
        person.printName();
        person.setName("Müller");
        System.out.println(person.getName());
    }
    
}

class Person {
    public enum Sex {
        MALE, FEMALE;
    }
    // Attributes
    private String name;
    private int age;
    private final Sex sex;

    // Constructors
    public Person() {
        this(Sex.FEMALE);
    }
    
    public Person(Sex sex) {
        this(18, sex);
    }
    
    public Person(int age, Sex sex) {
        this("Alex", age, sex);
    }
    
    public Person(String name, int age, Sex sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    // Methods
    public void printName() {
        System.out.println(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public int getAge() {
        return age;
    }
    
    public Sex getSex() {
        return sex;
    }
}
